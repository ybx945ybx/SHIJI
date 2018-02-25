package cn.yiya.shiji.entity.navigation;

/**
 * Created by jerry on 2016/4/21.
 */
public class RecommendInfo {

    /**
     * name : LV
     * logo : http://xxx/xx1.jpg
     * url :
     * id : 123421
     */

    private String name;
    private String logo;
    private String url;
    private String id;
    private String recommend; //推荐人

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }
}
