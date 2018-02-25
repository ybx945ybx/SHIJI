package cn.yiya.shiji.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.User;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.FloatingActionButton;
import cn.yiya.shiji.views.RoundedImageView;

public class UserFansAdapter extends RecyclerView.Adapter<UserFansAdapter.MyUserFansViewHolder> {
    private Context mContext;
    private ArrayList<User> mList;
    private OnActionClickListener mOnActionClickListener;
    private int type;                                           //  type是2是查看别人的关注粉丝  不可进行操作
    private boolean isCustom;                                    //  false是查看别人的关注粉丝  不可进行操作

    public UserFansAdapter(Context context, int type) {
        super();
        this.mContext = context;
        this.mList = new ArrayList<>();
        this.type = type;
    }

    public UserFansAdapter(Context context, boolean isCustom) {
        super();
        this.mContext = context;
        this.mList = new ArrayList<>();
        this.isCustom = isCustom;
    }

    public void setList(ArrayList<User> list) {
        this.mList = list;
    }

    public ArrayList<User> getList() {
        return mList;
    }

    @Override
    public MyUserFansViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyUserFansViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item_fans, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyUserFansViewHolder holder, final int position) {
        final User item = mList.get(position);

        holder.name.setText(item.getName());
        // 判断是否已关注
        if (item.is_follow()) {
            holder.status.postDelayed(new Runnable() {
                @Override
                public void run() {
                    holder.status.setLineMorphingState(1, false);
                }
            }, 200);
        } else {
            holder.status.setLineMorphingState(0, false);
        }

        Netroid.displayImage(Util.transfer(item.getHead()), holder.image, R.mipmap.user_icon_default);

        if(item.getRed() == 1){
            holder.ivRedPeople.setVisibility(View.VISIBLE);
        }else {
            holder.ivRedPeople.setVisibility(View.GONE);
        }

        if (isCustom) {
            holder.status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickFollow(item, holder);
                }
            });
        } else {
            holder.status.setClickable(false);
        }

        holder.llytUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnActionClickListener != null)
                    mOnActionClickListener.onUserClick(position);
            }
        });
    }


    private void clickFollow(final User user, final MyUserFansViewHolder holder) {
        holder.status.setLineMorphingState((holder.status.getLineMorphingState() + 1) % 2, true);
        if (!user.is_follow()) {
            new RetrofitRequest<>(ApiRequest.getApiShiji().setFollow(String.valueOf(user.getUser_id()))).handRequest(
                    new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            if (msg.isSuccess()) {
                                user.setIs_follow(true);
                                Util.toast(mContext, "关注", true);
                                if (mOnActionClickListener != null)
                                    mOnActionClickListener.onActionClick();
                            } else {
                                Util.toast(mContext, "关注失败", true);
                                holder.status.setLineMorphingState((holder.status.getLineMorphingState() + 1) % 2, true);
                            }
                        }
                    }
            );
        } else {
            new RetrofitRequest<>(ApiRequest.getApiShiji().setUnFollow(String.valueOf(user.getUser_id()))).handRequest(
                    new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            if (msg.isSuccess()) {
                                user.setIs_follow(false);
                                Util.toast(mContext, "取消关注", true);
                                if (mOnActionClickListener != null)
                                    mOnActionClickListener.onActionClick();
                            } else {
                                Util.toast(mContext, "取消关注失败", true);
                                holder.status.setLineMorphingState((holder.status.getLineMorphingState() + 1) % 2, true);
                            }
                        }
                    });
        }
    }


    @Override
    public int getItemCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

    public void setOnActionClickListener(OnActionClickListener onActionClickListener) {
        this.mOnActionClickListener = onActionClickListener;
    }

    public interface OnActionClickListener {
        void onActionClick();

        void onUserClick(int position);
    }

    class MyUserFansViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llytUser;
        TextView name;
        FloatingActionButton status;
        RoundedImageView image;
        ImageView ivRedPeople;

        public MyUserFansViewHolder(View itemView) {
            super(itemView);
            llytUser = (LinearLayout) itemView.findViewById(R.id.llyt_user);
            image = (RoundedImageView) itemView.findViewById(R.id.list_item_fans_image);
            ivRedPeople = (ImageView) itemView.findViewById(R.id.iv_red_people);
            name = (TextView) itemView.findViewById(R.id.list_item_fans_name);
            status = (FloatingActionButton) itemView.findViewById(R.id.list_item_fans_status);
        }
    }
}

