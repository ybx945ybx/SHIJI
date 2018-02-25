package cn.yiya.shiji.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.MallSearchAdapter;
import cn.yiya.shiji.adapter.MallsListAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.CarCountInfo;
import cn.yiya.shiji.entity.HotMallSortItem;
import cn.yiya.shiji.entity.HotMallSortObject;
import cn.yiya.shiji.entity.Item;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.ClearEditText;
import cn.yiya.shiji.views.SideBar;

/**
 * Created by jerryzhang on 2015/9/8.
 * 商城列表
 */
public class MallListActivity extends ListActivity implements View.OnClickListener {

    private SideBar sideBar;
    private MallsListAdapter adapter;
    private TextView tvCancle;
    private ImageView ivBack;
    private RelativeLayout rlytShopCart;
    private TextView tvCart;

    private TextView tvTitle;
    private boolean bShowOrder;                                                                     // 表示是否晒单
    //2015-09-16
    private Handler mHandler;
    private ArrayList<HotMallSortItem> list = new ArrayList<>();
    private ArrayList<HotMallSortItem> mList = new ArrayList<>();


    // 2015-12-03  添加搜索框
    private ListView mlistview;
    private MallSearchAdapter searchAdapter;
    private ClearEditText mClearEditText;
    private FrameLayout frameLayout;
    private String[] arr = new String['Z' - 'A' + 2];
    private static final int REQUEST_CODE_GOODS = 400;
    private boolean isLogin;
    private static final int REQUEST_LOGIN = 1001;

    public static final int MIN_CLICK_DELAY_TIME = 2000;
    public long lastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_malls_list);

        bShowOrder = getIntent().getBooleanExtra("show", false);

        mlistview = (ListView) findViewById(R.id.listview);
        mClearEditText = (ClearEditText) findViewById(R.id.title_txt);
        mClearEditText.clearFocus();
        frameLayout = (FrameLayout) findViewById(R.id.mall_framelayout);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        rlytShopCart = (RelativeLayout) findViewById(R.id.toolbar_right_layout);
        tvCart = (TextView) findViewById(R.id.toolbar_right_count);
        tvCancle = (TextView) findViewById(R.id.tv_cancel);
        tvCancle.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        rlytShopCart.setOnClickListener(this);
        getListView().setFastScrollEnabled(false);
        mHandler = new Handler();
        getMallSort();
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        sideBar.setVisibility(View.INVISIBLE);
        if (bShowOrder) {
            rlytShopCart.setVisibility(View.GONE);
        }

        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                if (adapter != null) {
                    int position = adapter.getPositionForSection(s.charAt(0));
                    if (position != -1) {
                        setSelection(position);
                    }
                }
            }
        });

        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HotMallSortItem item = (HotMallSortItem) parent.getItemAtPosition(position);

                Intent intent = new Intent(MallListActivity.this, NewMallGoodsActivity.class);
                intent.putExtra("id", item.getId());
                intent.putExtra("show", bShowOrder);
                startActivityForResult(intent, REQUEST_CODE_GOODS);
            }
        });

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HotMallSortItem item = (HotMallSortItem) parent.getItemAtPosition(position);
                if (TextUtils.isEmpty(item.getLogo())) {
                    return;
                }
                Intent intent = new Intent(MallListActivity.this, NewMallGoodsActivity.class);
                intent.putExtra("id", item.getId());
                intent.putExtra("show", bShowOrder);
                startActivityForResult(intent, REQUEST_CODE_GOODS);
            }
        });

        //2015-09-16
        getListView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Util.hintIMETool(MallListActivity.this, v);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        searchAdapter = new MallSearchAdapter(this);
        mlistview.setAdapter(searchAdapter);
        mlistview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Util.hintIMETool(MallListActivity.this, v);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        mClearEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mlistview.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.INVISIBLE);
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
                    rlytShopCart.setVisibility(bShowOrder ? View.GONE : View.VISIBLE);
                }
            }
        });

//        initCarCount();
    }

