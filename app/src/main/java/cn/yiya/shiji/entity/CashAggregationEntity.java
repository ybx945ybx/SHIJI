package cn.yiya.shiji.entity;

/**
 * Created by Tom on 2016/10/18.
 */
public class CashAggregationEntity {

    /**
     * cash : 0
     * cash_in : 0
     * be_money_all : 0
     * order_used_all : 0
     */

    private float cash;
    private float cash_in;
    private float be_money_all;
    private float order_used_all;

    public void setCash(float cash) {
        this.cash = cash;
    }

    public void setCash_in(float cash_in) {
        this.cash_in = cash_in;
    }

    public void setBe_money_all(float be_money_all) {
        this.be_money_all = be_money_all;
    }

    public void setOrder_used_all(float order_used_all) {
        this.order_used_all = order_used_all;
    }

    public float getCash() {
        return cash;
    }

    public float getCash_in() {
        return cash_in;
    }

    public float getBe_money_all() {
        return be_money_all;
    }

    public float getOrder_used_all() {
        return order_used_all;
    }
}
