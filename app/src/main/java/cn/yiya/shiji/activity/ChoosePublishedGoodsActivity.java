package cn.yiya.shiji.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import cn.yiya.shiji.R;
import cn.yiya.shiji.fragment.PageFragment;

public class ChoosePublishedGoodsActivity extends BaseAppCompatActivity implements View.OnClickListener{
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private TextView tvCancle;
    private TextView tvRight;
    private SimpleFragmentPagerAdapter mPagerAdapter;
    private String[] mTitle = new String[]{"购买记录","购物车"};
    private static final int REQUEST_CODE_GOODES = 400;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_published_goods);
        initViews();
        initEvents();
        init();
    }

    @Override
    protected void initViews() {

        tvCancle = (TextView)findViewById(R.id.title_cancle);
        tvRight = (TextView)findViewById(R.id.title_right);
        mTabLayout = (TabLayout)findViewById(R.id.tabs);
        mViewPager = (ViewPager)findViewById(R.id.viewpager);
        mPagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), this);

        mTabLayout.setTabsFromPagerAdapter(mPagerAdapter);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);  //必须在ViewPager.setAdapter之后调用
    }

    @Override
    protected void initEvents() {
        tvCancle.setOnClickListener(this);
        tvRight.setOnClickListener(this);
    }

    @Override
    protected void init() {

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.title_cancle:
                onBackPressed();
                break;
            case R.id.title_right:
                Intent intent = new Intent(ChoosePublishedGoodsActivity.this, MallListActivity.class);
                intent.putExtra("show", true);
                startActivityForResult(intent, REQUEST_CODE_GOODES);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_GOODES) {
                if (data != null) {
                    String goodes = data.getStringExtra("goodes");
                    Intent intent = new Intent();
                    intent.putExtra("json", goodes);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        } else if (resultCode == RESULT_CANCELED){
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }



    public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

        private Context context;

        public SimpleFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            return PageFragment.instancePage(position);
        }

        @Override
        public int getCount() {
            return mTitle.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitle[position];
        }
    }
}
