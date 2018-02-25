package cn.yiya.shiji.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.AppRecommendAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.AppRecommendObject;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.ProgressDialog;

/**
 * Created by jerry on 2016/1/7.
 */
public class AppRecommendActivity extends BaseAppCompatActivity implements View.OnClickListener{
    private RecyclerView mRecyclerView;
    private AppRecommendAdapter adapter;
    private String urlAdress, appName;
    private TextView tv_right, tv_title, iv_remind;
    private ImageView iv_back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_recommend_layout);

        initViews();
        initEvents();
        init();
    }

    @Override
    protected void initViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        tv_right = (TextView) findViewById(R.id.title_right);
        tv_title = (TextView) findViewById(R.id.title_txt);
        iv_back = (ImageView) findViewById(R.id.title_back);
        iv_remind = (TextView) findViewById(R.id.imageview);

        tv_right.setVisibility(View.INVISIBLE);
        tv_title.setText("优秀APP推荐");
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new AppRecommendAdapter(this);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void initEvents() {
        iv_back.setOnClickListener(this);

        final View.OnClickListener firstListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };

        final View.OnClickListener secondListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.downloadApp(AppRecommendActivity.this, urlAdress, appName);
            }
        };

        adapter.setOnItemClickListener(new AppRecommendAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String url, String name, String desc) {
                urlAdress = url;
                appName = name;
                ProgressDialog.showCommonScrollDialog(AppRecommendActivity.this, "提示", desc, "下载", "下次再说", firstListener, secondListener);
            }
        });
    }

    @Override
    protected void init() {
        getAppRecommendList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;
        }
    }

    private void getAppRecommendList() {
        showPreDialog("");
        new RetrofitRequest<AppRecommendObject>(ApiRequest.getApiShiji().getAppRecommend())
                .handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            hidePreDialog();
                            AppRecommendObject object = (AppRecommendObject) msg.obj;
                            if (object.list.size() != 0) {
                                iv_remind.setVisibility(View.GONE);
                                mRecyclerView.setVisibility(View.VISIBLE);
                                adapter.setList(object.list);
                                adapter.notifyDataSetChanged();
                            } else {
                                iv_remind.setVisibility(View.VISIBLE);
                                mRecyclerView.setVisibility(View.GONE);
                            }
                        }else {
                            hidePreDialog();
                            showTips(msg.message);
                            iv_remind.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
}
