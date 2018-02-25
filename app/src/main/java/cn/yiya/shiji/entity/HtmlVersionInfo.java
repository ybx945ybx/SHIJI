package cn.yiya.shiji.entity;

/**
 * Created by chenjian on 2015/12/30.
 */
public class HtmlVersionInfo {

    HtmlAndroidInfo android;
    boolean empty;

    public HtmlAndroidInfo getAndroid() {
        return android;
    }

    public void setAndroid(HtmlAndroidInfo android) {
        this.android = android;
    }

    public static class HtmlAndroidInfo {
        String md5;
        String latest_version;
        String download_path;

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }

        public String getLatest_version() {
            return latest_version;
        }

        public void setLatest_version(String latest_version) {
            this.latest_version = latest_version;
        }

        public String getDownload_path() {
            return download_path;
        }

        public void setDownload_path(String download_path) {
            this.download_path = download_path;
        }
    }
}
