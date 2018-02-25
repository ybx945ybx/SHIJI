package cn.yiya.shiji.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by chenjian on 2015/11/20.
 */
public class SortInfo implements Serializable {

    String key;
    String name;
    String cn_name;
    int id;
    String logo;
    boolean check;
    int product_type_id;
    String product_type;
    int red;
    int green;
    int blue;
    ArrayList<SortInfo> sizes;
    String[] alias;

    public String[] getAlias() {
        return alias;
    }

    public void setAlias(String[] alias) {
        this.alias = alias;
    }

    public int getProduct_type_id() {
        return product_type_id;
    }

    public void setProduct_type_id(int product_type_id) {
        this.product_type_id = product_type_id;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getCn_name() {
        return cn_name;
    }

    public void setCn_name(String cn_name) {
        this.cn_name = cn_name;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public ArrayList<SortInfo> getSizes() {
        return sizes;
    }

    public void setSizes(ArrayList<SortInfo> sizes) {
        this.sizes = sizes;
    }
}
