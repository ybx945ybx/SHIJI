package cn.yiya.shiji.entity.navigation;

/**
 * Created by jerry on 2016/4/15.
 */
public class ShareInfo {
    private String url;
    private String des;
    private String title;
    private String image;
    private ShareMoreInfo wx;
    private ShareMoreInfo sina;
    private ShareMoreInfo fq;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ShareMoreInfo getWx() {
        return wx;
    }

    public void setWx(ShareMoreInfo wx) {
        this.wx = wx;
    }

    public ShareMoreInfo getSina() {
        return sina;
    }

    public void setSina(ShareMoreInfo sina) {
        this.sina = sina;
    }

    public ShareMoreInfo getFq() {
        return fq;
    }

    public void setFq(ShareMoreInfo fq) {
        this.fq = fq;
    }
}
