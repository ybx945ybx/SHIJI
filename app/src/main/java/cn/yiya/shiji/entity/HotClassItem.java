package cn.yiya.shiji.entity;

/**
 * Created by leosu on 2015/9/19.
 */
public class HotClassItem {
    private String icon;
    private String id;
    private String name;
    private int followed;

    public int getFollowed() {
        return followed;
    }

    public void setFollowed(int followed) {
        this.followed = followed;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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
}
