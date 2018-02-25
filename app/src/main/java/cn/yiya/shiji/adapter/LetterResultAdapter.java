package cn.yiya.shiji.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.SortInfo;

/**
 * Created by Tom on 2016/6/1.
 */
public class LetterResultAdapter extends RecyclerView.Adapter<LetterResultAdapter.LetterResultViewHolder>{

    private Context mContext;
    private ArrayList<SortInfo> mList;
    private OnItemClickListener onItemClickListener;

    public LetterResultAdapter(Context context){
        this.mContext = context;
    }

    public LetterResultAdapter(Context context, ArrayList<SortInfo> mList){
        this.mContext = context;
        this.mList = mList;
    }

    public ArrayList<SortInfo> getmList() {
        return mList;
    }

    public void setmList(ArrayList<SortInfo> mList) {
        this.mList = mList;
    }

    @Override
    public LetterResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LetterResultViewHolder(LayoutInflater.from(mContext).inflate(R.layout.seach_brands_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final LetterResultViewHolder holder, final int position) {
        SortInfo info = mList.get(position);

        holder.tvBrandName.setText(info.getName());
        if(info.isCheck()){
            holder.ivSelected.setVisibility(View.VISIBLE);
        }else {
            holder.ivSelected.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean beChecked = mList.get(position).isCheck();
                mList.get(position).setCheck(!beChecked);
                if(!beChecked){
                    holder.ivSelected.setVisibility(View.VISIBLE);
                }else {
                    holder.ivSelected.setVisibility(View.INVISIBLE);
                }
                if(onItemClickListener != null){
                    onItemClickListener.OnItemClick(mList.get(position).getName(), !beChecked);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mList == null) {
            return 0;
        }else {
            return mList.size();
        }
    }

    public interface OnItemClickListener{
        void OnItemClick(String brandName, boolean isSelected);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    class LetterResultViewHolder extends RecyclerView.ViewHolder{
        ImageView ivSelected;
        TextView tvBrandName;
        public LetterResultViewHolder(View itemView) {
            super(itemView);
            ivSelected = (ImageView) itemView.findViewById(R.id.iv_selected);
            tvBrandName = (TextView) itemView.findViewById(R.id.tv_brand_name);
        }
    }
}

