package fan_catch.dao;

import fan_catch.pojo.MFanInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FanInfoDao extends JpaRepository<MFanInfo,Long> {
}
