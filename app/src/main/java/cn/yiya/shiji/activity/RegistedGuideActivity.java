package cn.yiya.shiji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.ExRcvAdapterWrapper;
import cn.yiya.shiji.adapter.GuideItemAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.GuideItem;
import cn.yiya.shiji.entity.GuideItemObject;
import cn.yiya.shiji.entity.HotBrandsItem;
import cn.yiya.shiji.entity.HotBrandsObject;
import cn.yiya.shiji.entity.HotClassItem;
import cn.yiya.shiji.entity.HotClassObject;
import cn.yiya.shiji.views.GuideDividerItemDecoration;

/**
 * 注册后的引导界面
 * created by tomyang on 2015/12/21
 */

public class RegistedGuideActivity extends BaseAppCompatActivity implements View.OnClickListener{
    private LinearLayout guideListFooter;
    private TextView footer;
    private LinearLayout llyTips;
    private TextView tvNumberTop;
    private TextView tvTips;
    private ImageView ivBack;
    private TextView tvTitle;
    private TextView tvStepOver;
    private RecyclerView mRecyclerView;
    private ExRcvAdapterWrapper mExRcvAdapterWrapper;
    private GridLayoutManager gridLayoutManager;
    private RelativeLayout llyNext;
    private TextView tvNext;
    private TextView tvNumberBottom;
    private GuideItemAdapter mAdapter;
    private ArrayList<GuideItem> mList = new ArrayList<>();
    private int type;                          // 1为选择品牌 2为选择分类
    private String category_list;
    private String tag_list;
    private int selectedNumber = 0;
    private ArrayList<GuideItem> selectedList = new ArrayList<>();
    private ArrayList<GuideItem> brandDataList = new ArrayList<>();
    private ArrayList<GuideItem> categroyDataList = new ArrayList<>();
    private ArrayList<GuideItem> brandSelectedList = new ArrayList<>();
    private ArrayList<GuideItem> categroySelectedList = new ArrayList<>();
    private String jsonBrand;
    private String jsonCategroy;
    private int brandSelectedNumber;
    private int categroySelectedNumber;

    private Animation animShow;
    private Animation animHide;
    private int nMove;
    private RelativeLayout rltyGroup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registed_guide);

        type = getIntent().getIntExtra("type", 0);
        nMove = getIntent().getIntExtra("nMove", 0);
        if(type == 2){
            tag_list = getIntent().getStringExtra("tag_list");
        }
        jsonBrand = getIntent().getStringExtra("jsonBrand");
        jsonCategroy = getIntent().getStringExtra("jsonCategroy");
        brandSelectedNumber = getIntent().getIntExtra("brandSelectedNumber", 0);
        categroySelectedNumber = getIntent().getIntExtra("categroySelectedNumber",0);
        if(type == 1){
            selectedNumber = brandSelectedNumber;
        }else {
            selectedNumber = categroySelectedNumber;
        }

        initViews();
        initEvents();
        init();
    }

    @Override
    protected void initViews() {
        guideListFooter = (LinearLayout) getLayoutInflater().inflate(R.layout.guide_list_footer, null);
        footer = (TextView)guideListFooter.findViewById(R.id.guide_footer);
        rltyGroup = (RelativeLayout) findViewById(R.id.layout_group);
        llyTips = (LinearLayout) findViewById(R.id.lly_tips);
        tvNumberTop = (TextView) findViewById(R.id.guide_selected_number_top);
        tvTips = (TextView) findViewById(R.id.guide_tips);
        ivBack = (ImageView) findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_text);
        tvStepOver = (TextView) findViewById(R.id.title_right);
        llyNext = (RelativeLayout) findViewById(R.id.lly_guid_next);
        llyNext.setVisibility(View.GONE);
        tvNext = (TextView) findViewById(R.id.guide_next);
        tvNumberBottom = (TextView) findViewById(R.id.guide_selected_number_bottom);
        mRecyclerView = (RecyclerView) findViewById(R.id.guid_list);
        gridLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new GuideDividerItemDecoration(this, R.drawable.guide_divider_decoration));
        mAdapter = new GuideItemAdapter(this,type,selectedNumber,false);
        mExRcvAdapterWrapper = new ExRcvAdapterWrapper(mAdapter,gridLayoutManager);
        mExRcvAdapterWrapper.setFooterView(guideListFooter);
        mRecyclerView.setAdapter(mExRcvAdapterWrapper);
        footer.setVisibility(View.GONE);
        if(type == 1){
            ivBack.setVisibility(View.GONE);
            tvTitle.setText("1/2");
            tvNumberTop.setText("添加您喜欢的品牌");
        }else {
            ivBack.setVisibility(View.VISIBLE);
            tvTitle.setText("2/2");
            tvNumberTop.setText("添加您喜欢的分类");
        }
    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        tvStepOver.setOnClickListener(this);
        llyNext.setOnClickListener(this);
        mAdapter.setOnItemClickListener(new GuideItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int snb) {
                selectedNumber = snb;
                upDateView(selectedNumber);
            }

            @Override
            public void onItemClick(int snb, int position) {

            }
        });
    }

    @Override
    protected void init() {
        getData();
        initAnim();
        upDateView(selectedNumber);
    }

    private Handler mHandler = new Handler(){
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case 0:
                llyTips.setVisibility(View.VISIBLE);
                break;
            }
        }
    };

    private void upDateView(int selectedNumber){
        if(nMove == 0){
            nMove = llyTips.getHeight();
        }
        if (selectedNumber > 0 && selectedNumber < 5) {
            if(llyNext.getVisibility() == View.VISIBLE){
                llyNext.clearAnimation();
                llyNext.startAnimation(animHide);
                llyNext.setVisibility(View.GONE);
            }
            if(llyTips.getVisibility() == View.INVISIBLE){
                ViewPropertyAnimator.animate(rltyGroup).translationY(0).setDuration(300).start();
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    public void run() {
                        mHandler.sendEmptyMessage(0);
                    }
                }, 300);
            }
