package cn.yiya.shiji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.NewFragmentPagerAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.UserGrowRuleEntity;
import cn.yiya.shiji.fragment.RedRangFragment;
import cn.yiya.shiji.fragment.RedTaskFragment;
import cn.yiya.shiji.views.DisableViewPage;

/**
 * Created by Tom on 2017/1/6.
 */

public class RedPeopleTaskActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private ImageView ivBack;
    private TextView tvTitle;
    private TextView tvRight;

    private TabLayout tabLayout;
    private DisableViewPage viewPage;
    private NewFragmentPagerAdapter newFragmentPagerAdapter;
    private List<Fragment> mFragments;
    private static final String[] mTitles = new String[]{"任务栏", "成长值排行榜"};

    private UserGrowRuleEntity entity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_people_task);
        initViews();
        initEvents();
        init();
    }

    @Override
    protected void initViews() {
        ivBack = (ImageView) findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText("红人任务");
        tvRight = (TextView) findViewById(R.id.title_right);
        tvRight.setText("我的等级");

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPage = (DisableViewPage) findViewById(R.id.viewpager);

//        mFragments = new ArrayList<>();
//        mFragments.add(new RedTaskFragment());
//        mFragments.add(new RedRangFragment());
//        newFragmentPagerAdapter = new NewFragmentPagerAdapter(getSupportFragmentManager(), mFragments, mTitles);
//        viewPage.setAdapter(newFragmentPagerAdapter);
//        tabLayout.setupWithViewPager(viewPage);
//        for (int i = 0; i < tabLayout.getTabCount(); i++) {
//            TabLayout.Tab tab = tabLayout.getTabAt(i);
//            tab.setCustomView(getTabView(i));
//        }
//        viewPage.setCurrentItem(0);
    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        tvRight.setOnClickListener(this);
    }

    @Override
    protected void init() {
        getGrowRule();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.title_right:
                Intent intent = new Intent(this, MyRedLevelActivity.class);
                intent.putExtra("entity", new Gson().toJson(entity));
                startActivity(intent);
                break;
        }
    }

    /**
     * 自定义tab
     * @param position
     * @return
     */
    public View getTabView(int position) {
        View v = LayoutInflater.from(this).inflate(R.layout.item_tab_view, null);
        TextView tv = (TextView) v.findViewById(R.id.tv_title);
        tv.setText(mTitles[position]);
        if (position == 0) {
            v.setSelected(true);
        }
        return v;
    }

    private void getGrowRule() {
        new RetrofitRequest<UserGrowRuleEntity>(ApiRequest.getApiShiji().getUserGrowRule()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if(msg.isSuccess()){
                    entity = (UserGrowRuleEntity) msg.obj;
                    mFragments = new ArrayList<>();
                    mFragments.add(new RedTaskFragment().getInstance(entity));
                    mFragments.add(new RedRangFragment().getInstance(entity));
                    newFragmentPagerAdapter = new NewFragmentPagerAdapter(getSupportFragmentManager(), mFragments, mTitles);
                    viewPage.setAdapter(newFragmentPagerAdapter);
                    tabLayout.setupWithViewPager(viewPage);
                    for (int i = 0; i < tabLayout.getTabCount(); i++) {
                        TabLayout.Tab tab = tabLayout.getTabAt(i);
                        tab.setCustomView(getTabView(i));
                    }
                    viewPage.setCurrentItem(0);
                }
            }
        });
    }
}
