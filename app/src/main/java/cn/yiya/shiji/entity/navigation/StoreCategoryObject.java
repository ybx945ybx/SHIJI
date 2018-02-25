package cn.yiya.shiji.entity.navigation;

import java.util.ArrayList;

/**
 * Created by jerry on 2016/4/14.
 */
public class StoreCategoryObject {
    public ArrayList<StoreCategoryInfo> category_list;

    public ArrayList<StoreCategoryInfo> getCategory_list() {
        return category_list;
    }

    public void setCategory_list(ArrayList<StoreCategoryInfo> category_list) {
        this.category_list = category_list;
    }

    public ArrayList<StoreCategoryInfo> brand_list;
}
