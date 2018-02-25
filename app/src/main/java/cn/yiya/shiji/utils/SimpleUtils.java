/**
 * @Title: SimpleUtils.java
 * @Package cn.net_show.doctor.utils
 * @author 王帅
 * @date 2015年3月2日 上午11:59:56
 */
package cn.yiya.shiji.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.yiya.shiji.config.Configration;

/**
 * @author 王帅
 * @ClassName: SimpleUtils
 * @date 2015年3月2日 上午11:59:56
 */
public class SimpleUtils {
    /**
     * @param context 上下文
     * @param uri
     * @return File
     * @Title: Uri2File
     * @Description: 将系统传过来的Uri转化为File文件描述符
     */
    public static File Uri2File(Context context, Uri uri) {

        String[] proj = {MediaStore.Images.Media.DATA};

        Cursor actualimagecursor = context.getContentResolver().query(uri,
                proj, null, null, null);

        int actual_image_column_index = actualimagecursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        actualimagecursor.moveToFirst();

        String img_path = actualimagecursor
                .getString(actual_image_column_index);

        File file = new File(img_path);
        Log.i("FILE", "file size = " + file.length());
        actualimagecursor.close();

        return file;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static long randomTime(String beginDate, String endDate) {

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",
                    Locale.CHINA);
            Date start = format.parse(beginDate);// 构造开始日期
            Date end = format.parse(endDate);// 构造结束日期
            // getTime()表示返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的毫秒数。
            if (start.getTime() >= end.getTime()) {
                return 0;
            }
            long date = random(start.getTime(), end.getTime());
            // return new Date(date);
            return date;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }

    private static long random(long begin, long end) {
        long rtn = begin + (long) (Math.random() * (end - begin));
        // 如果返回的是开始时间和结束时间，则递归调用本函数查找随机值
        if (rtn == begin || rtn == end) {
            return random(begin, end);
        }
        return rtn;
    }

    /**
     * 获取外置存储路径
     *
     * @return File
     * @Title: getExtraStoragePath
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    public static File getExtraStoragePath() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File path = Environment.getExternalStorageDirectory();
            return path;
        } else {
            return null;
        }
    }

    /**
     * 判断外置存储是否可用
     *
     * @return boolean
     * @Title: isExtraStorageEnable
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    public static boolean isExtraStorageEnable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 验证手机号
     *
     * @param str
     * @return boolean
     * @Title: isMobile
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    public static String encodePassword(String password) {

        try {
            // String pd = DigestUtils.sha256Hex(password);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = password.getBytes();
            md.update(bytes);
            byte[] b = md.digest();
            String str = bytes2HexString(b);
            Log.e("sha256", str);
            return Base64.encodeToString(str.getBytes(), Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
            return password;
        }

    }

    public static String getFileNameFromPath(String path) {
        if (path == null || "".equals(path.trim())) {
            return null;
        }

        String[] arr = path.trim().split("/");

        return arr[arr.length - 1];
    }

    public static String bytes2HexString(byte[] bs) {
        StringBuilder sb = new StringBuilder("");
        int bit;
        char[] chars = "0123456789ABCDEF".toCharArray();
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
        }

        return sb.toString();
    }

    /**
     * @param list
     * @param selected
     * @return
     */
    public static int getPosition(ArrayList<String> list, String selected) {
        if (TextUtils.isEmpty(selected)) {
            return 0;
        }
        int position = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).trim().equals(selected.trim())) {
                position = i;
                break;
            }
        }
        return position;
    }

    public static void saveImageToSdCard(Context context, String name,
                                         Bitmap bmp) {
        File file = new File(context.getFilesDir(), name);
        BitmapTool.bitmap2File(bmp, file);
    }

