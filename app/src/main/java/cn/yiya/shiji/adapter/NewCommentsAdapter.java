package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.CommunityHomePageActivity;
import cn.yiya.shiji.entity.CommentItem;
import cn.yiya.shiji.utils.DateTimeFormat;
import cn.yiya.shiji.utils.Util;

/**
 * Created by muaijuan on 16/6/16.
 */
public class NewCommentsAdapter extends RecyclerView.Adapter<NewCommentsAdapter.CommentsViewHolder> {
    private Context mContext;
    private Handler mHandler;

    private ArrayList<CommentItem> mList;
    private OnReplyClickListener onReplyClickListener;

    public NewCommentsAdapter(Context mContext, ArrayList<CommentItem> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mHandler = new Handler(mContext.getMainLooper());
    }

    @Override
    public CommentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommentsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_new_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(CommentsViewHolder holder, final int position) {
        final CommentItem item = mList.get(position);
        if (position == mList.size() - 1)
            holder.viewline.setVisibility(View.GONE);
        else holder.viewline.setVisibility(View.VISIBLE);
        holder.tvCommentUserName.setText(item.getUser_name());
        if (item.getRed() == 1) {
            holder.ivRedpeople.setVisibility(View.VISIBLE);
        } else {
            holder.ivRedpeople.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(item.getParent_comment())) {
            holder.tvComment.setText(item.getComment());
        } else {
            holder.tvComment.setText(item.getParent_comment() + " " + item.getComment());
        }
        holder.tvCommentTime.setText(DateTimeFormat.formatLatelyTime(item.getTime()));
        holder.ivCommentUserAvatar.setImageURI(Util.transfer(item.getUser_head()));
        holder.rlytCommentUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击跳转到用户详情页
                Intent intent = new Intent(mContext, CommunityHomePageActivity.class);
                intent.putExtra("user_id", item.getUser_id());
                mContext.startActivity(intent);
            }
        });
        holder.tvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onReplyClickListener != null) {
                    onReplyClickListener.OnReplyClick(item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public interface OnReplyClickListener {
        void OnReplyClick(CommentItem commentItem);
    }

    public void setOnReplyClickListener(OnReplyClickListener onReplyClickListener) {
        this.onReplyClickListener = onReplyClickListener;
    }

    class CommentsViewHolder extends RecyclerView.ViewHolder {
        TextView tvCommentUserName, tvComment, tvCommentTime;
        SimpleDraweeView ivCommentUserAvatar;
        View viewline;
        RelativeLayout rlytCommentUser;
        ImageView ivRedpeople;

        public CommentsViewHolder(View itemView) {
            super(itemView);
            tvCommentUserName = (TextView) itemView.findViewById(R.id.tv_comment_user_name);
            tvComment = (TextView) itemView.findViewById(R.id.tv_comment);
            tvCommentTime = (TextView) itemView.findViewById(R.id.tv_comment_time);
            ivCommentUserAvatar = (SimpleDraweeView) itemView.findViewById(R.id.iv_comment_user_avatar);
            viewline = itemView.findViewById(R.id.viewline);

            rlytCommentUser = (RelativeLayout) itemView.findViewById(R.id.rlyt_comment_user);
            ivRedpeople = (ImageView) itemView.findViewById(R.id.iv_red_people);
        }
    }

}
