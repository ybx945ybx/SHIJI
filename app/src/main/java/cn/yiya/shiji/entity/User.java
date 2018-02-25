package cn.yiya.shiji.entity;

import android.graphics.Bitmap;

public class User {
    private String name;
    private String head;
    private int stage;
    private int user_id;
    private int gender;
    private String expected_date;
    private String birth_date;
    private String hospital;
    private String city;
    private int liked_count;
    private int posts_count;
    private int tags_count;
    private int following_count;
    private int fans_count;
    private int private_photo_count;
    private String child_weight;
    private String child_name;
    private String child_gender;
    private boolean has_spouse;
    private String spouse_head;
    private int score;
    private int packages;
    private String m_id;
    private String reg_status;
    private boolean is_follow;
    private int followed; //"followed": 1 //关注 1   未关注2  未登录0
    private String red_desc; //红人描述
    private int like_count; //已赞
    private Bitmap bitmap;
    private int red;       // 1是红人
    private boolean have_shop;  //true:开店  false：未开店
    private String shop_name;  // 店铺名
    private String en_name;   //域名
    private String shop_province;  //店铺省
    private String shop_city;        //店铺市
    private int modify;         // 0:域名可修改  1:不可更改
    private String shop_desc;     //店铺简介
    private int shop_order_num;     //店铺订单数量
    private int invite_shop_num;    //邀请开店数量
    private int cash_account;      //现金账户
    private int shop_id;          //店铺ID
    private String shop_type;     // 店铺类型
    private String shop_msg;      //  店铺消息
    private String phone;
    private int level;         // 红人等级

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getShop_msg() {
        return shop_msg;
    }

    public void setShop_msg(String shop_msg) {
        this.shop_msg = shop_msg;
    }

    public String getShop_type() {
        return shop_type;
    }

    public void setShop_type(String shop_type) {
        this.shop_type = shop_type;
    }

    public String getShop_desc() {
        return shop_desc;
    }

    public void setShop_desc(String shop_desc) {
        this.shop_desc = shop_desc;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getEn_name() {
        return en_name;
    }

    public void setEn_name(String en_name) {
        this.en_name = en_name;
    }

    public String getShop_province() {
        return shop_province;
    }

    public void setShop_province(String shop_province) {
        this.shop_province = shop_province;
    }

    public String getShop_city() {
        return shop_city;
    }

    public void setShop_city(String shop_city) {
        this.shop_city = shop_city;
    }

    public int getModify() {
        return modify;
    }

    public void setModify(int modify) {
        this.modify = modify;
    }

    public int getShop_order_num() {
        return shop_order_num;
    }

    public void setShop_order_num(int shop_order_num) {
        this.shop_order_num = shop_order_num;
    }

    public int getInvite_shop_num() {
        return invite_shop_num;
    }

    public void setInvite_shop_num(int invite_shop_num) {
        this.invite_shop_num = invite_shop_num;
    }

    public int getCash_account() {
        return cash_account;
    }

    public void setCash_account(int cash_account) {
        this.cash_account = cash_account;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public boolean isHave_shop() {
        return have_shop;
    }

    public void setHave_shop(boolean have_shop) {
        this.have_shop = have_shop;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public String getReg_status() {
        return reg_status;
    }

    public void setReg_status(String reg_status) {
        this.reg_status = reg_status;
    }

    public String getM_id() {
        return m_id;
    }

    public void setM_id(String m_id) {
        this.m_id = m_id;
    }

    public int getPackages() {
        return packages;
    }

    public void setPackages(int packages) {
        this.packages = packages;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean is_follow() {
        return is_follow;
    }

    public void setIs_follow(boolean is_follow) {
        this.is_follow = is_follow;
    }

    public int getFollowed() {
        return followed;
    }

    public void setFollowed(int followed) {
        this.followed = followed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getExpected_date() {
        return expected_date;
    }

    public void setExpected_date(String expected_date) {
        this.expected_date = expected_date;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getLiked_count() {
        return liked_count;
    }

    public void setLiked_count(int liked_count) {
        this.liked_count = liked_count;
    }

    public int getPosts_count() {
        return posts_count;
    }

    public void setPosts_count(int posts_count) {
        this.posts_count = posts_count;
    }

    public int getTags_count() {
        return tags_count;
    }

    public void setTags_count(int tags_count) {
        this.tags_count = tags_count;
    }

    public int getFollowing_count() {
        return following_count;
    }

    public void setFollowing_count(int following_count) {
        this.following_count = following_count;
    }

    public int getFans_count() {
        return fans_count;
    }

    public void setFans_count(int fans_count) {
        this.fans_count = fans_count;
    }

    public int getPrivate_photo_count() {
        return private_photo_count;
    }

    public void setPrivate_photo_count(int private_photo_count) {
        this.private_photo_count = private_photo_count;
    }

    public String getChild_weight() {
        return child_weight;
    }

    public void setChild_weight(String child_weight) {
        this.child_weight = child_weight;
    }

    public String getChild_name() {
        return child_name;
    }

    public void setChild_name(String child_name) {
        this.child_name = child_name;
    }

    public String getChild_gender() {
        return child_gender;
    }

    public void setChild_gender(String child_gender) {
        this.child_gender = child_gender;
    }

    public boolean isHas_spouse() {
        return has_spouse;
    }

    public void setHas_spouse(boolean has_spouse) {
        this.has_spouse = has_spouse;
    }

    public String getSpouse_head() {
        return spouse_head;
    }

    public void setSpouse_head(String spouse_head) {
        this.spouse_head = spouse_head;
    }

    public String getRed_desc() {
        return red_desc;
    }

    public void setRed_desc(String red_desc) {
        this.red_desc = red_desc;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
