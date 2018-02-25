package cn.yiya.shiji.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.MallLimitOptionInfo;
import cn.yiya.shiji.entity.SortInfo;

/**
 * Created by Tom on 2016/5/26.
 */
public class GoodsListOrderAdapter extends RecyclerView.Adapter<GoodsListOrderAdapter.GoodsListOrderViewHolder>{

    private Context mContext;
    private MallLimitOptionInfo mInfos;
    private OnItemClickListener onItemClickListener;
    private int selectedPosition;

    public GoodsListOrderAdapter(Context context, MallLimitOptionInfo mInfos){
        this.mContext = context;
        this.mInfos = mInfos;
    }

    @Override
    public GoodsListOrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GoodsListOrderViewHolder(LayoutInflater.from(mContext).inflate(R.layout.goods_list_order_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final GoodsListOrderViewHolder holder, final int position) {
        final SortInfo sortInfo = mInfos.getSorts().get(position);

        if(sortInfo.isCheck()){
            selectedPosition = position;
            holder.tvOrder.setTextColor(Color.parseColor("#ffffff"));
            holder.tvOrder.setBackgroundColor(Color.parseColor("#212121"));
        }else {
            holder.tvOrder.setTextColor(Color.parseColor("#212121"));
            holder.tvOrder.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        holder.tvOrder.setText(sortInfo.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position == selectedPosition){
                    return;
                }
                mInfos.getSorts().get(selectedPosition).setCheck(false);
                mInfos.getSorts().get(position).setCheck(true);
                selectedPosition = position;
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(mInfos);
                }
                notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount() {
        if(mInfos.getSorts() == null){
            return 0;
        }else {
            return mInfos.getSorts().size();
        }
    }

    public interface OnItemClickListener{
        void onItemClick(MallLimitOptionInfo mInfos);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    class GoodsListOrderViewHolder extends RecyclerView.ViewHolder{
        TextView tvOrder;
        public GoodsListOrderViewHolder(View itemView) {
            super(itemView);
            tvOrder = (TextView) itemView.findViewById(R.id.tv_order);
        }
    }
}

