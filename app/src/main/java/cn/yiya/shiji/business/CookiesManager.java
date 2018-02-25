package cn.yiya.shiji.business;


import android.content.Context;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.util.List;

import okhttp3.Cookie;

/**
 * @author chenjian
 */

public class CookiesManager {

    // 将cookie设置到网页中
    public static void setUrlCookie(Context mContenx, String url) {

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(mContenx);
            CookieSyncManager.getInstance().sync();
        }

        List<Cookie> cookies = new SharedPrefsCookiePersistor(mContenx).loadAll();
        if (cookies != null) {
            for (Cookie cookie : cookies)
                cookieManager.setCookie(url, cookie.toString());
        }
    }
}






