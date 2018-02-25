package cn.yiya.shiji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.CollocationGoodsListAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.QiNiuCloud;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.entity.PicDiaryUploadItem;
import cn.yiya.shiji.entity.goodes.NewGoodsItem;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.BitmapTool;
import cn.yiya.shiji.utils.DateTimeFormat;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.views.FullyLinearLayoutManager;

/**
 * 搭配预览
 * Created by Tom on 2016/7/20.
 */
public class CollocationPreviewActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ImageView ivCollocation;
    private ImageView ivOne;
    private ImageView ivTwo;
    private ImageView ivThree;

    private EditText etScribe;

    private RecyclerView rycvGoodsList;
    private CollocationGoodsListAdapter mAdapter;
    private ArrayList<NewGoodsItem> mList = new ArrayList<>();

    private TextView tvBack;
    private TextView tvFinish;

    private String mPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collocation_preview);
        initIntent();
        initViews();
        initEvents();
        init();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if(intent != null){
            mPath = getIntent().getStringExtra("path");

            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonArray Jarray = parser.parse(intent.getStringExtra("list")).getAsJsonArray();

            for(JsonElement obj : Jarray ){
                NewGoodsItem cse = gson.fromJson( obj , NewGoodsItem.class);
                mList.add(cse);
            }
        }
    }

    @Override
    protected void initViews() {
        findViewById(R.id.title_back).setVisibility(View.GONE);
        TextView tvTitle = (TextView)findViewById(R.id.title_txt);
        tvTitle.setText("搭配预览");
        findViewById(R.id.title_right).setVisibility(View.GONE);

        ivCollocation = (ImageView) findViewById(R.id.iv_collocation);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.width = SimpleUtils.getScreenWidth(this) - SimpleUtils.dp2px(this, 128);
        layoutParams.height = SimpleUtils.dp2px(this, 368);
        ivCollocation.setLayoutParams(layoutParams);
        ivCollocation.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        ivCollocation.setImageBitmap(BaseApplication.getInstance().collocation);
        BitmapTool.disaplayImage("file://" + mPath, ivCollocation, null);
        ivOne = (ImageView) findViewById(R.id.iv_one);
        ivTwo = (ImageView) findViewById(R.id.iv_two);
        ivThree = (ImageView) findViewById(R.id.iv_three);

        etScribe = (EditText) findViewById(R.id.et_scribe);

        rycvGoodsList = (RecyclerView) findViewById(R.id.rycv_goods_list);
        rycvGoodsList.setItemAnimator(new DefaultItemAnimator());
        rycvGoodsList.setLayoutManager(new FullyLinearLayoutManager(this));
        mAdapter = new CollocationGoodsListAdapter(this, false, true);
        rycvGoodsList.setAdapter(mAdapter);

        tvBack = (TextView) findViewById(R.id.tv_back);
        tvFinish = (TextView) findViewById(R.id.tv_finish);

    }

    @Override
    protected void initEvents() {
        tvBack.setOnClickListener(this);
        tvFinish.setOnClickListener(this);
    }

    @Override
    protected void init() {
        setImage();
        setList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_back:
                onBackPressed();
                break;
            case R.id.tv_finish:
                doPublish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void setImage() {
        // TODO: 2016/7/21
//        setImageLayout(ivOne, 4);
//        setImageLayout(ivTwo, 4);
//        setImageLayout(ivThree, 0);
        if(mList.size() == 1){
            Netroid.displayImage(mList.get(0).getCover(), ivTwo);
        }else if(mList.size() == 2){
            Netroid.displayImage(mList.get(0).getCover(), ivOne);
            Netroid.displayImage(mList.get(1).getCover(), ivTwo);
        }else {
            Netroid.displayImage(mList.get(0).getCover(), ivOne);
            Netroid.displayImage(mList.get(1).getCover(), ivTwo);
            Netroid.displayImage(mList.get(2).getCover(), ivThree);
        }
    }

    private void setList() {
        mAdapter.setmList(mList);
        mAdapter.notifyDataSetChanged();
    }

    private void setImageLayout(View view, int bottomMargin){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.width = SimpleUtils.getScreenWidth(this) - SimpleUtils.dp2px(this, 232);
        layoutParams.height = layoutParams.width;
        layoutParams.bottomMargin = SimpleUtils.dp2px(this, bottomMargin);
        view.setLayoutParams(layoutParams);
    }

    private void doPublish() {
        PicDiaryUploadItem item = new PicDiaryUploadItem();
        item.date = DateTimeFormat.getCurrentDate();
        item.text = etScribe.getText().toString().trim();
        String key = BaseApplication.getInstance().readUserId() + "_work/diary_" + System.currentTimeMillis();
        item.key = key;
        item.type = 2;
        item.images = new ArrayList<>();
        String url = "http://diary.cdnqiniu02.qnmami.com/" + key;
        PicDiaryUploadItem.ImageInfo image = new PicDiaryUploadItem.ImageInfo();
        image.url = url;
        image.goods_ids = new String[mList.size()];
        for (int i = 0; i < mList.size(); i++){
            image.goods_ids[i] = mList.get(i).getId();
        }
        item.images.add(image);

        showPreDialog("正在发布");
        publishContent(mPath, item, item.key, new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    hidePreDialog();
                    SimpleUtils.deleteCropImg(CollocationPreviewActivity.this, mPath);
                    BaseApplication.getInstance().bCollocationSuccess = true;
                    showTips("发布成功");
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }

    private void publishContent(String collocationData, final PicDiaryUploadItem item, final String key, final MsgCallBack callBack){
        QiNiuCloud.getInstance().upLoadToServer(collocationData, key, new MsgCallBack() {
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
}
