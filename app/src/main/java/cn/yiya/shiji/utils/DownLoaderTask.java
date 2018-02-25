package cn.yiya.shiji.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import cn.yiya.shiji.activity.NewMainActivity;


/**
 * Created by chenjian on 2015/12/31.
 */
public class DownLoaderTask extends AsyncTask<Void, Integer, Long> {

    private final String TAG = "DownLoaderTask";
    private URL mUrl;
    private File mFile;
    private ProgressDialog mDialog;
    private int mProgress = 0;
    private ProgressReportingOutputStream mOutputStream;
    private Context mContext;
    private ZipExtractorTask.OnFinishUnZipListner mListener;
    public DownLoaderTask(String url, String out, Context context, ZipExtractorTask.OnFinishUnZipListner listner){
        super();
        this.mListener = listner;
        if(context!=null){
            mDialog = new ProgressDialog(context);
            mDialog.setCanceledOnTouchOutside(false);
            mContext = context;
        }
        else{
            mDialog = null;
        }

        try {
            mUrl = new URL(url);
            String fileName = new File(mUrl.getFile()).getName();
            mFile = new File(out, fileName);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPreExecute() {
        if(mDialog!=null){
            mDialog.setTitle("正在下载资源");
//            mDialog.setMessage(mFile.getName());
            mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    cancel(true);
                }
            });
            
            //tom  修改听云bug
            if(mContext instanceof NewMainActivity){
                if(!((NewMainActivity)mContext).isFinishing()){
                    mDialog.show();
                }
            }
//            mDialog.show();
        }
    }

    @Override
    protected Long doInBackground(Void... params) {
        return download();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if(mDialog==null)
            return;
        if(values.length>1){
            int contentLength = values[1];
            if(contentLength==-1){
                mDialog.setIndeterminate(true);
            }
            else{
                mDialog.setMax(contentLength);
            }
        }
        else{
            mDialog.setProgress(values[0].intValue());
        }
    }

    @Override
    protected void onPostExecute(Long result) {
        if(mDialog!=null&&mDialog.isShowing()){
            mDialog.dismiss();
        }
        if(isCancelled())
            return;
        File sdcardDir = Environment.getExternalStorageDirectory();
        String pathZip = sdcardDir.getPath() + "/shiji/assets.gz";
        String pathFile = sdcardDir.getPath() + "/shiji/";
        ZipExtractorTask task = new ZipExtractorTask(ZipExtractorTask.ZIP_TYPE, pathZip, pathFile, mContext, true, mListener);
        task.execute();
    }

    private long download(){
        URLConnection connection = null;
        int bytesCopied = 0;
        try {
            connection = mUrl.openConnection();
            int length = connection.getContentLength();
            if(mFile.exists()&&length == mFile.length()){
                return 0l;
            }
            mOutputStream = new ProgressReportingOutputStream(mFile);
            publishProgress(0,length);
            bytesCopied =copy(connection.getInputStream(),mOutputStream);
            if(bytesCopied!=length&&length!=-1){
            }
            mOutputStream.close();
        } catch (IOException e) {
            if (mListener != null) {
                mListener.onFailDownload();
            }
            e.printStackTrace();
        }
        return bytesCopied;
    }
    private int copy(InputStream input, OutputStream output){
        byte[] buffer = new byte[1024*8];
        BufferedInputStream in = new BufferedInputStream(input, 1024*8);
        BufferedOutputStream out  = new BufferedOutputStream(output, 1024*8);
        int count =0,n=0;
        try {
            while((n=in.read(buffer, 0, 1024*8))!=-1){
                out.write(buffer, 0, n);
                count+=n;
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
}
