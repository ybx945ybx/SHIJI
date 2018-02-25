package cn.yiya.shiji.entity;

import java.util.HashMap;

/**
 * Created by jerry on 2016/3/7.
 */
public class SearchPopularWords {
    public String stamp;
    public String pstamp;
//    public ArrayList<HashMap<String,String[]>> data;
//
//    public ArrayList<HashMap<String, String[]>> getData() {
//        return data;
//    }
//
//    public void setData(ArrayList<HashMap<String, String[]>> data) {
//        this.data = data;
//    }
    public HashMap<String, String[]>[] data;

    public HashMap<String, String[]>[] getData() {
        return data;
    }

    public void setData(HashMap<String, String[]>[] data) {
        this.data = data;
    }

    public String getPstamp() {
        return pstamp;
    }

    public void setPstamp(String pstamp) {
        this.pstamp = pstamp;
    }

    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }

}
