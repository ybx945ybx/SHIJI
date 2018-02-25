package cn.yiya.shiji.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import cn.yiya.shiji.R;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.entity.MallLimitOptionInfo;
import cn.yiya.shiji.entity.MallLimitOptionObject;
import cn.yiya.shiji.fragment.GoodsListBrandsFragment;
import cn.yiya.shiji.fragment.GoodsListFilterFragment;
import cn.yiya.shiji.utils.SharedPreUtil;

/**
 * 商品列表的筛选界面（包含品牌和筛选两个fragment）
 * Created by jerry on 2016/5/13.
 */
public class GoodsListFilterActivity extends BaseAppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    /*
    标题栏
     */
    private TextView tvTitle;
    private ImageView ivClose;
    /*
    左侧品牌和筛选选择按钮
     */
    private RadioGroup radioGroup;
    private RadioButton rbBrands;
    private RadioButton rbFilter;
    private ImageView ivBrands;
    private ImageView ivFilter;
    /*
    底部清除全部和确定按钮
     */
    private Button btnClear;
    private Button btnOk;
    /*
    显示筛选内容的布局
     */
    private FrameLayout flytContext;

    private LayoutInflater mInflater;
    private FragmentManager fm;
    private Fragment mFragment;

    private final String BRANDS = "brands";     // 品牌fragment的标签
    private final String FILTER = "filter";     // 筛选fragment的标签

    private int source;  //1000 搜索  1001 首页   1002 二级分类  1003 商城   1004 品牌 必填
    private int siteId; //某一网站商城id或者某一分类类型id
    private boolean bShowBrand; //是否显示品牌 true显示  默认显示
    private String word; //搜索词
    private String categoryId = ""; //当source=1002是会有
    private MallLimitOptionInfo mInfos = new MallLimitOptionInfo();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_list_filter_layout);

        mInflater = LayoutInflater.from(this);
        fm = getSupportFragmentManager();

        initIntent();

        initViews();
        initEvents();
        init();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            String data = "";
            source = intent.getIntExtra("source", 0);
            siteId = intent.getIntExtra("siteId", 0);
            word = intent.getStringExtra("word");
            categoryId = intent.getStringExtra("categoryid");

            bShowBrand = intent.getBooleanExtra("showBrand", true);
            if (bShowBrand) {
                data = SharedPreUtil.getString(this, Configration.SHAREDPREFERENCE, "filter_option", "");
            } else {
                data = intent.getStringExtra("data");
            }
            if (!TextUtils.isEmpty(data)) {
                mInfos = new Gson().fromJson(data, MallLimitOptionInfo.class);
            }

            if (mInfos.getBrands() == null) {
                getBrandsData();
            }
        }

    }

    @Override
    protected void initViews() {
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText("筛选");
        tvTitle.setTextColor(Color.parseColor("#212121"));
        findViewById(R.id.title_back).setVisibility(View.GONE);
        ivClose = (ImageView) findViewById(R.id.title_right);
        ivClose.setImageResource(R.mipmap.xia_fanhui);

        radioGroup = (RadioGroup) findViewById(R.id.radio);
        rbBrands = (RadioButton) findViewById(R.id.brands);
        rbFilter = (RadioButton) findViewById(R.id.filter);
        ivBrands = (ImageView) findViewById(R.id.iv_brands_indicator);
        ivFilter = (ImageView) findViewById(R.id.iv_filter_indicator);

        if (!bShowBrand) {
            rbBrands.setVisibility(View.GONE);
            ivBrands.setVisibility(View.GONE);
        }
        btnClear = (Button) findViewById(R.id.bt_clear);
        btnOk = (Button) findViewById(R.id.bt_ok);

        flytContext = (FrameLayout) findViewById(R.id.framelayout);

        turnPage(FILTER);

        if (mInfos.getBrands() != null) {
            for (int i = 0; i < mInfos.getBrands().size(); i++) {
                if (mInfos.getBrands().get(i).isCheck()) {
                    setIndicatorLight(2, true);
                }
            }
        } else {
            setIndicatorLight(2, false);
        }
    }

    @Override
    protected void initEvents() {
        ivClose.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(this);
        btnClear.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    @Override
    protected void init() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_right:
                setResult(RESULT_CANCELED);
                onBackPressed();
                break;
            case R.id.bt_clear:
                resetSelect();
                break;
            case R.id.bt_ok:
                commitSelect();
                onBackPressed();
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.brands:
                turnPage(BRANDS);
                break;
            case R.id.filter:
                turnPage(FILTER);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_out_fixed, R.anim.slide_out_top_buttom);
    }

    private void turnPage(String tag) {
        FragmentTransaction ft = fm.beginTransaction();
        if (mFragment != null) {
            ft.hide(mFragment);
        }
        mFragment = fm.findFragmentByTag(tag);
        if (mFragment == null) {
            switch (tag) {
                case BRANDS:
                    mFragment = new GoodsListBrandsFragment();
                    Bundle bundleBrands = new Bundle();
                    bundleBrands.putString("data", new Gson().toJson(mInfos));
                    mFragment.setArguments(bundleBrands);
                    break;
                case FILTER:
                    mFragment = new GoodsListFilterFragment();
                    Bundle bundleFilter = new Bundle();
                    bundleFilter.putString("data", new Gson().toJson(mInfos));
                    bundleFilter.putInt("type", 0);
                    if (!TextUtils.isEmpty(categoryId)) {
                        bundleFilter.putString("categoryid", categoryId);
                    }
                    mFragment.setArguments(bundleFilter);
                    break;
            }
            if (mFragment != null) {
                ft.add(R.id.framelayout, mFragment, tag);
            }
        }
        ft.show(mFragment).commit();

        btnClear.setBackgroundResource(R.drawable.goods_list_filter_clear_commit_unclickable_bg);
        btnClear.setClickable(false);
        if (tag.equals(BRANDS)) {
            if (ivBrands.getVisibility() == View.VISIBLE) {
                btnClear.setBackgroundResource(R.drawable.goods_list_filter_clear_commit_bg);
                btnClear.setClickable(true);
            }
        } else {
            if (ivFilter.getVisibility() == View.VISIBLE) {
                btnClear.setBackgroundResource(R.drawable.goods_list_filter_clear_commit_bg);
                btnClear.setClickable(true);
            }
        }
    }

    /**
     * 清除全部选项
     */
    private void resetSelect() {
        if (rbBrands.isChecked()) {
            ((GoodsListBrandsFragment) fm.findFragmentByTag(BRANDS)).reset();
        } else {
            ((GoodsListFilterFragment) fm.findFragmentByTag(FILTER)).reset();
        }
    }

    // 确定选择
    private void commitSelect() {
        MallLimitOptionInfo brandsInfo;
        MallLimitOptionInfo filterInfo;
        if (fm.findFragmentByTag(BRANDS) != null) {
            brandsInfo = ((GoodsListBrandsFragment) fm.findFragmentByTag(BRANDS)).commit();
            if (brandsInfo != null) {
                mInfos.setBrands(brandsInfo.getBrands());
            }
        }
        if (fm.findFragmentByTag(FILTER) != null) {
            filterInfo = ((GoodsListFilterFragment) fm.findFragmentByTag(FILTER)).commit();
            if (filterInfo != null) {
                mInfos.setGenders(filterInfo.getGenders());
                mInfos.setCategories(filterInfo.getCategories());
                mInfos.setSize(filterInfo.getSize());
                mInfos.setPrice_ranges(filterInfo.getPrice_ranges());
                mInfos.setColor(filterInfo.getColor());
            }
        }
        if (bShowBrand) {
            SharedPreUtil.putString(this, Configration.SHAREDPREFERENCE, "filter_option_result", new Gson().toJson(mInfos));
            setResult(RESULT_OK);
        } else {
            Intent intentCommit = new Intent();
            intentCommit.putExtra("infoData", new Gson().toJson(mInfos));
            setResult(RESULT_OK, intentCommit);
        }
    }


    // // TODO: 2016/6/6 判断小红点是否亮,清除全部按钮是否可点击，设置字体颜色
    public void setIndicatorLight(int type, boolean needLight) {       // type 1是筛选2是品牌

        if (needLight) {
            if (type == 1) {
                ivFilter.setVisibility(View.VISIBLE);
            } else {
                ivBrands.setVisibility(View.VISIBLE);
            }
            btnClear.setBackgroundResource(R.drawable.goods_list_filter_clear_commit_bg);
            btnClear.setClickable(true);
        } else {
            if (type == 1) {
                ivFilter.setVisibility(View.INVISIBLE);
            } else {
                ivBrands.setVisibility(View.INVISIBLE);
            }
            btnClear.setBackgroundResource(R.drawable.goods_list_filter_clear_commit_unclickable_bg);
            btnClear.setClickable(false);
        }

    }

    /**
     * 获取品牌筛选项
     */
    private void getBrandsData() {
        if (source == 1000) {
            new RetrofitRequest<MallLimitOptionObject>(ApiRequest.getApiShiji().getSearchBrandOption(word))
                    .handRequest(new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            if (msg.isSuccess()) {
                                MallLimitOptionObject object = (MallLimitOptionObject) msg.obj;
                                if (object != null) {
                                    mInfos.setBrands(object.getList().getBrands());
                                }
                            }
                        }
                    });
        } else if (source == 1001) {
            new RetrofitRequest<MallLimitOptionObject>(ApiRequest.getApiShiji().getMenuCategoryBrandOption(
                    String.valueOf(siteId))).handRequest(new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    if (msg.isSuccess()) {
                        MallLimitOptionObject object = (MallLimitOptionObject) msg.obj;
                        if (object != null) {
                            mInfos.setBrands(object.getList().getBrands());
                        }
                    }
                }
            });
        } else if (source == 1002) {
            new RetrofitRequest<MallLimitOptionObject>(ApiRequest.getApiShiji().getCategoryBrandOption(
                    String.valueOf(siteId))).handRequest(new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    if (msg.isSuccess()) {
                        MallLimitOptionObject object = (MallLimitOptionObject) msg.obj;
                        if (object != null) {
                            mInfos.setBrands(object.getList().getBrands());
                        }
                    }
                }
            });
        } else if (source == 1003) {
            new RetrofitRequest<MallLimitOptionObject>(ApiRequest.getApiShiji().getSiteBrandOption(
                    String.valueOf(siteId))).handRequest(new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    if (msg.isSuccess()) {
                        MallLimitOptionObject object = (MallLimitOptionObject) msg.obj;
                        if (object != null) {
                            mInfos.setBrands(object.getList().getBrands());
                        }
                    }
                }
            });
        }
    }
}
