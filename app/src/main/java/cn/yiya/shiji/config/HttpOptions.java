package cn.yiya.shiji.config;

/**
 * Created by chenjian on 2015/9/25.
 */
public final class HttpOptions {
    public static final boolean DeBug = false;              // 是否调试状态
    public static final boolean ShowLog = true;            // 是否显示LOG日志
    public static final boolean ShowTYPoint = false;         // 是否显示听云断点
    public static final boolean HtmlTest = false;           // html测试开关
    public static final boolean CrashDeBug = true;              // 是否调试状态
    public static String HTMLURL = "file:///sdcard/shiji/android/page";

    static {
        if (HtmlTest)
            HTMLURL = "file:///android_asset/android/page";
         else
            HTMLURL = "file:///sdcard/shiji/android/page";
    }
}
