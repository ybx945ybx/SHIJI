package cn.yiya.shiji.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.RedRangAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.UserGrowRankingEntity;
import cn.yiya.shiji.entity.UserGrowRuleEntity;

/**
 * Created by Tom on 2017/1/6.
 */

public class RedRangFragment extends BaseFragment implements View.OnClickListener {

    private View mView;
    private RecyclerView rycvRedRang;
    private RedRangAdapter mAdapter;
    private UserGrowRuleEntity entity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initIntent();
    }

    private void initIntent() {
        entity = new Gson().fromJson(getArguments().getString("entity"), UserGrowRuleEntity.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_red_rang, null);
        initViews();
        initEvents();
        init();
        return mView;
    }

    public RedRangFragment getInstance(UserGrowRuleEntity entity){
        RedRangFragment redTaskFragment = new RedRangFragment();
        Bundle bundle = new Bundle();
        bundle.putString("entity", new Gson().toJson(entity));
        redTaskFragment.setArguments(bundle);
        return redTaskFragment;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void initViews() {
        rycvRedRang = (RecyclerView) mView.findViewById(R.id.rycv_red_rang);
        rycvRedRang.setItemAnimator(new DefaultItemAnimator());
        rycvRedRang.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void init() {
        getUserGrowRanking();
    }

    private void getUserGrowRanking() {
        new RetrofitRequest<UserGrowRankingEntity>(ApiRequest.getApiShiji().getUserGrowRanking()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if(msg.isSuccess()){
                    UserGrowRankingEntity userGrowRankingEntity = (UserGrowRankingEntity) msg.obj;
                    if(userGrowRankingEntity.getRanking() != null && userGrowRankingEntity.getRanking().size() > 0){
                        mAdapter = new RedRangAdapter(getActivity(), entity.getRanking(), userGrowRankingEntity.getRanking());
                        rycvRedRang.setAdapter(mAdapter);
                    }
                }
            }
        });
    }
}
