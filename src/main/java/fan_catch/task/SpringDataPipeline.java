package fan_catch.task;

import fan_catch.pojo.MFanInfo;
import fan_catch.service.FanInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Component
public class SpringDataPipeline implements Pipeline {
    @Autowired
    private FanInfoService fanInfoService;
    @Override
    public void process(ResultItems resultItems, Task task) {
        MFanInfo mFanInfo =resultItems.get("mfanInfo");
        if (mFanInfo !=null)
        {
            this.fanInfoService.save(mFanInfo);
        }



    }
}
