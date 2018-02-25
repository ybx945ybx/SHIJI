package cn.yiya.shiji.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.LoginActivity;
import cn.yiya.shiji.activity.NewMainActivity;
import cn.yiya.shiji.adapter.NewMatchAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MapRequest;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.WorkItem;
import cn.yiya.shiji.entity.WorkObject;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.views.MySwipeRefreshLayout;


/**
 * 发现-搭配列表
 * Created by Amy on 2016/7/11.
 */
public class NewMatchFragment extends BaseFragment implements View.OnClickListener {
    private Activity mActivity;
    private View mView;
    private int iSelectType;  //0表示推荐 1表示关注

    private MySwipeRefreshLayout srlRefresh;
    private SwipeRefreshLayout.OnRefreshListener listener; //SwiperefreshLayout刷新监听
    private RecyclerView rvRecommend;
    private int lastVisibleItemPosition;
    private boolean isBottom;
    private LinearLayoutManager layoutManager;

    private ArrayList<WorkItem> mList = new ArrayList<>();
    private NewMatchAdapter mAdapter;
    private NewMatchAdapter.CustomActionListener matchListener;
    private int nOffect = 0;

    private static final int LOGIN_REQUSET = 300;
    private static final int DETAIL_REQUSET = 311;
    private static final int COMMENT_REQUSET = 312;

    private boolean isAutoRefresh = false;

