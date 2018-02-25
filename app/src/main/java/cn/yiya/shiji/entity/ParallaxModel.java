package cn.yiya.shiji.entity;

/**
 * Created by bobbyadiprabowo on 3/3/15.
 */
public class ParallaxModel {
    int id;
    String name;
    String cn_name;
    int city_num;
    String cover;
    String title;
    int ImageResource;

    public int getImageResource() {
        return ImageResource;
    }

    public void setImageResource(int imageResource) {
        ImageResource = imageResource;
    }
    //            "cn_name":"英国",
//            "city_num":4,
//            "cover": "http://xxx/xx1.jpg"

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
