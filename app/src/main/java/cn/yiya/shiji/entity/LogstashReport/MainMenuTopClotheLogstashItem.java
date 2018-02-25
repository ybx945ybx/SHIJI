package cn.yiya.shiji.entity.LogstashReport;

/**
 * Created by Tom on 2016/9/1.
 * 打点上报
 */
public class MainMenuTopClotheLogstashItem {
    private String type_name ;
    private String user_id;
    private String brand_id;

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
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
