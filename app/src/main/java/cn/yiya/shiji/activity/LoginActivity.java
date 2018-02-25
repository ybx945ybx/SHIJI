package cn.yiya.shiji.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wrapp.floatlabelededittext.FloatLabeledEditText;

import java.util.HashMap;

import cn.yiya.shiji.R;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.GeTui;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.config.CustomEvent;
import cn.yiya.shiji.entity.RegStatusInfo;
import cn.yiya.shiji.entity.RegisterInfo;
import cn.yiya.shiji.entity.User;
import cn.yiya.shiji.utils.DebugUtil;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.utils.RegexValidateUtil;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.ProgressDialog;

public class LoginActivity extends Activity implements View.OnClickListener {
    private ImageView ivCancle;
    private FloatLabeledEditText float_edt_phone, float_edt_code;
    private EditText edt_phone, edt_code;
    private View vPhone, vCode;
    private Button btn_code;
    private Button btn_login;
    private LinearLayout llytTips;
    private LinearLayout llytOtherLogin;
    private ImageView ivOtherLogin;
    private LinearLayout llytAgreement;
    private TextView tvAgreement;
    private View layoutTips;
    private TextView tvContext;

    private Handler mHandler;
    private boolean isShow;
    private String phone;
    private String code;
    private String channelId;
    private CountDownTimer timer;
    SmsContent content;
    private boolean bFinish = false;

    private IWXAPI api;
    private boolean register;
    public Dialog progressDialog;

