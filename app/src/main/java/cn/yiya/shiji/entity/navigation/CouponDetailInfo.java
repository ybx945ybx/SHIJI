package cn.yiya.shiji.entity.navigation;

/**
 * Created by jerry on 2016/3/25.
 */
public class CouponDetailInfo {

    /**
     * id : 12
     * brief : xx shop
     * cover : http://xxx/xx1.jpg
     * start_time : 2015-09-29 14:00:00
     * end_time : 2115-09-29 14:00:00
     * range : 使用范围
     * usage : 使用方法
     * des : 详细描述
     */

    private String id;
    private String brief;
    private String cover;
    private String start_time;
    private String end_time;
    private String range;               // 使用范围
    private String usage;               // 使用方法
    private String des;                 // 详细描述
    private String scene;               // 使用场景
    private int collected;
    private String period;
    private String store_name;
    private String country_name;
    private String country_id;

    private ShareInfo share_info;

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

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public ShareInfo getShare_info() {
        return share_info;
    }

    public void setShare_info(ShareInfo share_info) {
        this.share_info = share_info;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public int getCollected() {
        return collected;
    }

    public void setCollected(int collected) {
        this.collected = collected;
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

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

}
