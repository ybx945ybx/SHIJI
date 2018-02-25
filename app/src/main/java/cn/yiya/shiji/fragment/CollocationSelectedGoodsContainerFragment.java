package cn.yiya.shiji.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import cn.yiya.shiji.R;

/**
 * Created by Tom on 2016/7/26.
 */
public class CollocationSelectedGoodsContainerFragment extends BaseFragment {

    private View mView;
    private FrameLayout flytContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_collocation_selected_goods_container, null);
        initViews();
        initEvents();
        init();
        return mView;
    }

    @Override
    protected void initViews() {
        flytContainer = (FrameLayout) mView.findViewById(R.id.fragment_container);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        CollocationSelectedGoodsFragment collocationSelectedGoodsFragment = new CollocationSelectedGoodsFragment();
        ft.add(R.id.fragment_container, collocationSelectedGoodsFragment, "first_second");
        ft.commit();
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void init() {

    }
}
