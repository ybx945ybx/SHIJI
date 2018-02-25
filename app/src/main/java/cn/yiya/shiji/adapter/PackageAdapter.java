package cn.yiya.shiji.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.yiya.shiji.R;

/**
 * Created by chenjian on 2016/3/3.
 */
public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.PackageViewHolder>{

    private Context mContext;
    private ArrayList<String> mList;
    private OnItemClickListenter mListenter;
    private int nPosition;

    public PackageAdapter(Context mContext, ArrayList<String> mList, int position, OnItemClickListenter listenter) {
        this.mContext = mContext;
        this.mList = mList;
        this.mListenter = listenter;
        this.nPosition = position;
    }

    @Override
    public PackageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PackageViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_package, parent, false));
    }

    @Override
    public void onBindViewHolder(PackageViewHolder holder, final int position) {
        String strPackage = mList.get(position);
        holder.tvTxt.setText(strPackage);
        holder.tvTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nPosition = position;
                if (mListenter != null)
                    notifyDataSetChanged();
                    mListenter.onItemClick(position);
            }
        });
        if (nPosition == position) {
            holder.tvTxt.setBackgroundColor(Color.parseColor("#ffffff"));
        } else {
            holder.tvTxt.setBackgroundColor(Color.parseColor("#f9f9f9"));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public interface OnItemClickListenter {
        public void onItemClick(int position);
    }

    class PackageViewHolder extends RecyclerView.ViewHolder {
        TextView tvTxt;

        public PackageViewHolder(View itemView) {
            super(itemView);
            tvTxt = (TextView) itemView.findViewById(R.id.item_package_txt);
        }
    }
}

