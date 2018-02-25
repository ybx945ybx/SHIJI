package cn.yiya.shiji.entity;

/**
 * Created by Tom on 2016/6/22.
 */
public class HtmlData {
    String[] shopping_cart_ids;
    int siteId;
    String goodsId;

    public String[] getShopping_cart_ids() {
        return shopping_cart_ids;
    }

    public void setShopping_cart_ids(String[] shopping_cart_ids) {
        this.shopping_cart_ids = shopping_cart_ids;
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }
}
