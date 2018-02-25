package cn.yiya.shiji.entity;

import java.util.List;

/**
 * Created by Tom on 2016/11/4.
 */
public class GoodsSkusEntity {

    private List<GoodsDetailEntity.SkusEntity> skus;

    public void setSkus(List<GoodsDetailEntity.SkusEntity> skus) {
        this.skus = skus;
    }

    public List<GoodsDetailEntity.SkusEntity> getSkus() {
        return skus;
    }

}
