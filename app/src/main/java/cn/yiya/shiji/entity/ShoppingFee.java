package cn.yiya.shiji.entity;

/**
 * Created by chenjian on 2015/9/28.
 */
public class ShoppingFee {

    float foreignFee;
    float channelFee;
    float serviceFee;

    public float getForeignFee() {
        return foreignFee;
    }

    public void setForeignFee(float foreignFee) {
        this.foreignFee = foreignFee;
    }

    public float getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(float serviceFee) {
        this.serviceFee = serviceFee;
    }

    public float getChannelFee() {
        return channelFee;
    }

    public void setChannelFee(float channelFee) {
        this.channelFee = channelFee;
    }
}
