package cn.yiya.shiji.adapter;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.views.FullyLinearLayoutManager;

/**
 * Created by Tom on 2017/1/18.
 */

public class LevelNeedAdapter extends RecyclerView.Adapter<LevelNeedAdapter.LevelNeedViewHolder> {

    private Context mContext;
    private List<List<String>> mList;
//    private List<Integer> levelList;
    private int myLevel;
    private boolean red;
    private int selectedPosition;

    public LevelNeedAdapter(Context mContext, int myLevel, List<List<String>> mList, int selectedPosition){
        this.mContext = mContext;
        this.mList = mList;
        this.myLevel = myLevel;
        this.selectedPosition = selectedPosition;
    }

    public List<List<String>> getmList() {
        return mList;
    }

    public void setmList(List<List<String>> mList) {
        this.mList = mList;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    @Override
    public LevelNeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LevelNeedViewHolder(LayoutInflater.from(mContext).inflate(R.layout.levle_need_item, parent, false));
    }

    @Override
    public void onBindViewHolder(LevelNeedViewHolder holder, int position) {
        if(selectedPosition <= myLevel){
            red = true;
        }else {
            red = false;
        }

        holder.rycvPower.setItemAnimator(new DefaultItemAnimator());
        holder.rycvPower.setLayoutManager(new FullyLinearLayoutManager(mContext));
        holder.rycvPower.setAdapter(new LevelPowerAdapter(mContext, red, mList.get(position)));

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class LevelNeedViewHolder extends RecyclerView.ViewHolder{

//        View viewCirle;
//        TextView tvLevelNeed;
        RecyclerView rycvPower;

        public LevelNeedViewHolder(View itemView) {
            super(itemView);

//            viewCirle = itemView.findViewById(R.id.view_cirle);
//            tvLevelNeed = (TextView) itemView.findViewById(R.id.tv_level_need);
            rycvPower = (RecyclerView) itemView.findViewById(R.id.rycv_power);

        }
    }
}
