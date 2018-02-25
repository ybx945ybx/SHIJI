package cn.yiya.shiji.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.yiya.shiji.R;

/**
 * Created by Tom on 2017/1/13.
 */

public class MyRedLevelAdapter extends RecyclerView.Adapter<MyRedLevelAdapter.MyRedLevelViewHolder>{

    private Context mContext;
    private int myLevel;
    private OnClickListener onClickListener;
    private int checkedPosition;

    public MyRedLevelAdapter(Context mContext, int myLevel){
        this.mContext = mContext;
        this.myLevel = myLevel;
        checkedPosition = myLevel;
    }

    @Override
    public MyRedLevelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyRedLevelViewHolder(LayoutInflater.from(mContext).inflate(R.layout.red_level_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyRedLevelViewHolder holder, final int position) {
        holder.tvLevel.setText(transferLevel(position));

        if(position == 5){
            holder.viewDash.setVisibility(View.GONE);
        }else {
            holder.viewDash.setVisibility(View.VISIBLE);
        }

        if(position < myLevel){
            holder.tvLevel.setTextColor(Color.parseColor("#ffffff"));
            holder.tvLevel.setBackgroundResource(R.mipmap.red_level_gray);
//            holder.viewTriangle.setVisibility(View.INVISIBLE);
        }else if (position == myLevel){
            holder.tvLevel.setTextColor(Color.parseColor("#ffffff"));
            holder.tvLevel.setBackgroundResource(R.mipmap.red_level_red);
//            holder.viewTriangle.setVisibility(View.VISIBLE);
        }else {
            holder.tvLevel.setTextColor(Color.parseColor("#999999"));
            holder.tvLevel.setBackgroundResource(R.mipmap.red_level_gray);
//            holder.viewTriangle.setVisibility(View.INVISIBLE);
        }

        if(position == checkedPosition){
            holder.viewTriangle.setVisibility(View.VISIBLE);
            if(position == myLevel){
                holder.tvLevel.setBackgroundResource(R.mipmap.red_level_red);
            }else {
                holder.tvLevel.setBackgroundResource(R.mipmap.red_level_white);
            }
        }else {
            holder.viewTriangle.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedPosition = position;
                notifyDataSetChanged();
                if(onClickListener != null){
                    onClickListener.onClick(position);
                }
            }
        });
    }

    public interface OnClickListener{
        void onClick(int position);
    }

    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    class MyRedLevelViewHolder extends RecyclerView.ViewHolder{

        TextView tvLevel;
        View viewDash;
        View viewTriangle;

        public MyRedLevelViewHolder(View itemView) {
            super(itemView);
            tvLevel = (TextView) itemView.findViewById(R.id.tv_level);
            viewDash = itemView.findViewById(R.id.view_dash);
            viewTriangle = itemView.findViewById(R.id.view_triangle);
        }
    }

    private String transferLevel(int level){
        switch (level){
            case 0:
                return "H1";
            case 1:
                return "H2";
            case 2:
                return "H3";
            case 3:
                return "H4";
            case 4:
                return "H5";
            default:
                return "H6";
        }
    }
}
