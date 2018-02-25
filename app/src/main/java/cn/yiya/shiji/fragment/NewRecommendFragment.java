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
import cn.yiya.shiji.adapter.NewRecommendAdapter;
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
 * 发现-购物笔记
 * Created by Amy on 2016/6/8.
 */
public class NewRecommendFragment extends BaseFragment implements View.OnClickListener {
    private View mView;
    private Activity mActivity;
    private int iSelectType = 0;        //0表示推荐 1表示关注
    private ArrayList<WorkItem> mList;
    private int nOffset = 0;

    private MySwipeRefreshLayout srlRefresh;
    private SwipeRefreshLayout.OnRefreshListener listener; //SwiperefreshLayout刷新监听
    private RecyclerView rvRecommend;
    private int lastVisibleItemPosition;
    private boolean isBottom;
    private LinearLayoutManager layoutManager;
    //推荐
    private NewRecommendAdapter mRecommendAdapter;
    private NewRecommendAdapter.LoginListener mLoginListener;

    private static final int LOGIN_REQUSET = 300;
    private static final int WORKDETAIL_REQUSET = 301;
    private static final int COMMENT_REQUSET = 302;

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
        mView = inflater.inflate(R.layout.fragment_new_recommend, null);
        initViews();
        initEvents();
        init();
        return mView;
    }

    public NewRecommendFragment getInstance(int iSelectType) {
        NewRecommendFragment fragment = new NewRecommendFragment();
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
        initDefaultNullView(R.mipmap.zanwugouwubiji, "", this, 0, 230);
    }

    @Override
    protected void initEvents() {
        mLoginListener = new NewRecommendAdapter.LoginListener() {
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
            public void goToWorkDetail(Intent intent) {
                getParentFragment().startActivityForResult(intent, WORKDETAIL_REQUSET);
            }

            @Override
            public void goToCommnets(Intent intent) {
                getParentFragment().startActivityForResult(intent, COMMENT_REQUSET);
            }
        };
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
                        getRecommendList(nOffset);
                    } else {
                        getFollowList(nOffset);
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

//        toolbar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!isEffectClick()){
//                    layoutManager.scrollToPosition(0);
//                }
//            }
//        });
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
                nOffset = 0;
                isBottom = false;
                mList = new ArrayList<>();
                if (iSelectType == 0) {
                    getRecommendList(0);
                } else if (iSelectType == 1) {
                    getFollowList(0);
                }
            }
        };
        srlRefresh.setOnRefreshListener(listener);
        srlRefresh.post(new Runnable() {
            @Override
            public void run() {
                srlRefresh.setRefreshing(true);
            }
        });
        listener.onRefresh();
    }

    /**
     * 获取推荐-购物笔记列表
     */
    private void getRecommendList(final int offset) {
        nOffset += 10;
        new RetrofitRequest<WorkObject>(ApiRequest.getApiShiji().getRecommendList(
                MapRequest.setMapTen(offset))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    srlRefresh.setVisibility(View.VISIBLE);
                    WorkObject obj = (WorkObject) msg.obj;
                    if (obj == null || obj.list == null || obj.list.isEmpty()) {
                        if (offset == 0) {
                            mView.findViewById(R.id.scroll).setVisibility(View.VISIBLE);
                            setNullView(srlRefresh);
                        }
                        isBottom = true;
                    } else {
                        if (offset > 0) {
                            mList.addAll(obj.list);
                            mRecommendAdapter.notifyItemRangeInserted(mRecommendAdapter.getList().size(), obj.list.size());
                        } else {
                            mList = obj.list;
                            mView.findViewById(R.id.scroll).setVisibility(View.GONE);
                            setSuccessView(srlRefresh);
                            mRecommendAdapter = new NewRecommendAdapter(mActivity, mList, mLoginListener, iSelectType);
                            rvRecommend.setAdapter(mRecommendAdapter);
                        }
                    }
                } else {
                    isBottom = true;
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
     * 获取关注-购物笔记列表
     */
    private void getFollowList(final int offset) {
        nOffset += 10;
        new RetrofitRequest<WorkObject>(ApiRequest.getApiShiji().getFollowList(
                MapRequest.setMapTen(offset))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    srlRefresh.setVisibility(View.VISIBLE);
                    WorkObject obj = (WorkObject) msg.obj;
                    if (obj == null || obj.list == null || obj.list.isEmpty()) {
                        if (offset == 0) {
                            mView.findViewById(R.id.scroll).setVisibility(View.VISIBLE);
                            setNullView(srlRefresh);
                        }
                        isBottom = true;
                    } else {
                        if (offset > 0) {
                            mList.addAll(obj.list);
                            mRecommendAdapter.notifyItemRangeInserted(mRecommendAdapter.getList().size(), obj.list.size());
                        } else {
                            mList = obj.list;
                            mView.findViewById(R.id.scroll).setVisibility(View.GONE);
                            setSuccessView(srlRefresh);
                            mRecommendAdapter = new NewRecommendAdapter(mActivity, mList, mLoginListener, iSelectType);
                            rvRecommend.setAdapter(mRecommendAdapter);
                        }
                    }
                } else {
                    isBottom = true;
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_reload:
                setSwipeRefresh(iSelectType);
                break;
            default:
                break;
        }
    }

    private void findAllSameFollow(WorkItem item) {
        int userid = item.getUser_id();
        int followed = item.getUser().getFollowed();
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).getUser_id() == userid && mList.get(i).getWork_id() != item.getWork_id()) {
                mList.get(i).getUser().setFollowed(followed);
                if (iSelectType == 0) {
                    mRecommendAdapter.notifyItemChanged(i + 1);
                } else {
                    mRecommendAdapter.notifyItemChanged(i);
                }
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == LOGIN_REQUSET) {
                setSwipeRefresh(iSelectType);
            } else if (requestCode == WORKDETAIL_REQUSET || requestCode == COMMENT_REQUSET) {
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
                mRecommendAdapter.notifyItemRemoved(position);
                mRecommendAdapter.notifyItemRangeChanged(position, mRecommendAdapter.getItemCount());
            } else if (position != -1) {
                WorkItem item = new Gson().fromJson(data, WorkItem.class);

                if (iSelectType == 0) {
                    mList.set(position - 1, item);
                } else if (iSelectType == 1) {
                    mList.set(position, item);
                }
                mRecommendAdapter.notifyItemChanged(position);
                findAllSameFollow(item);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            bInit = true;
            if (bFirst) {
                bFirst = false;
            } else {
                if (!bEnable) {
                    srlRefresh.setEnabled(false);
                }
            }
        }
    }
}
