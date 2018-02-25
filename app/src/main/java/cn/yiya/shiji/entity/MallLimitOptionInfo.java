package cn.yiya.shiji.entity;

import java.util.ArrayList;

/**
 * 商品筛选条件
 * Created by chenjian on 2015/10/29.
 */
public class MallLimitOptionInfo {

    ArrayList<SortInfo> genders;
    ArrayList<SortInfo> brands;
    ArrayList<SortInfo> categories;
    ArrayList<SortInfo> price_ranges;
    ArrayList<SortInfo> sorts;
    ArrayList<SortInfo> color;
    ArrayList<SortInfo> size;

    public ArrayList<SortInfo> getGenders() {
        return genders;
    }

    public void setGenders(ArrayList<SortInfo> genders) {
        this.genders = genders;
    }

    public ArrayList<SortInfo> getBrands() {
        return brands;
    }

    public void setBrands(ArrayList<SortInfo> brands) {
        this.brands = brands;
    }

    public ArrayList<SortInfo> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<SortInfo> categories) {
        this.categories = categories;
    }

    public ArrayList<SortInfo> getPrice_ranges() {
        return price_ranges;
    }

    public void setPrice_ranges(ArrayList<SortInfo> price_ranges) {
        this.price_ranges = price_ranges;
    }

    public ArrayList<SortInfo> getSorts() {
        return sorts;
    }

    public void setSorts(ArrayList<SortInfo> sorts) {
        this.sorts = sorts;
    }

    public ArrayList<SortInfo> getColor() {
        return color;
    }

    public void setColor(ArrayList<SortInfo> color) {
        this.color = color;
    }

    public ArrayList<SortInfo> getSize() {
        return size;
    }

    public void setSize(ArrayList<SortInfo> size) {
        this.size = size;
    }
}
