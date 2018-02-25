package cn.yiya.shiji.business;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.kf5sdk.utils.Utils;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.sina.weibo.sdk.utils.LogUtil;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.BaseAppCompatActivity;
import cn.yiya.shiji.activity.SelectForwardedGoodsActivity;
import cn.yiya.shiji.activity.WeiboAuthActivity;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.config.Constants;
import cn.yiya.shiji.entity.ShareFinishEntity;
import cn.yiya.shiji.entity.TotalInfo;
import cn.yiya.shiji.utils.Util;

public class ShareTools {
    private Context mContext;
    private static ShareTools instance;
    //public static final int Platform_QQ =0;
    public static final int Platform_WX = 1;
    //public static final int Platform_SMS =2;
    public static final int Platform_WXZone = 3;
    //public static final int Platform_QQZone = 4;
    public static final int Platform_WeiBo = 5;
    public static final String LogoUrl = "http://7xjc5h.com2.z0.glb.qiniucdn.com/512pt_@1x.png";

    private IWeiboShareAPI mWeiboShareAPI = null;
    /** 当前 Token 信息 */
    private Oauth2AccessToken mAccessToken;
    /** 用于获取微博信息流等操作的API */
    private StatusesAPI mStatusesAPI;

    private boolean isActivity;
    private int shareTo;						//  分享到哪里。1-微信朋友、2-微信朋友圈、3-微博
    private String url;                         //  分享出去的url

    private ShareTools() {
    }

    public static ShareTools getInstance() {
        if (instance == null) {
            instance = new ShareTools();
        }
        return instance;
    }

    //初始化分享
    public void initShare(Context context) {
        ShareSDK.initSDK(context);
        this.mContext = context;
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context, Constants.APP_KEY);
        mWeiboShareAPI.registerApp();
    }

    public void share(int type, String title, final String imageUrl, String url, final String content) {
        IWXAPI api = WXAPIFactory.createWXAPI(mContext, Configration.WX_APPID, false);
        if (!api.isWXAppInstalled()) {
            Utils.showMessageToast(mContext, "请先安装微信客户端");
            return;
        }
        this.isActivity = false;
        final ShareParams sp = new ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setTitle(title);
        sp.setText(content);
        sp.setUrl(url);
        if(!TextUtils.isEmpty(imageUrl)) {
            sp.setImageUrl(imageUrl);
        }else {
            sp.setImageUrl("");
        }

        String platformName = "";
        switch (type) {
            case Platform_WX:
                platformName = Wechat.NAME;
                break;
            case Platform_WXZone:
                platformName = WechatMoments.NAME;
                break;
            default:
                return;
        }
        Platform platform = ShareSDK.getPlatform(platformName);
        platform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                mHandler.sendEmptyMessage(2);
            }

            @Override
            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
//				DebugUtil.e("分享成功");
                mHandler.sendEmptyMessage(1);
            }

            @Override
            public void onCancel(Platform arg0, int arg1) {
                mHandler.sendEmptyMessage(3);
            }
        }); // 设置分享事件回调// 设置分享事件回调
        // 执行图文分享
        platform.share(sp);
    }

    public void share(int type, String title, final String imageUrl, String url, final String content, boolean isActivity, int shareTo) {
        IWXAPI api = WXAPIFactory.createWXAPI(mContext, Configration.WX_APPID, false);
        if (!api.isWXAppInstalled()) {
            Utils.showMessageToast(mContext, "请先安装微信客户端");
            return;
        }
        this.isActivity = isActivity;
        this.shareTo = shareTo;
        this.url = url;
        final ShareParams sp = new ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setTitle(title);
        sp.setText(content);
        sp.setUrl(url);
        if(!TextUtils.isEmpty(imageUrl)) {
            sp.setImageUrl(imageUrl);
        }else {
            sp.setImageUrl("");
        }

        String platformName = "";
        switch (type) {
            case Platform_WX:
                platformName = Wechat.NAME;
                break;
            case Platform_WXZone:
                platformName = WechatMoments.NAME;
                break;
            default:
                return;
        }
        Platform platform = ShareSDK.getPlatform(platformName);
        platform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                mHandler.sendEmptyMessage(2);
            }

            @Override
            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
