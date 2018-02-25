package cn.yiya.shiji.test;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.BaseAppCompatActivity;

/**
 * Created by jerry on 2016/3/16.
 * 测试接口
 */
public class TestActivity extends BaseAppCompatActivity implements View.OnClickListener{
    private Handler handler;
    public static RecyclerView recyclerView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_test);
        initViews();
        initEvents();
        init();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.test_set:
                setRetrofit();
                break;
            case R.id.test_get:
                break;
        }
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void init() {

    }

    @Override
    protected void initEvents() {
        findViewById(R.id.test_get).setOnClickListener(this);
        findViewById(R.id.test_set).setOnClickListener(this);
        findViewById(R.id.test_get).setEnabled(false);
    }


    private void setRetrofit(){

//        new RetrofitRequest<CMBPayInfo>(ApiRequest.getApiShiji().getOrderNO("345220160513120810398")).handRequest(new MsgCallBack() {
//            @Override
//            public void onResult(HttpMessage msg) {
//                CMBPayInfo item = (CMBPayInfo) msg.obj;
//            }
//        });
    }

}
