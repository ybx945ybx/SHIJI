package cn.yiya.shiji.entity.LogstashReport;

/**
 * Created by Tom on 2016/9/1.
 * 打点上报
 */
public class MainMenuTopBannerLogstashItem {
    private String type_name ;
    private String user_id;
    private String banner_id;
    private String banner_title;

    public String getBanner_id() {
        return banner_id;
    }

    public void setBanner_id(String banner_id) {
        this.banner_id = banner_id;
    }

    public String getBanner_title() {
        return banner_title;
    }

    public void setBanner_title(String banner_title) {
        this.banner_title = banner_title;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
