package cn.yiya.shiji.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import cn.yiya.shiji.R;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.views.CirclePgBar;

/**
 * Created by jerry on 2016/2/22.
 * 广告页
 */
public class ADActivity extends Activity implements View.OnClickListener {
    private Intent intent;
    private ImageView startimage;
    private View viewGo;
    boolean isnull = true;
    private Handler handler;
    private Runnable runnable;
    private CirclePgBar circlePgBar;

    private String adContent;
    private int adType;
    private String adShare;
    private String bottomName;
    private String bottomUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        initViews();
        initEvents();
        init();
    }

    protected void initViews() {
        handler = new Handler(getMainLooper());
        intent = new Intent(ADActivity.this, NewMainActivity.class);
        Intent intentPush = getIntent();
        if (intentPush != null) {
            intent.putExtra("bottomName", intentPush.getStringExtra("bottomName"));
            intent.putExtra("bottomUrl", intentPush.getStringExtra("bottomUrl"));
            intent.putExtra("bottomTitle", intentPush.getStringExtra("bottomTitle"));
            if (!TextUtils.isEmpty(intentPush.getStringExtra("main_activity_dest"))) {

                intent.putExtra("main_activity_dest", intentPush.getStringExtra("main_activity_dest"));
                intent.putExtra("flashId", intentPush.getStringExtra("flashId"));
                intent.putExtra("type", intentPush.getStringExtra("type"));
                intent.putExtra("data", intentPush.getStringExtra("data"));
                intent.putExtra("activityId", intentPush.getStringExtra("activityId"));
                intent.putExtra("workId", intentPush.getStringExtra("workId"));
                intent.putExtra("matchId", intentPush.getStringExtra("matchId"));
                intent.putExtra("userId", intentPush.getStringExtra("userId"));
                intent.putExtra("subOrderNum", intentPush.getStringExtra("subOrderNum"));
                intent.putExtra("orderNum", intentPush.getStringExtra("orderNum"));
                intent.putExtra("name", intentPush.getStringExtra("name"));
                intent.putExtra("url", intentPush.getStringExtra("url"));
                intent.putExtra("hshare", intentPush.getStringExtra("hshare"));
            } else {
                adContent = intentPush.getStringExtra("adContent");
                adType = intentPush.getIntExtra("adType", -1);
                adShare = intentPush.getStringExtra("adShare");
            }
        }
        circlePgBar = (CirclePgBar) findViewById(R.id.circle_pg_bar);
        startimage = (ImageView) findViewById(R.id.startimage);
        findViewById(R.id.startgif).setVisibility(View.GONE);
        circlePgBar.setVisibility(View.VISIBLE);
        viewGo = findViewById(R.id.view_go);
        viewGo.setVisibility(View.VISIBLE);
        if ((BitmapFactory.decodeFile((BaseApplication.ALBUM_PATH + "ad.jpg"))) != null) {
            isnull = false;
            startimage.setImageBitmap(BitmapFactory.decodeFile((BaseApplication.ALBUM_PATH + "ad.jpg")));    // 替换启动图片
        } else {
            startActivity(intent);
        }
    }

    protected void initEvents() {
        runnable = new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
            }
        };
        if (!isnull) {
            handler.postDelayed(runnable, 3000);

        }

        circlePgBar.setOnClickListener(this);
        viewGo.setOnClickListener(this);
    }

    protected void init() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.circle_pg_bar:
                startActivity(intent);
                break;
            case R.id.view_go:
                if(adType == 4){
                    return;
                }
                // 1:url  2，红人id 3，活动id  4 纯广告图
                intent.putExtra("main_activity_dest", "AD");
                intent.putExtra("adContent", adContent);
                intent.putExtra("adType", adType);
                intent.putExtra("adShare", adShare);
                startActivity(intent);
                break;

        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
        finish();
        overridePendingTransition(R.anim.in_right, R.anim.lsq_empty);
    }
}
