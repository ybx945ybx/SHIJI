package cn.yiya.shiji.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.NewFragmentPagerAdapter;
import cn.yiya.shiji.fragment.CashAccountListFragment;

/**
 * Created by Tom on 2016/10/9.
 */
public class IncomeDetailActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ImageView ivBack;
    private TextView tvTitle;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private NewFragmentPagerAdapter fragmentPagerAdapter;

    private List<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles = new String[]{"返利收入", "返利扣除(退货)", "提成收入", "其他收入"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_detail);
        initViews();
        initEvents();
        init();
    }

    @Override
    protected void initViews() {
        ivBack = (ImageView) findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText("已入账明细");
        findViewById(R.id.title_right).setVisibility(View.GONE);

        mTabLayout = (TabLayout) findViewById(R.id.tblyt);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mFragments.add(new CashAccountListFragment().getInstance(4));
        mFragments.add(new CashAccountListFragment().getInstance(5));
        mFragments.add(new CashAccountListFragment().getInstance(6));
        mFragments.add(new CashAccountListFragment().getInstance(7));
        fragmentPagerAdapter = new NewFragmentPagerAdapter(getSupportFragmentManager(), mFragments, mTitles);
        mTabLayout.setTabsFromPagerAdapter(fragmentPagerAdapter);
        mViewPager.setAdapter(fragmentPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            tab.setCustomView(getTabView(i));
        }
        mViewPager.setCurrentItem(0);
    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
    }

    @Override
    protected void init() {

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

    private boolean bSelected = true;
    /**
     * 自定义tab
     */
    public View getTabView(int position) {
        View v = LayoutInflater.from(this).inflate(R.layout.can_cash_tab_view, null);
        TextView tv = (TextView) v.findViewById(R.id.tv_title);
        tv.setText(mTitles[position]);
        if (position == 0 && bSelected) {
            v.setSelected(true);
            bSelected = false;
        }
        return v;
    }
}
