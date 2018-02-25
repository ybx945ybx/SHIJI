package cn.yiya.shiji.business;

import com.google.gson.internal.LinkedTreeMap;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;

import java.util.HashMap;


public class QiNiuCloud {
	private UploadManager uploadManager;
	private static QiNiuCloud mInstance;

	private QiNiuCloud(){
		uploadManager = new UploadManager();
	}

	public static QiNiuCloud getInstance(){
		if(mInstance == null){
			mInstance = new QiNiuCloud();
		}
		return mInstance;
	}
/*	参数说明：

	参数	类型	说明
	data	byte[]/String/File	数据，可以是byte数组，文件路径，文件。
	key	String	保存在服务器上的资源唯一标识。请参见关键概念：键值对。
	token	String	服务器分配的token。
	completionHandler	UpCompletionHandler	上传回调函数，必填。
	options	UploadOptions	如果需要进度通知、crc校验、中途取消、指定mimeType则需要填写相应字段，详见UploadOptions参数说明
	
	UploadOptions参数说明：
	参数	类型	说明
	params	String	自定义变量，key必须以 x: 开始。
	mimeType	String	指定文件的mimeType。
	checkCrc	boolean	是否验证上传文件。
	progressHandler	UpProgressHandler	上传进度回调。
	cancellationSignal	UpCancellationSignal	取消上传，当isCancelled()返回true时，不再执行更多上传。
*/
	
	public void upLoad(byte[] data, String key, String token, QiNiuCallback callBack){
		UploadOptions options = new UploadOptions(null, null, false, callBack, null);
		uploadManager.put(data, key, token, callBack, options);
	}
//
	public void upLoad(String filePath, String key, String token, QiNiuCallback callBack){
		UploadOptions options = new UploadOptions(null, null, false, callBack, null);
		uploadManager.put(filePath, key, token, callBack, options);
	}

	public static abstract class QiNiuCallback implements UpCompletionHandler,UpProgressHandler {
		
	}

	/**
	 * 上传头像  私有存储
	 * @param data
	 * @param key
	 * @param callBack
	 */
	public void upLoadToServer(final byte[] data, final String key, final MsgCallBack callBack){
		HashMap<String, String> maps = new HashMap<>();
		maps.put("key", key);
		final HttpMessage error = HttpMessage.getDefault();
		error.message = "上传失败";
		new RetrofitRequest<>(ApiRequest.getApiShiji().getQiNiuToken(maps)).handRequest(
				new MsgCallBack() {
			@Override
			public void onResult(HttpMessage msg) {
				if(msg.isSuccess()){
					LinkedTreeMap<String, String> result = (LinkedTreeMap<String, String>)msg.obj;
					upLoad(data, key, result.get("token"), new QiNiuCallback() {
						@Override
						public void progress(String arg0, double arg1) {}
						@Override
						public void complete(String key, ResponseInfo info, JSONObject arg2) {
							if(info.isOK()){
								uploadQiniuKey(key, callBack);
							}else{
								callBack.onResult(error);
							}
						}
					});
				}else{
					callBack.onResult(error);
				}
			}
		});
	}

	/**
	 * 上传单张图片
	 * @param data  要上传的图片路径
	 * @param key	  key
	 * @param callBack
	 */
	public void upLoadToServer(final String data, final String key, final MsgCallBack callBack){
		HashMap<String, String> maps = new HashMap<>();
		maps.put("key", key);
		maps.put("bucket", "qnmami-diary");
		final HttpMessage error = HttpMessage.getDefault();
		error.message = "上传失败";
		new RetrofitRequest<>(ApiRequest.getApiShiji().getQiNiuToken(maps)).handRequest(
				new MsgCallBack() {
					@Override
					public void onResult(HttpMessage msg) {
						if(msg.isSuccess()){
							LinkedTreeMap<String, String> result = (LinkedTreeMap<String, String>)msg.obj;
							upLoad(data, key, result.get("token"), new QiNiuCallback() {
								@Override
								public void progress(String arg0, double arg1) {
								}
								@Override
								public void complete(final String key, final ResponseInfo info, JSONObject arg2) {
									if (info.isOK()) {
										error.code = 0;
										error.message = key;
										callBack.onResult(error);
									} else {
										callBack.onResult(error);
									}
								}
							});
						}else{
							callBack.onResult(error);
						}
					}
				}
		);
	}

	/**
	 * 上传身份证图片
	 * @param data  要上传的图片路径
	 * @param key	  key
	 * @param callBack
	 */
	public void upLoadToIdcard(final String data, final String key, final MsgCallBack callBack){
		HashMap<String, String> maps = new HashMap<>();
		maps.put("key", key);
		maps.put("bucket", "shiji-idcard");
		final HttpMessage error = HttpMessage.getDefault();
		error.message = "上传失败";
		new RetrofitRequest<>(ApiRequest.getApiShiji().getQiNiuToken(maps)).handRequest(
				new MsgCallBack() {
			@Override
			public void onResult(HttpMessage msg) {
				if (msg.isSuccess()) {
					LinkedTreeMap<String, String> result = (LinkedTreeMap<String, String>)msg.obj;
					upLoad(data, key, result.get("token"), new QiNiuCallback() {
						@Override
						public void progress(String arg0, double arg1) {
						}

						@Override
						public void complete(final String key, final ResponseInfo info, JSONObject arg2) {
							if (info.isOK()) {
								error.code = 0;
								error.message = key;
								callBack.onResult(error);
							} else {
								callBack.onResult(error);
							}
						}
					});
				} else {
					callBack.onResult(error);
				}
			}
		});
	}

	/**
	 * 上传头像key
	 */
	private void uploadQiniuKey(String key, MsgCallBack callBack){
		new RetrofitRequest<>(ApiRequest.getApiShiji().changeHead(key)).handRequest(callBack);
	}
}
