package cn.yiya.shiji.netroid;

import android.content.Context;

import com.duowan.mobile.netroid.Listener;
import com.duowan.mobile.netroid.NetroidError;
import com.duowan.mobile.netroid.NoConnectionError;
import com.duowan.mobile.netroid.ServerError;
import com.duowan.mobile.netroid.TimeoutError;

public class NetListener<T> extends Listener<T> {
	private static final String TAG = "NetListener";
	public static final int CODE_40004 = 40004; // 已经注册过

	private Context mContext;

	public NetListener() {
	}

	public NetListener(Context mContext) {
		this.mContext = mContext;
	}

	public void handleNoNetWork() {
	}

	@Override
	public void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	public void onSuccess(T response) {
	}

	@Override
	public void onFinish() {
		super.onFinish();
	}

	@Override
	public void onError(NetroidError error) {
		super.onError(error);
		handleError(error);
	}

	protected void handleFail(Context context) {
//		ToastUtil.show(context, "请求失败");
	}

	protected void handleError(NetroidError error) {

		if (error != null) {
			if (error instanceof NoConnectionError) {
//				ToastUtil.show(mContext, "网络不可用，请检查网络状态");
			} else if (error instanceof TimeoutError) {
//				ToastUtil.show(mContext, "网络连接超时");
			} else if (error instanceof ServerError) {
//				ToastUtil.show(mContext, "请求服务器失败");
			} else if (error.networkResponse != null) {
				int code = error.networkResponse.statusCode;
				switch (code) {
					case 404:
//						ToastUtil.show(mContext, "无法连接到服务器");
						break;

					default:
						break;
				}
			} else {
//				ToastUtil.show(mContext, "请求失败");
			}
		}

	}
}
