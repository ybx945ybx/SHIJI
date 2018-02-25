package cn.yiya.shiji.entity.LogstashReport;

/**
 * Created by Tom on 2016/9/1.
 * 打点上报
 */
public class MainMenuHotCategoryLogstashItem {
    private String type_name ;
    private String user_id;
    private String category_name;

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
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
