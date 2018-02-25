package cn.yiya.shiji.utils;

import android.content.Context;
import android.net.ConnectivityManager;

import com.ta.utdid2.android.utils.NetworkUtils;

/**
 * Created by chenjian on 2016/3/14.
 */
public class NetUtil {

    // 判断当前是否有网络链接，true表示有网络
    public static final boolean NetAvailable(Context context) {
        if (context == null) {
            return false;
        }
        boolean netSataus = false;
        ConnectivityManager cwjManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        cwjManager.getActiveNetworkInfo();

        if (cwjManager.getActiveNetworkInfo() != null) {
            netSataus = cwjManager.getActiveNetworkInfo().isAvailable();
        }

        return netSataus;
    }

    public static boolean IsInNetwork (Context context) {
        return NetAvailable(context) && NetworkUtils.isConnectInternet(context);
    }
}
