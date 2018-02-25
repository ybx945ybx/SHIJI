package cn.yiya.shiji.entity;

/**
 * Created by tomyang on 2015/9/15.
 */
public class NotifyContent {

    private String user;
    private int user_id;
    private String head;
    private int work_id;
    private int package_id;
    private String sub_order_num;
    private String work_image;
    int work_type;
    int comment;
    String order_num;
    int follow_user;
    int is_follow;

    public int getWork_type() {
        return work_type;
    }

    public void setWork_type(int work_type) {
        this.work_type = work_type;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public int getFollow_user() {
        return follow_user;
    }

    public void setFollow_user(int follow_user) {
        this.follow_user = follow_user;
    }

    public int getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(int is_follow) {
        this.is_follow = is_follow;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getWork_image() {
        return work_image;
    }

    public void setWork_image(String work_image) {
        this.work_image = work_image;
    }

    public String getSub_order_num() {
        return sub_order_num;
    }

    public void setSub_order_num(String sub_order_num) {
        this.sub_order_num = sub_order_num;
    }

    public int getPackage_id() {

        return package_id;
    }

    public void setPackage_id(int package_id) {
        this.package_id = package_id;
    }

    public int getWork_id() {

        return work_id;
    }

    public void setWork_id(int work_id) {
        this.work_id = work_id;
    }

}
