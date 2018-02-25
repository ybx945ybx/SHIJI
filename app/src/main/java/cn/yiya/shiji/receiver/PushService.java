package cn.yiya.shiji.receiver;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat.Builder;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.igexin.sdk.PushManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.CommunityHomePageActivity;
import cn.yiya.shiji.activity.HomeIssueActivity;
import cn.yiya.shiji.activity.MessageCenterActivity;
import cn.yiya.shiji.activity.NewLocalWebActivity;
import cn.yiya.shiji.activity.NewMatchDetailActivity;
import cn.yiya.shiji.activity.NewShoppingCartActivity;
import cn.yiya.shiji.activity.NewWorkDetailsActivity;
import cn.yiya.shiji.activity.OrderDetailActivity;
import cn.yiya.shiji.activity.ServiceMessageActivity;
import cn.yiya.shiji.activity.SplashActivity;
import cn.yiya.shiji.activity.WebViewActivity;
import cn.yiya.shiji.business.GeTui;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.config.CustomEvent;
import cn.yiya.shiji.config.UMengEvent;
import cn.yiya.shiji.entity.HtmlThematicInfo;
import cn.yiya.shiji.entity.NotifyItem;
import cn.yiya.shiji.entity.ShareEntity;
import cn.yiya.shiji.utils.SharedPreUtil;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.ProgressDialog;

public class PushService extends Service {
	private int emotinoType = 0;
	private String urlMessage = "";
	private String url = "";
	private String notifyMsg;
	private int cmd = 0;
	private JSONObject options;
	private String flashId;

	private String type;
	private String activityId;            // 活动id
	private String name;			   // 活动标题
	private String share;			  // 活动分享信息
	private String hshare;			  // H5分享信息
	private ShareEntity shareEntity;
	private int workId;
	private int matchId;
	private int userId;
	private String orderNum;
	private String subOrderNum;

