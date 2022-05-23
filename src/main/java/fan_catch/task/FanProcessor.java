package fan_catch.task;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import fan_catch.pojo.MFanInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Component
public class FanProcessor implements PageProcessor {
    private  int count=1;
    private String url="http://www.bimiacg.net/vodshow/fanzu--hits------1---/";
    private String today = String.valueOf(new Date());
    private List<Selectable> list1;
    private String a,b;
    @Override
    public void process(Page page) {

        WebClient webClient=new WebClient(BrowserVersion.FIREFOX);
        webClient.getOptions().setJavaScriptEnabled(true); //启用JS解释器，默认为true
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setActiveXNative(false);
        webClient.setJavaScriptTimeout(5000);//设置JS执行的超时时间
        webClient.getOptions().setCssEnabled(false); //禁用css支持
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.getOptions().setThrowExceptionOnScriptError(false); //js运行错误时，是否抛出异常
        webClient.getOptions().setTimeout(10000);

        webClient.getOptions().setDoNotTrackEnabled(false);
        HtmlPage page1 = null;
        try {
            if (page.getUrl().toString()==null)
            {page1 = webClient.getPage(url);}
            else {
                page1=webClient.getPage(page.getUrl().toString());
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }finally {
            webClient.close();
        }
        webClient.waitForBackgroundJavaScript(10000);
        String xml = page1.asXml();
        Html html = new Html(xml);
        List<Selectable> list = html.css("div.v_tb li.item div.info").nodes();
        if (list.size()==0)
        {
            this.savemfan(page);
        }else {

            list1=html.css("div.v_tb li.item").css("div.info").nodes();
            for (Selectable selectable : list) {
                String string = selectable.css("div.info").links().toString();
                page.addTargetRequest("//www.bimiacg.net"+string);

            }
            String nexturl = "//www.bimiacg.net/vodshow/fanzu--hits------" + (++count) + "---/";
            page.addTargetRequest(nexturl);
        }

    }

    private void savemfan(Page page) {
        MFanInfo mFanInfo =new MFanInfo();
        Html html = page.getHtml();
        for (Selectable selectable : list1) {
            if (("http://www.bimiacg.net" + selectable.links().toString()).equals(page.getUrl().toString())){

             a=selectable.regex("/bangumi.*").regex(".*\" title").toString();
             b=selectable.regex("title=\".*").regex(".*\" target").toString();
            }

        }
        mFanInfo.setMfan_name(b.substring(7,b.length()-8));
        mFanInfo.setUrl("http://www.bimiacg.net/"+a.substring(0,a.length()-7));
        mFanInfo.setTime(today);
        if (html.css("div.detail_js ul.player_list").toString()==null)
        {mFanInfo.setIf_look("无版权");}
        else{
            mFanInfo.setIf_look("可以看");
        }
        page.putField("mfanInfo", mFanInfo);

    }


    private Site site=Site.me()
            .setCharset("utf8")
            .setRetryTimes(3)
            .setRetrySleepTime(3000)
            .setTimeOut(10000);
    @Override
    public Site getSite() {
        return site;
    }
    @Autowired
    private SpringDataPipeline springDataPipeline;
    @Scheduled(initialDelay = 1000,fixedDelay = 100000)
    public void process()
    {
        HttpClientDownloader httpClientDownloader=new HttpClientDownloader();
        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("182.140.244.163",	8118)));
        Spider.create(new FanProcessor())
                .addUrl(url)
                //.setDownloader(httpClientDownloader)
                .setScheduler(new QueueScheduler()
                        .setDuplicateRemover(new BloomFilterDuplicateRemover(10000)))
                .addPipeline(this.springDataPipeline)
                .thread(10)
                .run();
    }
}
