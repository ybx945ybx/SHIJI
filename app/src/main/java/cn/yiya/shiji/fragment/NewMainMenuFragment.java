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

import java.util.ArrayList;
import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.LoginActivity;
import cn.yiya.shiji.adapter.NewMainMenuAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.NewGoodsObject;
import cn.yiya.shiji.entity.ThematicInfo;
import cn.yiya.shiji.entity.goodes.NewGoodsItem;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.views.MySwipeRefreshLayout;

/**
 * 商城主页-菜单分类
 * Created by Amy on 2016/8/17.
 */
public class NewMainMenuFragment extends BaseFragment implements View.OnClickListener {
    private String menuId;//菜单编号
    private String menuName;//菜单名称
    private View mView;

    private RecyclerView recyclerView;
    private MySwipeRefreshLayout srlRefresh;
    private SwipeRefreshLayout.OnRefreshListener listener; //SwiperefreshLayout刷新监听
    private NewMainMenuAdapter adapter;

    private List<NewGoodsItem> mList = new ArrayList<>();

    private NewMainMenuAdapter.LoginListener mListener;
    public static final int REQUEST_LOGIN = 100;

    private LinearLayoutManager layoutManager;
    private boolean bInit = false;
    private boolean bFirst = true;
    public static boolean bEnable = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            menuId = bundle.getString("menuId");
            menuName = bundle.getString("menuName");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.new_fragment_main_home, container, false);
        initViews();
        initEvents();
        init();
        return mView;
    }

    public NewMainMenuFragment getInstance(String menuId, String menuName) {
        NewMainMenuFragment fragment = new NewMainMenuFragment();
        Bundle bundle = new Bundle();
        bundle.putString("menuId", menuId);
        bundle.putString("menuName", menuName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initViews() {
        recyclerView = (RecyclerView) mView.findViewById(R.id.main_recyclerview);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.hasFixedSize();
        srlRefresh = (MySwipeRefreshLayout) mView.findViewById(R.id.srl_refresh);

        setMainView(mView);
        initDefaultNullView(R.mipmap.zanwugouwubiji, "", this, 0, 230);
    }

    @Override
    protected void initEvents() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (bInit) {
                    if (dy < 0 && layoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                        srlRefresh.setEnabled(true);
                        setSwipeRefresh();
                        bInit = false;
                    }
                }
            }
        });

        mListener = new NewMainMenuAdapter.LoginListener() {
            @Override
            public void goLogin() {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                getParentFragment().startActivityForResult(intent, REQUEST_LOGIN);
                getActivity().overridePendingTransition(R.anim.slide_in_top_buttom, R.anim.slide_out_top_buttom);
            }
        };
    }

    @Override
    protected void init() {
        setSwipeRefresh();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_reload:
                listener.onRefresh();
                init();
                break;
        }
    }

    /**
     * 设置下拉刷新，刷新后更新页面数据
     */
    public void setSwipeRefresh() {
        listener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mList = new ArrayList<>();
                getMenuThematic();
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
     * 获取菜单下某一专题信息
     */
    private void getMenuThematic() {
        new RetrofitRequest<ThematicInfo>(ApiRequest.getApiShiji().getMenuThematic(menuId))
                .handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            ThematicInfo info = (ThematicInfo) msg.obj;
                            getThematicActivity(info);
                        } else {
                            srlRefresh.setRefreshing(false);
                            if (!NetUtil.IsInNetwork(getActivity())) {
                                mView.findViewById(R.id.scroll).setVisibility(View.VISIBLE);
                                setOffNetView(srlRefresh);
                            }
                        }
                    }
                });
    }

    /**
     * 获取专题下活动列表
     *
     * @param info
     */
    private void getThematicActivity(ThematicInfo info) {
        if (info == null) {
            adapter = new NewMainMenuAdapter(getActivity(), menuId, menuName, null, mListener);
            recyclerView.setAdapter(adapter);
            srlRefresh.setRefreshing(false);
            return;
        }
        new RetrofitRequest<NewGoodsObject>(ApiRequest.getApiShiji().getThematicAct(info.getThematic_id()))
                .handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            NewGoodsObject obj = (NewGoodsObject) msg.obj;
                            if (obj == null || obj.getList() == null || obj.getList().isEmpty()) {
                                return;
                            }
                            mView.findViewById(R.id.scroll).setVisibility(View.GONE);
                            setSuccessView(srlRefresh);
                            mList = obj.getList();
                            adapter = new NewMainMenuAdapter(getActivity(), menuId, menuName, mList, mListener);
                            recyclerView.setAdapter(adapter);
                        }
                        srlRefresh.setRefreshing(false);
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_LOGIN) {
                if (adapter != null) {
                    // 更新订阅状态
                    adapter.notifyItemChanged(5);
                }
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
