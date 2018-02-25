package cn.yiya.shiji.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.SearchAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MapRequest;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.config.CustomEvent;
import cn.yiya.shiji.entity.BrandsSortItem;
import cn.yiya.shiji.entity.BrandsSortObject;
import cn.yiya.shiji.entity.CarCountInfo;
import cn.yiya.shiji.entity.CharacterParser;
import cn.yiya.shiji.fragment.PopularBrandsFragment;
import cn.yiya.shiji.fragment.SortBrandsFragment;
import cn.yiya.shiji.views.ClearEditText;
import cn.yiya.shiji.views.TabPageManager;

/**
 * Created by jerryzhang on 2015/10/9.
 */
public class PopularBrandsListActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private TabHost mTabHost;
    private TabPageManager mTabPageManager;
    private ViewPager mViewPager;
//    private ImageView iv_back;
    private TextView tvCancle;
    private ImageView ivBack;
    private RelativeLayout rlytShopCart;
    private TextView tvCart;

    private Handler mHandler;
    private ArrayList<BrandsSortItem> nlist = new ArrayList<>();
    private ClearEditText mClearEditText;
    private SortBrandsFragment fragmentFour;
    private ListView mlistview;
    private SearchAdapter adapter;
    private CharacterParser characterParser;
    private RelativeLayout mRelativeLayout;

    private boolean isLogin;
    private static final int REQUEST_LOGIN = 1001;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details_four);
        mHandler = new Handler();
        fragmentFour = new SortBrandsFragment();
        initViews();
        init();
        initEvents();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                mClearEditText.clearFocus();
                mClearEditText.setText("");
                hideSoftInput();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.toolbar_right_layout:
                gotoShoppingCart();
                break;
            default:
                break;
        }
    }

    //收起软键盘
    private void hideSoftInput(){
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mClearEditText.getWindowToken(),0);
    }

    private void gotoShoppingCart() {
        if (isEffectClick()) {
            Intent intentGoShopCart = new Intent(PopularBrandsListActivity.this, NewShoppingCartActivity.class);
            startActivity(intentGoShopCart);
        }
    }

    private View tabView(String title) {
        View indicatorview = android.view.LayoutInflater.from(this).inflate(R.layout.common_tab_view_four, null);
        TextView text = (TextView) indicatorview.findViewById(R.id.tabsText);
        text.setText(title);
        return indicatorview;
    }

    @Override
    protected void initViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        rlytShopCart = (RelativeLayout) findViewById(R.id.toolbar_right_layout);
        tvCart = (TextView) findViewById(R.id.toolbar_right_count);
        tvCancle = (TextView) findViewById(R.id.tv_cancel);

        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mTabPageManager = new TabPageManager(getSupportFragmentManager(), mTabHost, mViewPager);

        mlistview = (ListView) findViewById(R.id.listview);
        mClearEditText = (ClearEditText) findViewById(R.id.title_txt);
        mClearEditText.clearFocus();
        characterParser = CharacterParser.getInstance();
        adapter = new SearchAdapter(this);
        mlistview.setAdapter(adapter);

        mRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
    }

    @Override
    protected void initEvents() {
        tvCancle.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        rlytShopCart.setOnClickListener(this);

        mlistview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        InputMethodManager imm = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });


        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomEvent.onEvent(PopularBrandsListActivity.this, ((BaseApplication) getApplication()).getDefaultTracker(), "PopularBrandsListActivity", CustomEvent.SearchBrand);
                BrandsSortItem item = (BrandsSortItem) parent.getItemAtPosition(position);
                Intent intent = new Intent(PopularBrandsListActivity.this, NewSingleBrandActivity.class);
                intent.putExtra("brand_id", item.getId());
                startActivity(intent);
            }
        });

        mClearEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mlistview.setVisibility(View.VISIBLE);
                mTabHost.setVisibility(View.INVISIBLE);
                filterData(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mClearEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tvCancle.setVisibility(View.VISIBLE);
                    rlytShopCart.setVisibility(View.GONE);
                } else {
                    tvCancle.setVisibility(View.GONE);
                    rlytShopCart.setVisibility(View.VISIBLE);
                }
            }
        });

        mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
    }

    private void initCarCount() {
        tvCart.setVisibility(View.GONE);
        new RetrofitRequest<CarCountInfo>(ApiRequest.getApiShiji().getCarCount()).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            isLogin = true;
                            tvCart.setVisibility(View.VISIBLE);
                            CarCountInfo info = (CarCountInfo) msg.obj;
                            tvCart.setText("" + info.getCount());
                            if (info.getCount() == 0) {
                                tvCart.setVisibility(View.GONE);
                            }

                        } else if (msg.isLossLogin()) {
                            tvCart.setVisibility(View.GONE);
                            isLogin = false;
                        }
                    }
                }
        );
    }

    @Override
    protected void init() {
        showPreDialog("一大波数据正在加载...");
        mTabPageManager.addTab(mTabHost.newTabSpec("PopularBrandsFragment").setIndicator(tabView("热门品牌")), PopularBrandsFragment.class, null);
        mTabPageManager.addTab(mTabHost.newTabSpec("SortBrandsFragment").setIndicator(tabView("名称排序")), SortBrandsFragment.class, null);

        DownloadTask asyntask = new DownloadTask();
        asyntask.execute();
        loadMoreData(0);
//        initCarCount();
    }


    class DownloadTask extends AsyncTask<Object, Object, Object> {
        @Override
        protected Object doInBackground(Object... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }
    }

    private void loadMoreData(final int offset) {
        new RetrofitRequest<BrandsSortObject>(ApiRequest.getApiShiji().getBrandsSortList(MapRequest
                .setMapTen(offset))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    BrandsSortObject item = (BrandsSortObject) msg.obj;
                    if (item.list != null && item.list.size() > 0) {
                        nlist = item.list;
                        if (transforData != null) {
                            transforData.DataListenner(nlist);
                        }
                        adapter.setList(nlist);
                    } else {
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_LOGIN) {
                initCarCount();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initCarCount();
    }

    private void filterData(String filterStr) {
        ArrayList<BrandsSortItem> list = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            mlistview.setVisibility(View.INVISIBLE);
            mTabHost.setVisibility(View.VISIBLE);
        } else {
            list.clear();
            for (BrandsSortItem mBrandsSortItem : nlist) {
                String name = mBrandsSortItem.getName();
                String cn_name = mBrandsSortItem.getCn_name();
                if ((name.toUpperCase()).indexOf((filterStr.toString()).toUpperCase()) != -1 ||
                        cn_name.indexOf(filterStr.toString()) != -1) {
                    list.add(mBrandsSortItem);
                }
            }
        }
        adapter.updateListView(list);
    }

    private TransforData transforData;

    public interface TransforData {
        void DataListenner(ArrayList<BrandsSortItem> nlist);
    }

    public void setDataListenner(TransforData transforData) {
        this.transforData = transforData;
    }

}
