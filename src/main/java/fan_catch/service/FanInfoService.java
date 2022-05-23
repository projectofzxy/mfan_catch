package fan_catch.service;

import fan_catch.pojo.MFanInfo;

import java.util.List;

public interface FanInfoService {
void save(MFanInfo mFanInfo);
List<MFanInfo> FindFanInfo(MFanInfo mFanInfo);

}
