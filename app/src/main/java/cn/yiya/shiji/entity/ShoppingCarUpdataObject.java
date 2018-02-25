package cn.yiya.shiji.entity;

import java.util.ArrayList;

/**
 * Created by jerry on 2015/12/14.
 */
public class ShoppingCarUpdataObject {
    public ArrayList<Carts> carts;    //  修改购物车数据时传的参数

    public ArrayList<Carts> getCarts() {
        return carts;
    }

    public void setCarts(ArrayList<Carts> carts) {
        this.carts = carts;
    }

    public class Carts{
        String id;     // 新增的cartId字段
        String num;    // 商品数量

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
