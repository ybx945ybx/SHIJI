package cn.yiya.shiji.views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.SelectForwardedGoodsActivity;
import cn.yiya.shiji.business.ShareTools;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.config.Constants;
import cn.yiya.shiji.utils.ShareUtils;
import cn.yiya.shiji.utils.Util;

public class ShareDialog extends Dialog implements View.OnClickListener {
	private ShareTools shareTool;
	private Context mContext;

	private String title;
	private String imageUrl;
	private String url;
	private String content;
	private boolean isActivity;					      //  活动分享。成功后调接口发红包

	private boolean beForward;					      //  售卖转发
	private List<String> paths = new ArrayList<>();
	private List<String> imgs = new ArrayList<>();
	private Bitmap bitmap;
	private Dialog progressDialog;

	public ShareDialog(Context context, String title, String imageUrl, String url, String content) {
		super(context, R.style.ActionSheetDialogStyle);
		this.mContext = context;
		shareTool = ShareTools.getInstance();
		shareTool.initShare(context);
		this.title = title;
		this.imageUrl = imageUrl;
		this.url = url;
		this.content = content;
	}

	public ShareDialog(Context context, List<String> imgs, String content, boolean beForward) {
		super(context, R.style.ActionSheetDialogStyle);
		this.mContext = context;
		shareTool = ShareTools.getInstance();
		shareTool.initShare(context);
		this.content = content;
		this.imgs = imgs;
		this.beForward = beForward;
	}

	// 活动分享
	public ShareDialog(Context context, String title, String imageUrl, String url, String content, boolean isActivity) {
		super(context, R.style.ActionSheetDialogStyle);
		this.mContext = context;
		shareTool = ShareTools.getInstance();
		shareTool.initShare(context);
		this.title = title;
		this.imageUrl = imageUrl;
		this.url = url;
		this.content = content;
		this.isActivity = isActivity;
	}

	@SuppressLint({"RtlHardcoded", "InflateParams"})
	public Dialog build() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_share, null);
		TextView tvWXZone = (TextView) view.findViewById(R.id.tv_wx_zone);
		tvWXZone.setOnClickListener(this);
		TextView tvWX = (TextView) view.findViewById(R.id.tv_wx);
		tvWX.setOnClickListener(this);
		TextView tvWeiBo = (TextView) view.findViewById(R.id.tv_weibo);
		tvWeiBo.setOnClickListener(this);
		TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
		tvCancel.setOnClickListener(this);
		setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		Window dialogWindow = getWindow();
		dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		dialogWindow.setAttributes(lp);
		return this;
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_wx_zone:
				if(beForward) {
					downloadImage(imgs, 1);
				}else if(isActivity){
					shareTool.share(ShareTools.Platform_WXZone, title, imageUrl, url, content, true, 2);
				}else {
					shareTool.share(ShareTools.Platform_WXZone, title, imageUrl, url, content);
				}
				break;
			case R.id.tv_wx:
				if (beForward) {
					downloadImage(imgs, 2);
				}else if(isActivity){
					shareTool.share(ShareTools.Platform_WX, title, imageUrl, url, content, true, 1);
				}else {
					shareTool.share(ShareTools.Platform_WX, title, imageUrl, url, content);
				}
				break;
			case R.id.tv_weibo:
				if(beForward){
					getWeiboImge(imgs);
//					shareTool.forwardWeibo(title, paths);
				}else if(isActivity){
					shareTool.shareWeiBo(imageUrl, content, url, isActivity, 3);
				}else {
					shareTool.shareWeiBo(imageUrl, content, url);
				}
				break;
			case R.id.tv_cancel:
				break;
			default:
				break;
		}
		dismiss();

	}

	private void getWeiboImge(final List<String> imgs) {
		showPreDialog("图片处理中");
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					URL downUrl = new URL(imgs.get(0));
					InputStream is = downUrl.openStream();
					bitmap = BitmapFactory.decodeStream(is);
					is.close();
					saveFile(Configration.FORWARD_PATH_CASH, bitmap, "forwardcash" + 0 + ".png", true);
					paths.add(Configration.FORWARD_PATH_CASH + "forwardcash" + 0 + ".png");
					hidePreDialog();
					shareTool.forwardWeibo(content, paths);
				}catch (Exception e){
				}
			}
		}).start();
	}

	// 下载商品图片
	private void downloadImage(final List<String> images, final int type) { //  1是朋友圈2是好友

		showPreDialog("图片处理中");
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					for(int i = 0; i < images.size(); i++){
						URL downUrl = new URL(images.get(i));
						InputStream is = downUrl.openStream();
						bitmap = BitmapFactory.decodeStream(is);
						is.close();
						saveFile(Configration.FORWARD_PATH_CASH, bitmap, "forwardcash" + i + ".png", false);
						paths.add(Configration.FORWARD_PATH_CASH + "forwardcash" + i + ".png");
					}
					hidePreDialog();
					// 提示
					mHander.sendEmptyMessage(0);
					if(type == 1){
						ShareUtils.share9PicsToWXCircle(mContext, content, paths);
					}else {
						ShareUtils.share9PicToFriendNoSDK(mContext, content, paths);
					}
				}catch (Exception e){
					hidePreDialog();
				}
			}
		}).start();
	}

	private void saveFile(String forwardPath, Bitmap bitmap, String s, boolean zip) throws IOException {
		File dirFile = new File(forwardPath);
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}
		File myCaptureFile = new File(forwardPath + s);

		if (!myCaptureFile.exists()) {
			myCaptureFile.createNewFile();
		}

		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(myCaptureFile));

		if (!myCaptureFile.exists()) {
			myCaptureFile.createNewFile();
		}

		if(zip){
			bitmap.compress(Bitmap.CompressFormat.PNG, 30, bos);
		}else {
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
		}
		bos.flush();
		bos.close();
	}

	private void showPreDialog(String str) {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
		progressDialog = ProgressDialog.creatRequestDialog(mContext, str, false);
		progressDialog.show();
	}

	private void hidePreDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	private Handler mHander = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what){
				case 0:
					Toast.makeText(mContext, "文字已复制到剪贴板", Toast.LENGTH_SHORT).show();
					break;
			}
		}
	};
}

