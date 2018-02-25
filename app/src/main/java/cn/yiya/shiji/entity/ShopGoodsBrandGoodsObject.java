package cn.yiya.shiji.entity;

import java.util.ArrayList;

import cn.yiya.shiji.entity.goodes.NewGoodsItem;

/**
 * Created by jerry on 2015/11/11.
 */
public class ShopGoodsBrandGoodsObject {
    private int count;
    private ArrayList<NewGoodsItem> list;
    private ArrayList<NewGoodsItem> topic_list;

    public ArrayList<NewGoodsItem> getTopic_list() {
        return topic_list;
    }

    public void setTopic_list(ArrayList<NewGoodsItem> topic_list) {
        this.topic_list = topic_list;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<NewGoodsItem> getList() {
        return list;
    }

    public void setList(ArrayList<NewGoodsItem> list) {
        this.list = list;
    }

}
