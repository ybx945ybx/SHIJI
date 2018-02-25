package cn.yiya.shiji.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.ScoreItem;

/**
 * 积分详情列表适配器
 * Created by tomyang on 2015/9/14.
 */
public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder> {
    private Context mContext;
    private ArrayList<ScoreItem> mList;

    public ScoreAdapter(Context context) {
        super();
        this.mContext = context;
        this.mList = new ArrayList<ScoreItem>();
    }

    public void setList(ArrayList<ScoreItem> mList) {
        this.mList = mList;
    }

    public ArrayList<ScoreItem> getList() {
        return mList;
    }

    @Override
    public ScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ScoreViewHolder(LayoutInflater.from(mContext).inflate(R.layout.score_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ScoreViewHolder holder, int position) {
        ScoreItem item = mList.get(position);

        holder.scoreDay.setText(item.getTime().substring(0,10));
        holder.scoreTime.setText(item.getTime().substring(11));
        int score = item.getScore();
        String scoreValue;
        if(score>0){
            scoreValue = "+"+score;
        }else {
            scoreValue = "" + score;
        }
        holder.scoreContent.setText(item.getContent());
        holder.score.setText(scoreValue);
    }

    @Override
    public int getItemCount() {
        if(mList == null){
            return 0;
        }
        return mList.size();
    }

    class ScoreViewHolder extends RecyclerView.ViewHolder{
        TextView scoreContent;
        TextView score;
        TextView scoreTime;
        TextView scoreDay;

        public ScoreViewHolder(View itemView) {
            super(itemView);
            scoreContent = (TextView)itemView.findViewById(R.id.score_content);
            score = (TextView)itemView.findViewById(R.id.score);
            scoreDay = (TextView)itemView.findViewById(R.id.score_day);
            scoreTime = (TextView)itemView.findViewById(R.id.score_time);
        }
    }

}


