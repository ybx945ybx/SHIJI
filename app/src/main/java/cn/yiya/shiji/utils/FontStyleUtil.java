package cn.yiya.shiji.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * 设置特定字体
 * Created by Tom on 2016/7/1.
 */
public class FontStyleUtil {
    private static final String HELVE_CONDEN_BOLD_PATH = "fonts/Helvetica Condensed Bold.ttf";
    private static final String HELVE_CONDEN_PATH = "fonts/Helvetica-Condensed-Thin.ttf";

    public static void setHelveCondenBoldStyle (Context context, TextView textView){
        textView.setTypeface(Typeface.createFromAsset(context.getAssets(), HELVE_CONDEN_BOLD_PATH));
    }

    public static void setHelveCondenStyle (Context context, TextView textView){
        textView.setTypeface(Typeface.createFromAsset(context.getAssets(), HELVE_CONDEN_PATH));
    }
}
