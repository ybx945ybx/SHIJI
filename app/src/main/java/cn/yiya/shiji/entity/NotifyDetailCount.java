package cn.yiya.shiji.entity;

/**
 * Created by tomyang on 2015/9/15.
 */
public class NotifyDetailCount {
    private int logistics_count;            // 物流消息个数
    private int sns_count;                  // 社区消息个数

    public int getSns_count() {
        return sns_count;
    }

    public void setSns_count(int sns_count) {
        this.sns_count = sns_count;
    }

    public int getLogistics_count() {

        return logistics_count;
    }

    public void setLogistics_count(int logistics_count) {
        this.logistics_count = logistics_count;
    }
}
