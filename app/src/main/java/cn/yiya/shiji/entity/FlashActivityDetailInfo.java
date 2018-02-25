package cn.yiya.shiji.entity;

/**
 * Created by Tom on 2016/2/25.
 */
public class FlashActivityDetailInfo {
    String now;
    String id;
    String name;
    String description;
    String cover;               // 封面图
    int notice;                 // 1表示已设置开枪提醒，2表示未设置开枪提醒；无该字段则不出现按钮
    String discount;
    String begin_time;
    String end_time;
    int status;                 // 1表示在销售, 2表示已下架
    ShareEntity share;

    public ShareEntity getShare() {
        return share;
    }

    public void setShare(ShareEntity share) {
        this.share = share;
    }

    public String getNow() {
        return now;
    }

    public void setNow(String now) {
        this.now = now;
    }

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

    public int getNotice() {
        return notice;
    }

    public void setNotice(int notice) {
        this.notice = notice;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(String begin_time) {
        this.begin_time = begin_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
