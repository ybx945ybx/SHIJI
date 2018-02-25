package cn.yiya.shiji.entity.navigation;

/**
 * Created by jerry on 2016/4/15.
 */
public class TaxAndInfos {

    /**
     * country_id : 1
     * brief : guys
     */

    private String country_id;
    private String brief;
    private String content;
    private String title;
    private String des;

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
