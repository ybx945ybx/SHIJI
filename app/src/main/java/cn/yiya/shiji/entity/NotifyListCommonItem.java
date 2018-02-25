package cn.yiya.shiji.entity;


/**
 * Created by tomyang on 2015/9/15.
 */
public class NotifyListCommonItem {
    private String id;
    private int type;
    private String des;
    private String time;
    private NotifyContent content;
    String icon;
    String title;
    String image;
    NotifyShareInfo h5_share;
    String url;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

    public NotifyShareInfo getH5_share() {
        return h5_share;
    }

    public void setH5_share(NotifyShareInfo h5_share) {
        this.h5_share = h5_share;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public NotifyContent getContent() {
        return content;
    }

    public void setContent(NotifyContent content) {
        this.content = content;
    }

    public String getTime() {

        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDes() {

        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getType() {

        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class NotifyShareInfo{
        String url;
        String title;
        String description;
        String cover;
        String desc;

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }
    }
}