//	public static void showImage(final ImageView imageView, final String key,
//								 final Context context) {
//		if (imageView == null || TextUtils.isEmpty(key)) {
//			return;
//		}
//		final Handler handler = new Handler(context.getMainLooper());
//		File file = new File(context.getFilesDir(), key);
//		if (file.exists()) {
//			imageView.setImageURI(Uri.fromFile(file));
//		} else {
//			HashMap<String, Object> maps = new HashMap<>();
//			maps.put(Configration.HttpString.QiNiu_Key, key);
//			HttpRequest httpRequest = HttpRequest.getInstance();
//			httpRequest.getQiNiuDownloadUrl(new MsgCallBack() {
//				@Override
//				public void onResult(HttpMessage msg) {
//					if (msg.isSuccess()) {
//						// HttpClientUtils.download(msg.obj.toString(), key,
//						// context.getFilesDir());
//						Log.e("downloadUrl", msg.obj.toString());
//						if (HttpClientUtils.download(msg.obj.toString(), key,
//								context.getFilesDir())) {
//							handler.post(new Runnable() {
//								@Override
//								public void run() {
//									try {
//										File file = new File(context.getFilesDir(), key);
//										// Bitmap bmp =
//										// BitmapFactory.decodeFile(file.getAbsolutePath());
//										// Bitmap photo =
//										// BitmapTool.getRoundedCornerBitmap(bmp);
//										// SimpleUtils.saveImageToSdCard(context,
//										// key, bmp);
//										// imageView.setImageBitmap(photo);
//										// bmp.recycle();
//										imageView.setImageURI(Uri.fromFile(file));
//									} catch (Exception e) {
//										// TODO Auto-generated catch block
//										e.printStackTrace();
//									}
//								}
//							});
//						}
//					}
//				}
//			}, maps, null);
//		}
//	}

    public static synchronized void writeLog(String title, String content) {
        Log.e("writeLog", "-222------");
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return;
        }
        PrintWriter pw = null;
        try {
            File file = new File(Environment.getExternalStorageDirectory(),
                    "supermammy.log");
            if (!file.exists()) {
                // file.mkdirs()
                file.createNewFile();
            }
            // if(file.length()>5*1024*1024){
            // file.renameTo("log"+System.currentTimeMillis()+".log");
            // }
            pw = new PrintWriter(file);
            Log.e("writeLog", "-------");
            pw.append("--------\nTitle:" + title + "\nlog:" + content + "\n");
            // pw.flush();
            pw.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    public static void writeLogSyc(final String title, final String content) {
//		new Thread() {
//			@Override
//			public void run() {
//				writeLog(title, content);
//			}
//		}.start();
    }

    public static String getVersionString(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int getMianVersionCode(Context context) {
        try {
            String version = getVersionString(context);
            Log.d("version", version);
            return Integer.parseInt(version.split("\\.")[0]);
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        }
    }

    public static int getSubVersionCode(Context context) {
        String version = getVersionString(context);
        try {
            return Integer.parseInt(version.split("\\.")[1]);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     *
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;

    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     *
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return bp;

    }


    /**
     * 删除发布图片时残留的缓存图
     *
     * @param mActivity
     * @param imgPath
     */
    public static void deleteCropImg(Activity mActivity, String imgPath) {
        try {
            String path = imgPath.replace("file:", "");
            if (!TextUtils.isEmpty(path)) {
                //删除缓存图片同时删除缩略图
                ContentResolver mContentResolver = mActivity.getContentResolver();
                String where = MediaStore.Images.Media.DATA + "='" + path + "'";
                mContentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, where, null);

                //发送广播
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                File file = new File(path);
                Uri uri = Uri.fromFile(file);
                intent.setData(uri);
                mActivity.sendBroadcast(intent);
            }

        } catch (Exception e) {
//            DebugUtil.e("Exception>>" + e.getMessage());
        }
    }

    public static String transHtml(String s) {
        //String s = "去除以下标签：\n"
        //    + "1 <img  …  >\n"
        //    + "2 <video  …  ></video>\n"
        //    + "3 <audio  …  ></audio>\n"
        //    + "4 <embed  … >\n"
        //    + "5 <object   … ></object>\n"
        //    + "6 <source   … >\n"
        //    + "7 <script    … ></script>\n"
        //    + "8 <style     … ></style>\n"
        //    + "9 <applet    … ></applet>\n"
        //    + "10 <param   … >";
        //      <table ></table>        去掉尺码表
        String regImg = "<img[^>]*?>";
        String regVideo = "<video[^>]*?>[\\s\\S]*?<\\/video>";
        String regAudio = "<audio[^>]*?>[\\s\\S]*?<\\/audio>";
        String regEmbed = "<embed[^>]*?>";
        String regObj = "<object[^>]*?>[\\s\\S]*?<\\/object>";
        String regSource = "<source[^>]*?>";
        String regScript = "<script[^>]*?>[\\s\\S]*?<\\/script>";
        String regStyle = "<style[^>]*?>[\\s\\S]*?<\\/style>";
        String regApplet = "<applet[^>]*?>[\\s\\S]*?<\\/applet>";
        String regParam = "<param[^>]*?>";
        String regTable = "<table[^>]*?>[\\s\\S]*?<\\/table>";
        Pattern p_img=Pattern.compile(regImg, Pattern.CASE_INSENSITIVE);
        Matcher m_img=p_img.matcher(s);
        s=m_img.replaceAll("");

        Pattern p_video=Pattern.compile(regVideo, Pattern.CASE_INSENSITIVE);
        Matcher m_video=p_video.matcher(s);
        s=m_video.replaceAll("");

        Pattern p_audio=Pattern.compile(regAudio, Pattern.CASE_INSENSITIVE);
        Matcher m_audio=p_audio.matcher(s);
        s=m_audio.replaceAll("");

        Pattern p_embed=Pattern.compile(regEmbed, Pattern.CASE_INSENSITIVE);
        Matcher m_embed=p_embed.matcher(s);
        s=m_embed.replaceAll("");

        Pattern p_obj=Pattern.compile(regObj, Pattern.CASE_INSENSITIVE);
        Matcher m_obj=p_obj.matcher(s);
        s=m_obj.replaceAll("");

        Pattern p_source=Pattern.compile(regSource, Pattern.CASE_INSENSITIVE);
        Matcher m_source=p_source.matcher(s);
        s=m_source.replaceAll("");

        Pattern p_script=Pattern.compile(regScript, Pattern.CASE_INSENSITIVE);
        Matcher m_script=p_script.matcher(s);
        s=m_script.replaceAll("");

        Pattern p_style=Pattern.compile(regStyle, Pattern.CASE_INSENSITIVE);
        Matcher m_style=p_style.matcher(s);
        s=m_style.replaceAll("");

        Pattern p_applet=Pattern.compile(regApplet, Pattern.CASE_INSENSITIVE);
        Matcher m_applet=p_applet.matcher(s);
        s=m_applet.replaceAll("");

        Pattern p_param=Pattern.compile(regParam, Pattern.CASE_INSENSITIVE);
        Matcher m_param=p_param.matcher(s);
        s=m_param.replaceAll("");

        Pattern p_table=Pattern.compile(regTable, Pattern.CASE_INSENSITIVE);
        Matcher m_table=p_table.matcher(s);
        s=m_table.replaceAll("");
        //Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
//        return Configration.HTML_STYLE_HEAD + s + Configration.HTML_STYLE_TAIL;
        return s;
    }
}
