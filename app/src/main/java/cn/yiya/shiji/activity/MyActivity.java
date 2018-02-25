package cn.yiya.shiji.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import cn.yiya.shiji.R;
import cn.yiya.shiji.utils.ShareUtils;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class MyActivity extends Activity implements View.OnClickListener{

    private File[] files;
    private List<String> paths = new ArrayList<String>();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // 遍历 SD 卡下 .png 文件通过微信分享，保证SD卡根目录下有.png的文件
        //File root = Environment.getExternalStorageDirectory();
        //files = root.listFiles(new FileFilter() {
        //    @Override
        //    public boolean accept(File pathname) {
        //        if (pathname.getName().endsWith(".PNG"))
        //            return true;
        //        return false;
        //    }
        //});
        //
        //for(File file :files){
        //    paths.add(file.getAbsolutePath());
        //}

        for (int i = 2; i < 9; i++){
            paths.add(SplashActivity.SKIN_PATH + i + ".png");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.share_btn1:
                if(paths == null || paths.size() == 0){
                    Toast.makeText(this,"SD卡根目录下无.png格式照片",Toast.LENGTH_SHORT).show();
                }else{
                    ShareUtils.share9PicToFriendNoSDK(this,"hshshshs",paths);
                }
                break;
            
        }
    }
}
