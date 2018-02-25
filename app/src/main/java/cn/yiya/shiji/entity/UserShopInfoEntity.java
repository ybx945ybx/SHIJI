package cn.yiya.shiji.entity;

/**
 * Created by Tom on 2016/10/28.
 */
public class UserShopInfoEntity {
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
    private String shop_url;
    private String shop_name_type;
    private String shareTimelineTitle;
    private String shareTimelineImage;
    private String shareAppMessageImage;
    private String shareAppMessageTitle;
    private String shareAppMessageDesc;

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

    public String getShop_desc() {
        return shop_desc;
    }

    public void setShop_desc(String shop_desc) {
        this.shop_desc = shop_desc;
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

    public String getShop_type() {
        return shop_type;
    }

    public void setShop_type(String shop_type) {
        this.shop_type = shop_type;
    }

    public String getShop_msg() {
        return shop_msg;
    }

    public void setShop_msg(String shop_msg) {
        this.shop_msg = shop_msg;
    }

    public String getShop_url() {
        return shop_url;
    }

    public void setShop_url(String shop_url) {
        this.shop_url = shop_url;
    }

    public String getShop_name_type() {
        return shop_name_type;
    }

    public void setShop_name_type(String shop_name_type) {
        this.shop_name_type = shop_name_type;
    }

    public String getShareTimelineTitle() {
        return shareTimelineTitle;
    }

    public void setShareTimelineTitle(String shareTimelineTitle) {
        this.shareTimelineTitle = shareTimelineTitle;
    }

    public String getShareTimelineImage() {
        return shareTimelineImage;
    }

    public void setShareTimelineImage(String shareTimelineImage) {
        this.shareTimelineImage = shareTimelineImage;
    }

    public String getShareAppMessageImage() {
        return shareAppMessageImage;
    }

    public void setShareAppMessageImage(String shareAppMessageImage) {
        this.shareAppMessageImage = shareAppMessageImage;
    }

    public String getShareAppMessageTitle() {
        return shareAppMessageTitle;
    }

    public void setShareAppMessageTitle(String shareAppMessageTitle) {
        this.shareAppMessageTitle = shareAppMessageTitle;
    }

    public String getShareAppMessageDesc() {
        return shareAppMessageDesc;
    }

    public void setShareAppMessageDesc(String shareAppMessageDesc) {
        this.shareAppMessageDesc = shareAppMessageDesc;
    }
}
