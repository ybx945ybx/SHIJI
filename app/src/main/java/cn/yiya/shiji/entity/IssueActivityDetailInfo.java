package cn.yiya.shiji.entity;

/**
 * Created by Tom on 2016/2/25.
 */
public class IssueActivityDetailInfo {
    int id;                 // 编号
    int thematic_id;        // 专题编号
    String cover;           // 活动图片
    String description;     // 活动描述
    String name;            // 标题
    String title;           // 标题
    ShareEntity share;      //分享

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getThematic_id() {
        return thematic_id;
    }

    public void setThematic_id(int thematic_id) {
        this.thematic_id = thematic_id;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ShareEntity getShare() {
        return share;
    }

    public void setShare(ShareEntity share) {
        this.share = share;
    }
}
