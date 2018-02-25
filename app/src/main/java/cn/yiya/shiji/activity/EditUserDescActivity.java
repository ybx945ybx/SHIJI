package cn.yiya.shiji.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.yiya.shiji.R;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;

/**
 * 个人介绍
 * Created by Amy on 2016/9/5.
 */
public class EditUserDescActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private String userDesc;

    private ImageView ivBack;
    private TextView tvTitle;
    private TextView tvSave;

    private EditText etDesc;

    private boolean bSaved = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_desc);
        initIntent();
        initViews();
        initEvents();
        init();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            userDesc = intent.getStringExtra("user_desc");
        }
    }

    @Override
    protected void initViews() {
        ivBack = (ImageView) findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText("个人介绍");
        tvSave = (TextView) findViewById(R.id.title_right);
        tvSave.setText("保存");
        tvSave.setTextColor(Color.parseColor("#ed5137"));

        etDesc = (EditText) findViewById(R.id.et_desc);
        etDesc.setText(userDesc);
        if (userDesc.length() <= 84) {
            etDesc.setSelection(userDesc.length());
        }
    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        tvSave.setOnClickListener(this);
    }

    @Override
    protected void init() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.title_right:
                //保存
                saveDesc();
                break;
            default:
                break;
        }
    }

    private void saveDesc() {
        userDesc = etDesc.getText().toString();
        new RetrofitRequest<>(ApiRequest.getApiShiji().changeRedInfo(userDesc))
                .handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            bSaved = true;
                            showTips("保存成功");
                            onBackPressed();
                        } else {
                            showTips("保存失败");
                        }
                    }
                });

    }

    @Override
    public void onBackPressed() {
        if (bSaved) {
            Intent intent = new Intent();
            intent.putExtra("desc", userDesc);
            setResult(RESULT_OK, intent);
        }
        super.onBackPressed();
    }
}
