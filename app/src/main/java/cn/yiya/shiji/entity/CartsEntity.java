package cn.yiya.shiji.entity;

/**
 * Created by Amy on 2016/11/15.
 */

public class CartsEntity {
    /**
     * id : XXXXXX
     * skuId : XXXXXX
     * num : 12
     * recommend : sadfsdf
     */
    String id;
    String skuId;
    int num;
    String recommend;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

//    public String getRecommend() {
//        return recommend;
//    }
//
//    public void setRecommend(String recommend) {
//        this.recommend = recommend;
//    }
}