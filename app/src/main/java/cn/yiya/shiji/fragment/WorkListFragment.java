package cn.yiya.shiji.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.CollocationAddImgActivity;
import cn.yiya.shiji.activity.PublishWorkActivity;
import cn.yiya.shiji.adapter.MineMatchGridAdapter;
import cn.yiya.shiji.adapter.MineWorkGridAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MapRequest;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.WorkObject;

/**
 * 社区——搭配、购物笔记列表
 * Created by Tom on 2016/6/16.
 */
public class WorkListFragment extends BaseFragment {

    private View mView;
    private RecyclerView rycvWorkList;
    private MineWorkGridAdapter workListAdapter;
    private MineMatchGridAdapter matchListAdapter;

    private Handler mHandler;
    private int user_id;
    private int lastVisibleItemPosition;
    private static final int REQUEST_PUBLISH = 999;
    private static final int REQUEST_COLLOCATION = 1000;
    private boolean isBottom;
    private int nOffset;
    private boolean isCustom;                       // 主客态  true是主态false是客态
    private int type;                               // 2搭配 1购物笔记
    private boolean isLike;                         // 是否已赞页面  true则是已赞页面 false则社区主页
    private TextView tvNullView;

    private static final int WORK_DETAIL = 1001;
    private static final int MATCH_DETAIL = 1002;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler(Looper.getMainLooper());
        initIntent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_work_list, null);
        initViews();
        initEvents();
        init();
        return mView;
    }

    public WorkListFragment getInstance(int user_id, boolean isCustom, int type, boolean isLike) {
        WorkListFragment fragment = new WorkListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("user_id", user_id);
        bundle.putBoolean("isCustom", isCustom);
        bundle.putInt("type", type);
        bundle.putBoolean("isLike", isLike);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initViews() {
        rycvWorkList = (RecyclerView) mView.findViewById(R.id.rycv_work_list);
        rycvWorkList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rycvWorkList.setItemAnimator(new DefaultItemAnimator());
        tvNullView = (TextView) mView.findViewById(R.id.tv_default_null);
        if (type == 1) {
            workListAdapter = new MineWorkGridAdapter(getActivity(), isCustom);
            rycvWorkList.setAdapter(workListAdapter);
            tvNullView.setCompoundDrawablesWithIntrinsicBounds(null, ResourcesCompat.getDrawable(getResources(), R.mipmap.zanwugouwubiji, null), null, null);
            tvNullView.setText("暂无购物笔记");
        } else if (type == 2) {
            matchListAdapter = new MineMatchGridAdapter(getActivity(), isCustom);
            rycvWorkList.setAdapter(matchListAdapter);
            tvNullView.setCompoundDrawablesWithIntrinsicBounds(null, ResourcesCompat.getDrawable(getResources(), R.mipmap.zanwudapei, null), null, null);
            tvNullView.setText("暂无搭配");
        }
    }


    @Override
    protected void initEvents() {
        rycvWorkList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //获取适配器的Item个数以及最后可见的Item
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int visivleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                //如果最后可见的item比总数小1，则表示最后一个，这里小3，预加载的意思
                if (visivleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE &&
                        (lastVisibleItemPosition) >= totalItemCount - 3 && !isBottom) {
                    loadMore();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                lastVisibleItemPosition = gridLayoutManager.findLastCompletelyVisibleItemPosition();
            }
        });

        if (type == 1) {
            workListAdapter.setAddNewWorkListener(new MineWorkGridAdapter.AddNewWorkListener() {
                @Override
                public void AddNewWork() {
                    Intent intent1 = new Intent(getActivity(), PublishWorkActivity.class);
                    intent1.putExtra("FLAG", 1);
                    startActivityForResult(intent1, REQUEST_PUBLISH);
                }

                @Override
                public void goToDetail(Intent intent) {
                    //跳转到笔记详情
                    startActivityForResult(intent, WORK_DETAIL);
                }
            });
        } else if (type == 2) {
            matchListAdapter.setAddNewWorkListener(new MineMatchGridAdapter.AddNewWorkListener() {
                @Override
                public void AddNewWork() {
                    Intent intent1 = new Intent(getActivity(), CollocationAddImgActivity.class);
                    startActivityForResult(intent1, REQUEST_COLLOCATION);
                }

                @Override
                public void goToDetail(Intent intent) {
                    //跳转到搭配详情
                    startActivityForResult(intent, MATCH_DETAIL);
                }
            });
        }
    }

    @Override
    protected void init() {
        getWorkList();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_PUBLISH || requestCode == REQUEST_COLLOCATION) {
                getWorkList();
            }
            if (requestCode == WORK_DETAIL || requestCode == MATCH_DETAIL) {
                if (data != null) {
                    boolean bDelete = data.getBooleanExtra("delete", false);
                    int position = data.getIntExtra("position", -1);
                    if (bDelete) {
                        delete(position);
                    }
                }
            }
        }
    }

    private void delete(int position) {
        if (type == 1) {
            if (isCustom) {
                workListAdapter.getmList().remove(position - 1);
            } else {
                workListAdapter.getmList().remove(position);
            }
            workListAdapter.notifyItemRemoved(position);
            workListAdapter.notifyItemRangeChanged(position, workListAdapter.getItemCount());
        } else if (type == 2) {
            if (isCustom) {
                matchListAdapter.getmList().remove(position - 1);
            } else {
                matchListAdapter.getmList().remove(position);
            }
            matchListAdapter.notifyItemRemoved(position);
            matchListAdapter.notifyItemRangeChanged(position, matchListAdapter.getItemCount());
        }
    }

    private void initIntent() {
        user_id = getArguments().getInt("user_id");
        isCustom = getArguments().getBoolean("isCustom");
        type = getArguments().getInt("type");
        isLike = getArguments().getBoolean("isLike");
    }

    private void getWorkList() {
        showPreDialog("");
        nOffset = 0;

        if (!isLike) {
            new RetrofitRequest<WorkObject>(ApiRequest.getApiShiji().getWorkList(
                    MapRequest.setWorkListMap(user_id, type, 0))).handRequest(new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    handleMessage(msg, 0);
                }
            });
        } else {
            new RetrofitRequest<WorkObject>(ApiRequest.getApiShiji().getLikeWorkList(
                    MapRequest.setWorkListMap(user_id, type, 0))).handRequest(new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    handleMessage(msg, 0);
                }
            });
        }
    }

    private void handleMessage(HttpMessage msg, int offset) {
        hidePreDialog();
        if (msg.isSuccess()) {
            WorkObject obj = (WorkObject) msg.obj;

            if (obj.list != null && obj.list.size() > 0) {
                if (offset == 0) {
                    if (type == 1) {
                        workListAdapter.setmList(obj.list);
                        workListAdapter.notifyDataSetChanged();
                    } else if (type == 2) {
                        matchListAdapter.setmList(obj.list);
                        matchListAdapter.notifyDataSetChanged();
                    }
                } else {
                    if (type == 1) {
                        workListAdapter.getmList().addAll(obj.list);
                        workListAdapter.notifyDataSetChanged();
                    } else if (type == 2) {
                        matchListAdapter.getmList().addAll(obj.list);
                        matchListAdapter.notifyDataSetChanged();
                    }
                }
                if (!isCustom) {
                    tvNullView.setVisibility(View.GONE);
                }
            } else {
                isBottom = true;
                if (offset == 0 && !isCustom) {
                    tvNullView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void loadMore() {
        nOffset += 20;
        if (!isLike) {
            new RetrofitRequest<WorkObject>(ApiRequest.getApiShiji().getWorkList(
                    MapRequest.setWorkListMap(user_id, type, nOffset))).handRequest(new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    handleMessage(msg, nOffset);
                }
            });
        } else {
            new RetrofitRequest<WorkObject>(ApiRequest.getApiShiji().getLikeWorkList(
                    MapRequest.setWorkListMap(user_id, type, nOffset))).handRequest(new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    handleMessage(msg, nOffset);
                }
            });
        }
    }
}
