package cn.yiya.shiji.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.views.FullyLinearLayoutManager;

/**
 * Created by Tom on 2017/1/19.
 */

public class LevelPowerAdapter extends RecyclerView.Adapter<LevelPowerAdapter.LevelPowerViewHolder> {

    private Context mContext;
    private boolean red;
    private List<String> mList;

    public LevelPowerAdapter(Context mContext, boolean red, List<String> mList){
        this.mContext = mContext;
        this.red = red;
        this.mList = mList;
    }

    public List<String> getmList() {
        return mList;
    }

    public void setmList(List<String> mList) {
        this.mList = mList;
    }

    @Override
    public LevelPowerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LevelPowerViewHolder(LayoutInflater.from(mContext).inflate(R.layout.level_power_item, parent, false));
    }

    @Override
    public void onBindViewHolder(LevelPowerViewHolder holder, int position) {
        holder.tvLevelNeed.setText(mList.get(position));
        if(position == 0){
            holder.viewCirle.setVisibility(View.VISIBLE);
            holder.tvLevelNeed.setTextSize(14);
            if(red){
                holder.tvLevelNeed.setTextColor(Color.parseColor("#ed5137"));
                holder.viewCirle.setBackgroundResource(R.drawable.level_red_cirle);
            }else {
                holder.tvLevelNeed.setTextColor(Color.parseColor("#ffffff"));
                holder.viewCirle.setBackgroundResource(R.drawable.level_gray_cirle);
            }
        }else {
            holder.viewCirle.setVisibility(View.INVISIBLE);
            holder.tvLevelNeed.setTextColor(Color.parseColor("#ffffff"));
            holder.tvLevelNeed.setTextSize(12);
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class LevelPowerViewHolder extends RecyclerView.ViewHolder{
        View viewCirle;
        TextView tvLevelNeed;

        public LevelPowerViewHolder(View itemView) {
            super(itemView);
            viewCirle = itemView.findViewById(R.id.view_cirle);
            tvLevelNeed = (TextView) itemView.findViewById(R.id.tv_power_detail);
        }
    }
}
