package cn.yiya.shiji.entity.navigation;

import java.util.ArrayList;

/**
 * Created by jerry on 2016/4/14.
 */
public class MallDetailObject {
    public String count;
    public ArrayList<MallDetailInfo> list;

    public ArrayList<MallDetailInfo> getList() {
        return list;
    }

    public void setList(ArrayList<MallDetailInfo> list) {
        this.list = list;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
