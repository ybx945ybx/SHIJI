package cn.yiya.shiji.utils;

import android.util.Log;

import cn.yiya.shiji.config.HttpOptions;

/**
 * Created by chenjian on 2016/1/13.
 */
public class DebugUtil {

    public static String Log_Tag = "chenjian";

    public static void v(String msg) {
        if(HttpOptions.ShowLog){
            Log.v(Log_Tag, msg);
        }}
    public static void e(String msg) {

        if(HttpOptions.ShowLog){
            Log.e(Log_Tag, msg);
        }
    }
    public static void i(String msg) {

        if(HttpOptions.ShowLog){
            Log.i(Log_Tag, msg);
        }
    }
    public static void w(String msg) {

        if(HttpOptions.ShowLog){
            Log.w(Log_Tag, msg);
        }
    }
}
