package cn.yiya.shiji.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.CommunityHomePageActivity;
import cn.yiya.shiji.activity.DiscoverFriendActivity;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.RememberUserItem;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.FloatingActionButton;
import cn.yiya.shiji.views.ProgressDialog;
import cn.yiya.shiji.views.RoundedImageView;

public class MyRememberUserAdapter extends RecyclerView.Adapter<MyRememberUserAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<RememberUserItem> mList;
    private OnActionClickListener mOnActionClickListener;
    private Handler mHandler;
    private Dialog progressDialog;

    public MyRememberUserAdapter(Context context) {
        super();
        this.mContext = context;
        this.mList = new ArrayList<>();
        mHandler = new Handler(Looper.getMainLooper());
    }

    public void setImageArrayParams(ImageView[] imageViews, List<RememberUserItem.Work> works) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.width = (SimpleUtils.getScreenWidth(mContext) - SimpleUtils.dp2px(mContext, 40)) / 3;
        layoutParams.height = layoutParams.width;
        layoutParams.rightMargin = SimpleUtils.dp2px(mContext, 4);
        layoutParams.leftMargin = SimpleUtils.dp2px(mContext, 4);
        for (int i = 0; i < 3; i++) {
            imageViews[i].setLayoutParams(layoutParams);
            if (works != null && works.size() > i) {
                final RememberUserItem.Work work = works.get(i);
                imageViews[i].setVisibility(View.VISIBLE);
                Netroid.displayImage(work.getImage(), imageViews[i]);
                imageViews[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnActionClickListener != null)
                            mOnActionClickListener.onWorkClick(work);
                    }
                });
            } else {
                imageViews[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    public void setList(ArrayList<RememberUserItem> mList) {
        this.mList = mList;
    }

    public ArrayList<RememberUserItem> getList() {
        return mList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item_remember_user, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final RememberUserItem item = mList.get(position);

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

        Netroid.displayImage(Util.transfer(item.getHead()), holder.avatar, R.mipmap.user_icon_default);
        setImageArrayParams(holder.images, item.getWorks());

        if(item.getRed() == 1){
            holder.ivRedPeople.setVisibility(View.VISIBLE);
        }else {
            holder.ivRedPeople.setVisibility(View.GONE);
        }
        holder.status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.is_follow()) {
                    return;
                }
                holder.status.setLineMorphingState((holder.status.getLineMorphingState() + 1) % 2, true);
                new RetrofitRequest<>(ApiRequest.getApiShiji().setFollow(String.valueOf(item.getUser_id()))).handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            item.setIs_follow(true);
                            Util.toast(mContext, "关注", true);
                            ((DiscoverFriendActivity) mContext).followChanged = true;
                        } else {
                            Util.toast(mContext, "关注失败", true);
                            holder.status.setLineMorphingState((holder.status.getLineMorphingState() + 1) % 2, true);
                        }
                    }
                });
            }
        });

        holder.llytUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(mContext, CommunityHomePageActivity.class);
                intent1.putExtra("user_id", item.getUser_id());
                intent1.putExtra("is_follow", item.is_follow());
                intent1.putExtra("isCurUser", false);
                mContext.startActivity(intent1);
            }
        });

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
        void onWorkClick(RememberUserItem.Work work);
    }

    /**
     * 加载前对话框
     *
     * @param str 提示文字，默认点击不消失
     */
    public void showPreDialog(String str) {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        progressDialog = ProgressDialog.creatRequestDialog(mContext, str, false);
        progressDialog.show();
    }

    public void hidePreDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llytUser;
        RoundedImageView avatar;
        ImageView ivRedPeople;
        TextView name;
        FloatingActionButton status;
        ImageView[] images = new ImageView[3];

        public MyViewHolder(View itemView) {
            super(itemView);
            llytUser = (LinearLayout) itemView.findViewById(R.id.llyt_user);
            avatar = (RoundedImageView) itemView.findViewById(R.id.list_item_remember_user_avatar);
            ivRedPeople = (ImageView) itemView.findViewById(R.id.iv_red_people);
            name = (TextView) itemView.findViewById(R.id.list_item_remember_user_name);
            status = (FloatingActionButton) itemView.findViewById(R.id.list_item_remember_user_status);

            images[0] = (ImageView) itemView.findViewById(R.id.list_item_remember_user_image0);
            images[1] = (ImageView) itemView.findViewById(R.id.list_item_remember_user_image1);
            images[2] = (ImageView) itemView.findViewById(R.id.list_item_remember_user_image2);

        }
    }
}

