package cn.yiya.shiji.entity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jerry on 2016/3/7.
 */
public class PopularDataObject {
    public ArrayList<String> arrayList;
    public String stamp;
    public  HashMap<String,String[]>[] datas;

    public HashMap<String, String[]>[] getDatas() {
        return datas;
    }

    public void setDatas(HashMap<String, String[]>[] datas) {
        this.datas = datas;
    }

    public String getStamp() {

        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }

    public ArrayList<String> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<String> arrayList) {
        this.arrayList = arrayList;
    }
}
