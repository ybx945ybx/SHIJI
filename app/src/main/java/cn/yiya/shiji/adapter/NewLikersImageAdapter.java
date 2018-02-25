package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.CommunityHomePageActivity;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.entity.LikerItem;
import cn.yiya.shiji.utils.Util;

/**
 * 笔记详情 点赞人头像列表 Adapter
 * Created by Amy on 2016/5/27.
 */
public class NewLikersImageAdapter extends RecyclerView.Adapter<NewLikersImageAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<LikerItem> likerItemList;

    public NewLikersImageAdapter(Context mContext) {
        this.mContext = mContext;
        this.likerItemList = new ArrayList<LikerItem>();
    }

    public void setList(ArrayList<LikerItem> list) {
        this.likerItemList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_liker_image, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final LikerItem item = likerItemList.get(position);
        holder.ivLikeUser.setImageURI(Util.transfer(item.getHead()));
        holder.ivLikeUser.setOnClickListener(new View.OnClickListener() {
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
    }

    @Override
    public int getItemCount() {
        return likerItemList == null ? 0 : likerItemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView ivLikeUser;

        public ViewHolder(View itemView) {
            super(itemView);
            ivLikeUser = (SimpleDraweeView) itemView.findViewById(R.id.iv_like_user);
        }
    }
}
