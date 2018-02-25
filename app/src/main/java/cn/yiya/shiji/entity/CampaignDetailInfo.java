package cn.yiya.shiji.entity;

/**
 * Created by Tom on 2016/6/27.
 */
public class CampaignDetailInfo {

    /**
     * image : http://7xjc5h.com2.z0.g1b.qiniucdn.com/banner_1_1438575122?
     * banner : http://7xjc5h.com2.z0.g1b.qiniucdn.com/banner_1_1438575127?
     * desc : 12
     * tag : 33
     * create_time : 2015-08-03, 12:12:09
     * type : 1
     * category : 1
     */

    private String image;
    private String banner;
    private String desc;
    private String tag;
    private String create_time;
    private int type;
    private int category;

    public void setImage(String image) {
        this.image = image;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public String getBanner() {
        return banner;
    }

    public String getDesc() {
        return desc;
    }

    public String getTag() {
        return tag;
    }

    public String getCreate_time() {
        return create_time;
    }

    public int getType() {
        return type;
    }

    public int getCategory() {
        return category;
    }
}
