package cn.yiya.shiji.entity;

/**
 * Created by weixuewu on 15/7/31.
 */
public class LikerItem {
    private String head;
    private String name;
    private int user_id;
    private int red;

    //"follow":1//1:关注，2:未关注
    private int follow;

    private String time;

    private boolean is_follow;

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public boolean is_follow() {
        return is_follow;
    }

    public void setIs_follow(boolean is_follow) {
        this.is_follow = is_follow;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getFollow() {
        return follow;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
