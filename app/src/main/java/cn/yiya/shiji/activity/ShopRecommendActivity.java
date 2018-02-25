package cn.yiya.shiji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linearlistview.LinearListView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.ShopRecommendAdapter;
import cn.yiya.shiji.adapter.ShopRecommendHAdapter;
import cn.yiya.shiji.adapter.ShopRecommendHorAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MapRequest;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.MallGoodsDetailObject;
import cn.yiya.shiji.entity.goodes.NewGoodsItem;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.ProgressDialog;

/**
 * 店主推荐
 * Created by Amy on 2016/11/29.
 */

public class ShopRecommendActivity extends BaseAppCompatActivity implements View.OnClickListener, ShopRecommendHAdapter.OnClickListener {

    private ImageView ivBack;
    private TextView tvTitle;

    private TextView tvAddRecommend;
    private TextView tvNullRecommend;

    private RelativeLayout rlytTop;
    private LinearLayout llSmall;
    private HorizontalScrollView rvSmall;
    private ArrayList<NewGoodsItem> recommendList = new ArrayList<>();
//    private ShopRecommendHorAdapter largeAdapter, smallAdapter;
    private LinearListView llvSmall;
    private ShopRecommendHAdapter smallAdapter;

    private LinearLayout tvLargeMore, tvSmallMore;

    private LinearLayoutManager layoutManager;
    private RecyclerView rvRecommend;
    private ShopRecommendAdapter mAdapter;
    private ArrayList<NewGoodsItem> mList = new ArrayList<>();

    private int lastVisibleItemPosition;
    private boolean isBottom;

    private int firstVisibleItemPosition;

    private boolean isInit;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_recommend);
        initViews();
        initEvents();
        init();
    }

    @Override
    protected void initViews() {
        ivBack = (ImageView) findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText("店主推荐");
        findViewById(R.id.title_right).setVisibility(View.GONE);

        tvAddRecommend = (TextView) findViewById(R.id.tv_add_recommend);
        tvNullRecommend = (TextView) findViewById(R.id.tv_null_recommend);

        rlytTop = (RelativeLayout) findViewById(R.id.rlyt_top);
        llSmall = (LinearLayout) findViewById(R.id.ll_small);
        rvSmall = (HorizontalScrollView) findViewById(R.id.rv_small);
        llvSmall = (LinearListView) findViewById(R.id.horizontal_list_small);
        smallAdapter = new ShopRecommendHAdapter(this, 2);
        llvSmall.setAdapter(smallAdapter);
        tvSmallMore = (LinearLayout) findViewById(R.id.tv_more_small);

        rvRecommend = (RecyclerView) findViewById(R.id.rv_recommend);
        layoutManager = new LinearLayoutManager(this);
        rvRecommend.setLayoutManager(layoutManager);
        mAdapter = new ShopRecommendAdapter(this);
        rvRecommend.setAdapter(mAdapter);
    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        tvAddRecommend.setOnClickListener(this);
        smallAdapter.setOnClickListener(this);
        tvSmallMore.setOnClickListener(this);

        rvRecommend.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 获取适配器的Item个数以及最后可见的Item
                int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                // 如果最后可见的Item比总数小1，表示最后一个，预加载的意思
                if ((visibleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE
                        && (lastVisibleItemPosition) == totalItemCount - 1) && !isBottom) {

                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if(recommendList.size() > 0) {
                    if (firstVisibleItemPosition > 0) {
                        rlytTop.setVisibility(View.VISIBLE);
                    } else {
                        rlytTop.setVisibility(View.GONE);
                    }
                }
            }
        });

        mAdapter.setOnLargeClickListener(new ShopRecommendAdapter.OnLargeClickListener() {
            @Override
            public void onLargeClick(int position) {
                delete(position);
            }
        });
    }

    @Override
    protected void init() {
//        getMyRecommendedGoods();
    }

    /**
     * 获取精选商品推荐列表
     */
    private void getGoodsList() {
        new RetrofitRequest<MallGoodsDetailObject>(ApiRequest.getApiShiji().getShopkeeperRecommend()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    MallGoodsDetailObject object = (MallGoodsDetailObject) msg.obj;
//                    if (object.getList() != null && object.getList().size() > 0) {
//                        mList.addAll(object.getList());
                        mAdapter.setmList(object.getList());
                        mAdapter.setmTopList(recommendList);
                        mAdapter.notifyDataSetChanged();
//                    }
                    isInit = true;
                }
            }
        });
    }

    /**
     * 获取已推荐
     */
    private void getMyRecommendedGoods() {

        new RetrofitRequest<MallGoodsDetailObject>(ApiRequest.getApiShiji().getRecommendGoods(MapRequest.setMapTwenty(0)))
                .handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            MallGoodsDetailObject object = (MallGoodsDetailObject) msg.obj;
                            if (object.getList() != null && object.getList().size() > 0) {
                                recommendList = object.getList();
                                smallAdapter.setmList(recommendList);
                                smallAdapter.notifyDataSetChanged();
                                setHaveRecommended();

                            } else {
                                setNullRecommended();
                            }
                        } else {
                            setNullRecommended();
                        }
                        getGoodsList();
                    }
                });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.tv_add_recommend:
                Intent intent = new Intent(this, NewSearchActivity.class);
                intent.putExtra("isRecommend", true);
                startActivity(intent);
                break;
//            case R.id.tv_more_large:
//                gotoMore();
//                break;
            case R.id.tv_more_small:
                gotoMore();
                break;
        }
    }

    private void gotoMore(){
        Intent intent = new Intent(this, SellerRecommendedActivity.class);
        startActivity(intent);
    }

    private void setNullRecommended() {
        recommendList = new ArrayList<>();
        tvNullRecommend.setVisibility(View.VISIBLE);
        rlytTop.setVisibility(View.GONE);
    }

    private void setHaveRecommended() {
        tvNullRecommend.setVisibility(View.GONE);
//        if(isInit){
//            if(firstVisibleItemPosition > 1) {
//                rlytTop.setVisibility(View.VISIBLE);
//            }else {
//                rlytTop.setVisibility(View.GONE);
//                layoutManager.scrollToPositionWithOffset(0, 0);
//            }
//        }else {
//            rlytTop.setVisibility(View.GONE);
//        }
    }

    /**
     * 删除已推荐
     * @param position
     */
    @Override
    public void delete(final int position) {
        Util.showCustomDialog(this, "确认删除该推荐商品？", new ProgressDialog.DialogClick() {
            @Override
            public void OkClick() {
                showPreDialog("正在删除");
                new RetrofitRequest<>(ApiRequest.getApiShiji().getRecommendGoodsEdit(MapRequest.setRecommendGoodsAddMap(recommendList.get(position).getId(), 2)))
                        .handRequest(new MsgCallBack() {
                            @Override
                            public void onResult(HttpMessage msg) {
                                if (msg.isSuccess()) {
                                    hidePreDialog();
                                    getMyRecommendedGoods();
//                                    getGoodsList();
//                                    mAdapter.notifyHotDelete(recommendList.get(position).getId());
//                                    mAdapter.notifyDelete(recommendList.get(position).getId());
//                                    mAdapter.notifyTopAdapter(layoutManager.getChildAt(0));
                                } else {
                                    Util.toast(ShopRecommendActivity.this, "删除失败", true);
                                }
                            }
                        });
            }

            @Override
            public void CancelClick() {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMyRecommendedGoods();
//        if(isInit) {
//            getGoodsList();
////            mAdapter.notifyHotAdapter();
////            mAdapter.notifyTopAdapter(layoutManager.getChildAt(0));
//        }
    }
}
