package cn.yiya.shiji.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.yiya.shiji.R;
import cn.yiya.shiji.utils.Util;

/**
 * Created by Tom on 2016/6/20.
 */
public class AboutUsActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private TextView tvTitle;
    private ImageView ivBack;

    private TextView tvVersion;

    private TextView tvPhone;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        initViews();
        initEvents();
        init();
    }


    @Override
    protected void initViews() {
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText("关于柿集");
        ivBack = (ImageView) findViewById(R.id.title_back);
        findViewById(R.id.title_right).setVisibility(View.GONE);

        tvVersion = (TextView) findViewById(R.id.version);
//        String htmlCode = SharedPreUtil.getString(this, "shiji", "htmlCode", "");
        String version = "版本：" + getVersion();
        tvVersion.setText(version);

        tvPhone = (TextView) findViewById(R.id.tv_phone);

    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        tvPhone.setOnClickListener(this);
    }

    @Override
    protected void init() {

    }

    // 获取版本号
    private String getVersion() {
        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "找不到版本号";
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.tv_phone:
                Util.dialPhoneNumber(this, tvPhone.getText().toString());     // 跳转至拨号界面
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
