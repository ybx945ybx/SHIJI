package cn.yiya.shiji.entity;

import java.util.ArrayList;

/**
 * Created by jerry on 2015/12/17.
 */
public class ShoppingCarSourceObject {
    String source;
    ArrayList<CartsEntity> carts;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public ArrayList<CartsEntity> getCarts() {
        return carts;
    }

    public void setCarts(ArrayList<CartsEntity> carts) {
        this.carts = carts;
    }

}