//            footer.setVisibility(View.GONE);
            if (type == 1) {
                tvNumberTop.setText("已经添加" + selectedNumber + "个品牌");
                tvTips.setText("至少需要添加5个以上您喜欢的品牌");
            } else {
                tvNumberTop.setText("已经添加" + selectedNumber + "个分类");
                tvTips.setText("至少需要添加5个以上您喜欢的分类");
            }
        } else if (selectedNumber >= 5) {
            if(llyNext.getVisibility() == View.GONE){
                llyNext.setVisibility(View.VISIBLE);
                llyNext.clearAnimation();
                llyNext.startAnimation(animShow);
            }
            if(llyTips.getVisibility() == View.VISIBLE){
                ViewPropertyAnimator.animate(rltyGroup).translationY(-nMove).setDuration(300).start();
                llyTips.setVisibility(View.INVISIBLE);
            }
//            footer.setVisibility(View.VISIBLE);
            tvNumberBottom.setText("(已经选定" + selectedNumber + "个)");
            if (type == 1) {
                tvNext.setText("下一步");
            } else {
                tvNext.setText("完成");
            }
        } else {
            llyTips.setVisibility(View.VISIBLE);
            llyNext.setVisibility(View.GONE);
//            footer.setVisibility(View.GONE);
            tvTips.setText("我们会根据您的选择定制个性化推送");
            if (type == 1) {
                tvNumberTop.setText("添加您喜欢的品牌");
            } else {
                tvNumberTop.setText("添加您喜欢的分类");
            }
        }
    }

    private void initAnim() {
        animShow = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1,
                Animation.RELATIVE_TO_SELF, 0);
        animShow.setDuration(300);

        animHide = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1);
        animHide.setDuration(300);
    }

    private void initData() {

    }

    private void getData() {
        if(type == 1){
            if(jsonBrand != null){
                brandDataList = new Gson().fromJson(jsonBrand, GuideItemObject.class).getData();
                mAdapter.setmList(brandDataList);
                mExRcvAdapterWrapper.notifyDataSetChanged();
            }else {
                getBrandData();
            }
        }else {
            if (jsonCategroy != null){
                categroyDataList = new Gson().fromJson(jsonCategroy, GuideItemObject.class).getData();
                mAdapter.setmList(categroyDataList);
                mExRcvAdapterWrapper.notifyDataSetChanged();
            }else {
                getCategoryData();
            }
        }
    }

    private void getCategoryData() {
        showPreDialog("请稍后");
        new RetrofitRequest<HotClassObject>(ApiRequest.getApiShiji().getCategoriesRegister()).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            hidePreDialog();
                            HotClassObject object = (HotClassObject) msg.obj;
                            ArrayList<HotClassItem> list = object.list;          // 获取注册页面热门品类
                            if (list != null && list.size() > 0) {
                                for (int i = 0; i < list.size(); i++) {
                                    GuideItem item = new GuideItem();
                                    item.setId(list.get(i).getId());
                                    item.setLogo(list.get(i).getIcon());
                                    item.setName(list.get(i).getName());
                                    categroyDataList.add(item);
                                }
                                mAdapter.setmList(categroyDataList);
                                mExRcvAdapterWrapper.notifyDataSetChanged();
                            }
                        } else {
                            hidePreDialog();
                        }
                    }
                }
        );
    }

    private void getBrandData() {
        showPreDialog("请稍后");
        new RetrofitRequest<HotBrandsObject>(ApiRequest.getApiShiji().getBrandsRegister()).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            hidePreDialog();
                            HotBrandsObject object = (HotBrandsObject) msg.obj;
                            ArrayList<HotBrandsItem> list = object.list;        // 注册页面热门品牌数据
                            if (list != null && list.size() > 0) {
                                for (int i = 0; i < list.size(); i++) {
                                    GuideItem item = new GuideItem();
                                    item.setTag_id(list.get(i).getTag_id() + "");
                                    item.setLogo(list.get(i).getLogo());
                                    item.setName(list.get(i).getName());
                                    brandDataList.add(item);
                                }
                                mAdapter.setmList(brandDataList);
                                mExRcvAdapterWrapper.notifyDataSetChanged();
                            }
                        } else {
                            hidePreDialog();
                        }
                    }
                }
        );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.title_right:
                postRegisterFolloeSkip();
                break;
            case R.id.lly_guid_next:
                if(type == 1) {
                    GuideItemObject objectBrand = new GuideItemObject();
                    objectBrand.setData(brandDataList);
                    jsonBrand = new Gson().toJson(objectBrand);
                    Intent intent1 = new Intent(RegistedGuideActivity.this, RegistedGuideActivity.class);
                    intent1.putExtra("type", 2);
                    intent1.putExtra("nMove", nMove);
                    intent1.putExtra("tag_list", tag_list);
                    intent1.putExtra("jsonBrand",jsonBrand);
                    intent1.putExtra("jsonCategroy",jsonCategroy);
                    intent1.putExtra("brandSelectedNumber",selectedNumber);
                    intent1.putExtra("categroySelectedNumber",categroySelectedNumber);
                    startActivity(intent1);
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }else {
                    postFolloeBrandsCategorise();
                    setResult(RESULT_OK);
                    this.finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                break;
        }
    }

    // 批量关注品牌品类
    public void postFolloeBrandsCategorise(){
        brandDataList = new Gson().fromJson(jsonBrand,GuideItemObject.class).getData();
        for(int i = 0; i < brandDataList.size(); i++){
            if(brandDataList.get(i).isSelected()){
                if(!TextUtils.isEmpty(brandDataList.get(i).getTag_id())){
                    brandSelectedList.add(brandDataList.get(i));
                }
            }
        }
        for(int i = 0; i < categroyDataList.size(); i++){
            if(categroyDataList.get(i).isSelected()){
                if(!TextUtils.isEmpty(categroyDataList.get(i).getId())) {
                    categroySelectedList.add(categroyDataList.get(i));
                }
            }
        }
        for (int i = 0; i<brandSelectedList.size(); i++){
            if(i == 0){
                tag_list = brandSelectedList.get(i).getTag_id();
            }else {
                tag_list = tag_list + "," + brandSelectedList.get(i).getTag_id();
            }
        }
        for (int i = 0; i<categroySelectedList.size(); i++){
            if(i == 0){
                category_list = categroySelectedList.get(i).getId();
            }else {
                category_list = category_list + "," + categroySelectedList.get(i).getId();
            }
        }

        HashMap<String, String> maps = new HashMap<>();
        maps.put("tag_list", tag_list);
        maps.put("category_list", category_list);
        new RetrofitRequest<>(ApiRequest.getApiShiji().postFolloeBrandsCategorise(maps)).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if(msg.isSuccess()){
                            showTips("已成功添加，我们会根据您的选择定制个性化推送");
                        }else {
                            showTips(msg.message);
                        }
                    }
                }
        );
    }

    // 跳过注册关注
    public void postRegisterFolloeSkip(){
        new RetrofitRequest<>(ApiRequest.getApiShiji().postRegisterFolloeSkip()).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            setResult(RESULT_OK);
                            finish();
                            overridePendingTransition(R.anim.slide_in_bottom_top, R.anim.slide_out_buttom_top);
                        } else {
                            showTips(msg.message);
                        }
                    }
                }
        );
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        if(type == 1){
            postRegisterFolloeSkip();
        }else {
            GuideItemObject object = new GuideItemObject();
            object.setData(categroyDataList);
            jsonCategroy = new Gson().toJson(object);
            Intent intentBack = new Intent(RegistedGuideActivity.this, RegistedGuideActivity.class);
            intentBack.putExtra("type", 1);
            intentBack.putExtra("nMove", nMove);
            intentBack.putExtra("jsonBrand",jsonBrand);
            intentBack.putExtra("jsonCategroy",jsonCategroy);
            intentBack.putExtra("brandSelectedNumber",brandSelectedNumber);
            intentBack.putExtra("categroySelectedNumber",selectedNumber);
            startActivity(intentBack);
            finish();
        }
    }
}
