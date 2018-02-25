package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.CommunityHomePageActivity;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.entity.CommentItem;
import cn.yiya.shiji.utils.DateTimeFormat;
import cn.yiya.shiji.utils.Util;

/**
 * Created by Amy on 2016/6/17.
 */
public class NewWorkCommentsAdapter extends BaseAdapter {
    private Context mContext;
    private Handler mHandler;

    private ArrayList<CommentItem> mList;

    public NewWorkCommentsAdapter(Context mContext, ArrayList<CommentItem> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mHandler = new Handler(mContext.getMainLooper());
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_new_comment, parent, false);
            holder.tvCommentUserName = (TextView) convertView.findViewById(R.id.tv_comment_user_name);
            holder.tvComment = (TextView) convertView.findViewById(R.id.tv_comment);
            holder.tvComment.setSingleLine(true);
            holder.tvComment.setEllipsize(TextUtils.TruncateAt.END);
            holder.tvCommentTime = (TextView) convertView.findViewById(R.id.tv_comment_time);
            holder.ivCommentUserAvatar = (SimpleDraweeView) convertView.findViewById(R.id.iv_comment_user_avatar);
            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();
        final CommentItem item = mList.get(position);
        holder.tvCommentUserName.setText(item.getUser_name());
        holder.tvComment.setText(item.getComment());
        holder.tvCommentTime.setText(DateTimeFormat.formatLatelyTime(item.getTime()));
        holder.ivCommentUserAvatar.setImageURI(Util.transfer(item.getUser_head()));
        holder.ivCommentUserAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击跳转到用户详情页
                Intent intent = new Intent(mContext, CommunityHomePageActivity.class);
                intent.putExtra("user_id", item.getUser_id());
                if (BaseApplication.getInstance().readUserId().equals(String.valueOf(item.getUser_id()))) {
                    intent.putExtra("isCurUser", true);
                }
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tvCommentUserName, tvComment, tvCommentTime;
        SimpleDraweeView ivCommentUserAvatar;
    }
}
