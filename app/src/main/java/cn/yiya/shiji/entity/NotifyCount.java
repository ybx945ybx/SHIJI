package cn.yiya.shiji.entity;

/**
 * Created by tomyang on 2015/9/15.
 */
public class NotifyCount {
    private int notify;
    private int pending_pay_order;
    private int pending_receive_order;
    private int shop_pending_pay_order;
    private int shop_pending_receive_order;

    public int getPending_receive_order() {
        return pending_receive_order;
    }

    public void setPending_receive_order(int pending_receive_order) {
        this.pending_receive_order = pending_receive_order;
    }

    public int getPending_pay_order() {

        return pending_pay_order;
    }

    public void setPending_pay_order(int pending_pay_order) {
        this.pending_pay_order = pending_pay_order;
    }

    public int getNotify() {

        return notify;
    }

    public void setNotify(int notify) {
        this.notify = notify;
    }

    public int getShop_pending_pay_order() {
        return shop_pending_pay_order;
    }

    public void setShop_pending_pay_order(int shop_pending_pay_order) {
        this.shop_pending_pay_order = shop_pending_pay_order;
    }

    public int getShop_pending_receive_order() {
        return shop_pending_receive_order;
    }

    public void setShop_pending_receive_order(int shop_pending_receive_order) {
        this.shop_pending_receive_order = shop_pending_receive_order;
    }
}
