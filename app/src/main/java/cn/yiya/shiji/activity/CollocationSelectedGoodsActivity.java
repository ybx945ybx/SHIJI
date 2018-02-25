package cn.yiya.shiji.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.fragment.CollocationSelectedGoodsContainerFragment;
import cn.yiya.shiji.fragment.CollocationUsedGoodsFragment;

/**
 * Created by Tom on 2016/7/20.
 */
public class CollocationSelectedGoodsActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ImageView ivBack;
    private View leftView;
    private View rightView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String[] mTitles = new String[]{"全部商品", "过去使用商品"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collocation_selected_goods);
        initItent();
        initViews();
        initEvents();
        init();
    }

    private void initItent() {

    }

    @Override
    protected void initViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        leftView = findViewById(R.id.left_view);
        leftView.setBackgroundColor(Color.parseColor("#ed5137"));
        rightView = findViewById(R.id.right_view);
//        leftView.setBackgroundColor(Color.parseColor("#ffffff"));
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        List<Fragment> fragmentList = new ArrayList<>();
//        CollocationSelectedGoodsFragment collocationSelectedGoodsFragment = new CollocationSelectedGoodsFragment();
        CollocationSelectedGoodsContainerFragment collocationSelectedGoodsContainerFragment = new CollocationSelectedGoodsContainerFragment();
        CollocationUsedGoodsFragment collocationUsedGoodsFragment = new CollocationUsedGoodsFragment();
        fragmentList.add(collocationSelectedGoodsContainerFragment);
        fragmentList.add(collocationUsedGoodsFragment);
        TabFragmentAdapter tabFragmentAdapter = new TabFragmentAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(tabFragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){
                    leftView.setBackgroundColor(Color.parseColor("#ed5137"));
                    rightView.setBackgroundColor(Color.parseColor("#ffffff"));
                }else if(tab.getPosition() == 1){
                    leftView.setBackgroundColor(Color.parseColor("#ffffff"));
                    rightView.setBackgroundColor(Color.parseColor("#ed5137"));
                }
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void init() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                setResult(RESULT_CANCELED);
                onBackPressed();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_out_fixed, R.anim.slide_out_top_buttom);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(data != null){
                setResult(RESULT_OK, data);
                finish();
            }
        }
    }

    private class TabFragmentAdapter extends FragmentPagerAdapter{
        private List<Fragment> fragments;

        public TabFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }
}
