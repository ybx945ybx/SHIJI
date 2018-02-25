package cn.yiya.shiji.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.CollocationSelectedGoodsListActivity;
import cn.yiya.shiji.adapter.CollocationSelectedThridAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.HotCategroyThridObject;

/**
 * Created by Tom on 2016/7/26.
 */
public class CollocationSelectedGoodsThridFragment extends BaseFragment {

    private View mView;
    private LinearLayout llytAllCategroy;
    private RecyclerView rycvThrid;
    private CollocationSelectedThridAdapter mAdapter;
    private int id;                                         // 二级热门分类id
    private static final int REQUEST_LIST = 1001;
    private String typeName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_collocation_selected_goods_thrid, null);
        initIntent();
        initViews();
        initEvents();
        init();
        return mView;
    }

    private void initIntent() {
        id = getArguments().getInt("id");
        typeName=getArguments().getString("typeName");
    }

    @Override
    protected void initViews() {
        llytAllCategroy = (LinearLayout) mView.findViewById(R.id.llyt_all_categroy);
        rycvThrid = (RecyclerView) mView.findViewById(R.id.rycv_collocation_selected_goods_thrid);
        rycvThrid.setItemAnimator(new DefaultItemAnimator());
        rycvThrid.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new CollocationSelectedThridAdapter(getActivity(), id);
        rycvThrid.setAdapter(mAdapter);
    }

    @Override
    protected void initEvents() {
        llytAllCategroy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = fm.findFragmentByTag("thrid");
                if(fragment != null){
                    ft.remove(fragment);
                }
                Fragment fragment1 = fm.findFragmentByTag("first_second");
                if(fragment1 != null){
                    ft.show(fragment1);
                }
                ft.commit();
            }
        });
        mAdapter.setOnItemClickListener(new CollocationSelectedThridAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int id, int category, String name) {
                Intent intent = new Intent(getActivity(), CollocationSelectedGoodsListActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("category", category + "");
                intent.putExtra("name", name);
                intent.putExtra("typeName",typeName);
                getActivity().startActivityForResult(intent, REQUEST_LIST);
            }
        });
    }

    @Override
    protected void init() {
        new RetrofitRequest<HotCategroyThridObject>(ApiRequest.getApiShiji().getHotCategoryThrid(String.valueOf(id))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if(msg.isSuccess()){
                    HotCategroyThridObject object = (HotCategroyThridObject) msg.obj;
                    if(object.getList() != null && object.getList().size() > 0){
                        HotCategroyThridObject.ThridListEntity entity = new HotCategroyThridObject.ThridListEntity();
                        entity.setName("全部");
                        ArrayList<HotCategroyThridObject.ThridListEntity> list = new ArrayList<>();
                        list.add(entity);
                        list.addAll(object.getList());
                        mAdapter.setmList(list);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }


}
