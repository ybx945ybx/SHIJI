package cn.yiya.shiji.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.text.SimpleDateFormat;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.NewSingleBrandActivity;
import cn.yiya.shiji.activity.PopularBrandsListActivity;
import cn.yiya.shiji.adapter.BransItemAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MapRequest;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.HotBrandsItem;
import cn.yiya.shiji.entity.HotBrandsObject;
import cn.yiya.shiji.views.PullDownScrollView;
import cn.yiya.shiji.views.XListView;

/**
 * Created by jerryzhang on 2015/10/15.
 */
public class PopularBrandsFragment extends BaseFragment implements XListView.IXListViewListener {
    private Handler mHandler;
    private XListView mListView;
    private BransItemAdapter mAdapter;
    private boolean isBusy = false;
    private PullDownScrollView mPullDownScorllView;
    private PauseOnScrollListener mPauseOnScrollListener;
    private PopularBrandsListActivity mActivity;
    private int i;
    private View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler(getActivity().getMainLooper());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragmentlist_four, container, false);
        initViews();
        init();
        initEvents();
        return mView;
    }

    @Override
    protected void initViews() {
        mActivity = (PopularBrandsListActivity) getActivity();
        mListView = (XListView) mView.findViewById(R.id.listView);
        mAdapter = new BransItemAdapter(getActivity());
        mPullDownScorllView = (PullDownScrollView) mView.findViewById(R.id.pulldown);
        mPauseOnScrollListener = new PauseOnScrollListener(ImageLoader.getInstance(), true, true);
    }

    @Override
    protected void initEvents() {
        mListView.setPullLoadEnable(false);
        mListView.setXListViewListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HotBrandsItem item = (HotBrandsItem) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), NewSingleBrandActivity.class);
                intent.putExtra("brand_id", item.getId());
                startActivity(intent);
            }
        });

        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        InputMethodManager imm = (InputMethodManager) getActivity().
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void init() {
        mListView.setAdapter(mAdapter);
        loadMoreData(0);
    }

    /**
     * 加载更多
     */
    private void loadMoreData(final int offset) {
        if (isBusy) {
            return;
        }
        isBusy = true;
        new RetrofitRequest<HotBrandsObject>(ApiRequest.getApiShiji().getHotBrandsList(
                MapRequest.setMapTen(offset))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                isBusy = false;
                if (msg.isSuccess()) {
                    mActivity.hidePreDialog();
                    HotBrandsObject obj = (HotBrandsObject) msg.obj;
                    if (obj.list != null && obj.list.size() > 0) {
                        if (offset > 0) {
                            mAdapter.getHotList().addAll(obj.list);
                        } else {
                            mAdapter.setHotList(obj.list);
                        }
                        mAdapter.notifyDataSetChanged();
                        mListView.stopLoadMore();
                        mListView.stopRefresh();
                        mListView.setPullLoadEnable(true);
                        mPullDownScorllView.finishRefresh("");
                    }
                } else {
                    showToast(msg.message);
                    mListView.stopLoadMore();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadMoreData(0);
                i = 0;
                onLoad();
            }
        }, 500);
    }

    @Override
    public void onLoadMore() {
        i = ++i;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadMoreData(i * 10);
            }
        }, 500);
    }

    private void onLoad() {
        mListView.setRefreshTime(getTime());
    }

    private String getTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return sDateFormat.format(new java.util.Date());
    }

}
