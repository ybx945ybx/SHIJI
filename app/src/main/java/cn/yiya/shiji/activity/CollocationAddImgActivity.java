package cn.yiya.shiji.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.seles.tusdk.FilterManager;

import java.io.File;

import cn.yiya.shiji.R;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.tusdk.CutPicTuSdk;
import cn.yiya.shiji.utils.BitmapTool;
import cn.yiya.shiji.utils.SimpleUtils;

/**
 * Created by Tom on 2016/7/19.
 */
public class CollocationAddImgActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private ImageView ivCollocation;
    private ImageView ivAdd;
    private TextView tvCancle;
    private TextView tvNext;
    private static final int REQUEST_ADDGOODS = 1001;
    CutPicTuSdk tuSdk;

    private String mSelectPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collocation_add_img);
        TuSdk.messageHub().setStatus(this, R.string.lsq_initing);
        TuSdk.checkFilterManager(mFilterManagerDelegate);

        initIntent();
        initViews();
        initEvents();
        init();
    }

    private void initIntent() {

    }

    @Override
    protected void initViews() {
        ivCollocation = (ImageView) findViewById(R.id.iv_collocation);
        ivAdd = (ImageView) findViewById(R.id.image_add);
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        layoutParams.width = SimpleUtils.getScreenWidth(this) - SimpleUtils.dp2px(this, 56);
//        layoutParams.height = layoutParams.width * 484/319;
////        layoutParams.alignWithParent = true;
//        ivCollocation.setLayoutParams(layoutParams);

        tvCancle = (TextView) findViewById(R.id.tv_cancle);
        tvNext = (TextView) findViewById(R.id.tv_next);
//        selectPic();
    }

    @Override
    protected void initEvents() {
        tvCancle.setOnClickListener(this);
        tvNext.setOnClickListener(this);
        ivAdd.setOnClickListener(this);

    }

    @Override
    protected void init() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancle:
                onBackPressed();
                break;
            case R.id.tv_next:
                if (TextUtils.isEmpty(mSelectPath)) {
                    selectPics();
                } else {
                    Intent intent = new Intent(this, CollocationAddGoodsActivity.class);
                    intent.putExtra("path",mSelectPath);
                    startActivityForResult(intent, REQUEST_ADDGOODS);
                }
                break;
            case R.id.image_add:
                selectPics();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        SimpleUtils.deleteCropImg(this,mSelectPath);
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1111) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                if (uri != null) {
                    startPhotoZoom(uri);
                }
            } else if (resultCode == RESULT_CANCELED) {
                onBackPressed();
            }
        } else if (requestCode == 1112) {
            if (resultCode != Activity.RESULT_CANCELED && data != null) {
                if (data.getExtras() != null) {
                    Bitmap photo = data.getExtras().getParcelable("data");
//                    BaseApplication.getInstance().collocation = photo;
//                    ivCollocation.setImageBitmap(photo);							//(drawable);
                }
            } else {
                onBackPressed();
            }
        } else if (requestCode == REQUEST_ADDGOODS) {
            if (resultCode == RESULT_OK) {
                if (BaseApplication.getInstance().bCollocationSuccess) {
                    BaseApplication.getInstance().bCollocationSuccess = false;
                    setResult(RESULT_OK);
                    finish();
                }
            }
        }
    }

    // 调用系统裁剪图片
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 319);
        intent.putExtra("aspectY", 484);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 319);
        intent.putExtra("outputY", 484);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 1112);
    }

    // 调用系统相册 选取图片
    private void selectPic() {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            intent.putExtra("return-data", true);
            startActivityForResult(intent, 1111);
        } catch (Exception e) {
            showTips("无法调用系统相册");
        }
    }

    private void selectPics() {
        tuSdk = new CutPicTuSdk();
        tuSdk.openTuSdkCutPicCollocation(this, true, new CutPicTuSdk.OnImageHandleListerner() {
            @Override
            public void onFinished(File imgeFile) {
                mSelectPath = imgeFile.getAbsolutePath();
//                BaseApplication.getInstance().collocation = mSelectPath;
                BitmapTool.disaplayImage("file://" + mSelectPath, ivCollocation, null);
                ivAdd.setVisibility(View.GONE);
            }
        });
    }

    private FilterManager.FilterManagerDelegate mFilterManagerDelegate = new FilterManager.FilterManagerDelegate() {
        @Override
        public void onFilterManagerInited(FilterManager manager) {
            TuSdk.messageHub().showSuccess(CollocationAddImgActivity.this, R.string.lsq_inited);
            selectPics();
        }
    };
}
