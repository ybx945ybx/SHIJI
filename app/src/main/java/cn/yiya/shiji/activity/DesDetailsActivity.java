package cn.yiya.shiji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.yiya.shiji.R;
import cn.yiya.shiji.netroid.Netroid;

/**
 * Created by jerryzhang on 2015/10/23.
 */
public class DesDetailsActivity extends BaseAppCompatActivity {
    private Intent intent;
    private TextView tvDes,tvTitle,tvRight;
    private ImageView ivLogo,ivback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.des_layout);
        intent = getIntent();

        initViews();
        initEvents();
        init();
    }

    @Override
    protected void initViews() {
        tvDes = (TextView)findViewById(R.id.textview);
        ivLogo = (ImageView)findViewById(R.id.imageview);
        tvTitle = (TextView)findViewById(R.id.title_txt);
        tvRight = (TextView)findViewById(R.id.title_right);
        ivback = (ImageView)findViewById(R.id.title_back);
        tvRight.setVisibility(View.GONE);
    }

    @Override
    protected void initEvents() {
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void init() {
        tvDes.setText(intent.getStringExtra("des"));
        Netroid.displayImage(intent.getStringExtra("logo"), ivLogo);
        tvTitle.setText(intent.getStringExtra("name"));
    }

}
