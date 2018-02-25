package cn.yiya.shiji.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.navigation.StoreCategoryInfo;

/**
 * Created by Tom on 2016/4/7.
 */
public class SortPopAdapter extends RecyclerView.Adapter<SortPopAdapter.SortPopAdapterViewHolder>{
    private Context mContext;
    private ArrayList<StoreCategoryInfo> mlist;
    private OnItemActionListener mOnItemActionListener;

    public SortPopAdapter (Context mContext, ArrayList<StoreCategoryInfo> mlist){
        this.mContext = mContext;
        this.mlist = mlist;
    }

    public ArrayList<StoreCategoryInfo> getMlist() {
        return mlist;
    }

    public void setMlist(ArrayList<StoreCategoryInfo> mlist) {
        this.mlist = mlist;
    }

    public interface OnItemActionListener {
        public void onItemClickListener(View v, int pos);
    }

    public void setOnItemActionListener(OnItemActionListener onItemActionListener) {
        this.mOnItemActionListener = onItemActionListener;
    }

    @Override
    public SortPopAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SortPopAdapterViewHolder(LayoutInflater.from(mContext).inflate(R.layout.shop_sort_pop_item, parent, false));
    }

    @Override
    public void onBindViewHolder(SortPopAdapterViewHolder holder, final int position) {
        StoreCategoryInfo info = mlist.get(position);

        holder.tvCategroy.setText(info.getName());
        holder.tvCategroy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemActionListener != null) {
                    mOnItemActionListener.onItemClickListener(v, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mlist == null){
            return 0;
        }else {
            return mlist.size();
        }
    }

    class SortPopAdapterViewHolder extends RecyclerView.ViewHolder{
        TextView tvCategroy;
        public SortPopAdapterViewHolder(View itemView) {
            super(itemView);
            tvCategroy = (TextView) itemView.findViewById(R.id.tv_category);
        }
    }
}

