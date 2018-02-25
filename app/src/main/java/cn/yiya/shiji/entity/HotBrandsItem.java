package cn.yiya.shiji.entity;

import java.util.ArrayList;

/**
 * Created by jerryzhang on 2015/9/15.
 */
public class HotBrandsItem {
    private int id;
    private String name;
    private String cn_name;
    private String logo;
    private String[] types;
    private ArrayList<Goods> goods;
    private String tag_id;
    private int followed;

    public int getFollowed() {
        return followed;
    }

    public void setFollowed(int followed) {
        this.followed = followed;
    }

    public String getTag_id() {
        return tag_id;
    }

    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String[] getTypes() {

        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }

    public ArrayList<Goods> getMlist() {
        return goods;
    }

    public void setMlist(ArrayList<Goods> mlist) {
        this.goods = mlist;
    }

    public String getCn_name() {
        return cn_name;
    }

    public void setCn_name(String cn_name) {
        this.cn_name = cn_name;
    }

    public ArrayList<Goods> getGoods() {
        return goods;
    }

    public void setGoods(ArrayList<Goods> goods) {
        this.goods = goods;
    }
}