	private int Notification_ID_BASE = 110;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
        flags = START_STICKY;
		if(intent != null ){
			String content = intent.getStringExtra("content");
			Log.e("个推", "推送内容" + content);
			if(!TextUtils.isEmpty(content)){
				parserJosn(content);
			}
		}
		
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		//初始化个推sdk
		PushManager.getInstance().initialize(this.getApplicationContext());
		GeTui.initGeTui(getApplicationContext());
		Log.e("个推", "初始化个推");
	}

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("InlinedApi")
	private void sendNotification(){
		if(!BaseApplication.canNotify){
			return;
		}
		String str = "";
		NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		Builder builder = new Builder(this);
		switch(cmd){
			case 1:
				str = "宝妈通知您应该去完成今天的任务了！";
				break;
			case 2:
				str = "宝爸完成了今天的任务！";
				break;
			case 3:
				str = "您收到一个"+getEmotionStr(emotinoType)+"表情！";
				break;
			case 101:
				str = urlMessage;
				break;
			case 104:
				str = notifyMsg;
				builder.setContentText("您订阅的" + str + "闪购活动已经开始");
				break;
			case 106:			// 客服消息 "type": "msg" or "id"    //"id"为工单
				str = notifyMsg;
				builder.setContentText("您有一条新的客服消息");
				break;
			case 107:
				str = notifyMsg;
				builder.setContentText("您有一条新的社区消息");
				break;
			case 201:			// 打开活动详情的推送
				str = notifyMsg;
				builder.setContentText(str);
				break;
			case 203: 			// 203表示为打开h5页面的推送
				str = notifyMsg;
				builder.setContentText(str);
				break;
			case 204:			// 打开购物笔记
				str = notifyMsg;
				builder.setContentText(str);
				break;
			case 205:			// 打来搭配
				str = notifyMsg;
				builder.setContentText(str);
				break;
			case 206: 			// 打开用户详情
				str = notifyMsg;
				builder.setContentText(str);
				break;
			case 207:			// 打开订单详情
				str = notifyMsg;
				builder.setContentText(str);
				break;
			case 208:			// 打开购物车的推送
				str = notifyMsg;
				builder.setContentText(str);
				break;
			default:
				str = notifyMsg;
				break;
		}

		PendingIntent contentIntent = getPendingIntent();//PendingIntent.getActivity(this, 100,  i,  PendingIntent.FLAG_UPDATE_CURRENT);
		if(contentIntent!=null){
			builder.setContentIntent(contentIntent);
		}

		builder.setAutoCancel(true);
		Bitmap btm = BitmapFactory.decodeResource(getResources(),
				R.mipmap.ic_launcher);
		builder.setLargeIcon(btm);
		builder.setSmallIcon(R.mipmap.ic_launcher);
		builder.setContentTitle("柿集");

		builder.setPriority(Notification.PRIORITY_DEFAULT);
		builder.setTicker("New message");
		if(BaseApplication.canVibrite){
			builder.setVibrate(new long[]{1000, 500, 1000, 500});
		}
		builder.setWhen(System.currentTimeMillis());
		Notification nf= builder.build();

		Notification_ID_BASE += 1;
		nm.notify("柿集", Notification_ID_BASE, nf);
	}

	private void parserJosn(String json){
		try {
			JSONObject jObj = new JSONObject(json);
			cmd = jObj.getInt("cmd");
			notifyMsg = jObj.getString("msg");
			if(cmd != 107 && cmd != 208) {
				options = jObj.getJSONObject("options");
			}
			NotifyItem nItem = new NotifyItem();
			nItem.setTitle(notifyMsg);
			nItem.setTime(System.currentTimeMillis());
			nItem.setType(cmd);

			switch (cmd){
				case 101:
					nItem.setTitle(urlMessage);
					nItem.setDescribe(url);
					sendNotification();
					break;
				case 104:
					//判断app在后台的话弹下拉菜单，在前台弹选择对话框
					flashId = options.getString("flash_id");
					if(!Util.isBackground(getApplicationContext())){
						ProgressDialog.showCommonFlashSaleDialogCancel(getApplicationContext(), "您订阅的" + notifyMsg + "闪购活动已经开始", "暂不", "前往", new ProgressDialog.DialogClick() {
							@Override
							public void OkClick() {
								Intent intent = new Intent(getApplicationContext(), HomeIssueActivity.class);
								intent.putExtra("type", Configration.FLASH_SALE);
								intent.putExtra("flashId", flashId);
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
								startActivity(intent);
							}
							@Override
							public void CancelClick() {

							}
						});
					}else {
						sendNotification();
					}
					break;
				case 106:			// 客服消息 "type": "msg" or "id"    //"id"为工单
					type = options.getString("type");
					if(type.equals("msg")){
						SharedPreUtil.putString(getApplicationContext(), SharedPreUtil.SERVICE_SP, SharedPreUtil.SERVICE_MSG, type);
					}else if (type.equals("id")){
						SharedPreUtil.putString(getApplicationContext(), SharedPreUtil.SERVICE_SP, SharedPreUtil.SERVICE_ID, type);
					}
					sendNotification();
					break;
				case 107:			// 社区消息
					SharedPreUtil.putString(getApplicationContext(), SharedPreUtil.SERVICE_SP, SharedPreUtil.SERVICE_COMMUNITY, cmd+"");
					sendNotification();
					break;
				case 201:			// 打开活动详情的推送
					activityId = String.valueOf(options.get("id"));
					name = options.getString("name");
					if(options.has("share")){
						share = options.getString("share");
					}
					sendNotification();
					break;
				case 202:
					activityId = String.valueOf(options.get("id"));
					sendNotification();
					break;

				case 203: 			// 203表示为打开h5页面的推送
					activityId = options.getString("id");
					name = options.getString("name");
					url = options.getString("url");
					if(options.has("share")){
						hshare = options.getString("share");
					}
					sendNotification();
					break;
				case 204:			// 打开购物笔记
					workId = options.getInt("id");
					sendNotification();
					break;
				case 205:			// 打来搭配
					matchId = options.getInt("id");
					sendNotification();
					break;
				case 206: 			// 打开用户详情
					userId = options.getInt("id");
					sendNotification();
					break;
				case 207:			// 打开订单详情
					subOrderNum = options.getString("subOrderNum");
					orderNum = options.getString("orderNum");
					sendNotification();
					break;
				case 208:			// 打开购物车的推送
					sendNotification();
					break;
				default:
					sendNotification();

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private PendingIntent getPendingIntent(){
		Intent intent = null;
		PendingIntent pd = null;
        MobclickAgent.onEvent(getApplicationContext(), UMengEvent.ClickNotification);
		CustomEvent.onEvent(getApplicationContext(), ((BaseApplication)getApplication()).getDefaultTracker(), "PushService", CustomEvent.ClickNotification);
		switch(cmd){
		case 1:
			intent = new Intent(this, SplashActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			pd = PendingIntent.getActivity(this, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			break;
		case 2:
			break;
		case 3:
			break;
		case 101:
			intent = new Intent(getApplicationContext(), WebViewActivity.class);
			intent.putExtra("url", url);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			pd = PendingIntent.getActivity(this, 100,  intent,  PendingIntent.FLAG_UPDATE_CURRENT);
			break;
		case 104:
			if(BaseApplication.isInit == false){
				intent = new Intent(getApplicationContext(), SplashActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("main_activity_dest", "flash");
				intent.putExtra("flashId",flashId);
			}else {
				intent = new Intent(getApplicationContext(), HomeIssueActivity.class);
				intent.putExtra("type", Configration.FLASH_SALE);
				intent.putExtra("flashId",flashId);
			}
			pd = PendingIntent.getActivity(this, 103, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			break;
		case 106:			// 客服消息 "type": "msg" or "id"    //"id"为工单
			if(BaseApplication.isInit == false){
				intent = new Intent(getApplicationContext(), SplashActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("main_activity_dest", "service");
				intent.putExtra("type", type);
			}else {
				intent = new Intent(getApplicationContext(), ServiceMessageActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("main_activity_dest", "service");
				intent.putExtra("type", type);

				Intent intentrecive = new Intent();
				intentrecive.setAction("cn.yiya.shiji.pushReciver");
				intentrecive.putExtra("type", type);
				sendBroadcast(intentrecive);
			}

			pd = PendingIntent.getActivity(this, 106, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			break;
		case 107:
			if(BaseApplication.isInit == false){
				intent = new Intent(getApplicationContext(), SplashActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("main_activity_dest", "community");
			}else {
				intent = new Intent(getApplicationContext(), MessageCenterActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("main_activity_dest", "community");

				Intent intentrecive = new Intent();
				intentrecive.setAction("cn.yiya.shiji.pushReciver");
				intentrecive.putExtra("type", "community");
				sendBroadcast(intentrecive);

			}
			pd = PendingIntent.getActivity(this, 107, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			break;
		case 201:			// 打开活动详情的推送
			if(BaseApplication.isInit == false){
				intent = new Intent(getApplicationContext(), SplashActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("main_activity_dest", "homeIssue");
				intent.putExtra("activityId", activityId);
			}else {
				intent = new Intent(getApplicationContext(), HomeIssueActivity.class);
				intent.putExtra("activityId", activityId);
			}

			pd = PendingIntent.getActivity(this, 201, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			break;
		case 202:      // 打开专题
			HtmlThematicInfo mInfo = new HtmlThematicInfo();
			mInfo.setItemId(activityId);
			mInfo.setTypeId("2");
			String dataJson = new Gson().toJson(mInfo);
			if(BaseApplication.isInit == false){
				intent = new Intent(getApplicationContext(), SplashActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("main_activity_dest", "newLocalWebViewThem");
				intent.putExtra("data", dataJson);
				intent.putExtra("type", 2);
				intent.putExtra("url", "http://h5.qnmami.com/app/homeActivities/html/index.html");//正式
			}else {
				intent = new Intent(getApplicationContext(), NewLocalWebActivity.class);
				intent.putExtra("data", dataJson);
				intent.putExtra("type", 2);
//              intent.putExtra("url", "http://t1.h5.qnmami.com/app/homeActivities/html/index.html");//测试
				intent.putExtra("url", "http://h5.qnmami.com/app/homeActivities/html/index.html");//正式
			}
			pd = PendingIntent.getActivity(this, 202, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			break;
		case 203: 			// 203表示为打开h5页面的推送
			if(BaseApplication.isInit == false){
				intent = new Intent(getApplicationContext(), SplashActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("main_activity_dest", "newLocalWebView");
				intent.putExtra("activityId", activityId);
				intent.putExtra("name", name);
				intent.putExtra("url", url);
				intent.putExtra("hshare", hshare);
			}else {
				intent = new Intent(getApplicationContext(), NewLocalWebActivity.class);
				intent.putExtra("activityId", activityId);
				intent.putExtra("title", name);
				intent.putExtra("url", url);
				intent.putExtra("hshare", hshare);
				intent.putExtra("type", 203);
			}
			hshare = "";
			pd = PendingIntent.getActivity(this, 203, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			break;
		case 204:			// 打开购物笔记
			if(BaseApplication.isInit == false){
				intent = new Intent(getApplicationContext(), SplashActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("main_activity_dest", "newWorkDetail");
				intent.putExtra("workId", workId);
			}else {
				intent = new Intent(getApplicationContext(), NewWorkDetailsActivity.class);
				intent.putExtra("workId", workId);
			}

			pd = PendingIntent.getActivity(this, 204, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			break;
		case 205:			// 打来搭配
			if(BaseApplication.isInit == false){
				intent = new Intent(getApplicationContext(), SplashActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("main_activity_dest", "newMatchDetail");
				intent.putExtra("matchId", matchId);
			}else {
				intent = new Intent(getApplicationContext(), NewMatchDetailActivity.class);
				intent.putExtra("matchId", matchId);
			}

			pd = PendingIntent.getActivity(this, 205, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			break;
		case 206: 			// 打开用户详情
			if(BaseApplication.isInit == false){
				intent = new Intent(getApplicationContext(), SplashActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("main_activity_dest", "userDetail");
				intent.putExtra("userId", userId);
			}else {
				intent = new Intent(getApplicationContext(), CommunityHomePageActivity.class);
				intent.putExtra("userId", userId);
			}

			pd = PendingIntent.getActivity(this, 206, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			break;
		case 207:			// 打开订单详情
			if(BaseApplication.isInit == false){
				intent = new Intent(getApplicationContext(), SplashActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("main_activity_dest", "orderDetail");
				intent.putExtra("subOrderNum", subOrderNum);
				intent.putExtra("orderNum", orderNum);
			}else {
				intent = new Intent(getApplicationContext(), OrderDetailActivity.class);
				intent.putExtra("orderId", subOrderNum);
				intent.putExtra("orderno", orderNum);
			}

			pd = PendingIntent.getActivity(this, 207, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			break;
		case 208:			// 打开购物车的推送
			if(BaseApplication.isInit == false){
				intent = new Intent(getApplicationContext(), SplashActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("main_activity_dest", "shoppingCart");
			}else {
				intent = new Intent(getApplicationContext(), NewShoppingCartActivity.class);
			}

			pd = PendingIntent.getActivity(this, 208, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			break;
		default:
			intent = getPackageManager().getLaunchIntentForPackage("cn.yiya.shiji");
			pd = PendingIntent.getActivity(this, 99,  intent,  PendingIntent.FLAG_UPDATE_CURRENT);
			break;
		}
		
		return pd;
	}

	public String getEmotionStr(int emotionType){
		String emotionStr="";
		switch(emotionType){
		case 1:
			emotionStr = "早安";
			break;
		case 2:
			emotionStr = "抱抱";
			break;
		case 3:
			emotionStr = "谢谢";
			break;
		case 4:
			emotionStr = "I Love You";
			break;
		case 5:
			emotionStr = "Miss You";
			break;
		case 6:
			emotionStr = "晚安";
			break;
		default:
			break;
		}
		
		return emotionStr;
	}
}
