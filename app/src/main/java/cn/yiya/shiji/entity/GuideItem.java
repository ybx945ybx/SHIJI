package cn.yiya.shiji.entity;

/**
 * Created by Tom on 2015/12/21.
 */
public class GuideItem {
    private String logo;                                // logo
    private String id;                                  // 品类id
    private String tag_id;                              // 品牌id
    private String name;
    private boolean isSelected;
    private int followed;                              //用户已关注为1，其它或不存在则为未关注。并会移动至list的开头

    public String getTag_id() {
        return tag_id;
    }

    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
    }

    public int getFollowed() {
        return followed;
    }

    public void setFollowed(int followed) {
        this.followed = followed;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

}
