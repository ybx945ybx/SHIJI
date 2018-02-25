package cn.yiya.shiji.business;

import android.app.Activity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kf5sdk.api.CallBack;
import com.kf5sdk.config.ChatActivityParamsConfig;
import com.kf5sdk.config.KF5SDKActivityParamsManager;
import com.kf5sdk.init.KF5SDKConfig;
import com.kf5sdk.init.UserInfo;
import com.kf5sdk.model.HelpCenterType;

import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.utils.Util;

/**
 * Created by chenjian on 2016/3/10.
 */
public class YiChuangYun {

    public static final int HelpCenter = 1;                      // 帮助中心界面
    public static final int FeedBack = 2;                        // 工单反馈界面
    public static final int KF5Chat = 3;                         // 即时交谈界面
    public static final int FeedBackList = 4;                    // 工单列表界面
    public static final int GOODES_DETAIL = 100;                 // 表示从商品详情进来
    public static final int ORDER_DETAIL = 200;                  // 表示从订单详情进来

    public static void openKF5(final int mode, final Activity activity, int nType, String no, final onFinishInitListener listener) {
        if (activity == null) {
            return;
        }
        UserInfo userInfo = new UserInfo();
        userInfo.name = BaseApplication.getInstance().readUserName();
        userInfo.email = BaseApplication.getInstance().readUserId() + "@shiji.com";
//        userInfo.deviceToken = Util.getUUID(activity);
        userInfo.deviceToken = BaseApplication.getInstance().readUserId();
        userInfo.appId = "00156c5aa1a3420e7aca9eaaf228560c857b97eda9dae10a";
        userInfo.helpAddress = "shijiapp.kf5.com";
        userInfo.sdkName = "Android_" + userInfo.name;

        ChatActivityParamsConfig config = new ChatActivityParamsConfig();
//        ChatActivitParamsConfig config = new ChatActivitParamsConfig();
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "手机");
        jsonObject.addProperty("value", BaseApplication.getInstance().readUserPhone());
        jsonArray.add(jsonObject);
        if (nType == GOODES_DETAIL) {
            JsonObject goodesObject = new JsonObject();
            goodesObject.addProperty("name", "商品详情");
            goodesObject.addProperty("value", no);
            jsonArray.add(goodesObject);
        } else if (nType == ORDER_DETAIL) {
            JsonObject orderObject = new JsonObject();
            orderObject.addProperty("name", "订单详情");
            orderObject.addProperty("value", no);
            jsonArray.add(orderObject);
        }

        config.setUserParams(jsonArray.toString());

//        KF5SDKActivityParamsConfig.setChatParamsConfig(config);
        KF5SDKActivityParamsManager.setChatParamsConfig(config);

        KF5SDKConfig.INSTANCE.init(activity, userInfo, new CallBack() {
            @Override
            public void onSuccess(String result) {
                if (listener != null) {
                    listener.onFinishInit();
                }
                switch (mode) {
                    case HelpCenter:
                        KF5SDKConfig.INSTANCE.startHelpCenterActivity(activity , HelpCenterType.HELPCENTERDEFAULT);
                        break;
                    case FeedBack:
                        KF5SDKConfig.INSTANCE.startFeedBackActivity(activity);
                        break;
                    case KF5Chat:
                        KF5SDKConfig.INSTANCE.startKF5ChatActivity(activity);
                        break;
                    case FeedBackList:
                        KF5SDKConfig.INSTANCE.startFeedBackListActivity(activity);
                        break;
                }
            }

            @Override
            public void onFailure(String result) {
            }
        });

    }

    public interface onFinishInitListener{
        void onFinishInit();
    }
}
