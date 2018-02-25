package cn.yiya.shiji.download;
/**
 * 类功能描述：下载任务对象</br>
 *
 * @version 1.0
 * </p>
 */
public class SQLDownLoadInfo {
    private String userID;
    private String taskID;
    private String url;
    private String filePath;
    private String fileName;
    private long fileSize;
    private long downloadSize;
    private String download;
    private String countryID;
    private String cityID;
    private String bUnZip;

    public String getCountryID() {
        return countryID;
    }

    public void setCountryID(String countryID) {
        this.countryID = countryID;
    }

    public String getCityID() {
        return cityID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }
    public String getTaskID() {
        return taskID;
    }
    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public long getFileSize() {
        return fileSize;
    }
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
    public long getDownloadSize() {
        return downloadSize;
    }
    public void setDownloadSize(long downloadSize) {
        this.downloadSize = downloadSize;
    }

    public String getbUnZip() {
        return bUnZip;
    }

    public void setbUnZip(String bUnZip) {
        this.bUnZip = bUnZip;
    }

    @Override
    public String toString() {
        return "userID="+userID+";taskID="+taskID+";url="+url+";filePath="+filePath+";fileName="+fileName+";fileSize="+fileSize+";downloadSize="+downloadSize;
    }
}
