package cn.yiya.shiji.entity.navigation;

/**
 * Created by chenjian on 2016/3/31.
 */
public class LocalPackageInfo {

    /**
     * version : 201510010070
     * url : http://www.baidu.com
     * size : 1000
     * md5 : XXXXXXX
     * status : 1//处理完成
     */

    private String version;
    private String url;
    private String size;
    private String md5;
    private boolean bNew;
    private int status;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isbNew() {
        return bNew;
    }

    public void setbNew(boolean bNew) {
        this.bNew = bNew;
    }
}
