package cn.yiya.shiji.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.LoginActivity;
import cn.yiya.shiji.activity.NewMainActivity;
import cn.yiya.shiji.adapter.NewMainAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MapRequest;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.entity.BrandsRecInfo;
import cn.yiya.shiji.entity.LayerItem;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.FloatingActionButton;
import cn.yiya.shiji.views.MySwipeRefreshLayout;


/**
 * 商城主页-首页
 * Created by chenjian on 2016/5/19.
 */
public class NewMainHomeFragment extends BaseFragment implements View.OnClickListener, AppBarLayout.OnOffsetChangedListener {

    private View mView;
    private RecyclerView recyclerView;
    private NewMainAdapter adapter;
    public static final int REQUEST_LOGIN = 100;
    private NewMainAdapter.LoginListener mListener;
    private List<BrandsRecInfo.ListEntity> mLists;
    private int lastVisibleItemPosition;
    private int nOffset = 0;
    private boolean isBottom;
    private Handler mHandler;
    private SparseArray<HashSet<Integer>> mArray;
    private MySwipeRefreshLayout srlRefresh;

    private SwipeRefreshLayout.OnRefreshListener listener; //SwiperefreshLayout刷新监听

    private static final int LIMIT = 20;                    // 每页加载20
    private static final int BORDER = 100;                  // 超过100后显示双击顶部快速回到顶部的提示

    private static final int HEAD_POS = 5;

