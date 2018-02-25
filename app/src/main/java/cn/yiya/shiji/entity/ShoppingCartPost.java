package cn.yiya.shiji.entity;

/**
 * Created by chenjian on 2015/9/17.
 */
public class ShoppingCartPost {

    String skuId;                     // 商品id
    String goodsId;                   // 商品编号
    int num;                          // 商品个数
    String recommend;                 // 推荐人

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }
}
