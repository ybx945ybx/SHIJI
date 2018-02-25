package cn.yiya.shiji.business;

import android.content.Context;

import com.igexin.sdk.PushManager;

import cn.yiya.shiji.config.BaseApplication;

public class GeTui {

	public static void initGeTui(Context context){
		PushManager.getInstance().initialize(context);
	}
	
	public static void getCID(Context context){
		String id = PushManager.getInstance().getClientid(context);
		((BaseApplication)(context.getApplicationContext())).saveCID(id);
	}
}
