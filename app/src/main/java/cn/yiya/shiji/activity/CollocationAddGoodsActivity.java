package cn.yiya.shiji.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.CollocationGoodsListAdapter;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.entity.goodes.NewGoodsItem;
import cn.yiya.shiji.utils.BitmapTool;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.views.FullyLinearLayoutManager;

/**
 * Created by Tom on 2016/7/19.
 */
public class CollocationAddGoodsActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private TextView tvRight;

    private RelativeLayout rlytExpand;
    private ImageView ivExpand;

    private ImageView ivCollocation;

    private LinearLayout llytAddYips;
    private RecyclerView rycvGoods;
    private CollocationGoodsListAdapter collocationGoodsListAdapter;
    private ArrayList<NewGoodsItem> mList = new ArrayList<>();

    private TextView tvBack;
    private TextView tvAddGoods;
    private TextView tvNext;

    private final static int REQUEST_ADD = 1111;
    private final static int REQUEST_ORDER = 2222;
    private final static int REQUEST_PREVIEW = 3333;

    private String mPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collocation_add_goods);
        initIntent();
        initViews();
        initEvents();
        init();
    }

    private void initIntent() {
        mPath = getIntent().getStringExtra("path");
    }

    @Override
    protected void initViews() {
        findViewById(R.id.title_back).setVisibility(View.GONE);
        ((TextView)findViewById(R.id.title_txt)).setText("添加商品");
        tvRight = (TextView) findViewById(R.id.title_right);
        tvRight.setText("排序");
        tvRight.setVisibility(View.INVISIBLE);

        rlytExpand = (RelativeLayout) findViewById(R.id.rlyt_expand);
        ivExpand = (ImageView) findViewById(R.id.iv_expand);

        ivCollocation = (ImageView) findViewById(R.id.iv_collocation);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.width = SimpleUtils.getScreenWidth(this) - SimpleUtils.dp2px(this, 136);
        layoutParams.height = layoutParams.width * 360/239;
        layoutParams.topMargin = SimpleUtils.dp2px(this, 16);
        layoutParams.bottomMargin = SimpleUtils.dp2px(this, 22);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        ivCollocation.setLayoutParams(layoutParams);
//        ivCollocation.setImageBitmap(BaseApplication.getInstance().collocation);
        BitmapTool.disaplayImage("file://" + mPath, ivCollocation, null);

        llytAddYips = (LinearLayout) findViewById(R.id.llyt_add_tips);
        rycvGoods = (RecyclerView) findViewById(R.id.rycv_goods);
        rycvGoods.setItemAnimator(new DefaultItemAnimator());
        rycvGoods.setLayoutManager(new FullyLinearLayoutManager(this));
        collocationGoodsListAdapter = new CollocationGoodsListAdapter(this, false, false);
        rycvGoods.setAdapter(collocationGoodsListAdapter);
        llytAddYips.setVisibility(View.VISIBLE);
        rycvGoods.setVisibility(View.GONE);

        tvBack = (TextView) findViewById(R.id.tv_back);
        tvAddGoods = (TextView) findViewById(R.id.tv_add_goods);
        tvNext = (TextView) findViewById(R.id.tv_next);
        tvNext.setClickable(false);
    }

    @Override
    protected void initEvents() {
        tvRight.setOnClickListener(this);
        rlytExpand.setOnClickListener(this);
        tvBack.setOnClickListener(this);
        tvNext.setOnClickListener(this);
        tvAddGoods.setOnClickListener(this);
        collocationGoodsListAdapter.setOnDeleteListener(new CollocationGoodsListAdapter.OnDeleteListener() {
            @Override
            public void onDelete(int position) {
                mList.remove(position);
                collocationGoodsListAdapter.setmList(mList);
                collocationGoodsListAdapter.notifyDataSetChanged();
                upDateView();
            }
        });
    }

    @Override
    protected void init() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_right:
                Intent intentOrder = new Intent(this, CollocationGoodsOrderActivity.class);
                intentOrder.putExtra("path",mPath);
                intentOrder.putExtra("list", new Gson().toJson(mList));
                startActivityForResult(intentOrder, REQUEST_ORDER);
                break;
            case R.id.rlyt_expand:
                if(ivCollocation.getVisibility() == View.GONE){
                    ivCollocation.setVisibility(View.VISIBLE);
                    ivExpand.setImageResource(R.mipmap.shousuo);
                }else {
                    ivCollocation.setVisibility(View.GONE);
                    ivExpand.setImageResource(R.mipmap.xiala);
                }
                break;
            case R.id.tv_back:
                onBackPressed();
                break;
            case R.id.tv_next:
                if(mList == null || mList.size() == 0){
                    return;
                }
                Intent intentPreview = new Intent(this, CollocationPreviewActivity.class);
                intentPreview.putExtra("path",mPath);
                intentPreview.putExtra("list", new Gson().toJson(mList));
                startActivityForResult(intentPreview, REQUEST_PREVIEW);
                break;
            case R.id.tv_add_goods:
                Intent intentAddGoods = new Intent(this, CollocationSelectedGoodsActivity.class);
                startActivityForResult(intentAddGoods, REQUEST_ADD);
                overridePendingTransition(R.anim.slide_in_bottom_top, R.anim.slide_in_out_fixed);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_ORDER){
                if(data != null){
                    Gson gson = new Gson();
                    JsonParser parser = new JsonParser();
                    JsonArray Jarray = parser.parse(data.getStringExtra("list")).getAsJsonArray();

                    if(mList != null){
                        mList.clear();
                    }
                    for(JsonElement obj : Jarray ){
                        NewGoodsItem cse = gson.fromJson( obj , NewGoodsItem.class);
                        mList.add(cse);
                    }
                    collocationGoodsListAdapter.setmList(mList);
                    collocationGoodsListAdapter.notifyDataSetChanged();
                }
            }else if(requestCode == REQUEST_ADD){
                if(data != null){
                    NewGoodsItem item = new Gson().fromJson(data.getStringExtra("info"), NewGoodsItem.class);
                    mList.add(item);
                    collocationGoodsListAdapter.setmList(mList);
                    collocationGoodsListAdapter.notifyDataSetChanged();
                    upDateView();
                }
            }else if(requestCode == REQUEST_PREVIEW){
                if(BaseApplication.getInstance().bCollocationSuccess){
                    setResult(RESULT_OK);
                    finish();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    // 根据已添加商品数量，判断下一步按钮和添加按钮、排序按钮是否可点击可显示
    private void upDateView(){
        if(mList!= null && mList.size() > 0){
            llytAddYips.setVisibility(View.GONE);
            rycvGoods.setVisibility(View.VISIBLE);
            if(mList.size() > 1) {
                tvRight.setVisibility(View.VISIBLE);
            }else {
                tvRight.setVisibility(View.INVISIBLE);
            }
            tvNext.setClickable(true);
            tvNext.setTextColor(Color.parseColor("#212121"));
            if(mList.size() == 6){
                tvAddGoods.setBackgroundColor(Color.parseColor("#bbbbbb"));
                tvAddGoods.setClickable(false);
            }else {
                tvAddGoods.setBackgroundColor(Color.parseColor("#212121"));
                tvAddGoods.setClickable(true);
            }
        }else {
            llytAddYips.setVisibility(View.VISIBLE);
            rycvGoods.setVisibility(View.GONE);
            tvRight.setVisibility(View.INVISIBLE);
            tvNext.setClickable(false);
            tvNext.setTextColor(Color.parseColor("#c7c7c7"));
            tvAddGoods.setClickable(true);
            tvAddGoods.setBackgroundColor(Color.parseColor("#212121"));
        }
    }
}
