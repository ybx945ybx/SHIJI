package cn.yiya.shiji.activity;
/**
 * 地址管理界面
 */

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.AddressItemAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.AddressListItem;
import cn.yiya.shiji.entity.AddressListObject;
import cn.yiya.shiji.utils.NetUtil;

public class AddressManageActivity extends BaseAppCompatActivity implements View.OnClickListener{

    private ImageView ivBack;
    private TextView tvTitle;

    private SwipeRefreshLayout srlytRefresh;
    private RecyclerView rycvAddress;
    private AddressItemAdapter addressAdapter;

    private TextView tvAddAddress;

    private static final int ADDRESS_ADD = 1111;
    private static final int ADDRESS_EDIT = 1112;

    private static final int DELETE_RESULT_OK = 111;
    private static final int SAVE_RESULT_OK = 222;

    private boolean isSelect;                             // true是选择地址

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_manage);
        initIntent();
        initViews();
        initEvents();
        init();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if(intent != null){
            isSelect = intent.getBooleanExtra("isSelect", false);
        }
    }

    private void initData() {
        showPreDialog("正在加载");
        new RetrofitRequest<AddressListObject>(ApiRequest.getApiShiji().addressList())
                .handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            hidePreDialog();
                            srlytRefresh.setRefreshing(false);
                            AddressListObject object = (AddressListObject) msg.obj;
                            if(object.list != null && object.list.size() > 0){
                                addressAdapter.setMlist(object.list);
                                addressAdapter.notifyDataSetChanged();
                                setSuccessView(srlytRefresh);
                            }else {
                                setNullView(srlytRefresh);
                            }
                        }else {
                            hidePreDialog();
                            srlytRefresh.setRefreshing(false);
                            showTips(msg.message);
                            if(!NetUtil.IsInNetwork(AddressManageActivity.this)){
                                setOffNetView(srlytRefresh);
                            }
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add_address:
                Intent intent = new Intent(this, EditAddressActivity.class);
                intent.putExtra("isSelect", isSelect);
                startActivityForResult(intent, ADDRESS_ADD);
                break;
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.tv_reload:
                initData();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADDRESS_EDIT) {
            switch (resultCode) {
                case DELETE_RESULT_OK:
                    if (data != null) {
                        int position = data.getIntExtra("position", -1);
                        if (position == -1) {
                            return;
                        }
                        addressAdapter.getMlist().remove(position);
                        addressAdapter.notifyDataSetChanged();
                        if(addressAdapter.getMlist().size() == 0){
                            setNullView(srlytRefresh);
                        }
                    }
                    break;
                case SAVE_RESULT_OK:
                    if (data != null) {
                        int position = data.getIntExtra("position", -1);
                        String addressinfo = data.getStringExtra("info");
                        AddressListItem info = new Gson().fromJson(addressinfo, AddressListItem.class);
                        if (position == -1) {
                            return;
                        }
                        addressAdapter.getMlist().remove(position);
                        addressAdapter.getMlist().add(position, info);
                        addressAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }else if(requestCode == ADDRESS_ADD){
            if(resultCode == SAVE_RESULT_OK){
                initData();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void initViews() {
        ivBack = (ImageView) findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText("地址管理");
        tvTitle.setTextColor(Color.parseColor("#212121"));
        findViewById(R.id.title_right).setVisibility(View.GONE);

        srlytRefresh = (SwipeRefreshLayout) findViewById(R.id.srlyt_address_list);
        rycvAddress = (RecyclerView) findViewById(R.id.rycv_address);
        rycvAddress.setLayoutManager(new LinearLayoutManager(this));
        rycvAddress.setItemAnimator(new DefaultItemAnimator());
        addressAdapter = new AddressItemAdapter(this, isSelect);
        rycvAddress.setAdapter(addressAdapter);

        tvAddAddress = (TextView) findViewById(R.id.tv_add_address);

        addDefaultNullView();
        initDefaultNullView(R.mipmap.zanwudizhi, "您还没有收货地址，赶紧创建吧~", this);
    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        tvAddAddress.setOnClickListener(this);
        addressAdapter.setOnItemClickListener(new AddressItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AddressListItem item, int position) {
                Intent intent = new Intent(AddressManageActivity.this, EditAddressActivity.class);
                intent.putExtra("info", new Gson().toJson(item));
                intent.putExtra("position", position);
                intent.putExtra("isSelect", isSelect);
                startActivityForResult(intent, ADDRESS_EDIT);
            }
        });
        if(isSelect) {
            addressAdapter.setOnItemSelectListener(new AddressItemAdapter.OnItemSelectListener() {
                @Override
                public void onItemSelect(AddressListItem item) {
                    Intent intent = new Intent();
                    String json = new Gson().toJson(item);
                    intent.putExtra("json", json);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }
        srlytRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
    }

    @Override
    protected void init() {
        initData();
    }
}
