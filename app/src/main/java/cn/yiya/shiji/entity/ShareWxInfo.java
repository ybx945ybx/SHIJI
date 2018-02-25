package cn.yiya.shiji.entity;

/**
 * Created by chenjian on 2015/12/18.
 */
public class ShareWxInfo {

    String url;                         // H5地址
    String shareTimelineTitle;          // 微信朋友标题
    String shareTimelineImage;          // 微信朋友分享图片
    String shareAppMessageImage;        // 微信朋友圈分享图片
    String shareAppMessageTitle;        // 微信朋友圈分享标题
    String shareAppMessageDesc;         // 微信朋友圈分享描述

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getShareTimelineTitle() {
        return shareTimelineTitle;
    }

    public void setShareTimelineTitle(String shareTimelineTitle) {
        this.shareTimelineTitle = shareTimelineTitle;
    }

    public String getShareTimelineImage() {
        return shareTimelineImage;
    }

    public void setShareTimelineImage(String shareTimelineImage) {
        this.shareTimelineImage = shareTimelineImage;
    }

    public String getShareAppMessageImage() {
        return shareAppMessageImage;
    }

    public void setShareAppMessageImage(String shareAppMessageImage) {
        this.shareAppMessageImage = shareAppMessageImage;
    }

    public String getShareAppMessageTitle() {
        return shareAppMessageTitle;
    }

    public void setShareAppMessageTitle(String shareAppMessageTitle) {
        this.shareAppMessageTitle = shareAppMessageTitle;
    }

    public String getShareAppMessageDesc() {
        return shareAppMessageDesc;
    }

    public void setShareAppMessageDesc(String shareAppMessageDesc) {
        this.shareAppMessageDesc = shareAppMessageDesc;
    }
}
