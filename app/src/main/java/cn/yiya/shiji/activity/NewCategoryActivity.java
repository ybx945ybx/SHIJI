package cn.yiya.shiji.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.ArrayList;
import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.NewCategorySelectAdapter;
import cn.yiya.shiji.adapter.NewFragmentPagerAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MapRequest;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.entity.BrandsSortItem;
import cn.yiya.shiji.entity.BrandsSortObject;
import cn.yiya.shiji.entity.CarCountInfo;
import cn.yiya.shiji.entity.HotCategoryObject;
import cn.yiya.shiji.entity.Item;
import cn.yiya.shiji.fragment.NewCategoryBrandFragment;
import cn.yiya.shiji.fragment.NewCategoryClassFragment;
import cn.yiya.shiji.views.FullyLinearLayoutManager;
import cn.yiya.shiji.views.ThemeManager;

/**
 * Created by Amy on 2016/6/21.
 */
public class NewCategoryActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private int typeId;
    private String typeName;
    private SharedPreferences sp;
    private List<HotCategoryObject.SecondItem> typeList;
    private List<HotCategoryObject.SecondItem> tempTypeList;

    //toolbar
    private Toolbar toolbar;
    private RelativeLayout rlToolbar;
    private ImageView ivLeft;
    private LinearLayout llMiddle;
    private TextView tvMiddleType;
    private ImageView ivMiddleArrow;
    private RelativeLayout rlRight;
    private TextView tvCarCount;
    //搜索
    private RelativeLayout rlSearch;
    //TabLayout
    private TabLayout tabLayout;
    private View lineLeft;
    private View lineRight;

    private ViewPager viewPager;
    private NewFragmentPagerAdapter mAdapter;
    private List<Fragment> mFragments;
    private String[] mTitles = new String[]{"品类", "品牌"};

    //按照名称排序品牌
    private ArrayList<BrandsSortItem> mList;
    private TransforDataListener transforlistener;
    private PopupWindow popupWindow;
    private RecyclerView rvCategorySelect;
    private NewCategorySelectAdapter selectAdapter;
    private NewCategoryClassFragment classFragment;

    private final static int CAR_REQUEST_CODE = 1000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);
        ThemeManager.init(this, 2, 0, null);
        initIntent();
        initViews();
        initEvents();
        init();
    }

    private void initIntent() {
        if (getIntent() != null) {
            typeId = getIntent().getIntExtra("typeId", 0);  //一级分类编号
            typeName = getIntent().getStringExtra("typeName");
        }
        sp = this.getSharedPreferences(Configration.SHAREDPREFERENCE, MODE_PRIVATE);
        getFirstCategoryFromSp();
    }

    @Override
    protected void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rlToolbar = (RelativeLayout) findViewById(R.id.rl_toolbar);
        ivLeft = (ImageView) findViewById(R.id.toolbar_left);
        ivLeft.setImageResource(R.mipmap.back_image_white);
        llMiddle = (LinearLayout) findViewById(R.id.toolbar_middle_layout);
        tvMiddleType = (TextView) findViewById(R.id.toolbar_middle_find_txt);
        if (!TextUtils.isEmpty(typeName)) {
            tvMiddleType.setText(typeName);
        }
        ivMiddleArrow = (ImageView) findViewById(R.id.toolbar_middle_arrow);
        rlRight = (RelativeLayout) findViewById(R.id.toolbar_right_layout);
        tvCarCount = (TextView) findViewById(R.id.toolbar_right_count);
        ivLeft.setVisibility(View.VISIBLE);
        llMiddle.setVisibility(View.VISIBLE);
        rlRight.setVisibility(View.VISIBLE);
        rlSearch = (RelativeLayout) findViewById(R.id.rl_search);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        lineLeft = findViewById(R.id.line_left);
        lineRight = findViewById(R.id.line_right);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        viewPager.setOffscreenPageLimit(2);
        mFragments = new ArrayList<>();
        classFragment = NewCategoryClassFragment.getInstance(typeId,typeName);
        mFragments.add(classFragment);
        mFragments.add(new NewCategoryBrandFragment());
        mAdapter = new NewFragmentPagerAdapter(getSupportFragmentManager(), mFragments, mTitles);
        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void initEvents() {
        ivLeft.setOnClickListener(this);
        llMiddle.setOnClickListener(this);
        rlRight.setOnClickListener(this);
        rlSearch.setOnClickListener(this);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    lineLeft.setBackgroundColor(getResources().getColor(R.color.new_red_color));
                    lineRight.setBackgroundColor(getResources().getColor(R.color.new_white_color));
                } else if (tab.getPosition() == 1) {
                    lineRight.setBackgroundColor(getResources().getColor(R.color.new_red_color));
                    lineLeft.setBackgroundColor(getResources().getColor(R.color.new_white_color));
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
        viewPager.setCurrentItem(0);
        lineLeft.setBackgroundColor(getResources().getColor(R.color.new_red_color));
        getBrandsSortList(0);
    }

    private void initCarCount() {
        new RetrofitRequest<CarCountInfo>(ApiRequest.getApiShiji().getCarCount()).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            CarCountInfo info = (CarCountInfo) msg.obj;
                            tvCarCount.setText("" + info.getCount());
                            tvCarCount.setVisibility(View.VISIBLE);
                            if (info.getCount() == 0) {
                                tvCarCount.setVisibility(View.GONE);
                            }

                        } else {
                            tvCarCount.setVisibility(View.GONE);
                        }
                    }
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        initCarCount();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_left:
                //返回
                onBackPressed();
                break;
            case R.id.toolbar_middle_layout:
                showPopupWindow();
                break;
            case R.id.toolbar_right_layout:
                //跳转到购物车页面
                goToCarView();
                break;
            case R.id.rl_search:
                //跳转到搜索页面
                startActivity(new Intent(this, NewSearchActivity.class));
                break;
        }
    }

    /**
     * 按照名称排序品牌
     *
     * @param offset
     */
    private void getBrandsSortList(int offset) {
        new RetrofitRequest<BrandsSortObject>(ApiRequest.getApiShiji().getBrandsSortList(MapRequest
        .setMapTen(offset))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    BrandsSortObject obj = (BrandsSortObject) msg.obj;
                    if (obj == null || obj.list == null || obj.list.size() == 0) return;
                    mList = obj.list;
                    if (transforlistener != null) {
                        transforlistener.transfor(getSortList());
                    }
                }
            }
        });
    }

    private ArrayList<BrandsSortItem> getSortList() {
        ArrayList<BrandsSortItem> list = new ArrayList<>();
        ArrayMap<String, ArrayList<BrandsSortItem>> map = new ArrayMap<>();
        if (mList == null) return null;
        for (int i = 0; i < mList.size(); i++) {
            String value = mList.get(i).getName().toString().toUpperCase();
            String key = "";
            char c = value.charAt(0);
            if (c >= 'A' && c <= 'Z') {
                key = String.valueOf(c);
            } else {
                key = "#";
            }
            ArrayList<BrandsSortItem> temp = new ArrayList<>();
            if (map.containsKey(key)) {
                temp = map.get(key);
            }
            temp.add(mList.get(i));
            map.put(key, temp);
        }

        String[] letters = new String[]{"#", "A", "B", "C", "D", "E", "F", "G", "H"
                , "I", "J", "K", "L", "M", "N", "O", "P", "Q"
                , "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

        for (int i = 0; i < letters.length; i++) {
            String nkey = letters[i];
            BrandsSortItem section = new BrandsSortItem();
            section.setType(Item.SECTION);
            section.setName(nkey);
            section.setCn_name("");
            section.sectionPosition = 0;
            section.listPosition = i;
            list.add(section);
            if (map.containsKey(nkey)) {
                list.addAll(map.get(nkey));
            }
        }
        return list;
    }

    public interface TransforDataListener {
        void transfor(ArrayList<BrandsSortItem> list);
    }

    public void setTransforListener(TransforDataListener listener) {
        this.transforlistener = listener;
    }

    /**
     * 跳转至购物车
     */
    private void goToCarView() {
        Intent intentGoShopCart = new Intent(NewCategoryActivity.this, NewShoppingCartActivity.class);
        startActivityForResult(intentGoShopCart, CAR_REQUEST_CODE);
//        new RetrofitRequest<>(ApiRequest.getApiShiji().isLogin()).handRequest(new MsgCallBack() {
//            @Override
//            public void onResult(HttpMessage msg) {
//                if (msg.isSuccess()) {
//
//                } else if (msg.isLossLogin()) {
//                    Intent intent = new Intent(NewCategoryActivity.this, LoginActivity.class);
//                    startActivityForResult(intent, CAR_REQUEST_CODE);
//                    overridePendingTransition(R.anim.slide_in_top_buttom, R.anim.slide_out_top_buttom);
//                } else {
//                    showTips(msg.message);
//                }
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAR_REQUEST_CODE:
                    initCarCount();
                    break;
            }
        }
    }

    /*=================弹出一级分类popupwindow===============*/

    /**
     * 获取一级热门分类
     */
    private void getFirstCategory() {
        new RetrofitRequest<HotCategoryObject>(ApiRequest.getApiShiji().getHotCategoryFirst(String.valueOf(1)))
                .handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            HotCategoryObject obj = (HotCategoryObject) msg.obj;
                            if (obj == null || obj.list.size() == 0) return;
                            typeList = obj.list;
                            if (TextUtils.isEmpty(typeName)) {
                                for (int i = 0; i < typeList.size(); i++) {
                                    if (typeId == typeList.get(i).getId()) {
                                        typeName = typeList.get(i).getName();
                                        tvMiddleType.setText(typeName);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                });
    }

    private void getFirstCategoryFromSp() {
        String hotcategory = sp.getString("hotcategory", "");
        if (TextUtils.isEmpty(hotcategory)) {
            getFirstCategory();
        } else {
            HotCategoryObject obj = new Gson().fromJson(hotcategory, HotCategoryObject.class);
            if (obj == null || obj.list.size() == 0) {
                getFirstCategory();
            } else {
                typeList = obj.list;
            }
        }
    }

    private void showPopupWindow() {
        if (typeList == null) return;

        View view = LayoutInflater.from(this).inflate(R.layout.view_new_category_select, null);
        rvCategorySelect = (RecyclerView) view.findViewById(R.id.rv_category_select);
        rvCategorySelect.setLayoutManager(new FullyLinearLayoutManager(this));
        tempTypeList = new ArrayList<>();
        tempTypeList.addAll(typeList);
        selectAdapter = new NewCategorySelectAdapter(this, getTempFirstList(tempTypeList));
        rvCategorySelect.setAdapter(selectAdapter);

        initPopEvent();

        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = 0.4f;
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(null, ""));

        popupWindow.showAsDropDown(rlToolbar);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ViewPropertyAnimator.animate(ivMiddleArrow).cancel();
                ViewPropertyAnimator.animate(ivMiddleArrow).rotation(0).setDuration(300).start();
            }
        });

        ViewPropertyAnimator.animate(ivMiddleArrow).cancel();
        ViewPropertyAnimator.animate(ivMiddleArrow).rotation(180).setDuration(300).start();
        popupWindow.showAsDropDown(toolbar);
    }

    private void initPopEvent() {
        selectAdapter.setOnItemClickListener(new NewCategorySelectAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(HotCategoryObject.SecondItem item) {
                tvMiddleType.setText(item.getName());
                typeName = item.getName();
                typeId = item.getId();
                classFragment.typeChanged(typeId);
                ViewPropertyAnimator.animate(ivMiddleArrow).cancel();
                ViewPropertyAnimator.animate(ivMiddleArrow).rotation(180).setDuration(300).start();
                hidePopWindow();
                viewPager.setCurrentItem(0);
            }
        });
    }

    private List<HotCategoryObject.SecondItem> getTempFirstList(List<HotCategoryObject.SecondItem> list) {
        List<HotCategoryObject.SecondItem> tempList = list;
        for (int i = 0; i < tempList.size(); i++) {
            HotCategoryObject.SecondItem item = tempList.get(i);
            if (item.getId() == typeId) {
                tempList.remove(i);
                break;
            }
        }
        return tempList;
    }

    private void hidePopWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            WindowManager.LayoutParams lp = this.getWindow()
                    .getAttributes();
            lp.alpha = 1f;
            popupWindow.dismiss();
        }
    }
}
