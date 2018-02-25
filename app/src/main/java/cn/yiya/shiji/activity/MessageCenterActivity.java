package cn.yiya.shiji.activity;
/**
 * 消息中心界面
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.HashMap;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.ExRcvAdapterWrapper;
import cn.yiya.shiji.adapter.MessageCentListAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MapRequest;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.business.YiChuangYun;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.entity.NotifyCommonListObject;
import cn.yiya.shiji.entity.NotifyDetailCount;
import cn.yiya.shiji.entity.NotifyListItem;
import cn.yiya.shiji.entity.NotifyListObject;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.utils.SharedPreUtil;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.MySwipeMenuListview;


public class MessageCenterActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ImageView ivBack;
    private TextView tvtitle;

    private RelativeLayout rlytService;
    private RelativeLayout rlytCommunity;
    private ImageView ivService;
    private ImageView ivMoreService;
    private ImageView ivMoreCommunity;
    private ImageView ivCommunity;

    public static final int SERVICE_TYPE = 1;
    public static final int COMMUNITY_TYPE = 2;

    private ArrayList<NotifyListItem> messageLists;
    private RecyclerView rycvMessage;
    private MessageCentListAdapter messageAdapter;
    private ExRcvAdapterWrapper mExrcvAdapter;

    private int nOffset;
    private boolean isBottom;
    private int lastVisibleItemPosition;
    private boolean isLogin;
    private final static int REQUEST_LOGIN = 11;

    private MsgReceiver msgReceiver;
    private int type;                       //  type=1 是卖家消息

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center);
        //动态注册广播接收器
        msgReceiver = new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("cn.yiya.shiji.pushReciver");
        registerReceiver(msgReceiver, intentFilter);

        initIntent();
        initViews();
        initEvents();
        init();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if(intent != null){
            type = intent.getIntExtra("type", 0);
        }
    }

    @Override
    protected void initViews() {
        ivBack = (ImageView)findViewById(R.id.title_back);
        tvtitle = (TextView)findViewById(R.id.title_txt);
        if(type == 1) {
            tvtitle.setText("店铺消息");
        }else {
            tvtitle.setText("消息中心");
        }
        findViewById(R.id.title_right).setVisibility(View.GONE);

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.message_center_head, null);
        rlytCommunity = (RelativeLayout) view.findViewById(R.id.msg_community);
        ivCommunity = (ImageView) view.findViewById(R.id.iv_community_dot);
        ivMoreCommunity = (ImageView) view.findViewById(R.id.iv_more_community);
        rlytService = (RelativeLayout) view.findViewById(R.id.msg_service);
        ivService = (ImageView) view.findViewById(R.id.iv_service_dot);
        ivMoreService = (ImageView) view.findViewById(R.id.iv_more_service);

        rycvMessage = (RecyclerView) findViewById(R.id.rycv_message);
        rycvMessage.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setSmoothScrollbarEnabled(false);
        rycvMessage.setLayoutManager(linearLayoutManager);
        messageAdapter = new MessageCentListAdapter(this);
        mExrcvAdapter = new ExRcvAdapterWrapper(messageAdapter, linearLayoutManager);
        if(type != 1){
            mExrcvAdapter.setHeaderView(view);
        }
        rycvMessage.setAdapter(mExrcvAdapter);

        addDefaultNullView();
        initDefaultNullView(R.mipmap.zanwubiaoqian, "暂无消息", this);

    }

    @Override
    protected  void initEvents(){
        ivBack.setOnClickListener(this);
        rlytService.setOnClickListener(this);
        rlytCommunity.setOnClickListener(this);
        rycvMessage.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
    }

    @Override
    protected void init() {
        getCommonMessageList();
        new RetrofitRequest<>(ApiRequest.getApiShiji().isLogin()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    isLogin = true;
                } else if (msg.isLossLogin()) {
                    isLogin = false;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getGroupData();
    }

    private void getGroupData() {
//        showPreDialog("正在加载");
//        new RetrofitRequest<NotifyDetailCount>(ApiRequest.getApiShiji().getNotifyDetailCount()).handRequest(
//                new MsgCallBack() {
//                    @Override
//                    public void onResult(HttpMessage msg) {
//                        hidePreDialog();
//                        if (msg.isSuccess()) {
//                            NotifyDetailCount info = (NotifyDetailCount) msg.obj;
//                            if (info.getSns_count() > 0) {
//                                ivCommunity.setVisibility(View.VISIBLE);
//                                ivMoreCommunity.setVisibility(View.GONE);
//                            } else {
//                                ivCommunity.setVisibility(View.GONE);
//                                ivMoreCommunity.setVisibility(View.VISIBLE);
//                            }
//                        }
//                    }
//                }
//        );
        if(TextUtils.isEmpty(SharedPreUtil.getString(MessageCenterActivity.this, SharedPreUtil.SERVICE_SP, SharedPreUtil.SERVICE_COMMUNITY, ""))){
            ivCommunity.setVisibility(View.GONE);
            ivMoreCommunity.setVisibility(View.VISIBLE);
        }else {
            ivCommunity.setVisibility(View.VISIBLE);
            ivMoreCommunity.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(SharedPreUtil.getString(MessageCenterActivity.this, SharedPreUtil.SERVICE_SP, SharedPreUtil.SERVICE_MSG, "")) ||
                !TextUtils.isEmpty(SharedPreUtil.getString(MessageCenterActivity.this, SharedPreUtil.SERVICE_SP, SharedPreUtil.SERVICE_ID, ""))){
            ivService.setVisibility(View.VISIBLE);
            ivMoreService.setVisibility(View.GONE);
        }else {
            ivService.setVisibility(View.GONE);
            ivMoreService.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_back:
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.msg_service:
                if(isLogin) {
                    Intent intent0 = new Intent(MessageCenterActivity.this, ServiceMessageActivity.class);
                    startActivity(intent0);
                }else {
                    goLogin();
                }
                break;
            case R.id.msg_community:
                if(isLogin) {
                    Intent intent = new Intent(MessageCenterActivity.this, CommunityMessageActivity.class);
                    intent.putExtra("type", 1);
                    startActivity(intent);
                    SharedPreUtil.putString(MessageCenterActivity.this, SharedPreUtil.SERVICE_SP, SharedPreUtil.SERVICE_COMMUNITY, "");
                }else {
                    goLogin();
                }
                break;
        }
    }

    private void goLogin(){
        Intent intentLogin = new Intent(MessageCenterActivity.this, LoginActivity.class);
        startActivityForResult(intentLogin, REQUEST_LOGIN);
        overridePendingTransition(R.anim.slide_in_top_buttom, R.anim.slide_out_top_buttom);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_LOGIN){
                isLogin = true;
                Util.getNewUserPullLayer(this, data);
            }
        }
    }

    private void getCommonMessageList() {
        nOffset = 0;
        isBottom = false;
        showPreDialog("");
        new RetrofitRequest<NotifyCommonListObject>(ApiRequest.getApiShiji().getNotifyCommonList(setMap(nOffset))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    hidePreDialog();
                    NotifyCommonListObject info = (NotifyCommonListObject) msg.obj;
                    if (info.list != null && info.list.size() > 0) {
                        messageAdapter.setmLists(info.list);
                        mExrcvAdapter.notifyDataSetChanged();
                        if (type == 1) {
                            setSuccessView(rycvMessage);
                        }
                    } else {
                        isBottom = true;
                        if (type == 1) {
                            setNullView(rycvMessage);
                        }
                    }
                } else {
                    hidePreDialog();
                    if (!NetUtil.IsInNetwork(MessageCenterActivity.this)) {
                        setOffNetView(rycvMessage);
                    }
                }
            }
        });
    }

    private HashMap setMap(int offset){
        HashMap<String, String> maps = new HashMap<>();
        maps.put("offset", String.valueOf(offset));
        maps.put("limit", String.valueOf(20));
        if(type == 1){
            maps.put("type", "shopkeeper_cansee");
        }
        return maps;
    }

    private void loardMore() {
        nOffset += 20;
        isBottom = false;
        new RetrofitRequest<NotifyCommonListObject>(ApiRequest.getApiShiji().getNotifyCommonList(setMap(nOffset))).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if(msg.isSuccess()) {
                            NotifyCommonListObject info = (NotifyCommonListObject) msg.obj;
                            if(info.list != null && info.list.size()>0){
                                messageAdapter.getmLists().addAll(info.list);
                                mExrcvAdapter.notifyDataSetChanged();
                            }else {
                                isBottom = true;
                            }
                        }else {
                            if(!NetUtil.IsInNetwork(MessageCenterActivity.this)){
                                setOffNetView(rycvMessage);
                            }
                        }
                    }
        });

    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(msgReceiver);
    }

    /**
     * 广播接收器
     * @author len
     *
     */
    public class MsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra("type").equals("community")){
                ivMoreCommunity.setVisibility(View.GONE);
                ivCommunity.setVisibility(View.VISIBLE);
            }else {
                ivMoreService.setVisibility(View.GONE);
                ivService.setVisibility(View.VISIBLE);
            }
        }

    }
}
