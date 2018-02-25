package cn.yiya.shiji.download;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.entity.navigation.LocalCityInfo;
import cn.yiya.shiji.entity.navigation.LocalCountryInfo;
import cn.yiya.shiji.entity.navigation.LocalObject;
import cn.yiya.shiji.entity.navigation.LocalPackageInfo;
import cn.yiya.shiji.utils.FileUtil;
import cn.yiya.shiji.utils.ZipExtractorTask;

/**
 * 类功能描述：下载执行类，每一个 DataKeeper对象 代表一个下载任务</br>
 *
 * @author chenjian
 * </p>
 */
public class DownLoader {
    private int TASK_START = 0;
    private int TASK_STOP = 1;
    private int TASK_PROGESS = 2;
    private int TASK_ERROR = 3;
    private int TASK_UNZIP = 4;
    private int TASK_SUCCESS = 5;

    private int ON_DOWNLOAD = 1;
    private int STOP_DOWNLOAD = 2;
    
    /**文件暂存路径*/
    private final String TEMP_FILEPATH = FileHelper.getTempDirPath();
    
    /**标识服务器是否支持断点续传*/
    private boolean isSupportBreakpoint = false;
    
    /**用户ID*/
    private String userID;
    
    private DataKeeper datakeeper;
    private HashMap<String, DownLoadListener> listenerMap;
    private DownLoadSuccess downloadsuccess;
    private SQLDownLoadInfo sqlDownLoadInfo;
    private DownLoadThread downLoadThread;
    private long fileSize = 0;//文件总大小
    private long downFileSize = 0;//已经下载的文件的大小
    private int downloadtimes = 0;//当前尝试请求的次数
    private int maxdownloadtimes = 3;//失败重新请求次数
    /**当前任务的状态 */
    private boolean ondownload = false;  
    /**线程池 */
    private ThreadPoolExecutor pool;
    private final static String SAVEFLAG = "saveflag";
    
    
    /**
     * @param context 上下文
     * @param sqlFileInfo 任务信息对象
     * @param pool  线程池
     * @param userID  用户ID
     * @param isSupportBreakpoint  服务器是否支持断点续传
     * @param isNewTask 标识是新任务还是根据数据库构建的任务
     */
    public DownLoader(Context context, SQLDownLoadInfo sqlFileInfo, ThreadPoolExecutor pool, String userID, boolean isSupportBreakpoint, boolean isNewTask){
        this.isSupportBreakpoint = isSupportBreakpoint;
        this.pool = pool;
        this.userID = userID;
        fileSize = sqlFileInfo.getFileSize();
        downFileSize = sqlFileInfo.getDownloadSize();
        datakeeper = new DataKeeper(context);
        listenerMap = new HashMap<String,DownLoadListener>();
        sqlDownLoadInfo = sqlFileInfo;
        //新建任务，保存任务信息到数据库
        if(isNewTask){
            saveDownloadInfo();
        }
    }
    
    public String getTaskID(){
        return sqlDownLoadInfo.getTaskID();
    }
    
    public void start(){
        if(downLoadThread == null){
            downloadtimes = 0;
            ondownload = true;
            handler.sendEmptyMessage(TASK_START);
            downLoadThread = new DownLoadThread();
            pool.execute(downLoadThread);
        }
    }
    
    public void stop(){
        if(downLoadThread != null){
            ondownload = false;
            downLoadThread.stopDownLoad();
            saveDownloadInfo();
            saveDownLoadInfoStatus("F");
            pool.remove(downLoadThread);
            downLoadThread = null;
        }
    }
    
    public void setDownLoadListener(String key, DownLoadListener listener){
        if (listener == null) {
            removeDownLoadListener(key);
        }else {
            listenerMap.put(key, listener);
        }
    }

    public void removeDownLoadListener(String key){
        if (listenerMap.containsKey(key)) {
            listenerMap.remove(key);
        }
    }
    
    public void setDownLodSuccesslistener(DownLoadSuccess downloadsuccess){
        this.downloadsuccess = downloadsuccess;
    }
    
    public void destroy(){
        if(downLoadThread != null){
            downLoadThread.stopDownLoad();
            downLoadThread = null;
        }
        datakeeper.deleteDownLoadInfo(userID,sqlDownLoadInfo.getTaskID());
        File downloadFile = new File(TEMP_FILEPATH + "/" + sqlDownLoadInfo.getCountryID() + "/" + sqlDownLoadInfo.getCityID() + "/" + sqlDownLoadInfo.getFileName());
        if(downloadFile.exists()){
            downloadFile.delete();
        }
    }
    
    /**当前任务进行的状态 */
    public boolean isDownLoading(){
        return ondownload;
    }
    
