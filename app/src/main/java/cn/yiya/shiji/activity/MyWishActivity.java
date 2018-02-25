package cn.yiya.shiji.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.WishListAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.MallGoodsDetailObject;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.ProgressDialog;
import cn.yiya.shiji.views.SwipeRecyclerView;

/**
 * Created by Tom on 2016/8/17.
 */
public class MyWishActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private ImageView ivBack;
    private TextView tvTitle;

    private SwipeRecyclerView mySwipeMenuListview;
    private WishListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wish);
        initViews();
        initEvents();
        init();
    }

    @Override
    protected void initViews() {
        ivBack = (ImageView) findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText("收藏夹");
        findViewById(R.id.title_right).setVisibility(View.GONE);

        mySwipeMenuListview = (SwipeRecyclerView) findViewById(R.id.wish_list);
        mySwipeMenuListview.setItemAnimator(new DefaultItemAnimator());
        mySwipeMenuListview.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new WishListAdapter(this);
        mySwipeMenuListview.setAdapter(mAdapter);

        addDefaultNullView();
        initDefaultNullView(R.mipmap.zanwushoucang, "暂无收藏", this);

    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        mAdapter.setOnDeleteListener(new WishListAdapter.OnDeleteListener() {
            @Override
            public void OnDelete(final int position) {
                deleteWish(mAdapter.getmList().get(position).getId(), position);
            }
        });
    }

    // 删除收藏
    private void deleteWish(String goods_id, final int position) {
        new RetrofitRequest<>(ApiRequest.getApiShiji().deleteWishGoods(goods_id)).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    mAdapter.getmList().remove(position);
                    mAdapter.notifyItemRemoved(position);
                    if (mAdapter.getmList().size() > 0) {
                        mAdapter.notifyItemRangeChanged(position, mAdapter.getmList().size());
                    }
                    if (mAdapter.getmList().size() == 0) {
                        setNullView(mySwipeMenuListview);
                    }
                }
            }
        });
    }

    @Override
    protected void init() {
//        getWishList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWishList();
    }

    private void getWishList() {
        new RetrofitRequest<MallGoodsDetailObject>(ApiRequest.getApiShiji().getWishGoods()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    MallGoodsDetailObject object = (MallGoodsDetailObject) msg.obj;
                    if (object.getList() != null && object.getList().size() > 0) {
                        mAdapter.setmList(object.getList());
                        mAdapter.notifyDataSetChanged();
                        setSuccessView(mySwipeMenuListview);
                    } else {
                        setNullView(mySwipeMenuListview);
                    }
                } else {
                    if (!NetUtil.IsInNetwork(MyWishActivity.this)) {
                        setOffNetView(mySwipeMenuListview);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.tv_reload:
                getWishList();
                break;
        }
    }
}
