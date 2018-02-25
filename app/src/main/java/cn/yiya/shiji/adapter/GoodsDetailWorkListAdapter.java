package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.NewWorkDetailsActivity;
import cn.yiya.shiji.entity.TagProduceItem;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.RoundedImageView;

/**
 * Created by Tom on 2016/11/9.
 */
public class GoodsDetailWorkListAdapter extends RecyclerView.Adapter<GoodsDetailWorkListAdapter.GoodsDetailWorkListViewHolder> {

    private Context mContext;
    private ArrayList<TagProduceItem> mWorklist;

    public GoodsDetailWorkListAdapter (Context mContext, ArrayList<TagProduceItem> mWorklist){
        this.mContext = mContext;
        this.mWorklist = mWorklist;
    }

    @Override
    public GoodsDetailWorkListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GoodsDetailWorkListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.home_detail_work_item, parent, false));
    }

    @Override
    public void onBindViewHolder(GoodsDetailWorkListViewHolder holder, int position) {
        final TagProduceItem item = mWorklist.get(position);

        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        lp.width = (SimpleUtils.getScreenWidth(mContext) - SimpleUtils.dp2px(mContext, 52)) * 3/7;
        int with = lp.width;
        lp.height = lp.width * 180/138;

        holder.itemView.setLayoutParams(lp);

        RelativeLayout.LayoutParams lpCover = (RelativeLayout.LayoutParams) holder.ivCover.getLayoutParams();
        lpCover.width = with;
        lpCover.height = with;
        holder.ivCover.setLayoutParams(lpCover);
        holder.ivCover.setImageURI(Util.transferImage(item.getImage(), with), R.mipmap.work_default);
        Netroid.displayImage(Util.transferImage(item.getUser_head(), SimpleUtils.dp2px(mContext, 34)), holder.ivUser, R.mipmap.user_icon_default);
        holder.tvUser.setText(item.getUser_name());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGoWorkDetail = new Intent(mContext, NewWorkDetailsActivity.class);
                intentGoWorkDetail.putExtra("work_id", item.getWork_id());
                mContext.startActivity(intentGoWorkDetail);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mWorklist == null ? 0 : mWorklist.size();
    }

    class GoodsDetailWorkListViewHolder extends RecyclerView.ViewHolder{
        private SimpleDraweeView ivCover;
        private RoundedImageView ivUser;
        private TextView tvUser;

        public GoodsDetailWorkListViewHolder(View itemView) {
            super(itemView);
            ivCover = (SimpleDraweeView) itemView.findViewById(R.id.iv_cover);
            ivUser = (RoundedImageView) itemView.findViewById(R.id.user_icon);
            tvUser = (TextView) itemView.findViewById(R.id.tv_user_name);
        }
    }
}
