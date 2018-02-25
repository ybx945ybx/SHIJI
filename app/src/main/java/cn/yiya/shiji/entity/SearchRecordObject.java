package cn.yiya.shiji.entity;

import java.util.ArrayList;

/**
 * Created by jerry on 2015/12/28.
 * 用来储存分类页面搜索数据
 */
public class SearchRecordObject {
    ArrayList<String> list;
    String stamp;
    String[] data;

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }

    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }
}
