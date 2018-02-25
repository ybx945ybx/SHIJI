package cn.yiya.shiji.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.HashMap;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.CommunityMessageAdapter;
import cn.yiya.shiji.adapter.WishListAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MapRequest;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.NotifyListObject;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.views.MySwipeMenuListview;

/**
 * Created by Tom on 2016/8/17.
 */
public class CommunityMessageActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private ImageView ivBack;
    private TextView tvTitle;
    private TextView tvRight;

    private RecyclerView rycvCommunity;
    private CommunityMessageAdapter mAdapter;

    private int nOffset;
    private boolean isBottom;
    private int lastVisibleItemPosition;

    private int type;       // 1是未读消息 2是历史消息


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_message);
        initIntent();
        initViews();
        initEvents();
        init();
    }

    private void initIntent() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
    }

    @Override
    protected void initViews() {
        ivBack = (ImageView)findViewById(R.id.title_back);
        tvTitle = (TextView)findViewById(R.id.title_txt);
        tvRight = (TextView) findViewById(R.id.title_right);
        if(type == 1) {
            tvTitle.setText("社区消息");
            tvRight.setText("历史消息");
            tvRight.setOnClickListener(this);
        }else {
            tvTitle.setText("历史消息");
            tvRight.setVisibility(View.GONE);
        }

        rycvCommunity = (RecyclerView) findViewById(R.id.rycv_community_message);
        rycvCommunity.setItemAnimator(new DefaultItemAnimator());
        rycvCommunity.setLayoutManager(new LinearLayoutManager(this));
//        SwipeMenuCreator delCreator = new SwipeMenuCreator() {
//            @Override
//            public void create(SwipeMenu menu) {
//                SwipeMenuItem deletItem = new SwipeMenuItem(CommunityMessageActivity.this);
//                deletItem.setBackground(Color.parseColor("#fe3824"));
//                deletItem.setTitle("删除");
//                deletItem.setTitleColor(Color.parseColor("#ffffff"));
//                deletItem.setTitleSize(15);
//                deletItem.setWidth(SimpleUtils.dp2px(CommunityMessageActivity.this, 74));
//                menu.addMenuItem(deletItem);
//            }
//        };
//        mySwipeMenuListview.setMenuCreator(delCreator);

        mAdapter = new CommunityMessageAdapter(this);
        rycvCommunity.setAdapter(mAdapter);

        addDefaultNullView();
        initDefaultNullView(R.mipmap.zanwubiaoqian, "暂无消息", this);

    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        rycvCommunity.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //获取适配器的Item个数以及最后可见的Item
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int visivleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                //如果最后可见的item比总数小1，则表示最后一个，这里小3，预加载的意思
                if ((visivleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE &&
                        (lastVisibleItemPosition) >= totalItemCount - 20) && !isBottom) {
                    loardMore();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                lastVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
            }
        });
//        mySwipeMenuListview.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
//                deleteMsg(position, mAdapter.getmList().get(position).getId());
//                return false;
//            }
//        });
    }

    @Override
    protected void init() {
        getMessageList();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.tv_reload:
                getMessageList();
                break;
            case R.id.title_right:
                Intent intent = new Intent(this, CommunityMessageActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);
                break;
        }

    }

    private void getMessageList() {
        nOffset = 0;
        isBottom = false;
        HashMap<String, String> maps = setMap();
        showPreDialog("");
        new RetrofitRequest<NotifyListObject>(ApiRequest.getApiShiji().getNotifyCommunityList(maps)).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                hidePreDialog();
                if(msg.isSuccess()) {
                    NotifyListObject info = (NotifyListObject) msg.obj;
                    if(info.list != null && info.list.size()>0){
                        mAdapter.setmList(info.list);
                        mAdapter.notifyDataSetChanged();
                        setSuccessView(rycvCommunity);
                    }else {
                        isBottom = true;
                        setNullView(rycvCommunity);
                    }
                }else {
                    if(!NetUtil.IsInNetwork(CommunityMessageActivity.this)){
                        setOffNetView(rycvCommunity);
                    }
                }
            }
        });
    }

    private void loardMore() {
        nOffset += 20;
        isBottom = false;
        HashMap<String, String> maps = setMap();
        new RetrofitRequest<NotifyListObject>(ApiRequest.getApiShiji().getNotifyCommunityList(maps)).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if(msg.isSuccess()) {
                    NotifyListObject info = (NotifyListObject) msg.obj;
                    if(info.list != null && info.list.size()>0){
                        mAdapter.getmList().addAll(info.list);
                        mAdapter.notifyDataSetChanged();
                    }else {
                        isBottom = true;
                    }
                }
            }
        });

    }

    private HashMap setMap(){
        HashMap<String, String> maps = new HashMap<>();
        maps.put("offset", String.valueOf(nOffset));
        maps.put("limit", String.valueOf(20));
        if(type == 1) {
            maps.put("read", String.valueOf(2));
        }else if(type == 2) {
            maps.put("read", String.valueOf(3));
        }
        return maps;
    }

//    private void deleteMsg(final int positon, int id) {
//        new RetrofitRequest<>(ApiRequest.getApiShiji().deleteNotify(String.valueOf(id))).handRequest(new MsgCallBack() {
//            @Override
//            public void onResult(HttpMessage msg) {
//                if (msg.isSuccess()) {
//                    showTips("删除成功");
//                    mAdapter.getmList().remove(positon);
//                    mAdapter.notifyDataSetChanged();
//
//                }
//            }
//        });
//    }
}
