package cn.yiya.shiji.entity;

/**
 * Created by chenjian on 2016/6/1.
 */
public class MainActImgInfo {
    String image;
    String url;
    String title;
    private ShareEntity share;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ShareEntity getShare() {
        return share;
    }

    public void setShare(ShareEntity share) {
        this.share = share;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
