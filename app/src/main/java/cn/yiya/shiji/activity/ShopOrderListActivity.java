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
import cn.yiya.shiji.fragment.ShopOrderListFragment;

/**
 * 店铺订单
 * Created by Amy on 2016/10/18.
 */

public class ShopOrderListActivity extends BaseAppCompatActivity {
    private ImageView ivBack;
    private TextView tvTitle;
    private int shopId;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private boolean bSelected = true;
    private String[] mTitles = {"全部订单", "待支付", "运输中", "已完成", "已失效"};
    private NewFragmentPagerAdapter pagerAdapter;
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    private TextView tvCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_order_list);
        initIntent();
        initViews();
        initEvents();
        init();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            shopId = intent.getIntExtra("shop_id", 0);
        }
    }

    @Override
    protected void initViews() {
        ivBack = (ImageView) findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText("店铺订单");
        findViewById(R.id.title_right).setVisibility(View.GONE);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        mFragments.add(new ShopOrderListFragment().getInstance(shopId, 0));
        mFragments.add(new ShopOrderListFragment().getInstance(shopId, 1));
        mFragments.add(new ShopOrderListFragment().getInstance(shopId, 2));
        mFragments.add(new ShopOrderListFragment().getInstance(shopId, 4));
        mFragments.add(new ShopOrderListFragment().getInstance(shopId, 5));

        pagerAdapter = new NewFragmentPagerAdapter(getSupportFragmentManager(), mFragments, mTitles);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(getTabView(i, mTitles));
        }
        viewPager.setCurrentItem(0);
    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void init() {

    }

    /**
     * 自定义tab
     *
     * @param position
     * @return
     */
    public View getTabView(int position, String[] mTitles) {
        View v = LayoutInflater.from(this).inflate(R.layout.item_tab_view, null);
        TextView tv = (TextView) v.findViewById(R.id.tv_title);
        tv.setText(mTitles[position]);
        if (position == 0 && bSelected) {
            v.setSelected(true);
            bSelected = false;
        }
        if (position == 1) {
            tvCount = (TextView) v.findViewById(R.id.tv_count);
        }
        return v;
    }

    /**
     * 获取店铺待支付订单数 店铺待收货订单数
     */
    private void loadNotifyData() {
        new RetrofitRequest<NotifyCount>(ApiRequest.getApiShiji().getNotifyCount()).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            NotifyCount obj = (NotifyCount) msg.obj;
                            if (obj != null) {
                                if (obj.getShop_pending_pay_order() > 0) {
                                    tvCount.setText("" + obj.getShop_pending_pay_order());
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
    protected void onResume() {
        super.onResume();
        loadNotifyData();
    }
}
