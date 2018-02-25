package cn.yiya.shiji.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.ForwardedGoodsAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MapRequest;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.MallGoodsDetailObject;
import cn.yiya.shiji.utils.NetUtil;

/**
 * Created by Tom on 2016/12/5.
 */

public class SearchForwardedGoodsActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private TextView tvCancle;
    private EditText etSearch;

    private RecyclerView rycvForwarded;
    private ForwardedGoodsAdapter mAdapter;

    private int nOffset;
    private int lastVisibleItemPosition;
    private boolean isBottom;
    private String word;
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_forwarded_goods);
        initViews();
        initEvents();
        init();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancel:
                onBackPressed();
                break;
            case R.id.tv_reload:
                searchGoods(etSearch.getText().toString());
                break;
        }
    }

    @Override
    protected void initViews() {
        tvCancle = (TextView) findViewById(R.id.tv_cancel);
        etSearch = (EditText) findViewById(R.id.tv_search); 
        
        rycvForwarded = (RecyclerView) findViewById(R.id.rycv_forwarded);
        rycvForwarded.setItemAnimator(new DefaultItemAnimator());
        rycvForwarded.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ForwardedGoodsAdapter(this);
        rycvForwarded.setAdapter(mAdapter);

        addDefaultNullView();
        initDefaultNullView(R.mipmap.zanwusousuojieguo, "暂无结果", this);
    }

    @Override
    protected void initEvents() {
        tvCancle.setOnClickListener(this);
        rycvForwarded.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 获取适配器的Item个数以及最后可见的Item
                int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                // 如果最后可见的Item比总数小1，表示最后一个，预加载的意思
                if ((visibleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE
                        && (lastVisibleItemPosition) == totalItemCount - 1) && !isBottom) {
                    loadMore();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItemPosition = ((LinearLayoutManager) (recyclerView.getLayoutManager())).findLastVisibleItemPosition();
            }
        });
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
                    searchGoods(etSearch.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    // 查询搜索结果
    private void searchGoods(String s) {
        if(TextUtils.isEmpty(s)){
            showTips("请输入搜索内容");
            return;
        }
        // // TODO: 2016/12/5
        word = etSearch.getText().toString();
        getGoodsList();
        hideSoftInput();
    }

    private void getGoodsList() {
        nOffset = 0;
        isBottom = false;
        new RetrofitRequest<MallGoodsDetailObject>(ApiRequest.getApiShiji().getForwardSearchGoods(MapRequest.setForwardSearchMap(word, nOffset))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    MallGoodsDetailObject object = (MallGoodsDetailObject) msg.obj;
                    if (object.getList() != null && object.getList().size() > 0) {
                        mAdapter.setmList(object.getList());
                        mAdapter.notifyDataSetChanged();
                        setSuccessView(rycvForwarded);
                        isBottom = false;
                    }else {
                        setNullView(rycvForwarded);
                        isBottom = true;
                    }
                }else {
                    if(!NetUtil.IsInNetwork(SearchForwardedGoodsActivity.this)) {
                        setOffNetView(rycvForwarded);
                    }
                }
            }
        });
    }

    private void loadMore() {
        nOffset += 20;
        new RetrofitRequest<MallGoodsDetailObject>(ApiRequest.getApiShiji().getForwardSearchGoods(MapRequest.setForwardSearchMap(word, nOffset))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    MallGoodsDetailObject object = (MallGoodsDetailObject) msg.obj;
                    if (object.getList() != null && object.getList().size() > 0) {
                        mAdapter.setmList(object.getList());
                        mAdapter.notifyDataSetChanged();
                        setSuccessView(rycvForwarded);
                        isBottom = false;
                    }else {
                        isBottom = true;
                    }
                }else {
                    if(!NetUtil.IsInNetwork(SearchForwardedGoodsActivity.this)) {
                        setOffNetView(rycvForwarded);
                    }
                }
            }
        });
    }

    //收起软键盘
    private void hideSoftInput(){
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etSearch.getWindowToken(),0);
    }

    @Override
    protected void init() {

    }
}
