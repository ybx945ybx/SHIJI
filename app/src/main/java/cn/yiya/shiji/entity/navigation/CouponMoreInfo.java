package cn.yiya.shiji.entity.navigation;

/**
 * Created by jerry on 2016/4/15.
 * 收藏的coupon列表，内嵌的关于coupon更多信息
 */
public class CouponMoreInfo {

    /**
     * id :
     * brief :
     * cover :
     * des :
     * range :
     * start_time :
     * end_time :
     * period : ~
     */

    private String id;
    private String brief;
    private String cover;
    private String start_time;
    private String end_time;
    private String range;
    private String des;
    private String period;
    private String store_name;
;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }
}
