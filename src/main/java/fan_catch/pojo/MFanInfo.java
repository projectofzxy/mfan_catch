package fan_catch.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MFanInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mfan_name;
    private String url;
    private String time;
    private String if_look;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMfan_name() {
        return mfan_name;
    }

    public void setMfan_name(String mfan_name) {
        this.mfan_name = mfan_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIf_look() {
        return if_look;
    }

    public void setIf_look(String iflook) {
        this.if_look = iflook;
    }

    @Override
    public String toString() {
        return "MFanInfo{" +
                "id=" + id +
                ", mfan_name='" + mfan_name + '\'' +
                ", url='" + url + '\'' +
                ", time='" + time + '\'' +
                ", iflook='" + if_look + '\'' +
                '}';
    }
}
