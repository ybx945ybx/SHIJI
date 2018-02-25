package cn.yiya.shiji.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.UserGrowRankingEntity;
import cn.yiya.shiji.entity.UserGrowRuleEntity;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.RoundedImageView;

/**
 * Created by Tom on 2017/1/9.
 */

public class RedRangAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private List<UserGrowRankingEntity.RankingEntity> mList;
    private static final int TYPE_TIP = 100;
    private static final int TYPE_TOP_THREE_LIST = 101;
    private static final int TYPE_AFTER_THREE_LIST = 102;
    private UserGrowRuleEntity.RankingEntity rankingEntity;

    public RedRangAdapter (Context mContext, UserGrowRuleEntity.RankingEntity rankingEntity, List<UserGrowRankingEntity.RankingEntity> mList){
        this.mContext = mContext;
        this.mList = mList;
        this.rankingEntity = rankingEntity;
    }

    public List<UserGrowRankingEntity.RankingEntity> getmList() {
        return mList;
    }

    public void setmList(List<UserGrowRankingEntity.RankingEntity> mList) {
        this.mList = mList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_TIP:
                return new RedRangTipViewHolder(LayoutInflater.from(mContext).inflate(R.layout.red_rang_tip_item, parent, false));
            case TYPE_TOP_THREE_LIST:
                return new RedRangTopViewHolder(LayoutInflater.from(mContext).inflate(R.layout.red_rang_top_three_item, parent, false));
            case TYPE_AFTER_THREE_LIST:
                return new RedRangAfterViewHolder(LayoutInflater.from(mContext).inflate(R.layout.red_rang_after_three_item, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)){
//            case TYPE_TIP:
//                ((RedRangTipViewHolder)holder).
//                break;
            case TYPE_TOP_THREE_LIST:
                RedRangTopViewHolder redRangTopViewHolder = (RedRangTopViewHolder) holder;
                UserGrowRankingEntity.RankingEntity entity = mList.get(position - 1);

                Netroid.displayImage(Util.transfer(entity.getUser_head()), redRangTopViewHolder.ivHead);
                redRangTopViewHolder.tvName.setText(entity.getUser_name());
                redRangTopViewHolder.tvScore.setText(entity.getGrow_month() + "");
                if(position == 1){
                    redRangTopViewHolder.ivRank.setImageResource(R.mipmap.red_one);
                    redRangTopViewHolder.tvJifen.setText(rankingEntity.getFirst_prize());
                }else if (position == 2){
                    redRangTopViewHolder.ivRank.setImageResource(R.mipmap.red_two);
                    redRangTopViewHolder.tvJifen.setText(rankingEntity.getSecond_prize());
                }else if (position == 3){
                    redRangTopViewHolder.ivRank.setImageResource(R.mipmap.red_three);
                    redRangTopViewHolder.tvJifen.setText(rankingEntity.getThird_prize());
                }
                break;
            case TYPE_AFTER_THREE_LIST:
                RedRangAfterViewHolder redRangAfterViewHolder = (RedRangAfterViewHolder) holder;
                UserGrowRankingEntity.RankingEntity entity2 = mList.get(position - 1);

                Netroid.displayImage(Util.transfer(entity2.getUser_head()), redRangAfterViewHolder.ivHead);
                redRangAfterViewHolder.tvName.setText(entity2.getUser_name());
                redRangAfterViewHolder.tvScore.setText(entity2.getGrow_month() + "");

                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return TYPE_TIP;
        }else if (position > 0 && position < 4){
            return TYPE_TOP_THREE_LIST;
        }else {
            return TYPE_AFTER_THREE_LIST;
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 1 : mList.size() + 1;
//        return 11;
    }

    class RedRangTipViewHolder extends RecyclerView.ViewHolder{

        TextView tvTip;

        public RedRangTipViewHolder(View itemView) {
            super(itemView);
            tvTip = (TextView) itemView.findViewById(R.id.tv_red_rang_tip);
            tvTip.setText("(" + rankingEntity.getSettle_date() + " 更新)");
        }
    }

    class RedRangTopViewHolder extends RecyclerView.ViewHolder{

        RoundedImageView ivHead;
        ImageView ivRank;
        TextView tvName;
        TextView tvScore;
        TextView tvJifen;

        public RedRangTopViewHolder(View itemView) {
            super(itemView);

            ivHead = (RoundedImageView) itemView.findViewById(R.id.iv_head);
            ivRank = (ImageView) itemView.findViewById(R.id.iv_rang);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvScore = (TextView) itemView.findViewById(R.id.tv_group_score);
            tvJifen = (TextView) itemView.findViewById(R.id.tv_jifen);

        }
    }

    class RedRangAfterViewHolder extends RecyclerView.ViewHolder{

        RoundedImageView ivHead;
        TextView tvName;
        TextView tvScore;

        public RedRangAfterViewHolder(View itemView) {
            super(itemView);

            ivHead = (RoundedImageView) itemView.findViewById(R.id.iv_head);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvScore = (TextView) itemView.findViewById(R.id.tv_group_score);
        }
    }
}
