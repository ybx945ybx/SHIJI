package cn.yiya.shiji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.AdressAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.AddressListItem;
import cn.yiya.shiji.entity.AddressListObject;

/**
 * Created by yiya on 2015/9/7.
 */
public class AdressSelectActivity extends BaseAppCompatActivity implements View.OnClickListener{
    private ListView mListView;
    private List<AddressListItem> mList;
    private AdressAdapter mAdapter;
    private ImageView ivBack;
    private TextView tvTitle;
    private TextView tvEdit;
    private Handler mHandler;
    private int addressId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adress);

        Intent intent = getIntent();
        if (intent != null) {
            addressId = intent.getIntExtra("id", addressId);
        }
        mHandler = new Handler(getMainLooper());

        initViews();
        initEvents();
        init();
    }

    @Override
    protected void initViews() {
        mListView = (ListView) findViewById(R.id.adress_listview);
        ivBack = (ImageView)findViewById(R.id.title_back);

        tvEdit = (TextView) findViewById(R.id.title_right);
        tvEdit.setText("编辑");
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText("地址选择");


        mList = new ArrayList<>();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                AddressListItem info = mList.get(position);
                String json = new Gson().toJson(info);
                intent.putExtra("json", json);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        tvEdit.setOnClickListener(this);
    }

    @Override
    protected void init() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.title_right:
                Intent intent = new Intent(AdressSelectActivity.this, AddressManageActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void getData() {
        showPreDialog("正在加载");

        new RetrofitRequest<AddressListObject>(ApiRequest.getApiShiji().addressList())
                .handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    hidePreDialog();
                    AddressListObject obj = (AddressListObject) msg.obj;
                    mList.clear();
                    if (obj.list != null && obj.list.size() > 0) {
                        mList = obj.list;
                        for (int i = 0; i < mList.size(); i++) {
                            if (addressId > 0) {
                                if (mList.get(i).getId() == addressId) {
                                    mList.get(i).setSelected(true);
                                }
                            }
                        }
                        mAdapter = new AdressAdapter(AdressSelectActivity.this, mList);
                        mListView.setAdapter(mAdapter);
                    }
                } else {
                    showTips(msg.message);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }
}
