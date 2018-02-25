package cn.yiya.shiji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import cn.yiya.shiji.R;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.QiNiuCloud;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.config.CustomEvent;
import cn.yiya.shiji.entity.PicDiaryUploadItem;
import cn.yiya.shiji.utils.BitmapTool;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.views.ProgressDialog;

/**
 * Created by chenjian on 2015/12/7.
 */
public class ShowOrderActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ImageView ivPhoto;
    private String mPath;
    private TextView tvPublish;
    private TextView tvCancel;
    private EditText etContent;
    private String tagInfo;
    private RelativeLayout rlytTip;
    private ImageView ivDelete;
    private boolean bSkip;
    private boolean bClick;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_order);
        initViews();
        initEvents();
        init();
    }

    @Override
    protected void initViews() {
        ivPhoto = (ImageView) findViewById(R.id.show_order_image);
        mPath = getIntent().getStringExtra("path");
        tvPublish = (TextView) findViewById(R.id.show_order_publish);
        tvCancel = (TextView) findViewById(R.id.show_order_back);
        rlytTip = (RelativeLayout) findViewById(R.id.show_tip_layout);
        ivDelete = (ImageView) findViewById(R.id.show_tip_delete);

        etContent = (EditText) findViewById(R.id.show_order_content);
        tagInfo = getIntent().getStringExtra("tag");
        if (TextUtils.isEmpty(mPath)) {
            rlytTip.setVisibility(View.VISIBLE);
        } else {
            rlytTip.setVisibility(View.GONE);
            BitmapTool.disaplayImage("file://" + mPath, ivPhoto, null);
        }

        String text = getIntent().getStringExtra("text");
        if (!TextUtils.isEmpty(text)) {
            etContent.setText(text);
        }

        BaseApplication.bShowOrder = false;
    }

    @Override
    protected void initEvents() {
        ivDelete.setOnClickListener(this);
        tvPublish.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        ivPhoto.setOnClickListener(this);
    }

    @Override
    protected void init() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_order_back:
                onBackPressed();
                break;
            case R.id.show_order_publish:
                if (TextUtils.isEmpty(mPath)) {
                    Toast.makeText(ShowOrderActivity.this, "请添加图片", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!bClick) {
                    if (NetUtil.IsInNetwork(this)) {
                        doPublish();
                    } else {
                        showTips(Configration.OFF_LINE_TIPS);
                    }

                }

                break;
            case R.id.show_tip_delete:
                rlytTip.setVisibility(View.GONE);
                break;
            case R.id.show_order_image:
                bSkip = getIntent().getBooleanExtra("skip", false);
                if (!bSkip) {
                    showChangeDialog("确定要更改图片吗？");
                } else {
                    skipToPublishWork(bSkip);
                }
                break;
        }
    }

    private void skipToPublishWork(boolean skip) {
        boolean bAuto = getIntent().getBooleanExtra("AUTO", false);
        String data = getIntent().getStringExtra("TAG");
        String cover = getIntent().getStringExtra("cover");
        String goodesId = getIntent().getStringExtra("goodsId");
        Intent intent = new Intent();
        if (skip) {
            intent.setClass(ShowOrderActivity.this, PublishWorkActivity.class);
            intent.putExtra("TAG", data);
            intent.putExtra("skip", skip);
            intent.putExtra("AUTO", bAuto);
            intent.putExtra("cover", cover);
            intent.putExtra("goodsId", goodesId);
            intent.putExtra("text", etContent.getText().toString().trim());
            startActivity(intent);
        } else {
            intent.putExtra("path", mPath);
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    private void doPublish() {
        bClick = true;
        showPreDialog("正在发布");
        PicDiaryUploadItem item = new Gson().fromJson(tagInfo, PicDiaryUploadItem.class);
        item.text = etContent.getText().toString().trim();
        publishContent(mPath, item, item.key, new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    bClick = false;
                    hidePreDialog();
                    BaseApplication.bShowOrder = true;
                    Toast.makeText(ShowOrderActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
//                    MobclickAgent.onEvent(ShowOrderActivity.this, UMengEvent.PublishWork);
                    SimpleUtils.deleteCropImg(ShowOrderActivity.this, mPath);

                    CustomEvent.onEvent(ShowOrderActivity.this, ((BaseApplication) getApplication()).getDefaultTracker(), "ShowOrderActivity", CustomEvent.PublishWork);
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    public void publishContent(String filepath, final PicDiaryUploadItem item, final String key, final MsgCallBack callBack) {
        QiNiuCloud.getInstance().upLoadToServer(filepath, key, new MsgCallBack() {
            @Override
            public void onResult(final HttpMessage msg) {
                if (msg.isSuccess()) {
                    new RetrofitRequest<>(ApiRequest.getApiShiji().publishOnePic(item)).handRequest(callBack);
                } else {
                    callBack.onResult(msg);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        showExitDialog("确定要放弃编辑吗？");
    }

    private void showExitDialog(String title) {
        showCustomMutiDialog(title, new ProgressDialog.DialogClick() {
            @Override
            public void OkClick() {
                setResult(RESULT_CANCELED);
                finish();
            }

            @Override
            public void CancelClick() {

            }
        });
    }

    private void showChangeDialog(String title) {
        showCustomMutiDialog(title, new ProgressDialog.DialogClick() {
            @Override
            public void OkClick() {
                skipToPublishWork(false);
            }

            @Override
            public void CancelClick() {

            }
        });
    }


}
