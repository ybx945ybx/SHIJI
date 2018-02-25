package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.duowan.mobile.netroid.image.NetworkImageView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.CommunityHomePageActivity;
import cn.yiya.shiji.activity.LoginActivity;
import cn.yiya.shiji.activity.NewCommentsActivity;
import cn.yiya.shiji.activity.NewMatchDetailActivity;
import cn.yiya.shiji.activity.NewWorkDetailsActivity;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.NotifyListItem;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.DateTimeFormat;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.FloatingActionButton;
import cn.yiya.shiji.views.RoundedImageView;

/**
 * Created by Tom on 2016/8/17.
 */
public class CommunityMessageAdapter extends RecyclerView.Adapter<CommunityMessageAdapter.CommunityMessageViewHolder> {
    private Context mContext;
    private ArrayList<NotifyListItem> mList = new ArrayList<>();

    public ArrayList<NotifyListItem> getmList() {
        return mList;
    }

    public void setmList(ArrayList<NotifyListItem> mList) {
        this.mList = mList;
    }

    public CommunityMessageAdapter (Context mContext){
        this.mContext = mContext;
    }

    @Override
    public CommunityMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommunityMessageViewHolder(LayoutInflater.from(mContext).inflate(R.layout.community_message_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final CommunityMessageViewHolder holder, int position) {
        final NotifyListItem item = mList.get(position);

        Netroid.displayImage(Util.transfer(item.getContent().getHead()), holder.ivHead, R.mipmap.user_icon_default);
        holder.tvUser.setText(item.getContent().getUser());
        holder.tvDes.setText(item.getDes());
        //0:回复,1:点赞,5:用户关注 type    work_type":2,//2为搭配，其余或不存在为笔记
        if (item.getType() == 5){
            holder.ivWork.setVisibility(View.GONE);
            holder.fbFollow.setVisibility(View.VISIBLE);
            if(item.getContent().getIs_follow() == 1){
//                holder.fbFollow.setLineMorphingState(0, false);
                holder.fbFollow.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.fbFollow.setLineMorphingState(1, false);
                    }
                }, 200);
            }else {
                holder.fbFollow.setLineMorphingState(0, false);
            }
        }else {
            Netroid.displayImage(item.getContent().getWork_image(), holder.ivWork, R.mipmap.work_default);
            holder.ivWork.setVisibility(View.VISIBLE);
            holder.fbFollow.setVisibility(View.GONE);
        }
        holder.tvTime.setText(DateTimeFormat.formatTime(item.getTime()));

        holder.ivHead.setOnClickListener(new View.OnClickListener() {   // 点击头像跳转个人中心
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(mContext, CommunityHomePageActivity.class);
                if(item.getContent().getUser_id() == 0){

                    intent1.putExtra("user_id", item.getContent().getFollow_user());
                }else {
                    intent1.putExtra("user_id", item.getContent().getUser_id());
                }
                intent1.putExtra("isCurUser", false);
                mContext.startActivity(intent1);
            }
        });

        holder.ivWork.setOnClickListener(new View.OnClickListener() {  // 点击图片跳转作品详情
            @Override
            public void onClick(View v) {
                Intent intent;
                if(item.getContent().getWork_type() == 2){
                    intent = new Intent(mContext, NewMatchDetailActivity.class);
                    intent.putExtra("work_id", item.getContent().getWork_id());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }else {
                    intent = new Intent(mContext, NewWorkDetailsActivity.class);
                    intent.putExtra("work_id",item.getContent().getWork_id());
//                    intent.putExtra("goodsId", item.getImages().get(0).getGoods_id());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }

            }
        });

        holder.fbFollow.setOnClickListener(new View.OnClickListener() {  //  关注按钮
            @Override
            public void onClick(View v) {
//                holder.fbFollow.setLineMorphingState((holder.fbFollow.getLineMorphingState() + 1) % 2, true);
                if (item.getContent().getIs_follow() == 1) {
                    new RetrofitRequest<>(ApiRequest.getApiShiji().setUnFollow(String.valueOf(item.getContent().getFollow_user()))).handRequest(
                            new MsgCallBack() {
                                @Override
                                public void onResult(HttpMessage msg) {
                                    if (msg.isSuccess()) {
                                        item.getContent().setIs_follow(0);
                                        holder.fbFollow.setLineMorphingState((holder.fbFollow.getLineMorphingState() + 1) % 2, true);
                                        showTips("取消关注");
                                    }
                                }
                            }
                    );
                }else {
                    new RetrofitRequest<>(ApiRequest.getApiShiji().setFollow(String.valueOf(item.getContent().getFollow_user()))).handRequest(
                            new MsgCallBack() {
                                @Override
                                public void onResult(HttpMessage msg) {
                                    if (msg.isSuccess()) {
                                        item.getContent().setIs_follow(1);
                                        holder.fbFollow.setLineMorphingState((holder.fbFollow.getLineMorphingState() + 1) % 2, true);
                                        showTips("关注");
                                    }
                                }
                            });
                }
            }
        });

        holder.tvDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.getType() == 0){
                    Intent intent3 = new Intent(mContext, NewCommentsActivity.class);
                    intent3.putExtra("work_id", item.getContent().getWork_id());
                    mContext.startActivity(intent3);
                }else if (item.getType() == 1){
                    Intent intent;
                    if(item.getContent().getWork_type() == 2){
                        intent = new Intent(mContext, NewMatchDetailActivity.class);
                        intent.putExtra("work_id", item.getContent().getWork_id());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }else {
                        intent = new Intent(mContext, NewWorkDetailsActivity.class);
                        intent.putExtra("work_id",item.getContent().getWork_id());
//                    intent.putExtra("goodsId", item.getImages().get(0).getGoods_id());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if(mList == null){
            return 0;
        }else {
            return mList.size();
        }
    }

    private void showTips(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    class CommunityMessageViewHolder extends RecyclerView.ViewHolder{

        RoundedImageView ivHead;
        TextView tvUser;
        TextView tvDes;
        TextView tvTime;
        NetworkImageView ivWork;
        FloatingActionButton fbFollow;

        public CommunityMessageViewHolder(View itemView) {
            super(itemView);
            ivHead = (RoundedImageView) itemView.findViewById(R.id.iv_head);
            tvUser = (TextView) itemView.findViewById(R.id.tv_name);
            tvDes = (TextView) itemView.findViewById(R.id.tv_desc);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            ivWork = (NetworkImageView) itemView.findViewById(R.id.iv_work);
            fbFollow = (FloatingActionButton) itemView.findViewById(R.id.follow_status);

        }
    }

}
