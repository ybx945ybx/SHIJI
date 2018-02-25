package cn.yiya.shiji.entity;

import java.util.ArrayList;

/**
 * Created by yiya on 2015/9/17.
 */
public class ShoppingCartObject {
    ArrayList<ShoppingCartList> list;
    private float balance;

    public ArrayList<ShoppingCartList> getList() {
        return list;
    }

    public void setList(ArrayList<ShoppingCartList> list) {
        this.list = list;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }
}
