package cn.yiya.shiji.entity;

/**
 * Created by jerryzhang on 2015/9/22.
 * 品牌详情
 */
public class BackgroundItem {
    private String id;
    private String name;
    private String des;
    private String tag;
    private String image;
    private String logo;
    private Info logo_info;
    private boolean is_follow;
    private int tag_id;

    public boolean is_follow() {
        return is_follow;
    }

    public void setIs_follow(boolean is_follow) {
        this.is_follow = is_follow;
    }

    public int getTag_id() {
        return tag_id;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Info getLogo_info() {
        return logo_info;
    }

    public void setLogo_info(Info logo_info) {
        this.logo_info = logo_info;
    }

    class Info{
        int width;
        int height;
    }
}
