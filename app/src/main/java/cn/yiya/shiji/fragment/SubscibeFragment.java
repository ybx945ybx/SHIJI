package cn.yiya.shiji.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.EditSubscibeActivity;
import cn.yiya.shiji.adapter.GuideItemAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.GuideItem;
import cn.yiya.shiji.entity.HotBrandsItem;
import cn.yiya.shiji.entity.HotBrandsObject;
import cn.yiya.shiji.entity.HotClassItem;
import cn.yiya.shiji.entity.HotClassObject;
import cn.yiya.shiji.views.GuideDividerItemDecoration;

/**
 * Created by Tom on 2016/2/22.
 */
public class SubscibeFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private GuideItemAdapter mAdapter;
    private int mPosition; //0 品牌订阅 //1 品类订阅
    private int selectedNumber;
    private ArrayList<GuideItem> brandDataList = new ArrayList<>();
    private ArrayList<GuideItem> categroyDataList = new ArrayList<>();
    private View mView;

    public static SubscibeFragment instancePage(int position) {
        Bundle args = new Bundle();
        args.putInt("position", position);
        SubscibeFragment subscibeFragment = new SubscibeFragment();
        subscibeFragment.setArguments(args);
        return subscibeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt("position");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_page, container, false);
        initViews();
        initEvents();
        init();
        return mView;
    }

    @Override
    protected void initViews() {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.pageList);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new GuideDividerItemDecoration(getActivity(), R.drawable.guide_divider_decoration));
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void init() {
        getData();
    }

    private void getData() {
        if (mPosition == 0) {
            getBrandData();
        } else if (mPosition == 1) {
            getCategoryData();
        }
    }

    private void getCategoryData() {
        showPreDialog("");
        new RetrofitRequest<HotClassObject>(ApiRequest.getApiShiji().getCategoriesRegister()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    hidePreDialog();
                    HotClassObject object = (HotClassObject) msg.obj;
                    ArrayList<HotClassItem> list = object.list;          // 获取注册页面热门品类
                    if (list != null && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            GuideItem item = new GuideItem();
                            item.setId(list.get(i).getId());
                            item.setLogo(list.get(i).getIcon());
                            item.setName(list.get(i).getName());
                            item.setFollowed(list.get(i).getFollowed());
                            categroyDataList.add(item);
                            if (list.get(i).getFollowed() == 1) {
                                selectedNumber += 1;
                            }
                        }
                        ((EditSubscibeActivity) getActivity()).upDateView(selectedNumber, mPosition);
                        mAdapter = new GuideItemAdapter(getActivity(), mPosition + 1, selectedNumber, true);
                        mRecyclerView.setAdapter(mAdapter);
                        mAdapter.setmList(categroyDataList);
                        mAdapter.notifyDataSetChanged();
                        mAdapter.setOnItemClickListener(new GuideItemAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int snb) {

                            }

                            @Override
                            public void onItemClick(int snb, int position) {
                                selectedNumber = snb;
                                ((EditSubscibeActivity) getActivity()).upDateView(selectedNumber, position);

                            }
                        });
                    }
                } else {
                    hidePreDialog();
                }
            }
        });
    }

    private void getBrandData() {
        showPreDialog("");
        new RetrofitRequest<HotBrandsObject>(ApiRequest.getApiShiji().getBrandsRegister()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    hidePreDialog();
                    HotBrandsObject object = (HotBrandsObject) msg.obj;
                    if (object == null) {
                        return;
                    }

                    ArrayList<HotBrandsItem> list = object.list;        // 注册页面热门品牌数据
                    if (list != null && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            GuideItem item = new GuideItem();
                            item.setTag_id(list.get(i).getTag_id() + "");
                            item.setLogo(list.get(i).getLogo());
                            item.setName(list.get(i).getName());
                            item.setFollowed(list.get(i).getFollowed());
                            brandDataList.add(item);
                            if (list.get(i).getFollowed() == 1) {
                                selectedNumber += 1;
                            }
                        }
                        ((EditSubscibeActivity) getActivity()).upDateView(selectedNumber, mPosition);
                        mAdapter = new GuideItemAdapter(getActivity(), mPosition + 1, selectedNumber, true);
                        mRecyclerView.setAdapter(mAdapter);
                        mAdapter.setmList(brandDataList);
                        mAdapter.notifyDataSetChanged();
                        mAdapter.setOnItemClickListener(new GuideItemAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int snb) {

                            }

                            @Override
                            public void onItemClick(int snb, int position) {
                                selectedNumber = snb;
                                ((EditSubscibeActivity) getActivity()).upDateView(selectedNumber, position);

                            }
                        });
                    }
                } else {
                    hidePreDialog();
                }
            }
        });
    }
}

