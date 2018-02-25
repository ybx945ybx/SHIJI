package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.HomeIssueActivity;
import cn.yiya.shiji.activity.NewLocalWebActivity;
import cn.yiya.shiji.entity.NotifyListCommonItem;
import cn.yiya.shiji.entity.NotifyListItem;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.DateTimeFormat;
import cn.yiya.shiji.utils.Util;

/**
 * Created by chenjian on 2015/10/22.
 */
public class MessageCentListAdapter extends RecyclerView.Adapter<MessageCentListAdapter.MessageCenterListViewHolder> {
    private Context mContext;
    private ArrayList<NotifyListCommonItem> mLists;

    public MessageCentListAdapter(Context mContext, ArrayList<NotifyListCommonItem> mLists) {
        this.mContext = mContext;
        this.mLists = mLists;
    }

    public MessageCentListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public ArrayList<NotifyListCommonItem> getmLists() {
        return mLists;
    }

    public void setmLists(ArrayList<NotifyListCommonItem> mLists) {
        this.mLists = mLists;
    }

    @Override
    public MessageCenterListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessageCenterListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.message_center_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MessageCenterListViewHolder holder, int position) {
        // TODO: 2016/8/18
        final NotifyListCommonItem item = mLists.get(position);

        Netroid.displayImage(item.getIcon(), holder.ivIcon, R.mipmap.work_default);
        holder.tvTitle.setText(item.getTitle());
        holder.tvTime.setText(DateTimeFormat.formatTime(item.getTime()));
        if(TextUtils.isEmpty(item.getImage())){
            holder.ivImage.setVisibility(View.GONE);
        }else {
            Netroid.displayImage(item.getImage(), holder.ivImage, R.mipmap.work_default);
            holder.ivImage.setVisibility(View.VISIBLE);
        }
        holder.tvDes.setText(item.getDes());

        //0:系统消息,1:app内活动,2:H5活动
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (item.getType()){
                    case 0:
                        return;
                    case 1:
                        Intent intent = new Intent(mContext, HomeIssueActivity.class);
                        intent.putExtra("activityId", item.getUrl());
                        intent.putExtra("menuId", 7);
                        mContext.startActivity(intent);
                        break;
                    case 2:
                        Intent intent2 = new Intent(mContext, NewLocalWebActivity.class);
                        intent2.putExtra("url", item.getUrl());
                        intent2.putExtra("type", 3);
                        intent2.putExtra("title", item.getTitle());
                        intent2.putExtra("data", new Gson().toJson(item));
                        mContext.startActivity(intent2);
                        break;

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if(mLists == null){
            return 0;
        }else {
           return mLists.size();
        }
    }

    class MessageCenterListViewHolder extends RecyclerView.ViewHolder{
        ImageView ivIcon;
        TextView tvTitle;
        TextView tvTime;
        ImageView ivImage;
        TextView tvDes;

        public MessageCenterListViewHolder(View itemView) {
            super(itemView);
            ivIcon = (ImageView) itemView.findViewById(R.id.iv_icon);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
            tvDes = (TextView) itemView.findViewById(R.id.tv_des);
        }
    }
}
