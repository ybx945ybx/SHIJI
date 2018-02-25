package cn.yiya.shiji.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.AppRecommendItem;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;

/**
 * Created by jerry on 2016/1/7.
 */
public class AppRecommendAdapter extends RecyclerView.Adapter<AppRecommendAdapter.AppRecommendViewHolder> {
    private ArrayList<AppRecommendItem> list;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public AppRecommendAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public AppRecommendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AppRecommendViewHolder(LayoutInflater.from(mContext).inflate(R.layout.app_recommend_item, parent, false));
    }

    @Override
    public void onBindViewHolder(AppRecommendViewHolder holder, final int position) {
        final AppRecommendItem item = list.get(position);
        GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) holder.cardView.getLayoutParams();
        layoutParams.height = SimpleUtils.getScreenWidth(mContext) / 3;
        layoutParams.width = SimpleUtils.getScreenWidth(mContext) / 3;
        holder.cardView.setLayoutParams(layoutParams);
        Netroid.displayImage(Util.transfer(item.getIcon()), holder.ivLogo);
        holder.tvName.setText(item.getTitle());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(item.getApk(), item.getTitle(), item.getDesc());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;

        }
        return list.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {

        void onItemClick(String url, String name, String desc);
    }

    public void setList(ArrayList<AppRecommendItem> list) {
        this.list = list;
    }

    class AppRecommendViewHolder extends RecyclerView.ViewHolder {
        ImageView ivLogo;
        TextView tvName;
        CardView cardView;

        public AppRecommendViewHolder(View itemView) {
            super(itemView);
            ivLogo = (ImageView) itemView.findViewById(R.id.image_logo);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
        }
    }
}