//    @Override
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//        HotMallSortItem item = (HotMallSortItem) getListView().getAdapter().getItem(position);
//
//        if (item != null) {
//            Intent intent = new Intent();
//            intent.setClass(this, ClassDetailsActivity.class);
//            startActivity(intent);
//        }
//    }

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                mClearEditText.clearFocus();
                hideSoftInput();
                mClearEditText.setText("");
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.toolbar_right_layout:
                gotoShoppingCart();
                break;
        }
    }

    //收起软键盘
    private void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mClearEditText.getWindowToken(), 0);
    }

    private void gotoShoppingCart() {
        if (isEffectClick()) {
            Intent intentGoShopCart = new Intent(MallListActivity.this, NewShoppingCartActivity.class);
            startActivity(intentGoShopCart);
//            if (isLogin) {
//
//            } else {
//                Intent intent = new Intent(MallListActivity.this, LoginActivity.class);
//                startActivityForResult(intent, REQUEST_LOGIN);
//                overridePendingTransition(R.anim.slide_in_top_buttom, R.anim.slide_out_top_buttom);
//            }
        }
    }

    // 按钮连续两次点击在2秒内认为是一次点击，多次的点击被视为无效
    public boolean isEffectClick() {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if ((currentTime - lastClickTime) > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            return true;
        }
        return false;
    }

    public void generateDataset(char from, char to) {

        final int sectionsNumber = to - from + 2;

        int sectionPosition = 0, listPosition = 0;
        boolean hasIndex = false;
        for (char i = 0; i < sectionsNumber; i++) {
            hasIndex = false;
            HotMallSortItem section = new HotMallSortItem();
            section.setType(Item.SECTION);
            section.setTitle(String.valueOf((char) ('A' + i)));
            section.sectionPosition = sectionPosition;
            section.listPosition = listPosition++;
            mList.add(section);
            if (('A' + i) > 'Z') {
                section.setType(Item.SECTION);
                section.setTitle("#");
            }
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).getTitle().toUpperCase(Locale.ENGLISH).charAt(0) == 'A' + i) {
                    mList.add(list.get(j));
                    hasIndex = true;
                } else if ((list.get(j).getTitle().toUpperCase(Locale.ENGLISH).charAt(0) < 'A') ||
                        (list.get(j).getTitle().toUpperCase(Locale.ENGLISH).charAt(0) > 'Z')) {
                    if (('A' + i) > 'Z') {
                        mList.add(list.get(j));
                        hasIndex = true;
                    }

                }
            }
            if (!hasIndex) {
                mList.remove(section);
            } else {
                arr[i] = (section.getTitle());
            }
            sectionPosition++;
        }
        SideBar.setStringArr(arr);
        sideBar.invalidate();
        sideBar.setVisibility(View.VISIBLE);
    }

    /**
     * 新增四期接口
     * 获取商城列表数据
     * jerryzhang
     * 2015-09-17
     */
    public void getMallSort() {
        new RetrofitRequest<HotMallSortObject>(ApiRequest.getApiShiji().getHotMallInfo()).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        HotMallSortObject item = (HotMallSortObject) msg.obj;
                        if (item != null) {
                            list = item.list;
                        } else {
                            return;
                        }
                        generateDataset('A', 'Z');
                        adapter = new MallsListAdapter(MallListActivity.this, mList);
                        setListAdapter(adapter);
                        if (item.list != null && item.list.size() > 0) {
                            MallListActivity activity = new MallListActivity();
                            activity.setList(item.list);
                        }
                    }
                }
        );
    }

    //2015-09-16
    public void setList(ArrayList<HotMallSortItem> list) {
        this.list = list;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void filterData(String filterStr) {
        ArrayList<HotMallSortItem> nlist = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            mlistview.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);
        } else {
            nlist.clear();
            for (HotMallSortItem mHotMallSortItem : list) {
                String cn_name = mHotMallSortItem.getTitle();
                if ((cn_name.toUpperCase()).indexOf((filterStr.toString()).toUpperCase()) != -1 ||
                        cn_name.indexOf(filterStr.toString()) != -1) {
                    nlist.add(mHotMallSortItem);
                } else {
                }
            }
        }
        searchAdapter.updateListView(nlist);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_GOODS) {
                if (data != null) {
                    Intent intent = new Intent();
                    String goodes = data.getStringExtra("goodes");
                    intent.putExtra("goodes", goodes);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            } else if (resultCode == RESULT_OK) {
                if (requestCode == REQUEST_LOGIN) {
                    initCarCount();
                }
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == REQUEST_CODE_GOODS) {
                if(bShowOrder) {
                    onBackPressed();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initCarCount();
    }
}
