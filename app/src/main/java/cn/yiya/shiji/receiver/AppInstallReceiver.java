package cn.yiya.shiji.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.yiya.shiji.config.BaseApplication;

/**
 * Created by chenjian on 2015/11/12.
 */
public class AppInstallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
            BaseApplication.bUpdate = true;
        }
    }
}