    @Override
    public void onClick(View v) {

        GeTui.getCID(BaseApplication.getInstance());
        switch (v.getId()) {
            case R.id.wx_login:        //微信登陆
                if (!api.isWXAppInstalled()) {
                    showTips("请先安装微信客户端");
                    return;
                }
                showPreDialog("微信登录中");
                api.registerApp(Configration.WX_APPID);
                final SendAuth.Req req = new SendAuth.Req();
                req.scope = Configration.WX_APP_SCOPE;
                req.state = Configration.WX_APP_STATE;
                req.transaction = "login" + System.currentTimeMillis();
                api.sendReq(req);
                break;
            case R.id.img_cancale:
                setResult(RESULT_CANCELED);
                finish();
                this.overridePendingTransition(R.anim.slide_in_bottom_top, R.anim.slide_out_buttom_top);
                break;
            case R.id.btn_login:    //登陆
                CustomEvent.onEvent(this, ((BaseApplication) getApplication()).getDefaultTracker(), "LoginActivity", CustomEvent.Login);
                hideSoftInput(edt_code);
                hideSoftInput(edt_phone);
                verifyInput();
                break;
            case R.id.btn_getCode:    //获取验证码
                hideSoftInput(edt_code);
                hideSoftInput(edt_phone);
                getVerificationCode();
                break;
            case R.id.agreement:
                Intent intentAccept = new Intent(this, AcceptActivity.class);
                intentAccept.putExtra("type", 2);
                startActivity(intentAccept);
                break;
            default:
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mHandler = new MyHandler(getMainLooper());

        initShareSdk(this);
        api = WXAPIFactory.createWXAPI(this, Configration.WX_APPID, false);

        initViews();
        initEvents();
        init();
    }

    protected void initViews() {
        ivCancle = (ImageView) findViewById(R.id.img_cancale);
        float_edt_phone = (FloatLabeledEditText) findViewById(R.id.float_et_phone);
        float_edt_code = (FloatLabeledEditText) findViewById(R.id.float_et_code);
        edt_code = (EditText) findViewById(R.id.edt_code);
        edt_phone = (EditText) findViewById(R.id.edt_phone);
        vPhone = findViewById(R.id.view_phone);
        vCode = findViewById(R.id.view_code);
        btn_code = (Button) findViewById(R.id.btn_getCode);
        btn_login = (Button) findViewById(R.id.btn_login);
        llytTips = (LinearLayout) findViewById(R.id.login_tips);
        llytOtherLogin = (LinearLayout) findViewById(R.id.llyt_other_login);
        ivOtherLogin = (ImageView) findViewById(R.id.wx_login);
        if (Util.isWeixinAvilible(this)) {
            llytOtherLogin.setVisibility(View.VISIBLE);
            ivOtherLogin.setVisibility(View.VISIBLE);
        }
        llytAgreement = (LinearLayout) findViewById(R.id.login_agreement);
        tvAgreement = (TextView) findViewById(R.id.agreement);
        tvAgreement.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        bFinish = getIntent().getBooleanExtra("finish", false);
    }

    protected void initEvents() {
        content = new SmsContent(new Handler());
        //注册短信变化监听
        this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, content);

        ivCancle.setOnClickListener(this);
        ivOtherLogin.setOnClickListener(this);
        (findViewById(R.id.agreement)).setOnClickListener(this);

        edt_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11) {
                    YoYo.with(Techniques.Flash).duration(300).playOn(vPhone);
                    vPhone.setBackgroundColor(Color.parseColor("#ffffff"));
                } else {
                    vPhone.setBackgroundColor(Color.parseColor("#a7a7a7"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 6) {
                    YoYo.with(Techniques.Flash).duration(300).playOn(vCode);
                    vCode.setBackgroundColor(Color.parseColor("#ffffff"));
                } else {
                    vCode.setBackgroundColor(Color.parseColor("#a7a7a7"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    protected void init() {

    }

    private void showCustomToast(String msg) {
        layoutTips = LayoutInflater.from(this).inflate(R.layout.login_custom_toast_layout, (ViewGroup) findViewById(R.id.toast_layout_root));
        tvContext = (TextView) layoutTips.findViewById(R.id.toast_context);
        tvContext.setTextColor(Color.parseColor("#3c3c3c"));
        tvContext.setText(msg);
        final Toast toast = new Toast(this);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(layoutTips);
        toast.show();
    }

    //收起软键盘
    private void hideSoftInput(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    // 用户输入验证并登录
    private boolean verifyInput() {
        if (!NetUtil.IsInNetwork(this)) {
            showCustomToast(Configration.OFF_LINE_TIPS);
            return false;
        }
        phone = edt_phone.getText().toString().trim();
        code = edt_code.getText().toString().trim();
        if (!verifyPhone(phone)) {
            return false;
        }
        if (!verifyCode(code)) {
            return false;
        }
        showPreDialog("");
        BaseApplication.Session = "";
        channelId = Util.getChannelName(this);
        //register=true为新用户注册
        if (register) {

            HashMap<String, String> params = new HashMap<>();
            params.put("mobile", phone);
            params.put("code", code);
            params.put("reg_follow_flag", String.valueOf(1));
            if (!TextUtils.isEmpty(channelId)) {
                params.put("channel_id", channelId);
            } else {
                params.put("channel_id", "");
            }

            if (TextUtils.isEmpty(BaseApplication.ClientId)) {
                params.put("cid", "");
            } else {
                params.put("cid", BaseApplication.ClientId);
            }

            new RetrofitRequest<>(ApiRequest.getApiShiji().register(params)).handRequest(
                    new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            mHandler.sendEmptyMessage(0);
                            if (msg.isSuccess()) {
                                getLoginUserInfo(false, true);
                            } else {
                                Message message = mHandler.obtainMessage();
                                message.obj = msg.message;
                                message.what = 1;
                                mHandler.sendMessage(message);
                                showCustomToast(msg.message);
                                edt_code.setText("");
                            }
                        }
                    }
            );

        } else {
            HashMap<String, String> maps = new HashMap<>();
            maps.put("mobile", phone);
            maps.put("code", code);
            maps.put("channel_id", channelId);
            new RetrofitRequest<RegStatusInfo>(ApiRequest.getApiShiji().usrLogin(maps)).handRequest(new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    hidePreDialog();
                    if (msg.isSuccess() && msg.obj != null) {
                        RegStatusInfo info = (RegStatusInfo) msg.obj;
                        if (info.is_complete()) {
                            int reg_status = info.getReg_status();
                            if (reg_status == 3) {
                                if(info.getNew_user() == 1){
                                    getLoginUserInfo(false, true);
                                }else {
                                    getLoginUserInfo(false, false);
                                }
                            } else {
                                if(info.getNew_user() == 1){
                                    getLoginUserInfo(true, true);
                                }else {
                                    getLoginUserInfo(true, false);
                                }
                            }
                        }
                    } else {
                        mHandler.sendEmptyMessage(0);
                        if (!isShow) {
                            return;
                        }
                        showCustomToast(msg.message);
                        edt_code.setText("");
                    }
                }
            });
        }
        return true;
    }

    // 获取用户信息
    private void getLoginUserInfo(final boolean bRegister, final boolean isNew) {
        new RetrofitRequest<RegStatusInfo>(ApiRequest.getApiShiji().getUsrInfo()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                getUserInfo(msg, bRegister, isNew);
            }
        });
    }

