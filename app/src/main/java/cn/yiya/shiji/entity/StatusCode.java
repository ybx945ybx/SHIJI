package cn.yiya.shiji.entity;

/**
 * Created by chenjian on 2016/3/14.
 */
public class StatusCode {
    /**
     * 网络链接错误code
     */
    public static final int NoNetWork = 1001;                        // 无网络链接
    public static final int NoInternet = 2001;                       // 没有接入互联网
    public static final int NoData = 3001;                           // 链接上后台，无数据
    public static final int UnKnow = 4001;                           // 未知错误连接
    public static final int NoLocationData = 5001;                   // 没有本地离线包

    /**
     * 离线下载状态码
     */
    public static final int UNDOWNLOAD = 100;                        // 未开始下载
    public static final int DOWNLOADING = 200;                       // 已经下载，但是未完成
    public static final int PAUSE_DOWNLOAD = 300;                    // 暂停状态
    public static final int SUCCESS_DOWNLOAD = 400;                  // 下载完成
    public static final int FAIL_DOWNLOAD = 500;                     // 下载失败
    public static final int UNZIP_DOWNLOAD = 600;                    // 下载解压中
}
