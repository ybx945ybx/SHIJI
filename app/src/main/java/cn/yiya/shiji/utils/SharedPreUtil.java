package cn.yiya.shiji.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by chenjian on 2015/10/7.
 */
public class SharedPreUtil {

    public static String DM_APP_DB = "app_db";
    public static String splashVersion = "splashVersion"; // 引导界面版本
    public static String newAppVersion = "newAppVersion";

    public static final String SERVICE_SP = "service_sp";                       //  客服消息推送
    public static final String SERVICE_MSG = "service_msg";			            //  客服消息推送msg
    public static final String SERVICE_ID = "service_id";			            //  客服消息推送工单
    public static final String SERVICE_COMMUNITY = "service_community";			//  社区消息推送

    public static SharedPreferences getSharedPreference(Context context, String spName) {
        return context.getSharedPreferences(spName, Context.MODE_PRIVATE);
    }

    public static String getString(Context context, String spName,
                                   String key) {
        if (context != null)
            return getString(context, spName, key, "");
        else
            return "";
    }

    public static String getString(Context context, String spName,
                                   String key, String defaultVaule) {
        SharedPreferences sp = (SharedPreferences) context
                .getSharedPreferences(spName, 0);
        String s = sp.getString(key, defaultVaule);
        return s;
    }
    public static void putString(Context context, String spName,
                                 String key, String value) {
        SharedPreferences sp = getSharedPreference(context, spName);
        sp.edit().putString(key, value).commit();
    }
    public static int getInt(Context context, String spName,
                             String key) {
        return getInt(context, spName, key, -1);
    }

    public static int getInt(Context context, String spName,
                             String key, int defaultValue) {
        SharedPreferences sp = getSharedPreference(context, spName);
        int i = sp.getInt(key, defaultValue);
        return i;
    }
    public static void putInt(Context context, String spName,
                              String key, int value) {
        SharedPreferences sp = getSharedPreference(context, spName);
        sp.edit().putInt(key, value).commit();
    }

    public static void putBoolean(Context context, String spName,
                                  String key, boolean value) {
        SharedPreferences sp = getSharedPreference(context, spName);
        sp.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Context context, String spName,
                                     String key) {
        return getSharePreBoolean(context, spName, key, false);
    }

    public static boolean getSharePreBoolean(Context context, String spName,
                                             String key, boolean defaultValue) {
        SharedPreferences sp = getSharedPreference(context, spName);
        return sp.getBoolean(key, defaultValue);
    }

    public static void putLong(Context context, String spName,
                               String key, long value) {
        SharedPreferences sp = getSharedPreference(context, spName);
        sp.edit().putLong(key, value).commit();
    }
    public static long getLong(Context context, String spName,
                               String key, long defaultVaule) {
        SharedPreferences sp = getSharedPreference(context, spName);
        long i = sp.getLong(key, defaultVaule);
        return i;
    }
}
