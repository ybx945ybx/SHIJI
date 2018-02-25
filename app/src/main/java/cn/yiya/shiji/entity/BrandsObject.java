package cn.yiya.shiji.entity;

import java.util.ArrayList;

/**
 * Created by jerryzhang on 2015/9/14.
 */
public class BrandsObject {
    public ArrayList<BrandsItem> list;
    String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<BrandsItem> getList() {
        return list;
    }

    public void setList(ArrayList<BrandsItem> list) {
        this.list = list;
    }
}
