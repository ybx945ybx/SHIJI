package cn.yiya.shiji.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.NewSecondCategoryAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.HotCategoryObject;
import cn.yiya.shiji.views.NoAlphaItemAnimator;


/**
 * Created by Amy on 2016/6/20.
 */
public class NewCategoryClassFragment extends BaseFragment {
    private AppCompatActivity mActivity;
    private View mView;
    private int typeId;
    private String typeName;

    private RecyclerView recyclerView;
    private NewSecondCategoryAdapter mAdapter;
    private LinearLayoutManager layoutManager;

    private ArrayMap<Integer, List<HotCategoryObject.SecondItem>> map = new ArrayMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (AppCompatActivity) getActivity();
        Bundle bundle = this.getArguments();
        typeId = bundle.getInt("typeId");
        typeName = bundle.getString("typeName");
    }

    public static NewCategoryClassFragment getInstance(int typeId, String typeName) {
        NewCategoryClassFragment fragment = new NewCategoryClassFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("typeId", typeId); //一级分类编号
        bundle.putString("typeName", typeName); //一级分类编号
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_new_category_class, null);
        initViews();
        initEvents();
        init();
        return mView;
    }

    @Override
    protected void initViews() {
        recyclerView = (RecyclerView) mView.findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(mActivity);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new NewSecondCategoryAdapter(mActivity,typeName);
        recyclerView.setItemAnimator(new NoAlphaItemAnimator());
        recyclerView.getItemAnimator().setChangeDuration(300);
        recyclerView.getItemAnimator().setMoveDuration(300);
    }

    @Override
    protected void initEvents() {
        mAdapter.setExpandCollapseListener(new NewSecondCategoryAdapter.ExpandCollapseListener() {
            @Override
            public void OnExpand(final int position) {
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        layoutManager.scrollToPositionWithOffset(position, 0);
                    }
                });
            }

            @Override
            public void OnCollapse(int position) {
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        layoutManager.scrollToPositionWithOffset(0, 0);
                    }
                });
            }
        });
    }

    @Override
    protected void init() {
        getCategoryFromMap(typeId);
    }

    private void getSecondThirdCategory(final int typeId) {
        new RetrofitRequest<HotCategoryObject>(ApiRequest.getApiShiji().getHotCategorySecondThird(String.valueOf(typeId))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    HotCategoryObject obj = (HotCategoryObject) msg.obj;
                    if (obj == null || obj.list == null) return;
                    //获取二级分类列表
                    mAdapter.setList(obj.list);
//                    mAdapter.setParentList(obj.list);
                    recyclerView.setAdapter(mAdapter);
                    if (!map.containsKey(typeId)) map.put(typeId, obj.list);
                } else {
                    showToast(msg.message);
                }
            }
        });
    }

    private void getCategoryFromMap(int typeId) {
        if (map.containsKey(typeId)) {
            mAdapter.setList(map.get(typeId));
//            mAdapter.setParentList(map.get(typeId));
            recyclerView.setAdapter(mAdapter);
        } else {
            getSecondThirdCategory(typeId);
        }
    }

    public void typeChanged(int typeId) {
        this.typeId = typeId;
        init();
    }
}
