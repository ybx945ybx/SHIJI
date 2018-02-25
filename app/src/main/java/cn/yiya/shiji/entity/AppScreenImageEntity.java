package cn.yiya.shiji.entity;

/**
 * Created by Tom on 2016/9/8.
 */
public class AppScreenImageEntity {

    /**
     * image : http://information.cdnqiniu02.qnmami.com/_1_1470381675.png
     * start_time : 2016-08-24 15:16:58
     * end_time : 2016-09-29 11:18:34
     * type : 1
     * content : http://www.qq.com
     */

    private String image;
    private int type;
    private String content;
    private Share share;

    public Share getShare() {
        return share;
    }

    public void setShare(Share share) {
        this.share = share;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public int getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public static class Share {
        private String title;
        private String cover;
        private String desc;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
