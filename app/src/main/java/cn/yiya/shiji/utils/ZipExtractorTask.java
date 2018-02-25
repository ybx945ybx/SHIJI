package cn.yiya.shiji.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * Created by chenjian on 2015/12/31.
 */
public class ZipExtractorTask extends AsyncTask<Void, Integer, Long> {

    private final String TAG = "ZipExtractorTask";
    private final File mInput;
    private final File mOutput;
    private final ProgressDialog mDialog;
    private int mProgress = 0;
    private final Context mContext;
    private boolean mReplaceAll;
    private OnFinishUnZipListner mListener;
    private String tarPath;
    private String gzPath;
    private String targzPath;
    private int type;

    public static final int ZIP_TYPE = 1;
    public static final int TARGZ_TYPE = 2;

    public ZipExtractorTask(int type, String in, String out, Context context, boolean replaceAll, OnFinishUnZipListner listner){
        super();
        mListener = listner;
        mInput = new File(in);
        mOutput = new File(out);
        this.type = type;

        if (type == TARGZ_TYPE) {
            tarPath = in.substring(0, in.length() - 3);
            gzPath = tarPath.substring(0, tarPath.length() - 4);
            this.targzPath = in;
        }

        if(!mOutput.exists()){
            if(!mOutput.mkdirs()){
            }
        }
        if(context!=null){
            mDialog = new ProgressDialog(context);
            mDialog.setCanceledOnTouchOutside(false);
        }
        else{
            mDialog = null;
        }
        mContext = context;
        mReplaceAll = replaceAll;
    }
    @Override
    protected Long doInBackground(Void... params) {
        switch (type) {
            case TARGZ_TYPE:
                return unGZip();
            case ZIP_TYPE:
                return unzip();
        }
        return 0l;
    }

    @Override
    protected void onPostExecute(Long result) {
        if(mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
        }
        if(isCancelled())
            return;

        if (mContext != null) {
            deleteFile(mInput);
        }

        if (type == TARGZ_TYPE) {
            deleteFile(new File(targzPath));
            deleteFile(new File(tarPath));
        }

        if (mListener != null) {
            mListener.onFinishUnZip();
        }
    }
    @Override
    protected void onPreExecute() {
        if(mDialog != null){
            mDialog.setTitle("正在加载资源");
//            mDialog.setMessage(mInput.getName());
            mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    cancel(true);
                }
            });
            mDialog.show();
        }
    }
    @Override
    protected void onProgressUpdate(Integer... values) {
        if(mDialog == null)
            return;
        if(values.length > 1){
            int max = values[1];
            mDialog.setMax(max);
        }
        else
            mDialog.setProgress(values[0].intValue());
    }
    private long unzip(){
        long extractedSize = 0L;
        Enumeration<ZipEntry> entries;
        ZipFile zip = null;
        try {
            zip = new ZipFile(mInput);
            long uncompressedSize = getOriginalSize(zip);
            publishProgress(0, (int) uncompressedSize);

            entries = (Enumeration<ZipEntry>) zip.entries();
            while(entries.hasMoreElements()){
                ZipEntry entry = entries.nextElement();
                if(entry.isDirectory()){
                    continue;
                }
                File destination = new File(mOutput, entry.getName());
                if(!destination.getParentFile().exists()){
                    Log.e(TAG, "make=" + destination.getParentFile().getAbsolutePath());
                    destination.getParentFile().mkdirs();
                }
                if(destination.exists() && mContext != null && !mReplaceAll){

                }
                ProgressReportingOutputStream outStream = new ProgressReportingOutputStream(destination);
                extractedSize += copy(zip.getInputStream(entry), outStream);
                outStream.close();

            }
        } catch (ZipException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                if (zip != null)
                    zip.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return extractedSize;
    }

    private long unGZip () {
        try {
            GZIPUtils.decompress(targzPath, tarPath);
            TarUtils.dearchive(tarPath, gzPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0l;
    }

    private long getOriginalSize(ZipFile file){
        Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) file.entries();
        long originalSize = 0l;
        while(entries.hasMoreElements()){
            ZipEntry entry = entries.nextElement();
            if(entry.getSize() >= 0){
                originalSize += entry.getSize();
            }
        }

        return originalSize;
    }

    private int copy(InputStream input, OutputStream output){
        byte[] buffer = new byte[1024*8];
        BufferedInputStream in = new BufferedInputStream(input, 1024*8);
        BufferedOutputStream out  = new BufferedOutputStream(output, 1024*8);
        int count = 0, n = 0;
        try {
            while((n = in.read(buffer, 0, 1024*8)) != -1){
                out.write(buffer, 0, n);
                count += n;
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return count;
    }

    private final class ProgressReportingOutputStream extends FileOutputStream {

        public ProgressReportingOutputStream(File file)
                throws FileNotFoundException {
            super(file);
        }

        @Override
        public void write(byte[] buffer, int byteOffset, int byteCount)
                throws IOException {
            super.write(buffer, byteOffset, byteCount);
            mProgress += byteCount;
            publishProgress(mProgress);
        }

    }

    private static String sdState = Environment.getExternalStorageState();
    public static void  deleteFile(File file)
    {
        if(sdState.equals(Environment.MEDIA_MOUNTED))
        {
            if (file.exists())
            {
                if (file.isFile())
                {
                    file.delete();
                }
                // 如果它是一个目录
                else if (file.isDirectory())
                {
                    // 声明目录下所有的文件 files[];
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++)
                    { // 遍历目录下所有的文件
                        deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                    }
                }
                file.delete();
            }
        }
    }

    public static interface OnFinishUnZipListner {
        public void onFinishUnZip();
        public void onFailDownload();
    }
}
