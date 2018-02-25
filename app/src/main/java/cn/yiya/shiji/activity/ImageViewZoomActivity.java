package cn.yiya.shiji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import cn.yiya.shiji.R;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.PhotoView;

/**
 * Created by Tom on 2016/4/29.
 */
public class ImageViewZoomActivity extends BaseAppCompatActivity {

    RelativeLayout rlytRoot;
    PhotoView zoomIv;
    private String imageUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_zoom);

        initViews();
        initEvents();
        init();


    }

    @Override
    protected void initViews() {
        imageUrl = getIntent().getStringExtra("imageUrl");
        zoomIv = (PhotoView) findViewById(R.id.photoview);
        zoomIv.enable();
        Netroid.displayImage(Util.transferImage(imageUrl, SimpleUtils.getScreenWidth(this)), zoomIv);
        zoomIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void init() {

    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.silde_in_center, R.anim.silde_out_border);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.silde_in_center, R.anim.silde_out_border);
    }
}
