package cn.yiya.shiji.entity.navigation;

import java.util.ArrayList;


/**
 * Created by jerry on 2016/3/18.
 * 储存解析的国家列表, 热门路线（复用）, 某一国家下的城市列表(复用)
 */
public class CountryListObject {
    private String count;
    public ArrayList<CountryListInfo> list;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public ArrayList<CountryListInfo> getList() {
        return list;
    }

    public void setList(ArrayList<CountryListInfo> list) {
        this.list = list;
    }
}
