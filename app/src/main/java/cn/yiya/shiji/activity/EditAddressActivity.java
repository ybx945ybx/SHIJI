package cn.yiya.shiji.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.seles.tusdk.FilterManager;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.yiya.shiji.R;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.QiNiuCloud;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.entity.AddressListItem;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.tusdk.CutPicTuSdk;
import cn.yiya.shiji.utils.BitmapTool;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.AreaSelectDialog;
import cn.yiya.shiji.views.ProgressDialog;

public class EditAddressActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private Handler mHandler;

    private ImageView ivBack;
    private TextView tvTitle;
    private TextView tvDelete;

    private EditText etName;
    private EditText etTelephone;
    private EditText etIdentificationNumber;
    private RelativeLayout rlytArea;
    private EditText etArea;
    private EditText etPostCode;
    private EditText etDetailAddress;


    private ImageView ivAddFront;
    private ImageView ivAddBack;

    private TextView tvSave;

    private String urlLeft = null;
    private String urlRight = null;
    private static final String qiniuHttp = "http://idcard.cdnqiniu02.qnmami.com/";
    private static final int LEFT_IMAGE = 100;
    private static final int RIGHT_IMAGE = 200;
    private int nImagePosition;
    private boolean bInit;

    private AddressListItem mInfo;
    private int position;
    private String[] strArray;
    private String mLeftSelectPath;
    private String mRightSelectPath;
    private boolean mLeftSuccess;
    private boolean mRightSuccess;
    private boolean uploadRight;
    private boolean uploadLeft;

    private static final int DELETE_RESULT_OK = 111;
    private static final int SAVE_RESULT_OK = 222;

    private boolean isSelect;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        mHandler = new Handler();
        initViews();
        initEvents();
        initIntent();
        init();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {

            mInfo = new Gson().fromJson(intent.getStringExtra("info"), AddressListItem.class);
            position = intent.getIntExtra("position", 0);
            if (mInfo != null) {
                tvTitle.setText("修改地址");
                tvDelete.setVisibility(View.VISIBLE);
                tvDelete.setText("删除");
                etName.setText(mInfo.getRecipient());
                etTelephone.setText(mInfo.getMobile());
                etArea.setText(mInfo.getProvince() + mInfo.getCity() + mInfo.getDistrict());

                strArray = new String[3];
                strArray[0] = mInfo.getProvince();
                strArray[1] = mInfo.getCity();
                strArray[2] = mInfo.getDistrict();

                etPostCode.setText(mInfo.getPost_code());
                etDetailAddress.setText(mInfo.getAddress());
                etIdentificationNumber.setText(mInfo.getIdentity_number());
                mLeftSelectPath = mInfo.getIdentity_copy_front();
                mRightSelectPath = mInfo.getIdentity_copy_back();
                urlLeft = Util.transfer2(mInfo.getIdentity_copy_front());
                urlRight = Util.transfer2(mInfo.getIdentity_copy_back());
                Netroid.displayImage(Util.transfer2(mInfo.getIdentity_copy_front()), ivAddFront);
                Netroid.displayImage(Util.transfer2(mInfo.getIdentity_copy_back()), ivAddBack);
            } else {
                tvTitle.setText("编辑地址");
                tvDelete.setVisibility(View.GONE);
            }
            isSelect = intent.getBooleanExtra("isSelect", false);
            if(isSelect){
                tvDelete.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void initViews() {
        ivBack = (ImageView) findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvDelete = (TextView) findViewById(R.id.title_right);

        etName = (EditText) findViewById(R.id.etName);
        etTelephone = (EditText) findViewById(R.id.etTelephone);
        rlytArea = (RelativeLayout) findViewById(R.id.rlyt_area);
        etArea = (EditText) findViewById(R.id.etArea);
        etPostCode = (EditText) findViewById(R.id.etPost_code);
        etDetailAddress = (EditText) findViewById(R.id.etDetailAdres);
        etIdentificationNumber = (EditText) findViewById(R.id.etIdentificationNumber);

        ivAddFront = (ImageView) findViewById(R.id.iv_front);
        ivAddBack = (ImageView) findViewById(R.id.iv_back);

        tvSave = (TextView) findViewById(R.id.tv_save_address);
    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        tvDelete.setOnClickListener(this);
        ivAddFront.setOnClickListener(this);
        ivAddBack.setOnClickListener(this);
        tvSave.setOnClickListener(this);
        etArea.setOnClickListener(this);
    }

    @Override
    protected void init() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.title_right:
                showCustomMutiDialog("您确定要删除该地址信息么？", new ProgressDialog.DialogClick() {
                    @Override
                    public void OkClick() {
                        deleteData();
                    }

                    @Override
                    public void CancelClick() {

                    }
                });
                break;
            case R.id.iv_front:
                nImagePosition = LEFT_IMAGE;
                if (!bInit) {
                    TuSdk.messageHub().setStatus(this, R.string.lsq_initing);
                    TuSdk.checkFilterManager(mFilterManagerDelegate);
                } else {
                    selectPics(LEFT_IMAGE);
                }
                break;
            case R.id.iv_back:
                nImagePosition = RIGHT_IMAGE;
                if (!bInit) {
                    TuSdk.messageHub().setStatus(this, R.string.lsq_initing);
                    TuSdk.checkFilterManager(mFilterManagerDelegate);
                }else {
                    selectPics(RIGHT_IMAGE);
                }
                break;
            case R.id.etArea:
                showAreaSelectDialog();
                break;
            case R.id.tv_save_address:
                if(isEffectClick()) {
                    saveAddress();
                }
                break;
        }
    }

    // 关闭软键盘
    private void hideKeyboard() {
        InputMethodManager manager = ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE));
        manager.hideSoftInputFromWindow(etArea.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void confirmDelete(String title){
        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("提示").setMessage(title).
                setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteData();
                    }
                }).
                setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    private void deleteData() {
        new RetrofitRequest<>(ApiRequest.getApiShiji().deleteAdress(String.valueOf(mInfo.getId())))
                .handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            Toast.makeText(mApp, "删除成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.putExtra("position", position);
                            setResult(DELETE_RESULT_OK, intent);
                            finish();
                        } else {
                            Toast.makeText(mApp, msg.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void showAreaSelectDialog() {
        AreaSelectDialog dialog = new AreaSelectDialog(this);
        dialog.builder().setOnSaveListener(new AreaSelectDialog.OnCitySaveListener() {
            @Override
            public void onSave(String[] text, AreaSelectDialog dialog) {
                strArray = text;
                for (int i = 0; i < text.length; i++) {
                    etArea.setText(text[0] + text[1] + text[2]);
                }
                dialog.dismiss();
            }
        }).show();
    }

    private void saveAddress() {
        final AddressListItem info = getAddressInfo();

        if (info == null) {
            return;
        }

        if(mInfo!= null) {
            info.setId(mInfo.getId());
        }

        if (tvTitle.getText().toString().equals("编辑地址")) {
            new RetrofitRequest<>(ApiRequest.getApiShiji().addAdress(info)).handRequest(new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    if (msg.isSuccess()) {
                        showTips("添加成功");
                        setResult(SAVE_RESULT_OK);
                        finish();
                    } else {
                        showTips(msg.message);
                    }
                }
            });
        } else {
            new RetrofitRequest<>(ApiRequest.getApiShiji().updateAdress(info)).handRequest(new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    if (msg.isSuccess()) {
                        Toast.makeText(mApp, "修改成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.putExtra("info", new Gson().toJson(info));
                        intent.putExtra("position", position);
                        setResult(SAVE_RESULT_OK, intent);
                        finish();
                    } else {
                        Toast.makeText(mApp, msg.message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    // 获取地址添加信息
    private AddressListItem getAddressInfo() {
        if(!TextUtils.isEmpty(etName.getText().toString())){
        }else {
            showTips("请正确填写姓名");
            return null;
        }
        if(!isMobileNO(etTelephone.getText().toString())){
            showTips("请正确填写手机号码");
            return null;
        }
        if(TextUtils.isEmpty(etPostCode.getText().toString())){
            showTips("请正确填写邮编");
            return null;
        }
        if(TextUtils.isEmpty(etArea.getText().toString())){
            showTips("请正确添加城市");
            return null;
        }
        if(TextUtils.isEmpty(etDetailAddress.getText().toString())){
            showTips("请正确填写详细地址");
            return null;
        }

        if(TextUtils.isEmpty(etIdentificationNumber.getText().toString())){
            showTips("请填写身份证号码");
            return null;
        }

        if(uploadLeft){
            if(!mLeftSuccess){
                showTips("身份证未上传完成");
                return null;
            }
        }

        if(uploadRight){
            if(!mRightSuccess){
                showTips("身份证未上传完成");
                return null;
            }
        }

        if(TextUtils.isEmpty(mLeftSelectPath)||TextUtils.isEmpty(mRightSelectPath)){
            showTips("请上传身份证照片");
            return null;
        }

//        if(!mLeftSuccess || !mRightSuccess){
//            showTips("身份证上传失败");
//            return null;
//        }

        AddressListItem info = new AddressListItem();
        info.setMobile(etTelephone.getText().toString().trim());
        info.setRecipient(etName.getText().toString().trim());
        info.setPost_code(etPostCode.getText().toString().trim());
        info.setProvince(strArray[0]);
        info.setCity(strArray[1]);
        info.setDistrict(strArray[2]);
        info.setAddress(etDetailAddress.getText().toString().trim());
        info.setIdentity_number(etIdentificationNumber.getText().toString().trim());
        info.setIdentity_copy_front(urlLeft);
        info.setIdentity_copy_back(urlRight);
        return info;
    }

    public void uploadTimeImage(String fileName, String key , MsgCallBack callBack) {
        QiNiuCloud qiniu = QiNiuCloud.getInstance();
        qiniu.upLoadToIdcard(fileName, key, callBack);
    }

    public static boolean isMobileNO(String mobiles) {
        Pattern pMobile = Pattern.compile("^1(3[0-9]|4[57]|5[0-35-9]|8[0-9]|70)\\d{8}$");
        Pattern pCM = Pattern.compile("(^1(3[4-9]|4[7]|5[0-27-9]|7[8]|8[2-478])\\d{8}$)|(^1705\\d{7}$)");
        Pattern pCU = Pattern.compile("(^1(3[0-2]|4[5]|5[56]|7[6]|8[56])\\d{8}$)|(^1709\\d{7}$)");
        Pattern pCT = Pattern.compile("(^1(33|53|77|8[019])\\d{8}$)|(^1700\\d{7}$)");
        if(pMobile.matcher(mobiles).matches()|| pCM.matcher(mobiles).matches()|| pCU.matcher(mobiles).matches()|| pCT.matcher(mobiles).matches()){
            return true;
        }
        return false;
    }

    //匹配邮编
    public static boolean isZipNO(String zipString) {
        String str = "^[1-9][0-9]{5}$";
        return Pattern.compile(str).matcher(zipString).matches();
    }

    public static boolean isUseNO(String userNumber) {
        // 15位号码
        Pattern p1 = Pattern.compile("/^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$/");
        Matcher m1 = p1.matcher(userNumber);
        // 18位号码
        Pattern p2 = Pattern.compile("/^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$/");
        Matcher m2 = p2.matcher(userNumber);
        return m1.matches()||m2.matches();
    }


    private void selectPics(final int type){

        CutPicTuSdk tuSdk = new CutPicTuSdk();
        tuSdk.openTuSdkSimpleHand(this, new CutPicTuSdk.OnImageHandleListerner() {
            @Override
            public void onFinished(File imgeFile) {
                if (type == LEFT_IMAGE) {
                    mLeftSelectPath = imgeFile.getAbsolutePath();
                    BitmapTool.disaplayImage("file://" + mLeftSelectPath, ivAddFront, null);
                    uploadImage(mLeftSelectPath, 0);
                } else {
                    mRightSelectPath = imgeFile.getAbsolutePath();
                    BitmapTool.disaplayImage("file://" + mRightSelectPath, ivAddBack, null);
                    uploadImage(mRightSelectPath, 1);
                }
            }
        });
    }

    private FilterManager.FilterManagerDelegate mFilterManagerDelegate = new FilterManager.FilterManagerDelegate(){
        @Override
        public void onFilterManagerInited(FilterManager manager){
            TuSdk.messageHub().showSuccess(EditAddressActivity.this, R.string.lsq_inited);
            bInit = true;
            selectPics(nImagePosition);
        }
    };

    private void uploadImage(String path, final int type) {
        final String key = "shiji-idcard_" + BaseApplication.getInstance().readUserMID() + "_" + System.currentTimeMillis() + "_back";
        if (type == 1) {
            uploadRight = true;
        } else {
            uploadLeft = true;
        }
        uploadTimeImage(path, key, new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    if (type == 1) {
                        urlRight = qiniuHttp + key;
                        mRightSuccess = true;
                    } else {
                        urlLeft = qiniuHttp + key;
                        mLeftSuccess = true;
                    }
                }else {
                    showTips(msg.message);
                }
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
