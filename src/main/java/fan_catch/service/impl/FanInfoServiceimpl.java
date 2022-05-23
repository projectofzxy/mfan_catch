package fan_catch.service.impl;

import fan_catch.dao.FanInfoDao;
import fan_catch.pojo.MFanInfo;
import fan_catch.service.FanInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FanInfoServiceimpl implements FanInfoService {
    @Autowired
    private FanInfoDao fanInfoDao;
    @Transactional
    @Override
    public void save(MFanInfo mFanInfo) {
        MFanInfo prarm=new MFanInfo();
        prarm.setUrl(mFanInfo.getUrl());
       // prarm.setTime(fanInfo.getTime());
        List<MFanInfo> list=this.FindFanInfo(prarm);
        if (list.size()==0)
        {
            this.fanInfoDao.saveAndFlush(mFanInfo);
        }

    }

    @Override
    public List<MFanInfo> FindFanInfo(MFanInfo mFanInfo) {
        Example example=Example.of(mFanInfo);
        List list = this.fanInfoDao.findAll(example);
        return list;
    }
}
