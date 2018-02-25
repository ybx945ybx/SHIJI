package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.DownloadFileActivity;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.download.DownLoadListener;
import cn.yiya.shiji.download.DownLoadManager;
import cn.yiya.shiji.download.SQLDownLoadInfo;
import cn.yiya.shiji.download.TaskInfo;
import cn.yiya.shiji.entity.OutLineCountryInfo;
import cn.yiya.shiji.entity.StatusCode;
import cn.yiya.shiji.entity.navigation.LocalCityInfo;
import cn.yiya.shiji.entity.navigation.LocalCountryInfo;
import cn.yiya.shiji.entity.navigation.LocalObject;
import cn.yiya.shiji.entity.navigation.LocalPackageInfo;
import cn.yiya.shiji.views.HorizontalProgressBarWithNumber;
import cn.yiya.shiji.views.ProgressDialog;

/**
 * Created by chenjian on 2016/3/29.
 */
public class DownloadAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private ArrayList<OutLineCountryInfo> mLists;
    private DownLoadManager downLoadManager;
    private ArrayList<TaskInfo> listdata;
    private final static String SAVEFLAG = "saveflag";

    public DownloadAdapter(Context mContext, ArrayList<OutLineCountryInfo> mLists, DownLoadManager downLoadManager
                            , ArrayList<TaskInfo> data) {
        this.mContext = mContext;
        this.mLists = mLists;
        this.downLoadManager = downLoadManager;
        this.listdata = data;
    }

    @Override
    public int getGroupCount() {
        return mLists.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mLists.get(groupPosition).getCity_list().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mLists.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mLists.get(groupPosition).getCity_list().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.group_download, parent, false);
            vh = new ViewHolder();
            vh.tvCountry = (TextView)convertView.findViewById(R.id.country_name);
            vh.ivTip = (ImageView)convertView.findViewById(R.id.countyr_arrow);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder)convertView.getTag();
        }

        OutLineCountryInfo mGroup = mLists.get(groupPosition);

        vh.tvCountry.setText(mGroup.getCountry_cn_name());
        if (isExpanded) {
            vh.ivTip.setImageResource(R.mipmap.arrow_up);
        } else {
            vh.ivTip.setImageResource(R.mipmap.arrow_down);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.child_download, parent, false);
            vh = new ViewHolder();
            vh.tvCnCity = (TextView) convertView.findViewById(R.id.city_cn_name);
            vh.tvEnCity = (TextView) convertView.findViewById(R.id.city_en_name);
            vh.ivStart = (ImageView) convertView.findViewById(R.id.wait_download);
            vh.tvStatus = (TextView) convertView.findViewById(R.id.download_status);
            vh.pgrDownload = (HorizontalProgressBarWithNumber) convertView.findViewById(R.id.download_progress);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder)convertView.getTag();
        }

        OutLineCountryInfo.CityListEntity mChild = mLists.get(groupPosition).getCity_list().get(childPosition);
        vh.tvCnCity.setText(mChild.getCn_name());
        vh.tvEnCity.setText("(" + mChild.getName() + ")");

        switch (mChild.getStatus()) {
            case StatusCode.DOWNLOADING:
                vh.ivStart.setVisibility(View.GONE);
                vh.pgrDownload.setVisibility(View.VISIBLE);
                vh.pgrDownload.setProgress(getTaskInfoProgress(mChild.getId()));
                downLoadManager.setSingleTaskListener(mChild.getId(), new DownloadManagerListener());
                vh.tvStatus.setText("正在下载");
                break;
            case StatusCode.UNDOWNLOAD:
                vh.ivStart.setVisibility(View.VISIBLE);
                vh.pgrDownload.setVisibility(View.GONE);
                vh.tvStatus.setText(mChild.getSize());
                break;
            case StatusCode.SUCCESS_DOWNLOAD:
                vh.ivStart.setVisibility(View.GONE);
                vh.pgrDownload.setVisibility(View.GONE);
                vh.tvStatus.setText("下载完成");
                break;
            case StatusCode.PAUSE_DOWNLOAD:
                vh.ivStart.setVisibility(View.GONE);  
                vh.pgrDownload.setVisibility(View.VISIBLE);
                vh.pgrDownload.setProgress(getTaskInfoProgress(mChild.getId()));
                vh.tvStatus.setText("点击继续下载");
                break;
            case StatusCode.FAIL_DOWNLOAD:
                vh.ivStart.setVisibility(View.GONE);
                vh.pgrDownload.setVisibility(View.GONE);
                vh.tvStatus.setText("下载失败，点击重试");
                break;
            case StatusCode.UNZIP_DOWNLOAD:
                vh.ivStart.setVisibility(View.GONE);
                vh.pgrDownload.setVisibility(View.GONE);
                vh.tvStatus.setText("正在解压");
                break;
        }

        convertView.setOnClickListener(new Child_ClickLitener(groupPosition, childPosition, vh));

        return convertView;
    }

    private int getTaskInfoProgress(String id) {
        for (int i = 0; i < listdata.size(); i++) {
            if (listdata.get(i).getTaskID().equals(id))
                return listdata.get(i).getProgress();
        }

        return 0;
    }

    class Child_ClickLitener implements View.OnClickListener {

        ViewHolder viewHolder;
        OutLineCountryInfo.CityListEntity mInfo;
        String nCountryID;
        int nGroup;
        int nChild;
        boolean bWIFI;

        public Child_ClickLitener(int group, int child, ViewHolder vh) {
            this.viewHolder = vh;
            this.nGroup = group;
            this.nChild = child;
            this.mInfo = mLists.get(group).getCity_list().get(child);
            this.nCountryID = mLists.get(group).getCountry_id();
            bWIFI = ((DownloadFileActivity)mContext).bWIFI();
        }

        @Override
        public void onClick(View v) {
            switch (mInfo.getStatus()) {
                case StatusCode.DOWNLOADING:
                    viewHolder.ivStart.setVisibility(View.GONE);
                    viewHolder.tvStatus.setText("点击继续下载");
                    mInfo.setStatus(StatusCode.PAUSE_DOWNLOAD);
                    downLoadManager.stopTask(mInfo.getId());
                    notifyDataSetChanged();
                    break;
                case StatusCode.UNDOWNLOAD:
                    if (!bWIFI) {
                        ProgressDialog.showDialog(mContext, "您现在使用的是运营商网络，下载可能会产生超额流量费用",
                                new ProgressDialog.DialogCallBack() {
                            @Override
                            public void clickCall() {
                                downLoadManager.addTask(nCountryID, mInfo.getId(), mInfo.getId(), mInfo.getDownload_url()
                                        , "version.tar.gz");
                                downLoadManager.setSingleTaskListener(mInfo.getId(), new DownloadManagerListener());
                                TaskInfo info = new TaskInfo();
                                info.setOnDownloading(true);
                                info.setTaskID(mInfo.getId());
                                addItem(info);
                                addLocal(nGroup, nChild);
                                viewHolder.ivStart.setVisibility(View.GONE);
                                viewHolder.tvStatus.setText("正在下载");
                                mInfo.setStatus(StatusCode.DOWNLOADING);
                                notifyDataSetChanged();
                            }
                        });
                    } else {
                        downLoadManager.addTask(nCountryID, mInfo.getId(), mInfo.getId(), mInfo.getDownload_url()
                                , "version.tar.gz");
                        downLoadManager.setSingleTaskListener(mInfo.getId(), new DownloadManagerListener());
                        TaskInfo info = new TaskInfo();
                        info.setOnDownloading(true);
                        info.setTaskID(mInfo.getId());
                        addItem(info);
                        addLocal(nGroup, nChild);
                        viewHolder.ivStart.setVisibility(View.GONE);
                        viewHolder.tvStatus.setText("正在下载");
                        mInfo.setStatus(StatusCode.DOWNLOADING);
                        notifyDataSetChanged();
                    }

                    break;
                case StatusCode.PAUSE_DOWNLOAD:
                    if (!bWIFI) {
                        ProgressDialog.showDialog(mContext, "您现在使用的是运营商网络，下载可能会产生超额流量费用",
                        new ProgressDialog.DialogCallBack() {
                            @Override
                            public void clickCall() {
                                downLoadManager.setSingleTaskListener(mInfo.getId(), new DownloadManagerListener());
                                viewHolder.ivStart.setVisibility(View.GONE);
                                viewHolder.tvStatus.setText("正在下载");
                                mInfo.setStatus(StatusCode.DOWNLOADING);
                                downLoadManager.startTask(mInfo.getId());
                                notifyDataSetChanged();
                            }
                        });
                    } else {
                        downLoadManager.setSingleTaskListener(mInfo.getId(), new DownloadManagerListener());
                        viewHolder.ivStart.setVisibility(View.GONE);
                        viewHolder.tvStatus.setText("正在下载");
                        mInfo.setStatus(StatusCode.DOWNLOADING);
                        downLoadManager.startTask(mInfo.getId());
                        notifyDataSetChanged();
                    }

                    break;
                case StatusCode.FAIL_DOWNLOAD:
                    downLoadManager.setSingleTaskListener(mInfo.getId(), new DownloadManagerListener());
                    viewHolder.ivStart.setVisibility(View.GONE);
                    viewHolder.tvStatus.setText("正在下载");
                    mInfo.setStatus(StatusCode.DOWNLOADING);
                    downLoadManager.startTask(mInfo.getId());
                    notifyDataSetChanged();
                    break;
            }

        }
    }

    // 添加本地数据
    private void addLocal(int group, int child) {

        SharedPreferences sp = mContext.getSharedPreferences(Configration.SHAREDPREFERENCE, mContext.MODE_PRIVATE);
        String jsonSave = sp.getString(SAVEFLAG, "");
        LocalObject object = null;
        if (!TextUtils.isEmpty(jsonSave)) {
            object = new Gson().fromJson(jsonSave, LocalObject.class);
        } else {
            object = new LocalObject();
        }

        ArrayList<LocalCountryInfo> infos = object.getPakages();

        // 如果国家数据为空
        if (infos == null || infos.size() == 0) {
            // 添加进去
            infos = new ArrayList<>();
            LocalCountryInfo countryInfo = new LocalCountryInfo();
            countryInfo.setCountryID(mLists.get(group).getCountry_id());
            countryInfo.setCountryName(mLists.get(group).getCountry_name());
            countryInfo.setCountryCnName(mLists.get(group).getCountry_cn_name());
            countryInfo.setbSuccess(false);

            // 添加城市
            ArrayList<LocalCityInfo> cityInfos = new ArrayList<>();
            LocalCityInfo cInfo = new LocalCityInfo();
            cInfo.setCityID(mLists.get(group).getCity_list().get(child).getId());
            cInfo.setCityName(mLists.get(group).getCity_list().get(child).getName());
            cInfo.setCityCnName(mLists.get(group).getCity_list().get(child).getCn_name());
            cInfo.setbSuccess(false);
            cInfo.setNewVersion(0);

            // 添加城市版本信息
            ArrayList<LocalPackageInfo> versionInfos = new ArrayList<>();
            LocalPackageInfo vInfo = new LocalPackageInfo();
            vInfo.setMd5(mLists.get(group).getCity_list().get(child).getMd5());
            vInfo.setStatus(0);
            vInfo.setSize(mLists.get(group).getCity_list().get(child).getSize());
            vInfo.setUrl(mLists.get(group).getCity_list().get(child).getDownload_url());
            vInfo.setVersion(mLists.get(group).getCity_list().get(child).getVersion());

            // 版本列表添加单独版本信息
            versionInfos.add(vInfo);
            // 城市添加版本列表
            cInfo.setVersions(versionInfos);
            // 城市列表添加城市
            cityInfos.add(cInfo);
            // 国家添加城市列表
            countryInfo.setCities(cityInfos);
            // 国家列表添加国家
            infos.add(countryInfo);
        } else {
            // 判断国家是否存在
            boolean bCountry = false;
            for (int i = 0; i < infos.size(); i++) {
                // 国家存在
                if (infos.get(i).getCountryID().equals(mLists.get(group).getCountry_id())) {
                    bCountry = true;
                    ArrayList<LocalCityInfo> cityInfos = infos.get(i).getCities();
                    // 城市列表为空
                    if (cityInfos.size() == 0) {
                        LocalCityInfo cInfo = new LocalCityInfo();
                        cInfo.setCityID(mLists.get(group).getCity_list().get(child).getId());
                        cInfo.setCityName(mLists.get(group).getCity_list().get(child).getName());
                        cInfo.setCityCnName(mLists.get(group).getCity_list().get(child).getCn_name());
                        cInfo.setbSuccess(false);
                        cInfo.setNewVersion(0);

                        ArrayList<LocalPackageInfo> versionInfos = new ArrayList<>();
                        LocalPackageInfo vInfo = new LocalPackageInfo();
                        vInfo.setMd5(mLists.get(group).getCity_list().get(child).getMd5());
                        vInfo.setStatus(0);
                        vInfo.setSize(mLists.get(group).getCity_list().get(child).getSize());
                        vInfo.setUrl(mLists.get(group).getCity_list().get(child).getDownload_url());
                        vInfo.setVersion(mLists.get(group).getCity_list().get(child).getVersion());
                        versionInfos.add(vInfo);
                        cInfo.setVersions(versionInfos);
                        cityInfos.add(cInfo);
                        infos.get(i).setCities(cityInfos);
                        // 城市列表存在
                    } else {

                        // 判断城市是否存在
                        boolean bCity = false;
                        for (int j = 0; j < cityInfos.size(); j++) {
                            // 单个城市已经存在
                            if (cityInfos.get(j).getCityID().equals(mLists.get(group).getCity_list().get(child).getId())) {
                                bCity = true;
                                ArrayList<LocalPackageInfo> versionInfos = cityInfos.get(j).getVersions();
                                // 城市中不存在版本列表，直接退出
                                if (versionInfos.size() == 0) {
                                    return;
                                // 城市版本列表中不存在该版本
                                } else {
                                    boolean bVersion = false;
                                    for (int k = 0; k < versionInfos.size(); k++) {
                                        // 版本列表中存在该版本，直接退出
                                        if (versionInfos.get(k).getVersion().equals(mLists.get(group).
                                                getCity_list().get(child).getVersion())){
                                            bVersion = true;
                                        }
                                    }

                                    if (!bVersion) {
                                        // 表示版本有更新
                                        cityInfos.get(j).setNewVersion(1);
                                        LocalPackageInfo vInfo = new LocalPackageInfo();
                                        vInfo.setMd5(mLists.get(group).getCity_list().get(child).getMd5());
                                        vInfo.setStatus(0);
                                        vInfo.setSize(mLists.get(group).getCity_list().get(child).getSize());
                                        vInfo.setUrl(mLists.get(group).getCity_list().get(child).getDownload_url());
                                        vInfo.setVersion(mLists.get(group).getCity_list().get(child).getVersion());
                                        // 将该版本信息添加进去
                                        versionInfos.add(vInfo);
                                        cityInfos.get(j).setVersions(versionInfos);
                                    }
                                }
                            }
                        }

                        // 如果城市不存在
                        if (!bCity) {
                            LocalCityInfo cInfo = new LocalCityInfo();
                            cInfo.setCityID(mLists.get(group).getCity_list().get(child).getId());
                            cInfo.setCityName(mLists.get(group).getCity_list().get(child).getName());
                            cInfo.setCityCnName(mLists.get(group).getCity_list().get(child).getCn_name());
                            cInfo.setbSuccess(false);
                            cInfo.setNewVersion(0);

                            ArrayList<LocalPackageInfo> versionInfos = new ArrayList<>();
                            LocalPackageInfo vInfo = new LocalPackageInfo();
                            vInfo.setMd5(mLists.get(group).getCity_list().get(child).getMd5());
                            vInfo.setStatus(0);
                            vInfo.setSize(mLists.get(group).getCity_list().get(child).getSize());
                            vInfo.setUrl(mLists.get(group).getCity_list().get(child).getDownload_url());
                            vInfo.setVersion(mLists.get(group).getCity_list().get(child).getVersion());
                            versionInfos.add(vInfo);
                            cInfo.setVersions(versionInfos);
                            cityInfos.add(cInfo);
                            infos.get(i).setCities(cityInfos);
                        }
                    }
                }
            }

            // 如果该国家不存在
            if (!bCountry) {
                LocalCountryInfo countryInfo = new LocalCountryInfo();
                countryInfo.setCountryID(mLists.get(group).getCountry_id());
                countryInfo.setCountryName(mLists.get(group).getCountry_name());
                countryInfo.setCountryCnName(mLists.get(group).getCountry_cn_name());
                countryInfo.setbSuccess(false);

                // 添加城市
                ArrayList<LocalCityInfo> cityInfos = new ArrayList<>();
                LocalCityInfo cInfo = new LocalCityInfo();
                cInfo.setCityID(mLists.get(group).getCity_list().get(child).getId());
                cInfo.setCityName(mLists.get(group).getCity_list().get(child).getName());
                cInfo.setCityCnName(mLists.get(group).getCity_list().get(child).getCn_name());
                cInfo.setbSuccess(false);
                cInfo.setNewVersion(0);

                // 添加城市版本信息
                ArrayList<LocalPackageInfo> versionInfos = new ArrayList<>();
                LocalPackageInfo vInfo = new LocalPackageInfo();
                vInfo.setMd5(mLists.get(group).getCity_list().get(child).getMd5());
                vInfo.setStatus(1);
                vInfo.setSize(mLists.get(group).getCity_list().get(child).getSize());
                vInfo.setUrl(mLists.get(group).getCity_list().get(child).getDownload_url());
                vInfo.setVersion(mLists.get(group).getCity_list().get(child).getVersion());
                versionInfos.add(vInfo);
                cInfo.setVersions(versionInfos);
                cityInfos.add(cInfo);
                countryInfo.setCities(cityInfos);
                infos.add(countryInfo);
            }
        }

        object.setPakages(infos);
        SharedPreferences.Editor editor = sp.edit();
        String json = new Gson().toJson(object);
        editor.putString(SAVEFLAG, json);
        editor.commit();
    }

    private class DownloadManagerListener implements DownLoadListener {

        @Override
        public void onStart(SQLDownLoadInfo sqlDownLoadInfo) {

        }

        @Override
        public void onProgress(SQLDownLoadInfo sqlDownLoadInfo, boolean isSupportBreakpoint) {
            //根据监听到的信息查找列表相对应的任务，更新相应任务的进度
            for(TaskInfo taskInfo : listdata){
                if(taskInfo.getTaskID().equals(sqlDownLoadInfo.getTaskID())){
                    taskInfo.setDownFileSize(sqlDownLoadInfo.getDownloadSize());
                    taskInfo.setFileSize(sqlDownLoadInfo.getFileSize());
                    notifyDataSetChanged();
                    break;
                }
            }
        }

        @Override
        public void onStop(SQLDownLoadInfo sqlDownLoadInfo, boolean isSupportBreakpoint) {

        }

        @Override
        public void onSuccess(SQLDownLoadInfo sqlDownLoadInfo) {
            //根据监听到的信息查找列表相对应的任务，删除对应的任务
            int nGroup = mLists.size();
            for (int i = 0; i < nGroup; i++) {
                for (OutLineCountryInfo.CityListEntity cityInfo : mLists.get(i).getCity_list()) {
                    if (cityInfo.getId().equals(sqlDownLoadInfo.getTaskID())) {
                        cityInfo.setStatus(StatusCode.SUCCESS_DOWNLOAD);
                        notifyDataSetChanged();
                        break;
                    }
                }
            }
        }

        @Override
        public void onError(SQLDownLoadInfo sqlDownLoadInfo) {
            //根据监听到的信息查找列表相对应的任务，停止该任务
            int nGroup = mLists.size();
            for (int i = 0; i < nGroup; i++) {
                for (OutLineCountryInfo.CityListEntity cityInfo : mLists.get(i).getCity_list()) {
                    if (cityInfo.getId().equals(sqlDownLoadInfo.getTaskID())) {
                        cityInfo.setStatus(StatusCode.FAIL_DOWNLOAD);
                        notifyDataSetChanged();
                        break;
                    }
                }
            }
        }

        @Override
        public void onUnZip(SQLDownLoadInfo sqlDownLoadInfo) {
            int nGroup = mLists.size();
            for (int i = 0; i < nGroup; i++) {
                for (OutLineCountryInfo.CityListEntity cityInfo : mLists.get(i).getCity_list()) {
                    if (cityInfo.getId().equals(sqlDownLoadInfo.getTaskID())) {
                        cityInfo.setStatus(StatusCode.UNZIP_DOWNLOAD);
                        notifyDataSetChanged();
                        break;
                    }
                }
            }
        }
    }

    public void addItem(TaskInfo taskinfo){
        this.listdata.add(taskinfo);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    class ViewHolder {
        TextView tvCountry;
        TextView tvEnCity;
        TextView tvCnCity;
        ImageView ivTip;
        TextView tvStatus;
        ImageView ivStart;
        HorizontalProgressBarWithNumber pgrDownload;
    }
}
