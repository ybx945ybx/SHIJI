package cn.yiya.shiji.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by chenjian on 2015/10/24.
 */
public class PhotoUtils {

    private static final String TAG = PhotoUtils.class.getName();

    public static final String PHOTO_DIR = FileUtil.getSDCardPath() + "/shiji/Camera";

    private File mCurrentPhotoFile;
    private String currFilePath;

    public File getFile() {
        return mCurrentPhotoFile;
    }
    public String getFilePath() {
        return currFilePath;
    }
    public void setCurrFilePath(String currFilePath) {
        this.currFilePath = currFilePath;
    }
    public void toTake(Activity activity, int resultCode) {
        try {
            File file = new File(PHOTO_DIR);
            if (!file.exists()) {
                file.mkdirs();
            }
            currFilePath = PHOTO_DIR + "/" + getPhotoFileName();
            mCurrentPhotoFile = new File(currFilePath);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
//			intent.putExtra("crop", "true");

            intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCurrentPhotoFile));
            activity.startActivityForResult(intent, resultCode);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'-yyyyMMdd-HHmmss", Locale.getDefault());
        return dateFormat.format(date) + ".jpg";
    }

    public void toSelect(Activity activity, int nCorp, int resultCode) {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			intent.setType("image/*");

            activity.startActivityForResult(intent, resultCode);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void handlerPhoto(Activity context, Uri uri, int nCrop, int resultCode) {
        if (uri == null) {
            return;
        }

        Intent intent = new Intent("com.android.camera.action.CROP");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            String url=getPath(context,uri);
            intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
        }else{
            intent.setDataAndType(uri, "image/*");
        }

        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", nCrop);
        intent.putExtra("aspectY", nCrop * 2 / 3);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", nCrop);
        intent.putExtra("outputY", nCrop * 2 / 3);
        intent.putExtra("return-data", true);
        context.startActivityForResult(intent, resultCode);
    }

    public Bitmap selectResult(Context context, Intent data) {
        Bitmap photo = data.getParcelableExtra("data");
        String fileName = getPhotoFileName();
        mCurrentPhotoFile = new File(PHOTO_DIR, fileName);
        currFilePath = PHOTO_DIR + "/" + getPhotoFileName();
        FileUtil.writeFile(photo, 100, PHOTO_DIR, fileName);

        Uri uri = data.getData();
        if (uri != null) {

            String path = FileUtil.uri2Path(context, uri);
            mCurrentPhotoFile = new File(path);

            ContentResolver resolver = context.getApplicationContext().getContentResolver();
            try {
                InputStream is = resolver.openInputStream(uri);
                final BitmapFactory.Options options = new BitmapFactory.Options();
                // 当inJustDecodeBounds设为true时,不会加载图片仅获取图片尺寸信息
                options.inJustDecodeBounds = true;
                // 此时仅会将图片信息会保存至options对象内,decode方法不会返回bitmap对象
                BitmapFactory.decodeStream(is, null, options);
                is.close();
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inPreferredConfig = Bitmap.Config.RGB_565;
                opt.inPurgeable = true;
                opt.inInputShareable = true;
                opt.inJustDecodeBounds = false;
//				opt.inSampleSize = CommonUtils.calculateInSampleSize(options,
//						480, 800);
//				photo = BitmapFactory.decodeStream(resolver.openInputStream(uri));
                photo = BitmapFactory.decodeStream(
                        resolver.openInputStream(uri), null, opt);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return photo;
    }

//    public Bitmap selectResult(Context context, Intent data,
//                               int width, int height) {
//        Bitmap photo = selectResult(context, data);
////		photo = CommonUtils.scaleBitmap(photo, width, height);
//        return photo;
//    }

//    public Bitmap takeResult(Activity activity, int width, int height) {
//        Bitmap bmp = null;
//        if (mCurrentPhotoFile != null) {
//            BitmapFactory.Options opt = new BitmapFactory.Options();
//            opt.inPreferredConfig = Bitmap.Config.RGB_565;
//            opt.inPurgeable = true;
//            opt.inInputShareable = true;
//            bmp = BitmapFactory.decodeFile(mCurrentPhotoFile.getAbsolutePath(), opt);
////			bmp = CommonUtils.readBitmap(mCurrentPhotoFile);
////			ImageUtils.getBitmapByFile(mCurrentPhotoFile, width, height);
//        }
//        return bmp;
//    }

//    public Bitmap cropResult(Activity activity, int width,
//                             int height) {
//        Bitmap bmp = null;
//        if (mCurrentPhotoFile != null) {
//            bmp = Utils.readBitmap(mCurrentPhotoFile);
//            //bmp = ImageUtils.getBitmapByFile(mCurrentPhotoFile, width, height);
//        }
//        return bmp;
//    }

    public void toCrop(Activity activity, File file, int nCrop, int resultCode) {

        try {
            // Add the image to the media store
            MediaScannerConnection.scanFile(activity,
                    new String[]{file.getAbsolutePath()},
                    new String[]{null}, null);

            Intent intent = new Intent("com.android.camera.action.CROP");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                String url = getPath(activity, Uri.fromFile(file));
                intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
            }
            intent.setDataAndType(Uri.fromFile(file), "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", nCrop);
            intent.putExtra("aspectY", nCrop * 2 / 3);
            intent.putExtra("outputX", nCrop);
            intent.putExtra("outputY", nCrop * 2 / 3);
            intent.putExtra("return-data", true);

            activity.startActivityForResult(intent, resultCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void toCrop(Activity activity, int nCrop, int resultCode) {
        toCrop(activity,mCurrentPhotoFile, nCrop, resultCode);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
