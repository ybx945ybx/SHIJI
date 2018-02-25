package cn.yiya.shiji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.LevelNeedAdapter;
import cn.yiya.shiji.adapter.MyRedLevelAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.User;
import cn.yiya.shiji.entity.UserGrowRuleEntity;
import cn.yiya.shiji.entity.UserGrowValueEntity;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.FullyLinearLayoutManager;
import cn.yiya.shiji.views.RoundedNormalIV;

/**
 * Created by Tom on 2017/1/6.
 */

public class MyRedLevelActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ImageView ivBack;
    private TextView tvTitle;

    private RoundedNormalIV ivHead;
    private TextView tvName;
    private TextView tvLevel;
    private TextView tvGrow;

    private RecyclerView rycvLevel;
    private MyRedLevelAdapter mAdapter;

    private RecyclerView rycvLevelNeed;
    private LevelNeedAdapter mLevelNeedAdapter;
//    private TextView tvLevelNeed;
    private LinearLayout llytNull;
    private View viewBottom;

    private UserGrowRuleEntity entity;
    private List<List<List<String>>> powerList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_red_level);
        initIntent();
        initViews();
        initEvents();
        init();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if(intent != null){
            entity = new Gson().fromJson(intent.getStringExtra("entity"), UserGrowRuleEntity.class);
            powerList = entity.getPower_desc();
            if(powerList != null && powerList.size() > 0) {
                for (int i = 0; i < powerList.size(); i++) {
                    List<String> list = new ArrayList<>();
                    list.add(transferLevelNeed(i));
                    powerList.get(i).add(0, list);
                }
            }

        }
    }

    @Override
    protected void initViews() {
        ivBack = (ImageView) findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText("我的等级");
        findViewById(R.id.title_right).setVisibility(View.GONE);

        ivHead = (RoundedNormalIV) findViewById(R.id.civ_head);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvLevel = (TextView) findViewById(R.id.iv_red_level);
        tvGrow = (TextView) findViewById(R.id.tv_group_point);

        rycvLevel = (RecyclerView) findViewById(R.id.rycv_level);
        rycvLevel.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rycvLevel.setLayoutManager(linearLayoutManager);

        rycvLevelNeed = (RecyclerView) findViewById(R.id.rycv_level_need);
        rycvLevelNeed.setItemAnimator(new DefaultItemAnimator());
        rycvLevelNeed.setLayoutManager(new FullyLinearLayoutManager(this));

        llytNull = (LinearLayout) findViewById(R.id.llyt_null);
        viewBottom = findViewById(R.id.view_bottom);

//        tvLevelNeed = (TextView) findViewById(R.id.tv_level_need);

    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);

    }

    @Override
    protected void init() {
        getUserInfo();
        getGrowValue();
    }

    private void getGrowValue() {
        new RetrofitRequest<UserGrowValueEntity>(ApiRequest.getApiShiji().getUserGrowValue()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if(msg.isSuccess()){
                    UserGrowValueEntity entity = (UserGrowValueEntity) msg.obj;
                    tvGrow.setText(entity.getGrow_value() + "成长值");
                }
            }
        });
    }

    private void getUserInfo() {
        new RetrofitRequest<User>(ApiRequest.getApiShiji().getUserDetail()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    User mUser = (User) msg.obj;
                    Netroid.displayImage(Util.transUrl(mUser.getHead()), ivHead);
                    tvName.setText(mUser.getName());
                    tvLevel.setText(transferLevel(mUser.getLevel()));

                    mAdapter = new MyRedLevelAdapter(MyRedLevelActivity.this, mUser.getLevel());
                    mAdapter.setOnClickListener(new MyRedLevelAdapter.OnClickListener() {
                        @Override
                        public void onClick(int position) {
//                            tvLevelNeed.setText(transferLevelNeed(position));
                            if(position == 5){
                                rycvLevelNeed.setVisibility(View.GONE);
                                llytNull.setVisibility(View.VISIBLE);
                                viewBottom.setVisibility(View.GONE);
                            }else {
                                rycvLevelNeed.setVisibility(View.VISIBLE);
                                llytNull.setVisibility(View.GONE);
                                viewBottom.setVisibility(View.VISIBLE);
                                mLevelNeedAdapter.setmList(powerList.get(position));
                                mLevelNeedAdapter.setSelectedPosition(position);
                                mLevelNeedAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                    rycvLevel.setAdapter(mAdapter);

//                    tvLevelNeed.setText(transferLevelNeed(mUser.getLevel()));
                    if(mUser.getLevel() == 5){
                        rycvLevelNeed.setVisibility(View.GONE);
                        llytNull.setVisibility(View.VISIBLE);
                        viewBottom.setVisibility(View.GONE);
                    }else {
                        rycvLevelNeed.setVisibility(View.VISIBLE);
                        llytNull.setVisibility(View.GONE);
                        viewBottom.setVisibility(View.VISIBLE);
                        mLevelNeedAdapter = new LevelNeedAdapter(MyRedLevelActivity.this, mUser.getLevel(), powerList.get(mUser.getLevel()), mUser.getLevel());
                        rycvLevelNeed.setAdapter(mLevelNeedAdapter);
                    }
//                    mLevelNeedAdapter.setmList(entity.getPower_desc().get(mUser.getLevel()));
//                    mLevelNeedAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private String transferLevel(int level){
        switch (level){
            case 0:
                return "H1";
            case 1:
                return "H2";
            case 2:
                return "H3";
            case 3:
                return "H4";
            case 4:
                return "H5";
            default:
                return "H6";
        }
    }

    private String transferLevelNeed(int level){
        switch (level){
            case 0:
                return "成长值无要求";
            case 1:
                return "成长值≥" + entity.getLevel().get(0);
            case 2:
                return "成长值≥" + entity.getLevel().get(1);
            case 3:
                return "成长值≥" + entity.getLevel().get(2);
            case 4:
                return "成长值≥" + entity.getLevel().get(3);
            default:
                return "成长值≥" + entity.getLevel().get(4);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                onBackPressed();
                break;
        }
    }

}
