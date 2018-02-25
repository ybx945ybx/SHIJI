package cn.yiya.shiji.entity;

/**
 * Created by Tom on 2017/1/3.
 */

public class TopicItem {

    /**
     * topic_id : 123
     * title : 标题 描述
     * cover : http://图片
     * cover_info : {"width":1600,"height":1526}
     */

    private int topic_id;
    private String title;
    private String cover;
    /**
     * width : 1600
     * height : 1526
     */

    private CoverInfoEntity cover_info;

    public void setTopic_id(int topic_id) {
        this.topic_id = topic_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setCover_info(CoverInfoEntity cover_info) {
        this.cover_info = cover_info;
    }

    public int getTopic_id() {
        return topic_id;
    }

    public String getTitle() {
        return title;
    }

    public String getCover() {
        return cover;
    }

    public CoverInfoEntity getCover_info() {
        return cover_info;
    }

    public static class CoverInfoEntity {
        private int width;
        private int height;

        public void setWidth(int width) {
            this.width = width;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }
}
