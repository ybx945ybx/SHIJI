package cn.yiya.shiji.entity;

import java.util.ArrayList;

import cn.yiya.shiji.entity.goodes.NewGoodsItem;

/**
 * Created by leosu on 2015/9/18.
 */
public class NewGoodsObject {
    public ArrayList<NewGoodsItem> goods;
    public ArrayList<NewGoodsItem> list;
    public ArrayList<NewGoodsItem> goods_list;

    public NewGoodsObject(ArrayList<NewGoodsItem> goods_list) {
        this.goods_list = goods_list;
    }

    public ArrayList<NewGoodsItem> getGoods() {
        return goods;
    }

    public void setGoods(ArrayList<NewGoodsItem> goods) {
        this.goods = goods;
    }

    public ArrayList<NewGoodsItem> getList() {
        return list;
    }

    public void setList(ArrayList<NewGoodsItem> list) {
        this.list = list;
    }
}
