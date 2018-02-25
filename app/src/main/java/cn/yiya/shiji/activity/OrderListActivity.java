package cn.yiya.shiji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.NewFragmentPagerAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.NotifyCount;
import cn.yiya.shiji.fragment.OrderListFragment;

/**
 * Created by jerry on 2016/4/7.
 */
public class OrderListActivity extends BaseAppCompatActivity implements View.OnClickListener, OrderListFragment.SendCount {
    private String[] mTitles = {"全部", "待支付", "运输中", "已完成", "已失效"};
    private NewFragmentPagerAdapter pagerAdapter;
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    private TabLayout tb;
    private ViewPager vp;
    private int type;
    private Intent intent;
    private TextView tvTab;
    private TextView tvCount;
    private TextView tvTitle;
    private ImageView tvRight;
    private ImageView ivBack;
    public static final int REQUEST_CAR = 100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_list_activity_layout);
        intent = getIntent();
        type = intent.getIntExtra("type", 0);

        initViews();
        initEvents();
        init();
    }

    @Override
    protected void initViews() {
        tb = (TabLayout) findViewById(R.id.tablayout);
        vp = (ViewPager) findViewById(R.id.viewpager);
        tvRight = (ImageView) findViewById(R.id.title_right);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        ivBack = (ImageView) findViewById(R.id.title_back);
        ivBack.setOnClickListener(this);
        tvRight.setVisibility(View.INVISIBLE);
        tvTitle.setText("订单列表");
    }

    @Override
    protected void initEvents() {
        tb.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1) {
                    tvTab.setTextColor(getResources().getColor(R.color.app_base_red));
                }
                vp.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1) {
                    tvTab.setTextColor(getResources().getColor(R.color.tab_text));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void init() {
        mFragments.add(new OrderListFragment().getInstance(0));
        mFragments.add(new OrderListFragment().getInstance(1));
        mFragments.add(new OrderListFragment().getInstance(2));
        mFragments.add(new OrderListFragment().getInstance(4));
        mFragments.add(new OrderListFragment().getInstance(5));

        pagerAdapter = new NewFragmentPagerAdapter(getSupportFragmentManager(), mFragments, mTitles);
        tb.setTabMode(TabLayout.MODE_FIXED);
        vp.setAdapter(pagerAdapter);
        tb.setupWithViewPager(vp);
        vp.setCurrentItem(type);
        hintNum();
        loadNotifyData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
        }
    }

    private void hintNum() {
        TabLayout.Tab tab = tb.getTabAt(1);
        View view = LayoutInflater.from(this).inflate(R.layout.hint_num_layout, null);
        tab.setCustomView(view);
        tvTab = (TextView) view.findViewById(R.id.tv);
        tvCount = (TextView) view.findViewById(R.id.counttext);
    }

    @Override
    public void chengCount(int size) {
        if (size == 0) {
            tvCount.setVisibility(View.GONE);
        } else {
            tvCount.setText("" + size);
        }
    }

    private void loadNotifyData() {
        new RetrofitRequest<NotifyCount>(ApiRequest.getApiShiji().getNotifyCount()).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            NotifyCount obj = (NotifyCount) msg.obj;
                            if (obj != null) {
                                if (obj.getPending_pay_order() > 0) {
                                    tvCount.setText("" + obj.getPending_pay_order());
                                    tvCount.setVisibility(View.VISIBLE);
                                } else {
                                    tvCount.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                }
        );
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }
}