    private boolean bInit = false;
    private boolean bFirst = true;
    public static boolean bEnable = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            iSelectType = bundle.getInt("selecType");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_new_recommend, container, false);
        initViews();
        initEvents();
        init();
        return mView;
    }

    public NewMatchFragment getInstance(int iSelectType) {
        NewMatchFragment fragment = new NewMatchFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("selecType", iSelectType); //0 推荐 1关注
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initViews() {
        srlRefresh = (MySwipeRefreshLayout) mView.findViewById(R.id.srl_refresh);
        rvRecommend = (RecyclerView) mView.findViewById(R.id.rv_recommend);
        rvRecommend.setHasFixedSize(true);
        rvRecommend.setItemAnimator(new DefaultItemAnimator());
        layoutManager = new LinearLayoutManager(getActivity());
        rvRecommend.setLayoutManager(layoutManager);

        //无网络状态
        setMainView(mView);
        initDefaultNullView(R.mipmap.zanwudapei, "", this, 0, 230);
    }

    @Override
    protected void initEvents() {
        rvRecommend.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 获取适配器的Item个数以及最后可见的Item
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                // 如果最后可见的Item比总数小1，表示最后一个，预加载的意思
                if ((visibleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE &&
                        (lastVisibleItemPosition) == totalItemCount - 1) && !isBottom) {
                    if (iSelectType == 0) {
                        getMatchRecommendList(nOffect);
                    } else if (iSelectType == 1) {
                        getMatchFollowList(nOffect);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (bInit) {
                    if (dy < 0 && layoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                        srlRefresh.setEnabled(true);
                        setSwipeRefresh(iSelectType);
                        bInit = false;
                    }
                }
            }
        });
        matchListener = new NewMatchAdapter.CustomActionListener() {
            @Override
            public void goLogin() {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getParentFragment().startActivityForResult(intent, LOGIN_REQUSET);
                getActivity().overridePendingTransition(R.anim.slide_in_top_buttom, R.anim.slide_out_top_buttom);
            }

            @Override
            public void setFollow(WorkItem item) {
                findAllSameFollow(item);
            }

            @Override
            public void goToDetail(Intent intent) {
                getParentFragment().startActivityForResult(intent, DETAIL_REQUSET);
            }

            @Override
            public void goToCommnets(Intent intent) {
                getParentFragment().startActivityForResult(intent, COMMENT_REQUSET);
            }
        };
    }

    private void findAllSameFollow(WorkItem item) {
        int userid = item.getUser_id();
        int followed = item.getUser().getFollowed();
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).getUser_id() == userid && mList.get(i).getWork_id() != item.getWork_id()) {
                mList.get(i).getUser().setFollowed(followed);
                if (iSelectType == 0) {
                    mAdapter.notifyItemChanged(i + 1);
                } else {
                    mAdapter.notifyItemChanged(i);
                }
            }
        }

    }

    @Override
    protected void init() {
        setSwipeRefresh(iSelectType);
    }

    /**
     * 设置下拉刷新，刷新后更新页面数据
     */
    public void setSwipeRefresh(int selectType) {
        iSelectType = selectType;
        listener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                nOffect = 0;
                isBottom = false;
                mList = new ArrayList<>();
                if (iSelectType == 0) {
                    getMatchRecommendList(0);
                } else if (iSelectType == 1) {
                    getMatchFollowList(0);
                }
                isAutoRefresh = false;
            }

        };
        srlRefresh.setOnRefreshListener(listener);
        srlRefresh.post(new Runnable() {
            @Override
            public void run() {
                isAutoRefresh = true;
                srlRefresh.setRefreshing(true);
            }
        });
        listener.onRefresh();
    }

    /**
     * 获取推荐搭配列表
     *
     * @param offset
     */
    private void getMatchRecommendList(final int offset) {
        nOffect += 10;
        new RetrofitRequest<WorkObject>(ApiRequest.getApiShiji().getRecommendList(
                MapRequest.setMapMatchRecommendList(offset, 10, "2"))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    WorkObject obj = (WorkObject) msg.obj;
                    if (obj != null && obj.list != null && !obj.list.isEmpty()) {
                        if (offset > 0) {
                            mList.addAll(obj.list);
                            mAdapter.notifyItemRangeInserted(mAdapter.getList().size(), obj.list.size());
                        } else if (offset == 0) {
                            mView.findViewById(R.id.scroll).setVisibility(View.GONE);
                            setSuccessView(srlRefresh);
                            mList = obj.list;
                            mAdapter = new NewMatchAdapter(mActivity, mList, matchListener, 0);
                            rvRecommend.setAdapter(mAdapter);
                            rvRecommend.scrollToPosition(0);

                            if (bDapeiMore && isAutoRefresh) {
                                if (dapeiLength < mList.size()) {
                                    rvRecommend.scrollToPosition(dapeiLength);
                                    bDapeiMore = false;
                                }
                            }
                        }
                    } else {
                        isBottom = true;
                        if (offset == 0) {
                            mView.findViewById(R.id.scroll).setVisibility(View.VISIBLE);
                            setNullView(srlRefresh);
                        }
                    }
                } else {
                    if (!NetUtil.IsInNetwork(mContext)) {
                        mView.findViewById(R.id.scroll).setVisibility(View.VISIBLE);
                        setOffNetView(srlRefresh);
                    }
                }
                srlRefresh.setRefreshing(false);
            }
        });
    }

    /**
     * 获取关注搭配列表
     *
     * @param offset
     */
    private void getMatchFollowList(final int offset) {
        nOffect += 10;
        new RetrofitRequest<WorkObject>(ApiRequest.getApiShiji().getFollowList(
                MapRequest.setMapMatchRecommendList(offset, 10, "2"))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    WorkObject obj = (WorkObject) msg.obj;
                    if (obj != null && obj.list != null && !obj.list.isEmpty()) {
                        if (offset > 0) {
                            mList.addAll(obj.list);
                            mAdapter.notifyItemRangeInserted(mAdapter.getList().size(), obj.list.size());
                        } else if (offset == 0) {
                            mView.findViewById(R.id.scroll).setVisibility(View.GONE);
                            setSuccessView(srlRefresh);
                            mList = obj.list;
                            mAdapter = new NewMatchAdapter(mActivity, mList, matchListener, 1);
                            rvRecommend.setAdapter(mAdapter);
                            rvRecommend.scrollToPosition(0);
                        }
                    } else {
                        isBottom = true;
                        if (offset == 0) {
                            mView.findViewById(R.id.scroll).setVisibility(View.VISIBLE);
                            setNullView(srlRefresh);
                        }
                    }
                } else {
                    if (!NetUtil.IsInNetwork(mContext)) {
                        mView.findViewById(R.id.scroll).setVisibility(View.VISIBLE);
                        setOffNetView(srlRefresh);
                    }
                }
                srlRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == LOGIN_REQUSET) {
                ((NewMainActivity) mActivity).bLogin = true;
                setSwipeRefresh(iSelectType);
            } else if (requestCode == DETAIL_REQUSET || requestCode == COMMENT_REQUSET) {
                ChangeItem(data);
            }
        }
    }

    private void ChangeItem(Intent intent) {
        if (intent != null) {
            boolean bDelete = intent.getBooleanExtra("delete", false);
            int position = intent.getIntExtra("position", -1);
            String data = intent.getStringExtra("data");
            if (bDelete) {
                if (iSelectType == 0) {
                    mList.remove(position - 1);
                } else if (iSelectType == 1) {
                    mList.remove(position);
                }
                mAdapter.notifyItemRemoved(position);
                mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());
            } else if (position != -1) {
                WorkItem item = new Gson().fromJson(data, WorkItem.class);

                if (iSelectType == 0) {
                    mList.set(position - 1, item);
                } else if (iSelectType == 1) {
                    mList.set(position, item);
                }
                mAdapter.notifyItemChanged(position);
                findAllSameFollow(item);
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_reload:
                if (NetUtil.IsInNetwork(mContext)) {
                    setSuccessView(srlRefresh);
                    setSwipeRefresh(iSelectType);
                }

                break;
            default:
                break;
        }
    }

    private boolean bDapeiMore = false;
    private int dapeiLength = 0;

    public void scrollToPosition(boolean bDapeiMore, int position) {
        this.bDapeiMore = bDapeiMore;
        this.dapeiLength = position;
        if (rvRecommend != null)
            rvRecommend.setNestedScrollingEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            bInit = true;
            if (bFirst) {
                bFirst = false;
                if (bDapeiMore) {
                    srlRefresh.setEnabled(false);
                }
            } else {
                if (!bEnable) {
                    srlRefresh.setEnabled(false);
                }
            }
        }
    }
}
