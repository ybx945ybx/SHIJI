package cn.yiya.shiji.entity.navigation;

import java.util.ArrayList;

/**
 * Created by jerry on 2016/3/23.
 */
public class ShopHoursInfo {

    /**
     * day_ofweek : 1
     * start_time : 14:00:00
     * end_time : 14:00:01
     */

    private int day_ofweek;
    private String start_time;
    private String end_time;

    private ArrayList<ShopHoursInfo> list;

    public ArrayList<ShopHoursInfo> getList() {
        return list;
    }

    public void setList(ArrayList<ShopHoursInfo> list) {
        this.list = list;
    }

    public int getDay_ofweek() {
        return day_ofweek;
    }

    public void setDay_ofweek(int day_ofweek) {
        this.day_ofweek = day_ofweek;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
}