    private LinearLayoutManager layoutManager;
    private boolean bInit = false;
    private boolean bFirst = true;
    public static boolean bEnable = true;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.new_fragment_main_home, container, false);
        BaseApplication.getInstance().fastTopGuide = false;
        mHandler = new Handler(Looper.getMainLooper());

        initViews();
        initEvents();
        init();
        return mView;
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
                        initData();
                        bInit = false;
                    }
                }
            }
        });
        mListener = new NewMainAdapter.LoginListener() {
            @Override
            public void goLogin() {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                getParentFragment().startActivityForResult(intent, REQUEST_LOGIN);
                getActivity().overridePendingTransition(R.anim.slide_in_top_buttom, R.anim.slide_out_top_buttom);
            }

            @Override
            public void notifyData(int id, int position) {
                notifyVisibleData(id, position);
            }

            @Override
            public void goToDiscoverFragment(int posoiton) {
                //搭配查看更多跳转到推荐搭配
                if (customlistener != null) customlistener.goToDiscover(posoiton);
            }
        };

        getActivity().findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEffectClick()) {
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                    linearLayoutManager.scrollToPosition(0);
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                // 获取适配器的Item个数以及最后可见的Item
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                // 如果最后可见的Item比总数小1，表示最后一个，这里小3，预加载的意思
                if ((visibleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE &&
                        (lastVisibleItemPosition) >= totalItemCount - 10) && !isBottom) {
                    loadMore();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;

                lastVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                for (int i = 0; i < recyclerView.getChildCount(); i++) {
                    View convertView = recyclerView.getChildAt(i);
                    final FloatingActionButton fabLike = (FloatingActionButton) convertView.findViewById(R.id.main_brands_like);
                    if (fabLike == null) {
                        continue;
                    }
                    int pos = (int) fabLike.getTag();
                    int vState = fabLike.getLineMorphingState();
                    final int nState = mLists.get(pos - HEAD_POS).getFollow();
                    if (vState == nState) {
                        return;
                    }

                    fabLike.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fabLike.setLineMorphingState(nState % 2, true);
                        }
                    }, 200);
                }
            }
        });

        listener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mArray = null;
                mArray = new SparseArray<>();
                initData();
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

    public void notifyVisibleData(int id, int position) {
        HashSet<Integer> set = mArray.get(id);
        Iterator<Integer> it = set.iterator();
        final int nState = mLists.get(position - HEAD_POS).getFollow();

        while (it.hasNext()) {
            int other = it.next();
            mLists.get(other - HEAD_POS).setFollow(nState);
        }

        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            View convertView = recyclerView.getChildAt(i);
            final FloatingActionButton fabLike = (FloatingActionButton) convertView.findViewById(R.id.main_brands_like);
            if (fabLike == null) {
                return;
            }
            int pos = (int) fabLike.getTag();
            if (set.contains(pos) && pos != position) {
                fabLike.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fabLike.setLineMorphingState(nState % 2, true);
                    }
                }, 200);
            }
        }
    }

    @Override
    protected void init() {
        mArray = new SparseArray<>();
        getHtmlVersion();
        getPullLayer();
    }

    private void initData() {
        nOffset = 0;
        new RetrofitRequest<BrandsRecInfo>(ApiRequest.getApiShiji().getBrandsRec(
                MapRequest.setMapTwenty(nOffset))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    srlRefresh.setVisibility(View.VISIBLE);
                    BrandsRecInfo info = (BrandsRecInfo) msg.obj;
                    if (info == null || info.getList() == null || info.getList().isEmpty()) {
                        return;
                    }
                    mLists = info.getList();
                    adapter = new NewMainAdapter(getActivity(), mArray, info.getList(), mListener);
                    recyclerView.setAdapter(adapter);
                    mView.findViewById(R.id.scroll).setVisibility(View.GONE);
                    setSuccessView(srlRefresh);
                } else {
                    if (!NetUtil.IsInNetwork(getActivity())) {
                        mView.findViewById(R.id.scroll).setVisibility(View.VISIBLE);
                        setOffNetView(srlRefresh);
                    }
                }
                srlRefresh.post(new Runnable() {
                    @Override
                    public void run() {
                        srlRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    private void getHtmlVersion() {
//        if (getActivity() == null) {
//            return;
//        }
//        Util.getHtmlVersion(getActivity(), mHandler, new DownLoadListener() {
//            @Override
//            public void onSuccess() {
//                srlRefresh.setRefreshing(false);
//            }
//
//            @Override
//            public void onFail() {
//                boolean bNet = NetUtil.IsInNetwork(getActivity());
//                if (bNet) {
//                } else {
//                    hidePreDialog();
//                    srlRefresh.setRefreshing(false);
//                }
//            }
//        });
    }

    /**
     * 首页弹层图片
     */
    private void getPullLayer() {
        new RetrofitRequest<LayerItem>(ApiRequest.getApiShiji().getPullLayer()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    LayerItem item = (LayerItem) msg.obj;
                    if (item == null) return;
                    int show = item.getShow();
                    if (show == 1) {
                        //展示
                        showImageDialog(item);
                    }
                }
            }
        });
    }

    private void showImageDialog(LayerItem item) {
        if (TextUtils.isEmpty(item.getImage()) || getActivity() == null) return;
        final Dialog dialog = new Dialog(getActivity(), R.style.ActionSheetDialogStyle);
        final View view = LayoutInflater.from(this.getActivity()).inflate(R.layout.dialog_pop_layer, null);
        ImageView ivLayer = (ImageView) view.findViewById(R.id.iv_layer);
        ImageView ivClose = (ImageView) view.findViewById(R.id.iv_close);

        int width = SimpleUtils.getScreenWidth(getActivity()) - SimpleUtils.dp2px(getActivity(), 50) * 2;
        Netroid.displayImage(Util.ScaleImageGoodes(item.getImage(), width), ivLayer);

        if (!TextUtils.isEmpty(item.getClose())) {
//            Netroid.displayImage(Util.ScaleImageGoodes(item.getClose(), width / 10), ivClose);
//            ivClose.setVisibility(View.VISIBLE);
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);

        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_LOGIN) {
                if (adapter != null) {
                    // 更新闪购提醒状态
                    adapter.notifyItemChanged(2);
                    // 更新订阅状态
                    adapter.notifyItemChanged(4);
                }
            }
        }
    }

    private void loadMore() {
        nOffset += LIMIT;
        if (nOffset >= BORDER) {
            ((NewMainActivity) getActivity()).addBackTopGuide();
        }

        new RetrofitRequest<BrandsRecInfo>(ApiRequest.getApiShiji().getBrandsRec(
                MapRequest.setMapTwenty(nOffset))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    BrandsRecInfo info = (BrandsRecInfo) msg.obj;
                    if (info == null || info.getList() == null || info.getList().isEmpty()) {
                        isBottom = true;

                    } else {
                        mLists.addAll(info.getList());
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                }
            }
        });
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

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            customlistener = (CustomActionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NewMainHomeFragment.CustomActionListener");
        }
    }

    private CustomActionListener customlistener;

    // Container Activity must implement this interface
    public interface CustomActionListener {
        void goToDiscover(int position);
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
