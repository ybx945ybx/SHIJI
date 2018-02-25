package cn.yiya.shiji.netroid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.ImageView;

import com.duowan.mobile.netroid.Listener;
import com.duowan.mobile.netroid.Network;
import com.duowan.mobile.netroid.Request;
import com.duowan.mobile.netroid.RequestQueue;
import com.duowan.mobile.netroid.cache.BitmapImageCache;
import com.duowan.mobile.netroid.cache.DiskCache;
import com.duowan.mobile.netroid.image.NetworkImageView;
import com.duowan.mobile.netroid.request.ImageRequest;
import com.duowan.mobile.netroid.stack.HurlStack;
import com.duowan.mobile.netroid.toolbox.BasicNetwork;
import com.duowan.mobile.netroid.toolbox.FileDownloader;
import com.duowan.mobile.netroid.toolbox.ImageLoader;
import com.duowan.mobile.netroid.toolbox.ImageLoader.ImageListener;

import java.io.File;
import java.util.concurrent.TimeUnit;

import cn.yiya.shiji.R;

public class Netroid {
	private static final String TAG = "Netroid";

    private static RequestQueue mRequestQueue;

    private static ImageLoader mImageLoader;

    private static FileDownloader mFileDownloader;
    
    private static Context mContext;

    private static String cacheDir = new String(Environment.getExternalStorageDirectory().getPath() + "/shiji/cache");

    private Netroid() {}

    @SuppressLint("NewApi")
	public static void init(Context context) {
    	mContext = context;
        if (mRequestQueue != null) throw new IllegalStateException("initialized");

        Network network = new BasicNetwork(new HurlStack(Const.USER_AGENT, null), "utf-8");
        mRequestQueue = new RequestQueue(network, Const.POOL_SIZE, new DiskCache(
            new File(cacheDir), Const.HTTP_DISK_CACHE_SIZE));

        mImageLoader = new NetImageLoader(
                mRequestQueue, new BitmapImageCache(Const.HTTP_MEMORY_CACHE_SIZE), context.getResources(), context.getAssets()){
            	@Override
    			public void makeRequest(ImageRequest request) {
//            		long cacheTime = request.getCacheExpireTime();
            		request.setCacheExpireTime(TimeUnit.DAYS, 10);
    			}
            };

        mFileDownloader = new FileDownloader(mRequestQueue, 1);

        mRequestQueue.start();
    }
    
    public static <T> void add(Request<T> request) {
    	mRequestQueue.add(request);
    }
    public static void stop() {
    	mRequestQueue.stop();
    }
    public static void displayImage(String url, ImageView imageView) {
        ImageListener listener = ImageLoader.getImageListener(imageView, R.drawable.user_dafault, R.drawable.user_dafault);
        if (TextUtils.isEmpty(url)) {
        	return;
        }
        mImageLoader.get(url, listener, 0, 0);
    }

    //加载头像图片，默认小黄图
    public static void displayImage(String url, ImageView imageView, int defaultDrawableId) {
        ImageListener listener = ImageLoader.getImageListener(imageView, defaultDrawableId, defaultDrawableId);
        if (TextUtils.isEmpty(url)) {
            imageView.setImageResource(defaultDrawableId);
            return;
        }
        mImageLoader.get(url, listener, 0, 0);
    }
    public static void displayImage(String url, NetworkImageView imageView, int defaultDrawableId) {
        imageView.setImageResource(defaultDrawableId);
        imageView.setImageUrl(url, mImageLoader);
        imageView.setDefaultImageResId(defaultDrawableId);
        imageView.setErrorImageResId(defaultDrawableId);
    }

    public static void displayImage(String url, ImageListener listener) {
        mImageLoader.get(url, listener, 0, 0);
    }

    public static void displayImage(String url, NetworkImageView imageView) {
        imageView.setImageUrl(url, mImageLoader);
    }
    public static FileDownloader.DownloadController addFileDownload(String storeFilePath, String url, Listener<Void> listener) {
        return mFileDownloader.add(storeFilePath, url, listener);
    }

    class Const {
        // http parameters
        public static final int HTTP_MEMORY_CACHE_SIZE = 5 * 1024 * 1024; // 2MB
        public static final int HTTP_DISK_CACHE_SIZE = 100 * 1024 * 1024; // 100MB
        public static final String HTTP_DISK_CACHE_DIR_NAME = "shiji";
        public static final String USER_AGENT = "shiji.cn";
        public static final int POOL_SIZE = 4;
    }
}

