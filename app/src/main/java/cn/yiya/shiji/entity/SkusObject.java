package cn.yiya.shiji.entity;

import java.util.ArrayList;

/**
 * Created by Tom on 2016/8/3.
 */
public class SkusObject {
    ArrayList<Skus> skus;

    public ArrayList<Skus> getList() {
        return skus;
    }

    public void setList(ArrayList<Skus> list) {
        this.skus = list;
    }

    public static class Skus {

        String sku_id;
        int num;

        public String getSku_id() {
            return sku_id;
        }

        public void setSku_id(String sku_id) {
            this.sku_id = sku_id;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }
}