    /**
     * (获取当前任务对象) 
     * @return
     */
    public SQLDownLoadInfo getSQLDownLoadInfo(){
        sqlDownLoadInfo.setDownloadSize(downFileSize);
        return sqlDownLoadInfo;
    }
    
    
    /**
     * (设置是否支持断点续传) 
     * @param isSupportBreakpoint
     */
    public void setSupportBreakpoint(boolean isSupportBreakpoint) {
        this.isSupportBreakpoint = isSupportBreakpoint;
    }


    /**
     * 类功能描述：文件下载线程</br>
     */
    class DownLoadThread extends Thread {
        private boolean isdownloading;
        private URL url;
        private RandomAccessFile localFile;
        private HttpURLConnection urlConn;
        private InputStream inputStream;
        private int progress = -1;
        
        public DownLoadThread(){
            isdownloading = true;
        }
        
        @Override
        public void run() {
            while(downloadtimes < maxdownloadtimes){ //做3次请求的尝试
                
                try {
                    if(downFileSize == fileSize 
                            && fileSize > 0){
                        ondownload = false;
                        Message msg = new Message();
                        msg.what = TASK_PROGESS;
                        msg.arg1 = 100;
                        handler.sendMessage(msg);
                        downloadtimes = maxdownloadtimes;
                        downLoadThread = null;
                        if (getSQLDownLoadInfo().getbUnZip().equals("F")) {
                            unZipFile();
                        }
                        return;
                    }
                    url = new URL(sqlDownLoadInfo.getUrl());
                    urlConn = (HttpURLConnection)url.openConnection();
                    urlConn.setConnectTimeout(5000);
                    urlConn.setReadTimeout(10000);
                    if(fileSize < 1){//第一次下载，初始化
                        openConnention();
                    }else{
                        if(new File(TEMP_FILEPATH + "/" + sqlDownLoadInfo.getCountryID() + "/" + sqlDownLoadInfo.getCityID()
                                + "/" + sqlDownLoadInfo.getFileName()).exists()){
                            localFile = new RandomAccessFile(TEMP_FILEPATH + "/" + sqlDownLoadInfo.getCountryID() + "/" +
                                    sqlDownLoadInfo.getCityID() + "/" + sqlDownLoadInfo.getFileName(),"rwd");
                            localFile.seek(downFileSize);
                            urlConn.setRequestProperty("Range", "bytes=" + downFileSize + "-"); 
                        }else{
                            fileSize = 0;
                            downFileSize = 0;
                            saveDownloadInfo();
                            openConnention();
                        }
                    }
                    inputStream = urlConn.getInputStream();
                    byte[] buffer = new byte[1024 * 4];
                    int length = -1;
                    while((length = inputStream.read(buffer)) != -1 && isdownloading){
                        localFile.write(buffer, 0, length);
                        downFileSize += length;
                        int nowProgress = (int)((100 * downFileSize)/fileSize);
                        if(nowProgress > progress){
                            progress = nowProgress;
                            handler.sendEmptyMessage(TASK_PROGESS);
                        }
                    }
                    //下载完了
                    if(downFileSize == fileSize ){
                        boolean renameResult = RenameFile();
                        if(renameResult){
                            unZipFile();
                            handler.sendEmptyMessage(TASK_UNZIP); //解压文件
                        }else{
                            new File(TEMP_FILEPATH + "/" + sqlDownLoadInfo.getCountryID() + "/" + sqlDownLoadInfo.getCityID()
                                    + "/" + sqlDownLoadInfo.getFileName()).delete();
                            handler.sendEmptyMessage(TASK_ERROR);//转移文件失败
                        }
                        //清除数据库任务
//                        datakeeper.deleteDownLoadInfo(userID,sqlDownLoadInfo.getTaskID());
                        saveDownloadInfo();
                        downLoadThread = null;
                        ondownload = false;
                    }
                    downloadtimes = maxdownloadtimes;
                } catch (Exception e) {
                    if(isdownloading){
                        if(isSupportBreakpoint){
                            downloadtimes ++;
                            if(downloadtimes >= maxdownloadtimes){
                                if(fileSize > 0){
                                    saveDownloadInfo();
                                }
                                pool.remove(downLoadThread);
                                downLoadThread = null;
                                ondownload = false;
                                handler.sendEmptyMessage(TASK_ERROR);
                            }
                        }else{
                            downFileSize = 0;
                            downloadtimes = maxdownloadtimes;
                            ondownload = false;
                            downLoadThread = null;
                            handler.sendEmptyMessage(TASK_ERROR);
                        }
                        
                    }else{
                        downloadtimes = maxdownloadtimes; 
                    }
                    e.printStackTrace();
                }finally{
                    try {
                        if(urlConn != null){
                            urlConn.disconnect();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        if(inputStream != null){
                            inputStream.close();
                        }
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                    try {
                       if(localFile != null){
                           localFile.close();
                       }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            
        }
        
        public void stopDownLoad(){
            isdownloading = false;
            downloadtimes = maxdownloadtimes;
            if(fileSize > 0){
                saveDownloadInfo();
            }
            handler.sendEmptyMessage(TASK_STOP);
        }
        
        private void openConnention() throws Exception {
            long urlfilesize = urlConn.getContentLength();
            if(urlfilesize > 0){
                isFolderExist();
                localFile = new RandomAccessFile(TEMP_FILEPATH + "/" + sqlDownLoadInfo.getCountryID() + "/" + sqlDownLoadInfo.getCityID()
                        + "/" + sqlDownLoadInfo.getFileName(), "rwd");
                localFile.setLength(urlfilesize);
                sqlDownLoadInfo.setFileSize(urlfilesize);
                fileSize = urlfilesize;
                if(isdownloading){
                    saveDownLoadInfoStatus("T");
                }
            }
        }
        
    }
    
    
    /**
     * (判断文件夹是否存在，不存在则创建) 
     * @return
     */
    private boolean isFolderExist(){
        boolean result = false;
        try{
            String filepath = TEMP_FILEPATH + "/" + sqlDownLoadInfo.getCountryID() + "/" + sqlDownLoadInfo.getCityID();
            File file = new File(filepath);
            if(!file.exists()){
                if(file.mkdirs()){
                    result = true;
                }
            }else{
                result = true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * (保存下载信息至数据库) 
     */
    private void saveDownloadInfo(){
            if(isSupportBreakpoint){
                sqlDownLoadInfo.setDownloadSize(downFileSize);
                datakeeper.saveDownLoadInfo(sqlDownLoadInfo);
            }
    }

    public void saveDownLoadInfoStatus(String s) {
        if(isSupportBreakpoint){
            sqlDownLoadInfo.setDownload(s);
            datakeeper.saveDownLoadInfo(sqlDownLoadInfo);
        }
    }

    public void saveDownLoadInfoZip(String s) {
        if(isSupportBreakpoint){
            sqlDownLoadInfo.setbUnZip(s);
            datakeeper.saveDownLoadInfo(sqlDownLoadInfo);
        }
    }
    
    /**
     * (通知监听器，任务已开始下载) 
     */
    private void startNotice(){
        if(!listenerMap.isEmpty()){
            Collection<DownLoadListener> c = listenerMap.values();
            Iterator<DownLoadListener> it = c.iterator();
            while(it.hasNext()){
                DownLoadListener listener = (DownLoadListener)it.next();
                listener.onStart(getSQLDownLoadInfo());
            }
        }
    }
    
    /**
     * (通知监听器，当前任务进度) 
     */
    private void onProgressNotice(){
        if(!listenerMap.isEmpty()){
            Collection<DownLoadListener> c = listenerMap.values();
            Iterator<DownLoadListener> it = c.iterator();
            while(it.hasNext()){
                DownLoadListener listener = (DownLoadListener)it.next();
                listener.onProgress(getSQLDownLoadInfo(), isSupportBreakpoint);
            } 
        }
    }
    
    /**
     * (通知监听器，当前任务已停止) 
     */
    private void stopNotice(){
        if(!isSupportBreakpoint){
            downFileSize = 0;
        }
        if(!listenerMap.isEmpty()){
            Collection<DownLoadListener> c = listenerMap.values();
            Iterator<DownLoadListener> it = c.iterator();
            while(it.hasNext()){
                DownLoadListener listener = (DownLoadListener)it.next();
                listener.onStop(getSQLDownLoadInfo(),isSupportBreakpoint);
            }
        }
    }
    
    /**
     * (通知监听器，当前任务异常，并进入停止状态) 
     */
    private void errorNotice(){
        if(!listenerMap.isEmpty()){
            Collection<DownLoadListener> c = listenerMap.values();
            Iterator<DownLoadListener> it = c.iterator();
            while(it.hasNext()){
                DownLoadListener listener = (DownLoadListener)it.next();
                listener.onError(getSQLDownLoadInfo());
            }
        }
    }

    private void unZipFileNotice() {
        if(!listenerMap.isEmpty()){
            Collection<DownLoadListener> c = listenerMap.values();
            Iterator<DownLoadListener> it = c.iterator();
            while(it.hasNext()){
                DownLoadListener listener = (DownLoadListener)it.next();
                listener.onUnZip(getSQLDownLoadInfo());
            }
        }
    }

    private void unZipFile() {
        String pathZip = getSQLDownLoadInfo().getFilePath();
        final String pathFile = pathZip.substring(0, pathZip.lastIndexOf("/") + 1);
        ZipExtractorTask task = new ZipExtractorTask(ZipExtractorTask.TARGZ_TYPE, pathZip, pathFile, null, true, new ZipExtractorTask.OnFinishUnZipListner() {
            @Override
            public void onFinishUnZip() {
                setLocalData(getTaskID(), pathFile);
            }

            @Override
            public void onFailDownload() {
                errorNotice();
            }
        });
        saveDownLoadInfoZip("F");
        task.execute();
    }

    private synchronized void setLocalData(String taskId, String path) {

        Context mContext = BaseApplication.getInstance();

        SharedPreferences sp = mContext.getSharedPreferences(Configration.SHAREDPREFERENCE, mContext.MODE_PRIVATE);
        String jsonSave = sp.getString(SAVEFLAG, "");
        LocalObject object;
        if (TextUtils.isEmpty(jsonSave)) {
            return;
        } else {
           object  = new Gson().fromJson(jsonSave, LocalObject.class);
        }

        if (object == null || object.getPakages() == null) {
            return;
        }

        List<LocalCountryInfo> countryInfos = object.getPakages();
        for (int i = 0; i < countryInfos.size(); i++) {
            ArrayList<LocalCityInfo> cityInfos = countryInfos.get(i).getCities();
            for (int j = 0; j < cityInfos.size(); j++) {
                LocalCityInfo info = cityInfos.get(j);
                if (info.getCityID().equals(taskId)) {
                    countryInfos.get(i).setbSuccess(true);
                    info.setbSuccess(true);
                    ArrayList<LocalPackageInfo> verInfos = info.getVersions();
                    for (int k = 0; k < verInfos.size(); k++) {
                        LocalPackageInfo vInfo = verInfos.get(k);
                        String md5 = FileUtil.getMD5Checksum(path);
                        if (vInfo.getMd5().equals(md5)) {
                            vInfo.setStatus(1);
                            vInfo.setbNew(true);
                        } else {
                            vInfo.setbNew(false);
                        }
                    }
                }
            }
        }

        SharedPreferences.Editor editor = sp.edit();
        BaseApplication.localObject = object;
        String json = new Gson().toJson(object);
        editor.putString(SAVEFLAG, json);
        editor.commit();

        successNotice();
        saveDownLoadInfoZip("T");
        saveDownLoadInfoStatus("F");
        saveDownloadInfo();
    }
    
    /**
     * (通知监听器，当前任务成功执行完毕) 
     */
    private void successNotice(){
        if(!listenerMap.isEmpty()){
            Collection<DownLoadListener> c = listenerMap.values();
            Iterator<DownLoadListener> it = c.iterator();
            while(it.hasNext()){
                DownLoadListener listener = (DownLoadListener)it.next();
                listener.onSuccess(getSQLDownLoadInfo());
            }
        }
        if(downloadsuccess != null){
            downloadsuccess.onTaskSeccess(sqlDownLoadInfo.getTaskID());
        }
    }
    
    /**
     * 类功能描述：该接口用于在任务执行完之后通知下载管理器,以便下载管理器将已完成的任务移出任务列表</br>
     */
    public interface DownLoadSuccess{
        public void onTaskSeccess(String TaskID);
    }
    
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == TASK_START){ //开始下载
                startNotice();
            }else if(msg.what == TASK_STOP){ //停止下载
                stopNotice();
            }else if(msg.what == TASK_PROGESS){ //改变进程
                onProgressNotice();
            }else if(msg.what == TASK_ERROR){ //下载出错
                errorNotice();
            }else if(msg.what == TASK_SUCCESS){ //下载完成
                successNotice();
            } else if(msg.what == TASK_UNZIP) {  // 解压文件
                unZipFileNotice();
            }
        }
    };
    
    public boolean RenameFile(){
        File newfile = new File(sqlDownLoadInfo.getFilePath());
        if(newfile.exists()){
            newfile.delete();
        }
        File oldfile = new File(TEMP_FILEPATH + "/" + sqlDownLoadInfo.getCountryID() + "/" + sqlDownLoadInfo.getCityID()
                + "/" + sqlDownLoadInfo.getFileName());
        
        String filepath = sqlDownLoadInfo.getFilePath();
        filepath = filepath.substring(0, filepath.lastIndexOf("/"));
        File file = new File(filepath);
        if(!file.exists()){
            file.mkdirs();
        }
        return oldfile.renameTo(newfile);
    }
}
