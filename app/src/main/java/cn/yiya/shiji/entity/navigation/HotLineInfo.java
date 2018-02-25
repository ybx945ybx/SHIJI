package cn.yiya.shiji.entity.navigation;

/**
 * Created by jerry on 2016/4/15.
 */
public class HotLineInfo {

    /**
     * id : 123213123
     * name : UK-line
     * cover :
     * url : http://xxx/xx1
     */

    private String id;
    private String name;
    private String cover;
    private String url;
    private ShareInfo share_info;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ShareInfo getShare_info() {
        return share_info;
    }

    public void setShare_info(ShareInfo share_info) {
        this.share_info = share_info;
    }
}
