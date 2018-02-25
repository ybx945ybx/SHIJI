package cn.yiya.shiji.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;

import cn.yiya.shiji.R;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.QiNiuCloud;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.User;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.BitmapTool;
import cn.yiya.shiji.utils.DebugUtil;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.AreaSelectDialog;
import cn.yiya.shiji.views.RoundedNormalIV;

public class EditUserInfoActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ImageView ivBack;
    private TextView tvTitle;
    private TextView tvSave;

    private LinearLayout llytHead;
    private RoundedNormalIV headImage;

    private EditText etName;

    private LinearLayout llRedDesc;
    private RelativeLayout rlRedDesc;
    private TextView tvRedDesc;

    private boolean isBusy;
    private Bitmap photo;
    private int user_id;
    private String key;

    private String userDesc="";
    private final static int USER_DESC = 1113;
    private boolean bSaveDesc=false;

    private LinearLayout llytmyShop;
    private EditText etShopName;
    private EditText etShopIntroduce;
    private EditText etShopSite;
    private EditText etAddress;

    private String strProvice;
    private String strCity;
    private String strDistrict;
    private String typeId;

    private boolean isHasShop;          //  已经开过店
    private boolean openShop;           //  是开店流程过来编辑资料
    private int modify;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo_edit);
        initIntent();
        initViews();
        initEvents();
        init();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if(intent != null){
            typeId = intent.getStringExtra("typeId");
            if(!TextUtils.isEmpty(typeId)) {
                openShop = true;
            }
        }
    }

    public void getUserInfo() {
        new RetrofitRequest<User>(ApiRequest.getApiShiji().getUserDetail()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    User mUser = (User) msg.obj;
                    if (mUser != null) {
                        etName.setText(mUser.getName() + "");
                        etName.setSelection(etName.getText().length());
                        Netroid.displayImage(Util.transfer(mUser.getHead()), headImage, R.mipmap.user_icon_default);
                        user_id = mUser.getUser_id();
                        typeId = mUser.getShop_type();
                        if (mUser.getRed() == 1) {
                            llRedDesc.setVisibility(View.VISIBLE);
                            userDesc = mUser.getRed_desc();
                            setUserDesc();
                        }
                        isHasShop = mUser.isHave_shop();
                        if (isHasShop) {
                            llytmyShop.setVisibility(View.VISIBLE);
                            etShopName.setText(mUser.getShop_name());
                            etShopIntroduce.setText(mUser.getShop_desc());
                            etShopSite.setText(mUser.getEn_name());
                            strProvice = mUser.getShop_province();
                            strCity = mUser.getShop_city();
                            etAddress.setText(mUser.getShop_province() + " " + mUser.getShop_city());
                            typeId = mUser.getShop_type();
                            modify = mUser.getModify();
                            if(modify == 1){
                                etShopSite.setFocusable(false);
                                etShopSite.setHint("已修改过，不可再更改域名");
                            }
                        }else {
                            llytmyShop.setVisibility(View.GONE);
                        }

                    }
                } else {
                    showTips(msg.message);
                }
            }
        });
    }

    // 调用系统裁剪图片
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 340);
        intent.putExtra("outputY", 340);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 1112);
    }

    // 上传头像或者名字到服务器
    private void upLoadToServer(Bitmap photo, final String name) {

//        if(photo == null){
//            showTips("请上传头像");
//            return;
//        }

        if (TextUtils.isEmpty(name)) {
            showTips("请填写昵称");
            return;
        }
        if(isHasShop) {

            if (TextUtils.isEmpty(etShopName.getText().toString())) {
                showTips("请填写店铺名");
                return;
            }
            if (TextUtils.isEmpty(etShopIntroduce.getText().toString())) {
                showTips("请填写店铺简介");
                return;
            }
            if (TextUtils.isEmpty(etShopSite.getText().toString())) {
                showTips("请填写店铺网址");
                return;
            }
            if (TextUtils.isEmpty(etAddress.getText().toString())) {
                showTips("请填写常住地");
                return;
            }
        }

        showPreDialog("正在保存");

        if (photo != null) {
            key = user_id + "_avatar_" + System.currentTimeMillis();
            QiNiuCloud.getInstance().upLoadToServer(BitmapTool.Bitmap2Bytes(photo), key, new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    if (msg.isSuccess()) {
                        upLoadNameToServer(name);
                    } else {
                        hidePreDialog();
                        showTips(msg.message);
                    }
                }
            });
        } else {
            upLoadNameToServer(name);
        }
    }

    // 保存名字数据
    private void upLoadNameToServer(final String name) {
        new RetrofitRequest<>(ApiRequest.getApiShiji().changeName(name)).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                hidePreDialog();
                if (msg.isSuccess()) {
                    if (isHasShop) {
                        saveShopInfo();
                    } else {
                        finishSave();

                    }

                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1111) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                if (uri != null) {
                    startPhotoZoom(uri);
                } else {
                    isBusy = false;
                }
            } else {
                isBusy = false;
                return;
            }
        } else if (requestCode == 1112) {
            isBusy = false;
            if (resultCode != Activity.RESULT_CANCELED && data != null) {
                if (data.getExtras() != null) {
                    photo = data.getExtras().getParcelable("data");
                    headImage.setImageBitmap(photo);                            //(drawable);
                }
            }
        }else if(requestCode == USER_DESC){
            if(resultCode==RESULT_OK){
                if(data!=null){
                    userDesc=data.getStringExtra("desc");
                    setUserDesc();
                    bSaveDesc=true;
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.et_shop_site:
                if(modify == 1){
                    showTips("已修改过，不可再更改域名");
                }
                break;
            case R.id.title_right:
                upLoadToServer(photo, etName.getText().toString());
                break;
            case R.id.llt_headImage:
                if (isBusy) {
                    break;
                }
                isBusy = true;
                try {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intent, 1111);
                } catch (Exception e) {
                    showTips("无法调用系统相册");
                }
                break;
            case R.id.rl_red_desc:
                //跳转到个人介绍编辑页面
                Intent intent = new Intent(this, EditUserDescActivity.class);
                intent.putExtra("user_desc", userDesc);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, USER_DESC);
                break;
            case R.id.et_address:
                showAreaSelectDialog();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(openShop){
            return;
        }
        if(bSaveDesc){
            Intent intent=new Intent();
            intent.putExtra("user_desc",userDesc);
            setResult(Activity.RESULT_OK,intent);
        }else {
            setResult(Activity.RESULT_CANCELED);
        }
        finish();
    }

    @Override
    protected void initViews() {
        ivBack = (ImageView) findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        if(openShop){
            tvTitle.setText("开店资料");
            ivBack.setVisibility(View.GONE);
        }else {
            tvTitle.setText("编辑资料");
            ivBack.setVisibility(View.VISIBLE);
        }
        tvSave = (TextView) findViewById(R.id.title_right);
        tvSave.setText("保存");
        tvSave.setTextColor(Color.parseColor("#ed5137"));

        llytHead = (LinearLayout) findViewById(R.id.llt_headImage);
        headImage = (RoundedNormalIV) findViewById(R.id.headImage);

        etName = (EditText) findViewById(R.id.etName);

        llRedDesc = (LinearLayout) findViewById(R.id.ll_red_desc);
        rlRedDesc = (RelativeLayout) findViewById(R.id.rl_red_desc);
        tvRedDesc = (TextView) findViewById(R.id.tv_red_desc);

        llytmyShop = (LinearLayout) findViewById(R.id.llyt_my_shop);
        etShopName = (EditText) findViewById(R.id.et_shop_name);
        etShopIntroduce = (EditText) findViewById(R.id.et_shop_introduce);
        etShopSite = (EditText) findViewById(R.id.et_shop_site);
        etAddress = (EditText) findViewById(R.id.et_address);
    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        tvSave.setOnClickListener(this);
        llytHead.setOnClickListener(this);
        rlRedDesc.setOnClickListener(this);
        etAddress.setOnClickListener(this);
        etShopSite.setOnClickListener(this);
    }

    @Override
    protected void init() {
        getUserInfo();
    }

    private void setUserDesc(){
       if(userDesc.replace("\n","").trim().length()<=11){
           tvRedDesc.setText(userDesc.replace("\n","").trim());
       }else {
           tvRedDesc.setText(userDesc.replace("\n","").trim().substring(0,11)+"...");
       }
    }

    private void showAreaSelectDialog() {
        AreaSelectDialog dialog = new AreaSelectDialog(this);
        dialog.builder().setOnSaveListener(new AreaSelectDialog.OnCitySaveListener() {
            @Override
            public void onSave(String[] text, AreaSelectDialog dialog) {
                strProvice = text[0];
                strCity = text[1];
                strDistrict = text[2];
                for (int i = 0; i < text.length; i++) {
                    etAddress.setText(text[0] + " " + text[1]);
                }
                dialog.dismiss();
            }
        }).show();
    }

    private boolean saveShopInfo() {

        HashMap<String, String> map = new HashMap<>();
        map.put("name", etShopName.getText().toString());
        map.put("province", strProvice);
        map.put("city", strCity);
        map.put("type_id", typeId);
        map.put("description", etShopIntroduce.getText().toString());
        map.put("en_name", etShopSite.getText().toString());


        new RetrofitRequest<>(ApiRequest.getApiShiji().upDateShopInfo(map)).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                hidePreDialog();
                if (msg.isSuccess()) {
                    if (openShop) {
                        Intent intent = new Intent(EditUserInfoActivity.this, NewMainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("main_activity_dest", "openShop");
                        startActivity(intent);
                    } else {
                        finishSave();
                    }
                } else {
                    showTips(msg.message);
                }
            }
        });

        return true;
    }

    private void finishSave(){
        Intent intent = new Intent();
        if (photo != null) {
            intent.putExtra("photo", photo);
        }
        intent.putExtra("name", etName.getText().toString());
        if (bSaveDesc) {
            intent.putExtra("user_desc", userDesc);
        }
        setResult(RESULT_OK, intent);
        finish();
        showTips("修改成功");
    }
}