    // 获取验证码
    private void getVerificationCode() {
        if (!NetUtil.IsInNetwork(this)) {
            showCustomToast(Configration.OFF_LINE_TIPS);
            return;
        }
        if (timer != null) {
            showCustomToast("验证码下发需要时间，请耐心等待！");
            return;
        }
        String phone = edt_phone.getText().toString().trim();
        if (!verifyPhone(phone)) {
            return;
        }
        showPreDialog("");

        HashMap<String, String> maps = new HashMap<>();
        maps.put("mobile", edt_phone.getText().toString().trim());
        maps.put("mode", "fast_reg");

        new RetrofitRequest<RegisterInfo>(ApiRequest.getApiShiji().getVerifyCode(maps)).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        // 如果未注册用户，先注册
                        if (msg.code == 200) {
                            HashMap<String, String> maps = new HashMap<>();
                            maps.put("mobile", edt_phone.getText().toString().trim());
                            maps.put("mode", "register");
                            new RetrofitRequest<RegisterInfo>(ApiRequest.getApiShiji().getVerifyCode(maps)).handRequest(
                                    new MsgCallBack() {
                                        @Override
                                        public void onResult(HttpMessage msg) {
                                            handleVerifyCodeMsg(msg);
                                        }
                                    });
                        } else {
                            handleVerifyCodeMsg(msg);
                        }

                    }
                }
        );
    }

    private void handleVerifyCodeMsg(HttpMessage msg) {
        hidePreDialog();
        if (msg.isSuccess()) {
            if (!isShow) {
                return;
            }
            if (msg.isSuccess()) {
                timer = new TimeCounter(60 * 1000, 1000);
                timer.start();
            }
            RegisterInfo info = (RegisterInfo) msg.obj;
            register = info.isRegister();
            if (register) {
                llytTips.setVisibility(View.VISIBLE);
                llytAgreement.setVisibility(View.VISIBLE);
            } else {
                llytTips.setVisibility(View.INVISIBLE);
                llytAgreement.setVisibility(View.INVISIBLE);
            }
        } else {
            showCustomToast(msg.message);
        }
    }

    //验证手机号输入
    private boolean verifyPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            showCustomToast("请输入手机号");
            return false;
        }
        if (!RegexValidateUtil.isMobileNO(phone)) {
            showCustomToast("手机号输入不正确，请检查");
            return false;
        }
        return true;
    }

    //验证验证码输入
    private boolean verifyCode(String code) {
        if (TextUtils.isEmpty(code)) {
            showCustomToast("请输入验证码");
            return false;
        }
        if (code.length() != 6) {
            showCustomToast("请输入6位验证码");
            return false;
        }
        return true;
    }

    @Override
    protected void onPause() {
        isShow = false;
        super.onPause();
    }

    @Override
    protected void onResume() {
        isShow = true;
        super.onResume();
        if (!TextUtils.isEmpty(BaseApplication.WX_CODE)) {
            HashMap<String, String> maps = new HashMap<>();
            maps.put("code", BaseApplication.WX_CODE);
            maps.put("cid", BaseApplication.ClientId);
            new RetrofitRequest<RegStatusInfo>(ApiRequest.getApiShiji().loginWexin(maps)).handRequest(
                    new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            hidePreDialog();
                            if (msg.isSuccess()) {
                                RegStatusInfo info = (RegStatusInfo) msg.obj;
                                if (info.getReg_status() == 3) {
                                    // 跳入品牌选择界面
                                    if(info.getNew_user() == 1){
                                        getLoginUserInfo(false, true);
                                    }else {
                                        getLoginUserInfo(false, false);
                                    }
                                } else {
                                    // 进入主页界面
                                    if(info.getNew_user() == 1){
                                        getLoginUserInfo(true, true);
                                    }else {
                                        getLoginUserInfo(true, false);
                                    }
                                }
                            }
                        }
                    }
            );
        } else {
            hidePreDialog();
        }
    }

    /**
     * 倒计时计时器
     *
     * @author dell
     */
    class TimeCounter extends CountDownTimer {
        public TimeCounter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            btn_code.setText("发送验证码");
            timer = null;
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            if (isShow) {
                btn_code.setText(millisUntilFinished / 1000 + "s");
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (timer != null) {
            timer.cancel();
        }
        this.getContentResolver().unregisterContentObserver(content);
        super.onDestroy();
    }

    class MyHandler extends Handler {
        public MyHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            hidePreDialog();
            switch (msg.what) {
                case 1:            //show tips
                    Toast.makeText(LoginActivity.this, msg.obj + "", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }

    //获取用户信息完成登录
    public void getUserInfo(HttpMessage msg, boolean bRegister, boolean isNew) {
        hidePreDialog();
        DebugUtil.v("获取用户" + msg.code);
        if (msg.isSuccess()) {
//			mApp = getCustomerApplication();
            BaseApplication.getInstance().saveSession();
            BaseApplication.getInstance().saveCookie();
            User info = (User) msg.obj;
            info.setPhone(phone);
            BaseApplication.getInstance().saveUserId(info.getUser_id() + "");
            BaseApplication.getInstance().saveUserName(info.getName());
            BaseApplication.getInstance().saveUserPhone(info.getPhone());
            BaseApplication.getInstance().saveUserMID(info.getM_id());
            BaseApplication.getInstance().saveHaveShop(info.isHave_shop());

            String cid = BaseApplication.getInstance().readUserCID();
            if (!TextUtils.isEmpty(cid)) {
                new RetrofitRequest<>(ApiRequest.getApiShiji().uploadCID(cid)).handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            Log.e("个推", "Got ClientId:" + "成功上传cid");
                        }
                    }
                });
            }
            // 绑定devicetoken和userid
//            HashMap<String, String> maps = new HashMap<>();
//            maps.put("user_id", String.valueOf(info.getUser_id()));
//            maps.put("device_token", Util.getUUID(this));
//            new RetrofitRequest<>(ApiRequest.getApiShiji().postDeviceToken(maps)).handRequest(new MsgCallBack() {
//                @Override
//                public void onResult(HttpMessage msg) {
//                    if(msg.isSuccess()){
////                        showTips("绑定成功");
//                    }
//
//                }
//            });


            BaseApplication.needRefresh = true;
            Intent intent = new Intent();
            intent.putExtra("success", true);
            intent.putExtra("isNew", isNew);
            setResult(RESULT_OK, intent);
            //第一次注册跳转到引导品牌分类, 已经注册过的直接结束, 老用户未订阅过的也跳转引导品牌 分类
            if (!bRegister) {
                Intent intentGuide = new Intent(LoginActivity.this, RegistedGuideActivity.class);
                intentGuide.putExtra("type", 1);
                startActivity(intentGuide);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else {
                this.finish();
                this.overridePendingTransition(R.anim.slide_in_bottom_top, R.anim.slide_out_buttom_top);
            }
        } else {
            showCustomToast("同步用户信息失败，请重新登陆！");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (bFinish) {
            setResult(RESULT_CANCELED);
            finish();
            this.overridePendingTransition(R.anim.slide_in_bottom_top, R.anim.slide_out_buttom_top);
        } else {
            setResult(RESULT_CANCELED);
            finish();
            this.overridePendingTransition(R.anim.slide_in_bottom_top, R.anim.slide_out_buttom_top);
        }
    }

    class SmsContent extends ContentObserver {
        private Cursor cursor = null;

        public SmsContent(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            try {
                Uri uri = Uri.parse("content://sms/inbox");
                String[] projection = new String[]{"_id", "address", "read", "body"};
                String sortOrder = "date desc";
                cursor = getContentResolver().query(uri, projection, null, null, sortOrder);

                if (cursor != null && cursor.getCount() > 0) {
                    ContentValues values = new ContentValues();
                    values.put("read", "1");        //修改短信为已读模式
                    cursor.moveToNext();
                    int smsbodyColumn = cursor.getColumnIndex("body");
                    String smsBody = cursor.getString(smsbodyColumn);
                    String codeStr = Util.getDynamicPassword(smsBody);
                    if (!TextUtils.isEmpty(codeStr)) {
                        edt_code.setText(codeStr);
                    }
                }

                if (Build.VERSION.SDK_INT < 14) {
                    cursor.close();
                }
            } catch (Exception e) {

            }
        }
    }

    public void initShareSdk(Context context) {
        //初始化SHARESDK SMS
        GeTui.getCID(context);
    }

    public void showPreDialog(String str) {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        progressDialog = ProgressDialog.creatRequestDialog(this, str, false);
        progressDialog.show();
    }

    public void hidePreDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public void showTips(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
