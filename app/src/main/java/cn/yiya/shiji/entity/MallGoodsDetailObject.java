package cn.yiya.shiji.entity;

import java.util.ArrayList;

import cn.yiya.shiji.entity.goodes.NewGoodsItem;

/**
 * Created by chenjian on 2015/10/29.
 */
public class MallGoodsDetailObject {
    ArrayList<NewGoodsItem> goods_list;
    ArrayList<NewGoodsItem> list;
    ArrayList<NewGoodsItem> topic_list;
    int count;
    private String siteTag;//商城官网名称
    
    public ArrayList<NewGoodsItem> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(ArrayList<NewGoodsItem> goods_list) {
        this.goods_list = goods_list;
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

    public String getSiteTag() {
        return siteTag;
    }

    public void setSiteTag(String siteTag) {
        this.siteTag = siteTag;
    }

    public ArrayList<NewGoodsItem> getTopic_list() {
        return topic_list;
    }

    public void setTopic_list(ArrayList<NewGoodsItem> topic_list) {
        this.topic_list = topic_list;
    }
}
