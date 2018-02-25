package cn.yiya.shiji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.DownloadAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.download.DownLoadManager;
import cn.yiya.shiji.download.DownLoadService;
import cn.yiya.shiji.download.TaskInfo;
import cn.yiya.shiji.entity.OutLineCountryInfo;
import cn.yiya.shiji.entity.OutLineCountryObject;
import cn.yiya.shiji.entity.StatusCode;
import cn.yiya.shiji.utils.Util;

/**
 * Created by chenjian on 2016/3/28.
 */
public class DownloadFileActivity extends BaseAppCompatActivity implements View.OnClickListener{

    private ImageView ivBack;
    private TextView tvTitle;
    private ExpandableListView exListView;
    private View vHeadView;
    private ArrayList<OutLineCountryInfo> mLists;
    private ArrayList<OutLineCountryInfo.CityListEntity> mChild;
    private ArrayList<TaskInfo> mTasks;
    private DownLoadManager manager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_data);
        startService(new Intent(getApplicationContext(), DownLoadService.class));
        initViews();
        init();
        initEvents();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            manager = DownLoadService.getDownLoadManager();
            manager.changeUser(BaseApplication.FileId + "");
            manager.setSupportBreakpoint(true);
            mTasks = manager.getAllTask();
            exListView.setAdapter(new DownloadAdapter(DownloadFileActivity.this, mLists, manager, mTasks));
            for (int i = 0; i < mLists.size(); i++) {
                ArrayList<OutLineCountryInfo.CityListEntity> cityLists = mLists.get(i).getCity_list();
                for (OutLineCountryInfo.CityListEntity city : cityLists) {
                    city.setStatus(StatusCode.UNDOWNLOAD);
                    for(TaskInfo task : mTasks) {
                        if (task.getTaskID().equals(city.getId())) {
                            if (task.isHasDownload())
                                city.setStatus(StatusCode.SUCCESS_DOWNLOAD);
                            else if(task.isOnDownloading())
                                city.setStatus(StatusCode.DOWNLOADING);
                            else
                                city.setStatus(StatusCode.PAUSE_DOWNLOAD);
                        }
                    }
                }
            }
        }
    };

    @Override
    protected void initViews() {
        ivBack = (ImageView) findViewById(R.id.title_back);
        ivBack.setOnClickListener(this);
        findViewById(R.id.title_right).setVisibility(View.GONE);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText("离线信息下载");
        exListView = (ExpandableListView) findViewById(R.id.download_listview);
        exListView.setDivider(null);
        newDirFile();
        addHeadView();
    }

    @Override
    protected void initEvents() {
        exListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                int nSize = mLists.size();
                for (int i = 0; i < nSize; i++) {
                    if (i != groupPosition) {
                        exListView.collapseGroup(i);
                    }
                }
            }
        });
    }

    @Override
    protected void init() {
        getData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                finish();
                break;
        }

    }

    public boolean bWIFI() {
        return Util.GetNetworkType(this).equals("WIFI");
    }

    private void addHeadView() {
        vHeadView = LayoutInflater.from(this).inflate(R.layout.download_head, null);
        exListView.addHeaderView(vHeadView);
    }

    private void getData() {
        showPreDialog("正在加载...");
        new RetrofitRequest<OutLineCountryObject>(ApiRequest.getApiShiji().getOutLineList()).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            OutLineCountryObject object = (OutLineCountryObject)msg.obj;
                            ArrayList<OutLineCountryInfo> mInfos = object.getList();
                            if (mLists != null) {
                                mLists.clear();
                            }
                            mLists = mInfos;
                            handler.sendEmptyMessageDelayed(1, 50);
                        } else {
                            showPreDialog("获取数据失败");
                        }

                        hidePreDialog();
                    }
                }
        );
    }

    private void newDirFile() {

    }
}
