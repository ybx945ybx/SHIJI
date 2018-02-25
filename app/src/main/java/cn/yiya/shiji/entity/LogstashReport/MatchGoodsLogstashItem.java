package cn.yiya.shiji.entity.LogstashReport;

/**
 * Created by Tom on 2016/9/1.
 * 打点上报
 */
public class MatchGoodsLogstashItem {
    private String user_id;
    private String goods_id;

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
