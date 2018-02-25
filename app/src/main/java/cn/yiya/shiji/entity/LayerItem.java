package cn.yiya.shiji.entity;

/**
 * Created by Amy on 2016/7/7.
 */
public class LayerItem {

    /**
     * close : http://pic.nipic.com/2007-11-09/20071.jpg
     * image : http://pic.nipic.com/2007-11-09/200711912453162_2.jpg
     * show : 1   //1:展示，0:不展示
     */

    private String close;
    private String image;
    private int show;

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getShow() {
        return show;
    }

    public void setShow(int show) {
        this.show = show;
    }
}
