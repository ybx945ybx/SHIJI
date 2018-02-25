package cn.yiya.shiji.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.LikerItem;
import cn.yiya.shiji.utils.DateTimeFormat;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.FloatingActionButton;

/**
 * 点赞人列表适配器
 * Created by Amy on 16/6/17.
 */
public class NewLikersAdapter extends RecyclerView.Adapter<NewLikersAdapter.LikersViewHolder> {
    private Context mContext;
    private Handler mHandler;
    private ArrayList<LikerItem> mList;
    private OnActionClickListener mListener;

    public NewLikersAdapter(Context mContext, ArrayList<LikerItem> mList, OnActionClickListener mListener) {
        this.mContext = mContext;
        this.mList = mList;
        this.mListener = mListener;
        mHandler = new Handler(mContext.getMainLooper());
    }

    @Override
    public LikersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LikersViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_liker, parent, false));
    }

    @Override
    public void onBindViewHolder(final LikersViewHolder holder, final int position) {
        final LikerItem item = mList.get(position);
        if (position == mList.size() - 1)
            holder.viewline.setVisibility(View.GONE);
        else holder.viewline.setVisibility(View.VISIBLE);
        holder.tvLikerName.setText(item.getName());
        holder.tvLikerTime.setText(DateTimeFormat.formatLatelyTime(item.getTime()));
        holder.ivLikerAvatar.setImageURI(Util.transfer(item.getHead()));
        if (item.getRed() == 1) {
            holder.ivRedPeople.setVisibility(View.VISIBLE);
        } else {
            holder.ivRedPeople.setVisibility(View.GONE);
        }
        holder.rlytLikerAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击跳转到用户详情页
                if (mListener != null) mListener.OnUserClick(position);
            }
        });
        if (item.getFollow() == 1) {
            holder.btnFollow.postDelayed(new Runnable() {
                @Override
                public void run() {
                    holder.btnFollow.setLineMorphingState(1, false);
                }
            }, 200);
        } else {
            holder.btnFollow.setLineMorphingState(0, false);
        }

        holder.btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击关注或取消关注
                clickFollow(holder, item);
            }
        });
    }

    /**
     * 点击关注按钮，关注或取消关注
     */
    private void clickFollow(LikersViewHolder holder, LikerItem item) {
        if (item == null) return;
        holder.btnFollow.setLineMorphingState((holder.btnFollow.getLineMorphingState() + 1) % 2, true);
        if (item.getFollow() == 1)
            unFollowUser(holder, item);
        else
            followUser(holder, item);
    }

    /**
     * 关注用户 接口 /user/follow-user
     *
     * @param holder
     */
    private void followUser(final LikersViewHolder holder, final LikerItem item) {
        new RetrofitRequest<>(ApiRequest.getApiShiji().setFollow(String.valueOf(item.getUser_id()))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    item.setFollow(1);
                    Util.toast(mContext, "关注", true);
                } else if (msg.isLossLogin()) {
                    holder.btnFollow.setLineMorphingState((holder.btnFollow.getLineMorphingState() + 1) % 2, true);
                    if (mListener != null) mListener.goLogin();
                } else {
                    Util.toast(mContext, "关注失败", true);
                    holder.btnFollow.setLineMorphingState((holder.btnFollow.getLineMorphingState() + 1) % 2, true);
                }
            }
        });
    }

    /**
     * 取消关注用户 接口/user/unfollow-user
     *
     * @param holder
     */
    private void unFollowUser(final LikersViewHolder holder, final LikerItem item) {
        new RetrofitRequest<>(ApiRequest.getApiShiji().setUnFollow(String.valueOf(item.getUser_id()))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    item.setFollow(2);
                    Util.toast(mContext, "取消关注", true);
                } else if (msg.isLossLogin()) {
                    holder.btnFollow.setLineMorphingState((holder.btnFollow.getLineMorphingState() + 1) % 2, true);
                    if (mListener != null) mListener.goLogin();
                } else {
                    Util.toast(mContext, "取消关注失败", true);
                    holder.btnFollow.setLineMorphingState((holder.btnFollow.getLineMorphingState() + 1) % 2, true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class LikersViewHolder extends RecyclerView.ViewHolder {
        TextView tvLikerName, tvLikerTime;
        SimpleDraweeView ivLikerAvatar;
        FloatingActionButton btnFollow;
        View viewline;
        RelativeLayout rlytLikerAvatar;
        ImageView ivRedPeople;

        public LikersViewHolder(View itemView) {
            super(itemView);
            tvLikerName = (TextView) itemView.findViewById(R.id.tv_liker_name);
            tvLikerTime = (TextView) itemView.findViewById(R.id.tv_liker_time);
            ivLikerAvatar = (SimpleDraweeView) itemView.findViewById(R.id.iv_liker_avatar);
            btnFollow = (FloatingActionButton) itemView.findViewById(R.id.btn_follow);
            viewline = itemView.findViewById(R.id.viewline);
            rlytLikerAvatar = (RelativeLayout) itemView.findViewById(R.id.rlyt_like_avatar);
            ivRedPeople = (ImageView) itemView.findViewById(R.id.iv_red_people);
        }
    }

    public interface OnActionClickListener {
        void OnUserClick(int position);

        void goLogin();
    }
}
