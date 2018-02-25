package cn.yiya.shiji.entity.navigation;

import java.util.ArrayList;

/**
 * Created by jerry on 2016/3/23.
 */
public class StoreLongInfo {
    private String name;
    private String cn_name;
    private String cover;
    private String introduction;
    private String logo;
    private String id;
    private ArrayList<StoreShortInfo> category;
    private String distance;
    private String country;
    private String city;
    private String area;
    private String address;
    private String telephone;
    private String branch_count;
    private String shop_num;
    private float rate;
    private int mall;
    private MapInfo coordinate;
    private ArrayList<ShopHoursInfo> open_time;

    private ArrayList<StoreCategoryInfo> category_list;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCn_name() {
        return cn_name;
    }

    public void setCn_name(String cn_name) {
        this.cn_name = cn_name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getBranch_count() {
        return branch_count;
    }

    public void setBranch_count(String branch_count) {
        this.branch_count = branch_count;
    }

    public String getShop_num() {
        return shop_num;
    }

    public void setShop_num(String shop_num) {
        this.shop_num = shop_num;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public ArrayList<StoreShortInfo> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<StoreShortInfo> category) {
        this.category = category;
    }

    public int getMall() {
        return mall;
    }

    public void setMall(int mall) {
        this.mall = mall;
    }

    public MapInfo getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(MapInfo coordinate) {
        this.coordinate = coordinate;
    }

    public ArrayList<ShopHoursInfo> getOpen_time() {
        return open_time;
    }

    public void setOpen_time(ArrayList<ShopHoursInfo> open_time) {
        this.open_time = open_time;
    }

    public ArrayList<StoreCategoryInfo> getCategory_list() {
        return category_list;
    }

    public void setCategory_list(ArrayList<StoreCategoryInfo> category_list) {
        this.category_list = category_list;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
