package cn.yiya.shiji.entity;

import java.util.ArrayList;

/**
 * 购物车弹层 参数对应的具体信息
 * Created by Amy on 2016/11/16.
 */

public class GoodsParamsInfo {
    String key;
    ArrayList<GoodsSortInfo> valueList;
    GoodsDetailEntity.SizeEntity size;  //size用
    int size_match_pos; //size用

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ArrayList<GoodsSortInfo> getValueList() {
        return valueList;
    }

    public void setValueList(ArrayList<GoodsSortInfo> valueList) {
        this.valueList = valueList;
    }

    public GoodsDetailEntity.SizeEntity getSize() {
        return size;
    }

    public void setSize(GoodsDetailEntity.SizeEntity size) {
        this.size = size;
    }

    public int getSize_match_pos() {
        return size_match_pos;
    }

    public void setSize_match_pos(int size_match_pos) {
        this.size_match_pos = size_match_pos;
    }
}
