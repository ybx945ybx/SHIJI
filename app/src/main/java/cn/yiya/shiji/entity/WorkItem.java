package cn.yiya.shiji.entity;

import java.util.ArrayList;
import java.util.List;

public class WorkItem {
    private int work_id;
    private int user_id;
    private int child_id;
    private String content;
    private int stage;
    private int day;
    //	private String gender;
    private ArrayList<WorkImage> images;
    private User user;
    private int like_count;
    private int comment_count;
    private String create_time;
    private boolean is_follow;
    private int[] like_users;
    private String timestamp;
    private int type; //type 2 搭配
    private String star_id;
    private int isAgree; //点赞  1   未点赞 2  未登录0
    private List<BrandsEntity> brands;

    public int[] getLike_users() {
        return like_users;
    }

    public void setLike_users(int[] like_users) {
        this.like_users = like_users;
    }

    public boolean is_follow() {
        return is_follow;
    }

    public void setIs_follow(boolean is_follow) {
        this.is_follow = is_follow;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getChild_id() {
        return child_id;
    }

    public void setChild_id(int child_id) {
        this.child_id = child_id;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getWork_id() {
        return work_id;
    }

    public void setWork_id(int work_id) {
        this.work_id = work_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<WorkImage> getImages() {
        return images;
    }

    public void setImages(ArrayList<WorkImage> images) {
        this.images = images;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getStar_id() {
        return star_id;
    }

    public void setStar_id(String star_id) {
        this.star_id = star_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsAgree() {
        return isAgree;
    }

    public void setIsAgree(int isAgree) {
        this.isAgree = isAgree;
    }

    public List<BrandsEntity> getBrands() {
        List<BrandsEntity> tempbrands = new ArrayList<>();
        for (int i = 0; i < brands.size(); i++) {
            boolean bTempHas = false;
            for (int j = 0; j < tempbrands.size(); j++) {
                if (tempbrands.get(j).getId() == brands.get(i).getId()) {
                    bTempHas = true;
                    break;
                }
            }
            if (!bTempHas) {
                tempbrands.add(brands.get(i));
            }
        }
        return tempbrands;
    }

    public void setBrands(List<BrandsEntity> brands) {
        this.brands = brands;
    }

    public class BrandsEntity {
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
