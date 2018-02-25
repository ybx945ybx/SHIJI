package cn.yiya.shiji.entity.navigation;

import java.util.ArrayList;

/**
 * Created by jerry on 2016/3/18.
 * 国家字段
 */
public class CountryListInfo {
    int imageResource;

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    /**
     * id : 123213123
     * name : UK
     * cn_name : 英国
     * city_num : 4
     * cover : http://xxx/xx1.jpg
     */
    private String id;
    private String name;
    private String cn_name;
    private int city_num;
    private String cover;
    private String brief;
    private ArrayList<TaxAndInfos> infos;
    private TaxAndInfos tax;
    private MapInfo coordinate;

    private String country_name;

    public ArrayList<TaxAndInfos> getInfos() {
        return infos;
    }

    public void setInfos(ArrayList<TaxAndInfos> infos) {
        this.infos = infos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getCity_num() {
        return city_num;
    }

    public void setCity_num(int city_num) {
        this.city_num = city_num;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }


    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }


    public MapInfo getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(MapInfo coordinate) {
        this.coordinate = coordinate;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public TaxAndInfos getTax() {
        return tax;
    }

    public void setTax(TaxAndInfos tax) {
        this.tax = tax;
    }
}
