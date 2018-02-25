package cn.yiya.shiji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.RedPackageAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.RedPackageObject;
import cn.yiya.shiji.entity.RedPackagrItem;
import cn.yiya.shiji.entity.SkusObject;
import cn.yiya.shiji.utils.NetUtil;

public class RedPackageActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private Handler mHandler;
    private RecyclerView packList;
    private LinearLayout llyfooter;

    private ImageView ivBack;
    private TextView tvTitle;

    private RedPackageAdapter mAdapter;
    private int id;
    private float total;
    private String skus;
    public int type;                            //type=1 为个人中心跳转，type = 2 为支付界面跳转

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_package);
        mHandler = new Handler();
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getIntExtra("id", 0);
            total = intent.getFloatExtra("fee", 0);
            skus = intent.getStringExtra("skus");
            type = intent.getIntExtra("type", 0);
        }

        initViews();
        initEvents();
        init();
    }

    private void getData(){
        showPreDialog("正在加载");
        if(type == 1){
            getRedPackageAll();
        }else if(type == 2){
            getRedPackageBySku();
        }

    }

    private void getRedPackageBySku() {
        SkusObject object = new Gson().fromJson(skus, SkusObject.class);
        new RetrofitRequest<RedPackageObject>(ApiRequest.getApiShiji().getRedPackageSku(object)).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            RedPackageObject obj = (RedPackageObject) msg.obj;
                            if (obj.list != null && obj.list.size() > 0) {
                                ArrayList<RedPackagrItem> info = obj.list;
                                //冒泡排序红包
                                for (int i = 0; i < info.size() - 1; i++) {
                                    for (int j = 0; j < info.size() - 1; j++) {
                                        if ((double) info.get(j).getConsumption() > (double) info.get(j + 1).getConsumption()) {
                                            RedPackagrItem redPackagrItem = info.get(j);
                                            info.remove(j);
                                            info.add(j + 1, redPackagrItem);
                                        }
                                    }
                                }
                                for (int i = 0; i < info.size() - 1; i++) {
                                    for (int j = 0; j < info.size() - 1; j++) {
                                        if ((double) info.get(j).getHandsel() < (double) info.get(j + 1).getHandsel() &&
                                                (double) info.get(j).getConsumption() == (double) info.get(j + 1).getConsumption()) {
                                            RedPackagrItem redPackagrItem = info.get(j);
                                            info.remove(j);
                                            info.add(j + 1, redPackagrItem);
                                        }
                                    }
                                }
//
                                for (int i = 0; i < info.size(); i++) {
                                    if (id > 0) {
                                        if (id == info.get(i).getId()) {
                                            info.get(i).setSelected(true);
                                        }
                                    }
                                    mAdapter.setList(info);
                                    mAdapter.notifyDataSetChanged();
                                    setSuccessView(packList);
                                }
                            } else {
                                setNullView(packList);
                            }
                        } else {
                            showTips(msg.message);
                            if (!NetUtil.IsInNetwork(RedPackageActivity.this)) {
                                setOffNetView(packList);
                            }
                        }
                        hidePreDialog();
                    }
                }
        );

    }

    private void getRedPackageAll() {
        new RetrofitRequest<RedPackageObject>(ApiRequest.getApiShiji().getRedPackageAll()).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            RedPackageObject obj = (RedPackageObject) msg.obj;
                            if (obj.list != null && obj.list.size() > 0) {
                                mAdapter.setList(obj.list);
                                mAdapter.notifyDataSetChanged();
                                setSuccessView(packList);
                            } else {
                                setNullView(packList);
                            }
                        } else {
                            showTips(msg.message);
                            if (!NetUtil.IsInNetwork(RedPackageActivity.this)) {
                                setOffNetView(packList);
                            }
                        }
                        hidePreDialog();
                    }
                }
        );
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_back:
                finish();
                break;
            case R.id.tv_reload:
                getData();
                break;
        }
    }

    @Override
    protected void initViews() {
        ivBack = (ImageView)findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText("我的红包");
        findViewById(R.id.title_right).setVisibility(View.GONE);

        packList = (RecyclerView)findViewById(R.id.package_detail_list);
//        llyfooter = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.red_package_footview, null);
//        packList.addFooterView(llyfooter);
        mAdapter = new RedPackageAdapter(this, type);
        packList.setLayoutManager(new LinearLayoutManager(this));
        packList.setItemAnimator(new DefaultItemAnimator());
        packList.setAdapter(mAdapter);

        addDefaultNullView();
        initDefaultNullView(R.mipmap.zanwuhongbao, "您还没有收到红包", this);
    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
    }

    @Override
    protected void init() {
        getData();
    }
}
