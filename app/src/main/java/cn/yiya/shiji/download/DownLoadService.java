package cn.yiya.shiji.download;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 类功能描述：下载器后台服务</br>
 * </p>
 */

public class DownLoadService extends Service {
    private static DownLoadManager downLoadManager;
    public static boolean bService = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    public static DownLoadManager getDownLoadManager(){
        return downLoadManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        downLoadManager = new DownLoadManager(DownLoadService.this);
        bService = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //释放downLoadManager
        downLoadManager.stopAllTask();
        downLoadManager = null;
        bService = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(downLoadManager == null){
            downLoadManager = new DownLoadManager(DownLoadService.this);
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
