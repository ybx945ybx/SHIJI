package cn.yiya.shiji.entity;

/**
 * Created by Tom on 2015/12/15.
 */
public class PublishedGoodsInfo {
    String cover;               // 商品图片
    String brand;               // 商品品牌
    String price;               // 商品价格
    String title;               // 商品描述
    String site;                // 商品官网地址
    String goods_id;            // 商品id
    String recommend;           // 推荐人

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }
}
