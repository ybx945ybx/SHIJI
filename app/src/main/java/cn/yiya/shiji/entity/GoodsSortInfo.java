package cn.yiya.shiji.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amy on 2016/11/10.
 */

public class GoodsSortInfo {

    boolean isChecked;
    boolean isValid;
    String title;
    String name;  //color用
    String cover; //color用
    int size_table_pos; //size用
    ArrayList<SizeTableEntity> sizemap;//size用
    List<GoodsDetailEntity.GoodsColorsEntity.ImagesEntity> images;//color用
    int min_price;//color用
    int max_price;//color用


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getSize_table_pos() {
        return size_table_pos;
    }

    public void setSize_table_pos(int size_table_pos) {
        this.size_table_pos = size_table_pos;
    }

    public ArrayList<SizeTableEntity> getSizemap() {
        return sizemap;
    }

    public void setSizemap(ArrayList<SizeTableEntity> sizemap) {
        this.sizemap = sizemap;
    }

    public List<GoodsDetailEntity.GoodsColorsEntity.ImagesEntity> getImages() {
        return images;
    }

    public void setImages(List<GoodsDetailEntity.GoodsColorsEntity.ImagesEntity> images) {
        this.images = images;
    }

    public int getMin_price() {
        return min_price;
    }

    public void setMin_price(int min_price) {
        this.min_price = min_price;
    }

    public int getMax_price() {
        return max_price;
    }

    public void setMax_price(int max_price) {
        this.max_price = max_price;
    }
}