//				DebugUtil.e("分享成功");
                mHandler.sendEmptyMessage(1);
            }

            @Override
            public void onCancel(Platform arg0, int arg1) {
                mHandler.sendEmptyMessage(3);
            }
        }); // 设置分享事件回调// 设置分享事件回调
        // 执行图文分享
        platform.share(sp);
    }

    // Handler 可以接受或者发送消息,从消息队列中提取消息,用户更新UI的操作
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    if(isActivity){
                        HashMap<String, String> maps = new HashMap<>();
                        maps.put("share-url", url);
                        maps.put("share-to", String.valueOf(shareTo));
                        maps.put("result", String.valueOf(1));
                        new RetrofitRequest<TotalInfo<ShareFinishEntity>>(ApiRequest.getApiShiji().shareFinish(maps)).handRequest(new MsgCallBack() {
                            @Override
                            public void onResult(HttpMessage msg) {
                                if(msg.isSuccess()){
                                    ShareFinishEntity obj = (ShareFinishEntity) msg.obj;
                                    if(obj == null){
                                        Utils.showMessageToast(mContext, "分享成功");
                                    }else {
                                        if (TextUtils.isEmpty(obj.getShow())) {
                                            Utils.showMessageToast(mContext, "分享成功");
                                        } else {
                                            Utils.showMessageToast(mContext, obj.getShow());
                                        }
                                    }
                                }else {
                                    Utils.showMessageToast(mContext, "分享成功");
                                }
                            }
                        });
                    }else {
                        Utils.showMessageToast(mContext, "分享成功");
                    }
                    break;
                case 2:
                    Utils.showMessageToast(mContext, "分享失败");
                    break;
                case 3:
                    Utils.showMessageToast(mContext, "分享取消");
                    break;
                default:
                    break;
            }
        }
    };

    // 微博分享
    public void shareWeiBo(String imageUrl, String content, String url) {
        if(!Util.isWeiboInstalled(mContext)){
            Utils.showMessageToast(mContext, "请先安装新浪微博客户端");
            return;
        }
        if(!initWeiBo()){
            authWeiBo();
            return;
        }
        String photoURL = imageUrl;
//        Toast.makeText(mContext, "要分享了", Toast.LENGTH_SHORT).show();
        mStatusesAPI.uploadUrlText(content + url, photoURL, null, null, null, mListener);
    }

    public void shareWeiBo(String imageUrl, String content, String url, boolean isActivity, int shareTo) {
        if(!Util.isWeiboInstalled(mContext)){
            Utils.showMessageToast(mContext, "请先安装新浪微博客户端");
            return;
        }
        if(!initWeiBo()){
            authWeiBo();
            return;
        }
        String photoURL = imageUrl;
        this.shareTo = shareTo;
        this.isActivity = isActivity;
        // 请注意：该接口暂不支持发布多图，即参数pic_id不可用（只能通过BD合作接入，不对个人申请）
        mStatusesAPI.uploadUrlText(content + url, photoURL, null, null, null, mListener);
    }

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     */
    public void forwardWeibo(String kdescription, List<String> paths) {
//        Utils.showMessageToast(mContext, "进来了");
        if(!Util.isWeiboInstalled(mContext)){
            Utils.showMessageToast(mContext, "请先安装新浪微博客户端");
            return;
        }
        if(!initWeiBo()){
            authWeiBo();
            return;
        }
        // 1. 初始化微博的分享消息
        // 用户可以分享文本、图片、网页、音乐、视频中的一种
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        TextObject textObject = new TextObject();
        textObject.text = kdescription;
        weiboMessage.textObject = textObject;

        ImageObject imageObject = new ImageObject();
        //设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
//        Bitmap  bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.work_default);
        Bitmap  bitmap = BitmapFactory.decodeFile(paths.get(0));
        imageObject.setImageObject(bitmap);
        weiboMessage.imageObject = imageObject;

        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;

//		Toast.makeText(mContext, "要唤起微博了", Toast.LENGTH_SHORT).show();
        // 3. 发送请求消息到微博，唤起微博分享界面
        mWeiboShareAPI.sendRequest((SelectForwardedGoodsActivity)mContext, request);
    }

    private boolean initWeiBo(){
        // 获取当前已保存过的 Token
        mAccessToken = AccessTokenKeeper.readAccessToken(mContext);
        // 对statusAPI实例化
        if(mAccessToken.isSessionValid()) {
            mStatusesAPI = new StatusesAPI(mContext, Constants.APP_KEY, mAccessToken);
            return true;
        }
        return false;
    }

    private void authWeiBo(){
        Intent intent = new Intent(mContext, WeiboAuthActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 微博 OpenAPI 回调接口。
     */
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
//                LogUtil.i(TAG, response);
                if (response.startsWith("{\"statuses\"")) {
                    // 调用 StatusList#parse 解析字符串成微博列表对象
                    StatusList statuses = StatusList.parse(response);
                    if (statuses != null && statuses.total_number > 0) {
                        Toast.makeText(mContext, "获取微博信息流成功, 条数: " + statuses.statusList.size(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.startsWith("{\"created_at\"")) {
                    // 调用 Status#parse 解析字符串成微博对象
//                    Status status = Status.parse(response);
//                    Toast.makeText(mContext,
//                            "发送一送微博成功, id = " + status.id,
//                            Toast.LENGTH_LONG).show();
                    if(isActivity){
                        HashMap<String, String> maps = new HashMap<>();
                        maps.put("share-url", url);
                        maps.put("share-to", String.valueOf(3));
                        maps.put("result", String.valueOf(1));
                        new RetrofitRequest<TotalInfo<ShareFinishEntity>>(ApiRequest.getApiShiji().shareFinish(maps)).handRequest(new MsgCallBack() {
                            @Override
                            public void onResult(HttpMessage msg) {
                                if(msg.isSuccess()){
                                    ShareFinishEntity obj = (ShareFinishEntity) msg.obj;
                                    if(obj == null){
                                        Utils.showMessageToast(mContext, "分享成功");
                                    }else {
                                        if (TextUtils.isEmpty(obj.getShow())) {
                                            Utils.showMessageToast(mContext, "分享成功");
                                        } else {
                                            Utils.showMessageToast(mContext, obj.getShow());
                                        }
                                    }
                                }else {
                                    Utils.showMessageToast(mContext, "分享成功");
                                }
                            }
                        });
                    }else {
                        Utils.showMessageToast(mContext, "分享成功");
                    }
                } else {
                    Toast.makeText(mContext, response, Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
//            LogUtil.e(TAG, e.getMessage());
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
            Toast.makeText(mContext, info.toString(), Toast.LENGTH_LONG).show();
        }
    };
}
