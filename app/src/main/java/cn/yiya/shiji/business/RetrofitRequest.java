package cn.yiya.shiji.business;

import android.content.Context;

import com.google.gson.Gson;
import com.ta.utdid2.android.utils.NetworkUtils;

import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.entity.StatusCode;
import cn.yiya.shiji.entity.TotalInfo;
import cn.yiya.shiji.utils.DebugUtil;
import cn.yiya.shiji.utils.NetUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by chenjian on 2016/7/26.
 * Retrofit请求后台并处理数据
 */
public class RetrofitRequest<T> {

    private Call<TotalInfo<T>> mCall;

    public RetrofitRequest(Call call) {
        mCall = call;
    }

    // 将返回的结构体再次进行解析封装
    public void parseCallBack(TotalInfo result, MsgCallBack callBack) {
        DebugUtil.e("url:" + mCall.request().url());
        DebugUtil.e("onResult:" + (result == null ? "null" : new Gson().toJson(result.getData())));
        if (result == null) {
            Context mContext = BaseApplication.getInstance().getApplicationContext();
            HttpMessage msg = new HttpMessage();

            if (!NetUtil.NetAvailable(mContext)) {
                msg.code = StatusCode.NoNetWork;
                msg.obj = "当前没有连接网络,请检查网络设置" + "(" + msg.code + ")";
            } else if (!NetworkUtils.isConnectInternet(mContext)) {
                msg.code = StatusCode.NoInternet;
                msg.obj = "当前网络无法连接" + "(" + msg.code + ")";
            } else {
                msg.code = StatusCode.UnKnow;
                msg.obj = "网络未知错误连接" + "(" + msg.code + ")";
            }

            if (callBack != null) {
                callBack.onResult(msg);
                return;
            }
            // 有数据显示判断
        } else {
            HttpMessage msg = new HttpMessage();
            if (result.getCode() == 101) {
                msg.code = 101;
                msg.message = "账号信息已过期，请重新登陆！" + "(" + msg.code + ")";
            } else {
                msg.code = result.getCode();
                msg.message = result.getMessage();
                msg.obj = result.getData();
            }
            if (callBack != null) {
                callBack.onResult(msg);
            }
        }
    }

    // 核心代码，请求服务器，并返回数据
    public void handRequest(final MsgCallBack msgCallBack) {
        mCall.enqueue(new Callback<TotalInfo<T>>() {
            @Override
            public void onResponse(Call<TotalInfo<T>> call, Response<TotalInfo<T>> response) {
                parseCallBack(response.body(), msgCallBack);
            }

            @Override
            public void onFailure(Call<TotalInfo<T>> call, Throwable t) {
                DebugUtil.e("throwable:" + t.getMessage());
            }
        });

    }

}
