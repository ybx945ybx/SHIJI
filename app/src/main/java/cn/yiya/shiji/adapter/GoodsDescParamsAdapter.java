package cn.yiya.shiji.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.yiya.shiji.R;

/**
 * 购物车中描述商品的参数 size color等
 * Created by Amy on 2016/11/16.
 */

public class GoodsDescParamsAdapter extends RecyclerView.Adapter<GoodsDescParamsAdapter.ParamViewHolder> {

    private Context mContext;
    private ArrayList<String[]> map;

    public GoodsDescParamsAdapter(Context mContext, ArrayList<String[]> map) {
        this.mContext = mContext;
        this.map = map;
    }

    @Override
    public ParamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ParamViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_goods_desc_params, parent, false));
    }

    @Override
    public void onBindViewHolder(ParamViewHolder holder, int position) {
        holder.tvKey.setText(map.get(position)[0] + "：");
        holder.tvValue.setText(map.get(position)[1]);
    }

    @Override
    public int getItemCount() {
        return map == null ? 0 : map.size();
    }

    class ParamViewHolder extends RecyclerView.ViewHolder {

        TextView tvKey, tvValue;

        public ParamViewHolder(View itemView) {
            super(itemView);
            tvKey = (TextView) itemView.findViewById(R.id.tv_key);
            tvValue = (TextView) itemView.findViewById(R.id.tv_value);
        }
    }
}
