package cn.yiya.shiji.entity.navigation;

/**
 * Created by jerry on 2016/4/15.
 */
public class NewsInfo {

    /**
     * id : 123213123
     * title : xx guys
     * brief : xxxx happen
     * cover : http://xxx/xx1.jpg
     */

    private String id;
    private String title;
    private String brief;
    private String cover;
    private String content;

    private ShareInfo share_info;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ShareInfo getShare_info() {
        return share_info;
    }

    public void setShare_info(ShareInfo share_info) {
        this.share_info = share_info;
    }
}
