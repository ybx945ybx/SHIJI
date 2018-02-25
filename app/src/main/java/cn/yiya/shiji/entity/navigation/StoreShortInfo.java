package cn.yiya.shiji.entity.navigation;

/**
 * Created by jerry on 2016/4/14.
 */
public class StoreShortInfo {

    /**
     * id : 123213123
     * name : 6pm
     * cover : http://xxx/xx1.jpg
     * rate : 5.0
     * category :
     * address : 20 Glen Ave,  オークランド
     */

    private String id;
    private String name;
    private String cover;
    private String rate;
    private String category;
    private String address;
    private String distance;
    private String mall;

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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getMall() {
        return mall;
    }

    public void setMall(String mall) {
        this.mall = mall;
    }
}
