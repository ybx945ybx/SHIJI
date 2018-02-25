package cn.yiya.shiji.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.ImageTag;
import cn.yiya.shiji.entity.PublishedGoodsInfo;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.ProgressDialog;
import cn.yiya.shiji.views.TagAbleImageView;

/**
 * Created by dell on 2015-07-13.
 */
public class SearchTagActivity extends BaseAppCompatActivity implements View.OnClickListener, TextWatcher {
    private Handler mHandler;
    private Runnable myRunnable;
    private AutoCompleteTextView etInput;
    private ImageView ivCancel;
    private ImageView ivAddAuto;
    private TextView tvBrand;
    private TextView tvGoodsName;
    private TextView tvAdress;
    private TextView tvDetailArea;
    private TextView tvMoneyType;
    private TextView tvPrice;
    private TextView tvAdd;
    private LinearLayout llytRoot;
    private LinearLayout llytAuto;
    private boolean bAutoShow;
    private List<TextView> mTextViewList;
    private String[] strDefault = new String[]{"添加品牌", "添加商品名称", "购买地点", "具体位置", "金额"};
    private String[] countryDefault = new String[]{"美国", "加拿大", "香港", "韩国", "日本", "英国", "意大利", "法国", "奥地利", "西班牙", "德国", "丹麦", "澳大利亚", "新西兰"};
    private int nPosition = 0;
    private ArrayList<ImageTag> tagList;
    private ArrayList<TagAbleImageView.TagInfo> tagLists;
    private ArrayList<TagAbleImageView.TagInfo> tagEditLists;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayAdapter<String> countryAdapter;
    private String[] arr;
    private static final int AUTO_TAG_CODE = 300;
    private PublishedGoodsInfo tagInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tag);
        initIntent();
        initViews();
        initEvents();
        init();
    }

    private void initIntent() {
        mHandler = new Handler(getMainLooper());
        String tag = getIntent().getStringExtra("TAG");
        if (!TextUtils.isEmpty(tag)) {
            Type type = new TypeToken<ArrayList<TagAbleImageView.TagInfo>>(){}.getType();
            tagEditLists = new Gson().fromJson(tag, type);
            bAutoShow = false;
        } else {
            bAutoShow = true;
        }

        if (tagList == null || tagList.size() == 0) {
            if(myRunnable!=null){
                mHandler.removeCallbacks(myRunnable);
            }
            myRunnable  = new MyRunnable();
            mHandler.post(myRunnable);
        }
    }

    @Override
    protected void initViews() {
        etInput = (AutoCompleteTextView) findViewById(R.id.title_txt);

        ivCancel = (ImageView) findViewById(R.id.search_cancel);
        ivAddAuto = (ImageView) findViewById(R.id.iv_add_auto);
        tvBrand = (TextView) findViewById(R.id.tag_brand);
        tvGoodsName = (TextView) findViewById(R.id.tag_brand_name);
        tvAdress = (TextView) findViewById(R.id.tag_adress);
        tvDetailArea = (TextView) findViewById(R.id.tag_adress_detail);
        tvMoneyType = (TextView) findViewById(R.id.money_type);
        tvPrice = (TextView) findViewById(R.id.money_type_count);
        tvAdd = (TextView) findViewById(R.id.tag_add);
        llytRoot = (LinearLayout) findViewById(R.id.tag_root_layout);
        llytAuto = (LinearLayout) findViewById(R.id.lly_add_auto);
        llytAuto.setVisibility(bAutoShow ? View.VISIBLE : View.GONE);
        countryAdapter = new ArrayAdapter<>(SearchTagActivity.this, android.R.layout.simple_list_item_1, countryDefault);
    }

    @Override
    protected void initEvents() {
        ivCancel.setOnClickListener(this);
        ivAddAuto.setOnClickListener(this);
        tvBrand.setOnClickListener(this);
        tvGoodsName.setOnClickListener(this);
        tvAdress.setOnClickListener(this);
        tvDetailArea.setOnClickListener(this);
        tvMoneyType.setOnClickListener(this);
        tvPrice.setOnClickListener(this);
        tvAdd.setOnClickListener(this);
        etInput.addTextChangedListener(this);
    }

    @Override
    protected void init() {
        controlKeyboardLayout(llytRoot, tvMoneyType);

        mTextViewList = new ArrayList<>();
        mTextViewList.add(tvBrand);
        mTextViewList.add(tvGoodsName);
        mTextViewList.add(tvAdress);
        mTextViewList.add(tvDetailArea);
        mTextViewList.add(tvPrice);

        if (tagEditLists != null) {
            tvBrand.setText(tagEditLists.get(0).getContent());
            tvGoodsName.setText(tagEditLists.get(0).getDetails());

            tvAdress.setText(tagEditLists.get(1).getContent());
            tvDetailArea.setText(tagEditLists.get(1).getDetails());

            if (!TextUtils.isEmpty(tagEditLists.get(2).getContent()))
                tvMoneyType.setText(tagEditLists.get(2).getContent());

            tvPrice.setText(tagEditLists.get(2).getDetails());
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tag_brand) {
            etInput.setAdapter(arrayAdapter);
        }else if(v.getId() == R.id.tag_adress){
            etInput.setAdapter(countryAdapter);
        } else {
            etInput.setAdapter(null);
        }

        if (v.getId() == R.id.money_type_count) {
            setNumInput(true);
        } else {
            setNumInput(false);
        }

        switch (v.getId()){
            case R.id.tag_brand:
                setClickEdit(0);
                break;
            case R.id.tag_brand_name:
                setClickEdit(1);
                break;
            case R.id.tag_adress:
                setClickEdit(2);
                break;
            case R.id.tag_adress_detail:
                setClickEdit(3);
                break;
            case R.id.money_type:
                showSelectDialog();
                break;
            case R.id.money_type_count:
                setClickEdit(4);
                break;
            case R.id.tag_add:
                doSumbit();
                break;
            case R.id.search_cancel:
                onBackPressed();
                break;
            case R.id.iv_add_auto:
                Intent intent = new Intent(this, ChoosePublishedGoodsActivity.class);
                startActivityForResult(intent, AUTO_TAG_CODE);
                break;
        }
    }

    private void doSumbit() {
        if (!TextUtils.isEmpty(etInput.getText().toString())) {
            mTextViewList.get(nPosition).setText(etInput.getText().toString().trim());
        }

        int count = 0;
        for (int i = 0; i < mTextViewList.size(); i++) {
            if (mTextViewList.get(i).getText().toString().equals(strDefault[i])){
                count++;
            }
        }
        if (count == strDefault.length) {
            Toast.makeText(this, "都进来了，加一个标签吧^_^", Toast.LENGTH_SHORT).show();
        }else {
            tagLists = new ArrayList<>();
            TagAbleImageView.TagInfo info1 = new TagAbleImageView.TagInfo();
            info1.setContent(getEditText(0));
            info1.setDetails(getEditText(1));
            info1.setType(1);
            if (tagEditLists != null) {
                info1.setX(tagEditLists.get(0).getX());
                info1.setY(tagEditLists.get(0).getY());
            }
            tagLists.add(info1);

            TagAbleImageView.TagInfo info2 = new TagAbleImageView.TagInfo();
            info2.setContent(getEditText(2));
            info2.setDetails(getEditText(3));
            info2.setType(3);
            if (tagEditLists != null) {
                info2.setX(tagEditLists.get(0).getX());
                info2.setY(tagEditLists.get(0).getY());
            }
            tagLists.add(info2);

            TagAbleImageView.TagInfo info3 = new TagAbleImageView.TagInfo();
            if (tvMoneyType.getText().toString().equals("币种")){
                info3.setContent("");
            }else {
                info3.setContent(tvMoneyType.getText().toString());
            }
            if (!TextUtils.isEmpty(getEditText(4))) {
                float money = Float.parseFloat(getEditText(4));
                info3.setDetails(Util.FloatKeepTwo(money));
            } else {
                info3.setDetails("");
            }

            info3.setType(2);
            if (tagEditLists != null) {
                info3.setX(tagEditLists.get(0).getX());
                info3.setY(tagEditLists.get(0).getY());
            }
            tagLists.add(info3);

            Intent intent =  new Intent();
            Type type = new TypeToken<ArrayList<TagAbleImageView.TagInfo>>(){}.getType();
            String tag = new Gson().toJson(tagLists, type);
            intent.putExtra("TAG", tag);
            intent.putExtra("AUTO", false);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void setNumInput(boolean bTrue) {
        if (bTrue){
            etInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        }else {
            etInput.setInputType(InputType.TYPE_CLASS_TEXT);
        }
    }

    private String getEditText(int nIndex) {
        String str = "";
        if (!mTextViewList.get(nIndex).getText().toString().equals(strDefault[nIndex])) {
            str = mTextViewList.get(nIndex).getText().toString().replace("#","").trim();
        }
        return str;
    }

    private void setClickEdit(int nIndex){
        // 判断当前输入框是否为空，如果为空，则上一个点击TextView则显示默认文字，否则将输入框的文字显示到上一个点击TextView
        if (!TextUtils.isEmpty(etInput.getText().toString().trim())) {
            mTextViewList.get(nPosition).setText(etInput.getText());
        }

        // 判断当前点击的TextView是不是与默认文字相等，如果相等，则把hint的默认文字显示到输入框，反之，将TextView的文字显示到输入框编辑
        if (mTextViewList.get(nIndex).getText().toString().equals(strDefault[nIndex])) {
            etInput.setText("");
            etInput.setHint(strDefault[nIndex]);
        }else {
            etInput.setText(mTextViewList.get(nIndex).getText());
            etInput.setSelection(mTextViewList.get(nIndex).getText().toString().length());
        }

        for (int i = 0; i < mTextViewList.size(); i++) {
            if (i == nIndex) {
                mTextViewList.get(i).setBackgroundResource(R.drawable.tag_view_text_bg);
                mTextViewList.get(i).setTextColor(Color.parseColor("#999999"));
            } else {
                mTextViewList.get(i).setBackgroundResource(R.drawable.tag_view_text_bg_normal);
                mTextViewList.get(i).setTextColor(Color.parseColor("#b9b8b8"));
            }
        }
        nPosition = nIndex;
        showInput(this, etInput);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
//        if (s.length() > 0)
//            mTextViewList.get(nPosition).setText(s);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void showSelectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择币种");
        final String[] mType = {"人民币", "港币", "美元", "日元", "欧元", "英镑", "澳元", "加拿大元"
                                , "新西兰元", "台币", "韩元", "泰铢", "澳门元"};
        builder.setItems(mType, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvMoneyType.setText(mType[which]);
            }
        });
        builder.show();
    }

    public class MyRunnable implements Runnable {
        @Override
        public void run() {
            new RetrofitRequest<ArrayList<ImageTag>>(ApiRequest.getApiShiji().getImageTagsByType(String.valueOf(1)))
                    .handRequest(new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            tagList = (ArrayList<ImageTag>) msg.obj;
                            //tom  修改听云bug
                            if (tagList == null) {
                                return;
                            }

                            arr = new String[tagList.size()];
                            for (int i = 0; i < tagList.size(); i++) {
                                arr[i] = tagList.get(i).content;
                            }
                            arrayAdapter = new ArrayAdapter<>(SearchTagActivity.this, android.R.layout.simple_list_item_1, arr);
                            if (nPosition == 0)
                                etInput.setAdapter(arrayAdapter);
                        }
                    });
        }
    }

    private void doAutoSubmit() {
        tagLists = new ArrayList<>();
        TagAbleImageView.TagInfo info1 = new TagAbleImageView.TagInfo();
        info1.setContent(tagInfo.getBrand());
        info1.setDetails(tagInfo.getTitle());
        info1.setType(1);
        tagLists.add(info1);

        TagAbleImageView.TagInfo info2 = new TagAbleImageView.TagInfo();
        info2.setContent("柿集");
        info2.setDetails(tagInfo.getSite());
        info2.setType(3);
        tagLists.add(info2);

        TagAbleImageView.TagInfo info3 = new TagAbleImageView.TagInfo();
        info3.setContent("人民币");
        info3.setDetails(tagInfo.getPrice());
        info3.setType(2);
        tagLists.add(info3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AUTO_TAG_CODE:
                    if (data != null) {
                        String result = data.getStringExtra("json");
                        tagInfo = new Gson().fromJson(result, PublishedGoodsInfo.class);
                        doAutoSubmit();
                        Intent intent = new Intent();
                        Type type = new TypeToken<ArrayList<TagAbleImageView.TagInfo>>(){}.getType();
                        String tag = new Gson().toJson(tagLists, type);
                        intent.putExtra("TAG", tag);
                        intent.putExtra("AUTO", true);
                        intent.putExtra("goodsId", tagInfo.getGoods_id());
                        intent.putExtra("cover", tagInfo.getCover());
                        intent.putExtra("recommend", tagInfo.getRecommend());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    break;
            }
        }else if(resultCode == RESULT_CANCELED) {
        }
    }

    @Override
    public void onBackPressed() {
        showExitDialog("确定要退出标签添加？");
    }

    public static void showInput(Context context, View view) {
        InputMethodManager manager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.showSoftInput(view, 0);
    }

    /**
     * @param root 最外层布局，需要调整的布局
     * @param scrollToView 被键盘遮挡的scrollToView，滚动root,使scrollToView在root可视区域的底部
     */
    private void controlKeyboardLayout(final View root, final View scrollToView) {
        root.getViewTreeObserver().addOnGlobalLayoutListener( new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //获取root在窗体的可视区域
                root.getWindowVisibleDisplayFrame(rect);
                //获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                //若不可视区域高度大于100，则键盘显示
                if (rootInvisibleHeight > 100) {
                    int[] location = new int[2];
                    //获取scrollToView在窗体的坐标
                    scrollToView.getLocationInWindow(location);
                    //计算root滚动高度，使scrollToView在可见区域
                    int srollHeight = (location[1] + scrollToView.getHeight()) - rect.bottom;
                    root.scrollTo(0, srollHeight);
                } else {
                    //键盘隐藏
                    root.scrollTo(0, 0);
                }
            }
        });
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
}
