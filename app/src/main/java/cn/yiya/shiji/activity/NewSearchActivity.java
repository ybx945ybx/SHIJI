package cn.yiya.shiji.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.NewRecordAdapter;
import cn.yiya.shiji.adapter.NewSearchAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.entity.HtmlThematicInfo;
import cn.yiya.shiji.entity.SearchHotWords;
import cn.yiya.shiji.entity.SearchRecordObject;
import cn.yiya.shiji.entity.search.NewSearchEntity;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.AllListView;

/**搜索页面
 * Created by Amy on 16/6/19.
 */
public class NewSearchActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private Handler mHandler;
    private SharedPreferences sp;

    private RelativeLayout rlytSearch;
    private EditText tvSearch;
    private ImageView ivClean;
    private TextView tvCancel;
    private TagFlowLayout tflSearchTips;
    private AllListView lvRecord;
    private TextView tvRecord;
    private Button btnCleanRecord;
    private ScrollView svScroll;
    private LinearLayout llWords;

    //搜索记录
    private SearchRecordObject searchRecordObject = new SearchRecordObject();
    private List<String> recordList = new ArrayList<>();
    private NewRecordAdapter mRecordAdapter;

    //热门搜索
    private SearchHotWords hotWords;
    private String[] words;
    private TagAdapter tagAdapter;

    //弹出搜索结果
    private RecyclerView rycvSearch;
    private NewSearchAdapter mSearchAdapter;

    private boolean isRecommend;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_search);
        initIntent();
        mHandler = new Handler();
        sp = this.getSharedPreferences(Configration.SHAREDPREFERENCE, MODE_PRIVATE);
        initViews();
        initEvents();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if(intent != null){
            isRecommend = intent.getBooleanExtra("isRecommend", false);
        }
    }

    @Override
    protected void initViews() {
        rlytSearch = (RelativeLayout) findViewById(R.id.rlyt_search);
        tvSearch = (EditText) findViewById(R.id.tv_search);
        ivClean = (ImageView) findViewById(R.id.iv_clean);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);

        tflSearchTips = (TagFlowLayout) findViewById(R.id.tfl_search_tips);
        lvRecord = (AllListView) findViewById(R.id.lv_record);
        tvRecord = (TextView) findViewById(R.id.tv_record);
        btnCleanRecord = (Button) findViewById(R.id.btn_clean_record);

        svScroll = (ScrollView) findViewById(R.id.sv_scroll);
        llWords = (LinearLayout) findViewById(R.id.ll_words);
        rycvSearch = (RecyclerView) findViewById(R.id.rycv_search);
        rycvSearch.setItemAnimator(new DefaultItemAnimator());
        rycvSearch.setLayoutManager(new LinearLayoutManager(this));
        mSearchAdapter = new NewSearchAdapter(this);
        rycvSearch.setAdapter(mSearchAdapter);
    }

    @Override
    protected void initEvents() {
        ivClean.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        btnCleanRecord.setOnClickListener(this);

        //搜索历史item点击事件
        lvRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addRecord(mRecordAdapter.getItem(position).toString());
                Intent intent = new Intent();
                intent.setClass(NewSearchActivity.this, NewGoodsListActivity.class);
                intent.putExtra("word", mRecordAdapter.getItem(position).toString());
                intent.putExtra("source", 1000);
                intent.putExtra("isRecommend", isRecommend);
                startActivity(intent);
            }
        });

        //热门搜索 tag点击事件
        tflSearchTips.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                addRecord(words[position]);
                Intent intent = new Intent();
                intent.setClass(NewSearchActivity.this, NewGoodsListActivity.class);
                intent.putExtra("word", words[position]);
                intent.putExtra("source", 1000);
                intent.putExtra("isRecommend", isRecommend);
                startActivity(intent);
                return false;
            }
        });

        //搜索框
        tvSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (TextUtils.isEmpty(s.toString())) {
                    ivClean.setVisibility(View.GONE);
                    llWords.setVisibility(View.GONE);
                    svScroll.setVisibility(View.VISIBLE);
                } else {
                    ivClean.setVisibility(View.VISIBLE);
                    llWords.setVisibility(View.VISIBLE);
                    svScroll.setVisibility(View.GONE);
                }

                getSearchData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //搜索商品和品牌
        tvSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {                                       // 点击回车键发送信息
                    addRecord(tvSearch.getText().toString());
                    Intent intent = new Intent();
                    intent.setClass(NewSearchActivity.this, NewGoodsListActivity.class);
                    intent.putExtra("word", tvSearch.getText().toString());
                    intent.putExtra("source", 1000);
                    intent.putExtra("isRecommend", isRecommend);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        mSearchAdapter.setOnItemClickListen(new NewSearchAdapter.OnItemClickListener() {
            @Override
            public void OnActItemClick(NewSearchEntity.ShopActListEntity shopActListEntity) {
//                addRecord(shopActListEntity.)
                switch (shopActListEntity.getType()) {
                    case 1:           // 活动
                        Intent intent = new Intent(NewSearchActivity.this, HomeIssueActivity.class);
                        intent.putExtra("activityId", shopActListEntity.getId() + "");
                        intent.putExtra("menuId", 7);
                        intent.putExtra("isRecommend", isRecommend);
                        startActivity(intent);
                        break;
                    case 2:           // 专题
                        Intent intent1 = new Intent(NewSearchActivity.this, NewLocalWebActivity.class);
                        HtmlThematicInfo info = new HtmlThematicInfo();
                        info.setItemId(shopActListEntity.getId() + "");
                        info.setTypeId("3");
                        String dataJson = new Gson().toJson(info);
                        intent1.putExtra("data", dataJson);
                        intent1.putExtra("url", "http://h5.qnmami.com/app/homeActivities/html/index.html");
                        intent1.putExtra("type", shopActListEntity.getType());
                        startActivity(intent1);
                        break;
                    case 3:           // h5分享
                        Intent intent2 = new Intent(NewSearchActivity.this, NewLocalWebActivity.class);
                        intent2.putExtra("url", shopActListEntity.getUrl());
                        intent2.putExtra("type", shopActListEntity.getType());
                        intent2.putExtra("title", shopActListEntity.getTitle());
                        intent2.putExtra("data", new Gson().toJson(shopActListEntity));
                        startActivity(intent2);
                        break;
                }

            }

            @Override
            public void OnBrandItemClick(NewSearchEntity.BrandListEntity brandListEntity) {
                addRecord(brandListEntity.getName());
                Intent intent = new Intent(NewSearchActivity.this, NewSingleBrandActivity.class);
                intent.putExtra("brand_id", brandListEntity.getId());
                intent.putExtra("isRecommend", isRecommend);
                startActivity(intent);
            }

            @Override
            public void OnSiteItemClick(NewSearchEntity.SiteListEntity siteListEntity) {
                addRecord(siteListEntity.getName());
                Intent intent = new Intent(NewSearchActivity.this, NewMallGoodsActivity.class);
                intent.putExtra("id", siteListEntity.getId());
                intent.putExtra("isRecommend", isRecommend);
                startActivity(intent);

            }

            @Override
            public void OnCategroyItemClick(NewSearchEntity.GoodsTypeListEntity goodsTypeListEntity) {
                addRecord(goodsTypeListEntity.getName());
                Intent intent = new Intent(NewSearchActivity.this, NewGoodsListActivity.class);
                intent.putExtra("id", 0);
                intent.putExtra("categoryid", String.valueOf(goodsTypeListEntity.getId()));
                intent.putExtra("title", goodsTypeListEntity.getName());
                intent.putExtra("source", 1002);
                intent.putExtra("isRecommend", isRecommend);
                startActivity(intent);

            }
        });
    }

    private void getSearchData(String s) {
        new RetrofitRequest<NewSearchEntity>(ApiRequest.getApiShiji().getSearchAuxiliary(s)).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    NewSearchEntity entity = (NewSearchEntity) msg.obj;
                    if (entity != null) {
                        mSearchAdapter.setSearchEntity(entity);
                        mSearchAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }

    @Override
    protected void init() {
        initSearchData();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //清除搜索框中输入的内容
            case R.id.iv_clean:
                tvSearch.setText("");
                break;
            //取消
            case R.id.tv_cancel:
                onBackPressed();
                break;
            //清除历史记录
            case R.id.btn_clean_record:
                recordList.clear();
                mRecordAdapter.notifyDataSetChanged();
                Util.cleanLocalSearchRecord(mHandler, sp);
                btnCleanRecord.setVisibility(View.GONE);
                tvRecord.setVisibility(View.VISIBLE);
                break;
            default:
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        llWords.setVisibility(View.GONE);
        svScroll.setVisibility(View.VISIBLE);
        tvSearch.setText("");
        initSearchData();
    }

    /**
     * 初始化搜索数据
     */
    private void initSearchData() {
        getSearchHotWords();
        hasHistory();
    }

    /**
     * 获取热门搜索列表
     */
    private void getSearchHotWords() {
        new RetrofitRequest<SearchHotWords>(ApiRequest.getApiShiji().getHotSearch()).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            hotWords = (SearchHotWords) msg.obj;
                            words = hotWords.getWords();
                            tagAdapter = new TagAdapter(words) {
                                @Override
                                public View getView(FlowLayout parent, int position, Object o) {
                                    TextView tvHotWords = (TextView) LayoutInflater.from(NewSearchActivity.this).inflate(R.layout.search_words_item, tflSearchTips, false);
                                    tvHotWords.setText(words[position]);
                                    return tvHotWords;
                                }
                            };
                            tflSearchTips.setAdapter(tagAdapter);
                        }
                    }
                }
        );
    }


    /**
     * 搜索记录
     */
    private void hasHistory() {
        searchRecordObject = new Gson().fromJson(sp.getString("record", ""), SearchRecordObject.class);
        if (searchRecordObject != null) {
            recordList = searchRecordObject.getList();
            //取最近5条搜索记录
            Collections.reverse(recordList);
            if (recordList.size() > 5) {
                recordList = recordList.subList(0, 5);
            }
            //搜索记录适配
            mRecordAdapter = new NewRecordAdapter(recordList, this);
            lvRecord.setAdapter(mRecordAdapter);
            tvRecord.setVisibility(View.GONE);                                             // 取消无记录提示
            btnCleanRecord.setVisibility(View.VISIBLE);                                    // 清除记录提示
        } else {
            tvRecord.setVisibility(View.VISIBLE);                                          // 显示无记录提示
            btnCleanRecord.setVisibility(View.GONE);                                       // 取消清除记录提示
        }
    }


    /**
     * 保存搜索记录到本地并跳转到商品品牌详情页
     *
     * @param word
     * @return
     */
    private boolean addRecord(String word) {
        if (TextUtils.isEmpty(word)) {
            Toast.makeText(this, "搜索不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        ArrayList<String> mList = new ArrayList<>();
        if ((new Gson().fromJson(sp.getString("record", ""), SearchRecordObject.class)) != null) {
            mList = (new Gson().fromJson(sp.getString("record", ""), SearchRecordObject.class)).getList();
            for (int i = 0; i < mList.size(); i++) {
                if (word.equals(mList.get(i).toString())) {
                    mList.remove(i);
                }
            }
        }
        //保存新搜索至本地
        mList.add(word);
        searchRecordObject = new SearchRecordObject();
        searchRecordObject.setList(mList);
        String json = new Gson().toJson(searchRecordObject);
        Util.saveLocalSearchRecord(mHandler, sp, json);

        return true;
    }

    @Override public void onBackPressed() {
        finish();
        hideSoftInput();
    }

    //收起软键盘
    private void hideSoftInput(){
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(tvSearch.getWindowToken(),0);
    }
}
