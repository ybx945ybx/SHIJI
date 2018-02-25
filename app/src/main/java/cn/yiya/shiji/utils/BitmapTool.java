/**
 * @Title: BitmapTool.java
 * @Package cn.net_show.doctor.utils
 * @author 王帅
 * @date 2015年3月7日 上午1:59:08
 */
package cn.yiya.shiji.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import cn.yiya.shiji.R;
import cn.yiya.shiji.netroid.Netroid;

/**
 * @ClassName: BitmapTool
 * @author 王帅
 * @date 2015年3月7日 上午1:59:08
 */
public class BitmapTool {

    /**
     * 初始化imageLoader默认配置
     *
     * @param context
     */
    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(
                context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(200 * 1024 * 1024); // 200 MiB
        config.memoryCache(new WeakMemoryCache());
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        //config.writeDebugLogs(); // Remove for release app
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    /**
     * 缩放/裁剪图片
     *
     * @Title: zoomImg
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param bm
     * @param newWidth
     * @param newHeight
     * @return
     * @return Bitmap
     */
    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                true);
        return newbm;
    }

    // 通过文件路径获取到bitmap
    public static Bitmap getBitmapFromPath(String path, int w, int h) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为ture只获取图片大小
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;

        BitmapFactory.decodeFile(path, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        float scaleWidth = 0.f, scaleHeight = 0.f;
        if (width > w || height > h) {
            // 缩放
            scaleWidth = ((float) width) / w;
            scaleHeight = ((float) height) / h;
        }
        opts.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth, scaleHeight);
        opts.inSampleSize = (int) scale;
        WeakReference<Bitmap> weak = new WeakReference<Bitmap>(
                BitmapFactory.decodeFile(path, opts));
        return Bitmap.createScaledBitmap(weak.get(), w, h, true);
    }

    public static Bitmap getBitmapFromRes(Resources res, int id, int w, int h) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.decodeResource(res, id, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        float scaleWidth = 0f, scaleHeight = 0f;
        if (width > w || height > h) {
            // 缩放
            scaleWidth = ((float) width) / w;
            scaleHeight = ((float) height) / h;
        }
        opts.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth, scaleHeight);
        opts.inSampleSize = (int) scale;
        // WeakReference<Bitmap> weak = new WeakReference<Bitmap>(
        // BitmapFactory.decodeResource(res, id, opts));
        // return Bitmap.createScaledBitmap(weak.get(), w, h, true);

        return BitmapFactory.decodeResource(res, id, opts);
    }

    /**
     * 将突破保存到文件
     *
     * @param bmp
     * @param fImage
     */
    public static void bitmap2File(Bitmap bmp, File fImage) {
        if (bmp == null || fImage == null) {
            Log.e("error", "photo == null");
            return;
        }
        try {
            fImage.createNewFile();
            FileOutputStream iStream = new FileOutputStream(fImage);
            bmp.compress(CompressFormat.PNG, 100, iStream);
            iStream.close();
            // fImage.close();
            iStream = null;
            // fImage =null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 将图片文件设置到imageview
     *
     * @param imageView
     * @param imageFile
     */
    @SuppressWarnings("deprecation")
    public static void setPicToImageView(ImageView imageView, File imageFile) {
        int imageViewWidth = imageView.getWidth();
        int imageViewHeight = imageView.getHeight();
        BitmapFactory.Options opts = new Options();
        // 设置这个，只得到Bitmap的属性信息放入opts，而不把Bitmap加载到内存中
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile.getPath(), opts);

        int bitmapWidth = opts.outWidth;
        int bitmapHeight = opts.outHeight;
        // 取最大的比例，保证整个图片的长或者宽必定在该屏幕中可以显示得下
        int scale = Math.max(imageViewWidth / bitmapWidth, imageViewHeight
                / bitmapHeight);
        // 缩放的比例
        opts.inSampleSize = scale;
        // 内存不足时可被回收
        opts.inPurgeable = true;
        // 设置为false,表示不仅Bitmap的属性，也要加载bitmap
        opts.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath(), opts);
        imageView.setImageBitmap(bitmap);
    }

    @SuppressWarnings("deprecation")
    public static Bitmap getSmallBitmap(ImageView imageView, File imageFile) {
        int imageViewWidth = imageView.getWidth();
        int imageViewHeight = imageView.getHeight();
        BitmapFactory.Options opts = new Options();

        // 设置这个，只得到Bitmap的属性信息放入opts，而不把Bitmap加载到内存中
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile.getPath(), opts);

        int bitmapWidth = opts.outWidth;
        int bitmapHeight = opts.outHeight;
        // 取最大的比例，保证整个图片的长或者宽必定在该屏幕中可以显示得下
        int scale = Math.max(imageViewWidth / bitmapWidth, imageViewHeight
                / bitmapHeight);

        // 缩放的比例
        opts.inSampleSize = scale;
        // 内存不足时可被回收
        opts.inPurgeable = true;
        // 设置为false,表示不仅Bitmap的属性，也要加载bitmap
        opts.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(imageFile.getPath(), opts);
        // imageView.setImageBitmap(bitmap);
    }

    /**
     * 读取本地资源的图片
     *
     * @param context
     * @param resId
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Bitmap ReadBitmapById(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /***
     * 根据资源文件获取Bitmap
     *
     * @param context
     * @param drawableId
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Bitmap ReadBitmapById(Context context, int drawableId,
                                        int screenWidth, int screenHight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Config.ARGB_8888;
        options.inInputShareable = true;
        options.inPurgeable = true;
        InputStream stream = context.getResources().openRawResource(drawableId);
        Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
        return getBitmap(bitmap, screenWidth, screenHight);
    }

    @SuppressWarnings("deprecation")
    public static Bitmap ReadBitmapById(Context context, File imgFile,
                                        ImageView imgView, int screenWidth, int screenHight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Config.ARGB_8888;
        options.inInputShareable = true;
        options.inPurgeable = true;
        // InputStream stream =
        // context.getResources().openRawResource(drawableId);
        // Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
        Bitmap bitmap = getSmallBitmap(imgView, imgFile);
        return getBitmap(bitmap, screenWidth, screenHight);
    }

    /***
     * 等比例压缩图片
     *
     * @param bitmap
     * @param screenWidth
     * @param screenHight
     * @return
     */
    public static Bitmap getBitmap(Bitmap bitmap, int screenWidth,
                                   int screenHight) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Log.e("jj", "图片宽度" + w + ",screenWidth=" + screenWidth);
        Matrix matrix = new Matrix();
        float scale = (float) screenWidth / w;
        float scale2 = (float) screenHight / h;

        scale = scale < scale2 ? scale : scale2;

        // 保证图片不变形.
        matrix.postScale(scale, scale);
        // w,h是原图的属性.
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
    }

    private static int FREE_SD_SPACE_NEEDED_TO_CACHE = 1;
    private static int MB = 1024 * 1024;
    public final static String DIR = Environment.getExternalStorageDirectory()
            .getPath() + "/yiya";
    ;

    /***
     * 保存图片至SD卡
     *
     * @param bm
     * @param fileName
     * @param quantity
     */
    public static void saveBmpToSd(Bitmap bm, String fileName, int quantity) {
        // 判断sdcard上的空间
        if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
            return;
        }
        if (!Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState()))
            return;
        String filename = fileName;
        // 目录不存在就创建
        File dirPath = new File(DIR);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }

        File file = new File(DIR + "/" + filename + ".jpg");
        try {
            file.createNewFile();
            OutputStream outStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, quantity, outStream);
            outStream.flush();
            outStream.close();

        } catch (FileNotFoundException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void saveBmpToSd(Bitmap bm, String path, String fileName,
                                   int quantity) {
        // 判断sdcard上的空间
        if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
            return;
        }
        if (!Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState()))
            return;
        String filename = fileName;
        // 目录不存在就创建
        File dirPath = new File(path);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }

        File file = new File(path + "/" + filename + ".jpg");
        try {
            file.createNewFile();
            OutputStream outStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, quantity, outStream);
            outStream.flush();
            outStream.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /***
     * 获取SD卡图片
     *
     * @param url
     * @param quantity
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Bitmap GetBitmap(String url, int quantity) {
        InputStream inputStream = null;
        String filename = "";
        Bitmap map = null;
        URL url_Image = null;
        String LOCALURL = "";
        if (url == null)
            return null;
        try {
            filename = url;
        } catch (Exception err) {
        }

        LOCALURL = URLEncoder.encode(filename);
        if (isExist(DIR + "/" + LOCALURL)) {
            map = BitmapFactory.decodeFile(DIR + "/" + LOCALURL);
        } else {
            try {
                url_Image = new URL(url);
                inputStream = url_Image.openStream();
                map = BitmapFactory.decodeStream(inputStream);
                // url = URLEncoder.encode(url, "UTF-8");
                if (map != null) {
                    saveBmpToSd(map, LOCALURL, quantity);
                }
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return map;
    }

    /***
     * 判断图片是存在
     *
     * @param url
     * @return
     */
    public static boolean isExist(String url) {
        File file = new File(DIR + url);
        return file.exists();
    }

    /** * 计算sdcard上的剩余空间 * @return */
    private static int freeSpaceOnSd() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
                .getPath());
        @SuppressWarnings("deprecation")
        double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat
                .getBlockSize()) / MB;

        return (int) sdFreeMB;
    }

    /**
     * 把view的内容保存到bitmap
     *
     * @param view
     * @param bitmapWidth
     * @param bitmapHeight
     * @return
     */
    public static Bitmap convertViewToBitmap(View view, int bitmapWidth,
                                             int bitmapHeight) {
        Bitmap bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight,
                Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(bitmap));

        return bitmap;
    }

    private static DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
            .cacheInMemory(true).cacheOnDisk(true)
            .showImageOnLoading(R.drawable.user_dafault)
            .showImageForEmptyUri(R.drawable.user_dafault)
            .showImageOnFail(R.drawable.user_dafault)
                    //.displayer(new FadeInBitmapDisplayer(100))
            .bitmapConfig(Bitmap.Config.RGB_565).build();

    /**
     * options 为null 时，使用默认值(建议使用默认options)
     *
     * @param uri
     * @param imageView
     * @param options
     */
    public static void disaplayImage(String uri, ImageView imageView,
                                     DisplayImageOptions options) {
        DisplayImageOptions ops = defaultOptions;
        if (options != null) {
            ops = options;
        }
        ImageLoader.getInstance().displayImage(uri, imageView, ops);
    }

    public static void disaplayImage(File file, ImageView imageView,
                                     DisplayImageOptions options) {
        String uri = Uri.fromFile(file).toString();
        disaplayImage(uri, imageView, options);
    }

    public static void writeSmallToSD(int size, Bitmap image) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 85, out);
        float zoom = (float) Math.sqrt(size * 1024
                / (float) out.toByteArray().length);

        Matrix matrix = new Matrix();
        matrix.setScale(zoom, zoom);

        Bitmap result = Bitmap.createBitmap(image, 0, 0, image.getWidth(),
                image.getHeight(), matrix, true);

        out.reset();
        result.compress(Bitmap.CompressFormat.JPEG, 85, out);
        while (out.toByteArray().length > size * 1024) {
            System.out.println(out.toByteArray().length);
            matrix.setScale(0.9f, 0.9f);
            result = Bitmap.createBitmap(result, 0, 0, result.getWidth(),
                    result.getHeight(), matrix, true);
            out.reset();
            result.compress(Bitmap.CompressFormat.JPEG, 85, out);
        }
    }

    @SuppressWarnings("deprecation")
    public static Bitmap compressImageFromFile(String srcPath, float width,
                                               float height) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;// 只读边,不读内容
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = width;//
        float ww = height;//
        int be = 1;
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置采样率

        newOpts.inPreferredConfig = Config.ARGB_8888;// 该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收

        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        // return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
        // 其实是无效的,大家尽管尝试
        return bitmap;
    }

    public static Bitmap compressImageFromFile(String srcPath) {
        return compressImageFromFile(srcPath, 800f, 480f);
    }

    public static Bitmap compressImageFromFile(String srcPath, String fileName) {
        Bitmap bmp = compressImageFromFile(srcPath);
        saveBmpToSd(bmp, fileName, 100);
        return bmp;
    }

    /**
     * 判断图片方向
     *
     * @param path
     * @return
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     *
     * @param bitmap
     * @param degress
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degress);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), m, true);
            return bitmap;
        }
        return bitmap;
    }

    // 生成圆角图片
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, w, h);
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    // 生成圆角图片
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        float roundPx = w > h ? h / 2f : w / 2f;
        Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, w, h);
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * bitmap转
     *
     * @param bm
     * @return
     */
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 判断SD卡是否存在
     */
    public static boolean existSDCard() {
        if (android.os.Environment.getExternalStorageDirectory().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }

    /**
     * 判断SDCard剩余空间
     */
    public static long getSDFreeSize() {
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)) {
            // 取得SD卡文件路径
            File path = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(path.getPath());
            // 获取单个数据块的大小（byte）
            long blockSize = sf.getBlockSize();
            // 空间的数据块的数量
            long freeBlocks = sf.getAvailableBlocks();
            // 返回SD卡空间大小
            //return freeBlocks * blockSize;  //单位Byte
            //return (freeBlocks * blockSize)/1024;   //单位KB
            return (freeBlocks * blockSize) / 1024 / 1024; //单位MB
        }
        return 0;
    }

    /**
     * SD卡的总容量
     */
    public static long getSDAllsize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        // 获取单个数据块的大小
        long blockSize = sf.getBlockSize();
        // 获取所有数据块数量
        long allBlocks = sf.getBlockCount();
        return (allBlocks * blockSize) / 1024 / 1024; // 单位MB
    }

    // 显示网络上的图片
    public static Bitmap returnBitMap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    // 判断当前图片地址是网络加载还是本地加载,来加载图片
    public static void showImageView(String path, ImageView imageView) {
        if (TextUtils.isEmpty(path))
            return;
        if (path.substring(0, 4).equals("http") || path.substring(0, 3).equals("www")) {
            Netroid.displayImage(path, imageView);
        } else {
            imageView.setImageBitmap(BitmapFactory.decodeFile(path));
            Log.i("图片", "path = " + path);
        }
    }

    public static void showImageView(String path, ImageView imageView, int defaultDrawableId) {
        if (TextUtils.isEmpty(path)){
            imageView.setImageResource(defaultDrawableId);
            return;
        }
        if (path.substring(0, 4).equals("http") || path.substring(0, 3).equals("www")) {
            Netroid.displayImage(path, imageView, defaultDrawableId);
        } else {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            if (bitmap == null) {
                imageView.setImageResource(defaultDrawableId);
            } else {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    public static void showFullImageView(Context context, String path, ImageView imageView) {
        if (TextUtils.isEmpty(path))
            return;
        if (path.substring(0, 4).equals("http") || path.substring(0, 3).equals("www")) {
            int width = SimpleUtils.getScreenWidth(context);
            Netroid.displayImage(Util.transferImage(path, width), imageView, R.drawable.travel_background);
        } else {
            int width = SimpleUtils.getScreenWidth(context);
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            if(bitmap == null){
                return;
            }
            int bmpWidth = bitmap.getWidth();
            int bmpHeight = bitmap.getHeight();

            float scale = (float) width / (float) bmpWidth;
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bmpWidth, bmpHeight, matrix, true);

            imageView.setImageBitmap(resizeBmp);
            Log.i("图片", "path = " + path);
        }
    }

    public static boolean isNetPath(String path) {
        if (path.substring(0, 4).equals("http") || path.substring(0, 3).equals("www")) {
            return true;
        }

        return false;
    }

    public static void clipShowImageView(String path, ImageView imageView, int defaultDrawableId, int width, int height) {
        if (TextUtils.isEmpty(path)) {
            imageView.setImageResource(defaultDrawableId);
            return;
        }
        if (path.substring(0, 4).equals("http") || path.substring(0, 3).equals("www")) {
            Netroid.displayImage(Util.clipImageViewByWH(path, width, height), imageView, defaultDrawableId);
        } else {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            if (bitmap == null) {
                imageView.setImageResource(defaultDrawableId);
            } else {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
