package cn.yiya.shiji.entity.goodes;

import java.util.List;

/**
 * Created by jerryzhang on 2015/9/18.
 */
public class NewGoodsItem {
    private String id;
    private String title;
    private String brand;
    private String cover;
    private String site;
    private String price;         // 现价
    private String list_price;    // 原价
    private CoverItem cover_info;
    private int status;           //3表示可售,其他表示不可售
    private float discount;       //10:不打折。保存两位小数
    private String sku_id;
    private String goods_id;
    private String sub_order_number;
    private String siteTag;
    private String recommend;      //推荐人
    private List<NewGoodsItem> goodses;
    private boolean is_recommend;
    private int for_seller;
    private String share_profit;
    private int topic_id;

    public int getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(int topic_id) {
        this.topic_id = topic_id;
    }

    public int getFor_seller() {
        return for_seller;
    }

    public void setFor_seller(int for_seller) {
        this.for_seller = for_seller;
    }

    public String getShare_profit() {
        return share_profit;
    }

    public void setShare_profit(String share_profit) {
        this.share_profit = share_profit;
    }

//    public boolean isSellerRecommend() {
//        return sellerRecommend;
//    }
//
//    public void setSellerRecommend(boolean sellerRecommend) {
//        this.sellerRecommend = sellerRecommend;
//    }


    public boolean is_recommend() {
        return is_recommend;
    }

    public void setIs_recommend(boolean is_recommend) {
        this.is_recommend = is_recommend;
    }

    public String getSku_id() {
        return sku_id;
    }

    public void setSku_id(String sku_id) {
        this.sku_id = sku_id;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getSub_order_number() {
        return sub_order_number;
    }

    public void setSub_order_number(String sub_order_number) {
        this.sub_order_number = sub_order_number;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getList_price() {
        return list_price;
    }

    public void setList_price(String list_price) {
        this.list_price = list_price;
    }

    public CoverItem getCover_info() {
        return cover_info;
    }

    public void setCover_info(CoverItem cover_info) {
        this.cover_info = cover_info;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getSiteTag() {
        return siteTag;
    }

    public void setSiteTag(String siteTag) {
        this.siteTag = siteTag;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public List<NewGoodsItem> getGoodses() {
        return goodses;
    }

    public void setGoodses(List<NewGoodsItem> goodses) {
        this.goodses = goodses;
    }
}
