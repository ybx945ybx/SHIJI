package cn.yiya.shiji.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.NewFragmentPagerAdapter;
import cn.yiya.shiji.fragment.WorkListFragment;
import cn.yiya.shiji.views.DisableViewPage;

/**
 * Created by Amy on 2016/7/26.
 */
public class NewUserLikedActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private int user_id;

    private ImageView ivBack;
    private TextView tvTitle;
    private TabLayout tabLayout;

    private DisableViewPage mViewPage;
    private NewFragmentPagerAdapter communityFragmentAdapter;
    private List<Fragment> mFragments;
    private static final String[] mTitles = new String[]{"搭配", "购物笔记"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_liked);
        initIntent();
        initViews();
        initEvents();
        init();
    }

    private void initIntent() {
        if (getIntent() != null) {
            user_id = getIntent().getIntExtra("user_id", 0);
        }
    }

    @Override
    protected void initViews() {
        ivBack = (ImageView) findViewById(R.id.title_back);
        findViewById(R.id.title_right).setVisibility(View.GONE);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText("已赞");

        tabLayout = (TabLayout) findViewById(R.id.tablayout);

        mViewPage = (DisableViewPage) findViewById(R.id.viewpager);
        mFragments = new ArrayList<>();
        mFragments.add(new WorkListFragment().getInstance(user_id, false, 2, true));//搭配
        mFragments.add(new WorkListFragment().getInstance(user_id, false, 1, true));//购物笔记
        communityFragmentAdapter = new NewFragmentPagerAdapter(getSupportFragmentManager(), mFragments, mTitles);
        mViewPage.setAdapter(communityFragmentAdapter);
        tabLayout.setupWithViewPager(mViewPage);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(getTabView(i));
        }
        mViewPage.setCurrentItem(0);
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
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    private boolean bSelected = true;

    /**
     * 自定义tab
     *
     * @param position
     * @return
     */
    public View getTabView(int position) {
        View v = LayoutInflater.from(this).inflate(R.layout.item_tab_view, null);
        TextView tv = (TextView) v.findViewById(R.id.tv_title);
        tv.setText(mTitles[position]);
        if (position == 0 && bSelected) {
            v.setSelected(true);
            bSelected = false;
        }
        return v;
    }
}
