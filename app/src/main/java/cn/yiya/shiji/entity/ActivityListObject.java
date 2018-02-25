package cn.yiya.shiji.entity;

import java.util.ArrayList;

/**
 * Created by jerry on 2015/11/9.
 */
public class ActivityListObject {
    public ArrayList<ActivityListItem> list;

    public ArrayList<ActivityListItem> getList() {
        return list;
    }

    public void setList(ArrayList<ActivityListItem> list) {
        this.list = list;
    }

    public class ActivityListItem{
        String cover;
        int id;

        //精选专题推荐
        String title;
        String startDate;
        int sort;
        int type;

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
