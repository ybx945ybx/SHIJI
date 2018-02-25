package cn.yiya.shiji.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.fragment.SubscibeFragment;

/**
 * 修改订阅界面
 * Created by Tom on 2016/2/22.
 */
public class EditSubscibeActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private ImageView ivBack;
    private TextView tvTitle;
    private ImageView ivRight;

    private TabLayout mTablayout;
    private ViewPager mViewPager;
    private SubscibeFragmentPageAdapter mAdapter;
    private String[] mTitle = new String[]{"品牌订阅","品类订阅"};
    private int selectedNumber = 0;
    private ArrayList<TextView> tabNumberList = new ArrayList<>();
    private ArrayList<TextView> tabTypeList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_subscibe);
        initViews();
        initEvents();
        init();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                onBackPressed();
                 break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void initViews() {
        ivBack = (ImageView)findViewById(R.id.title_back);
        tvTitle = (TextView)findViewById(R.id.title_txt);
        tvTitle.setText("订阅管理");
        ivRight = (ImageView)findViewById(R.id.title_right);
        ivRight.setVisibility(View.INVISIBLE);
        mTablayout = (TabLayout)findViewById(R.id.mTabLayout);
        mViewPager = (ViewPager)findViewById(R.id.mViewPager);
        mAdapter = new SubscibeFragmentPageAdapter(getSupportFragmentManager(),this);
        mTablayout.setTabsFromPagerAdapter(mAdapter);
        mViewPager.setAdapter(mAdapter);
        mTablayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < mTablayout.getTabCount(); i++){
            TabLayout.Tab tab = mTablayout.getTabAt(i);
            View view = LayoutInflater.from(this).inflate(R.layout.tab_item,null);
            TextView tvType = (TextView)view.findViewById(R.id.title_type);
            tabTypeList.add(tvType);
            tvType.setText(mTitle[i]);
            TextView tvSelectedNumber = (TextView)view.findViewById(R.id.selected_number);
            tabNumberList.add(tvSelectedNumber);
            if(i == 0){
                tvType.setTextColor(Color.parseColor("#ffaead"));
                tvSelectedNumber.setVisibility(View.VISIBLE);
            }
            upDateView(selectedNumber,i);
            tab.setCustomView(view);
        }
    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        mTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabTypeList.get(tab.getPosition()).setTextColor(Color.parseColor("#ffaead"));
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tabTypeList.get(tab.getPosition()).setTextColor(Color.parseColor("#9e9e9e"));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void init() {

    }

    public class SubscibeFragmentPageAdapter extends FragmentPagerAdapter {
        private Context mContext;
        public SubscibeFragmentPageAdapter(FragmentManager fm, Context mContext) {
            super(fm);
            this.mContext = mContext;
        }

        @Override
        public Fragment getItem(int position) {
            return SubscibeFragment.instancePage(position);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }

    //选择后更新数字
    public void upDateView(int selectedNumber,int position) {
        this.selectedNumber = selectedNumber;
        if(position == 0){
            tabNumberList.get(position).setText("已添加" + selectedNumber + "个品牌");
        }else {
            tabNumberList.get(position).setText("已添加" + selectedNumber + "个品类");
        }
    }

}
