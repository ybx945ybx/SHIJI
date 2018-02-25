package cn.yiya.shiji.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.Goods;
import cn.yiya.shiji.netroid.Netroid;

/**
 * Created by jerry on 2016/2/26.
 */
public class HorOrderListAdapter extends RecyclerView.Adapter<HorOrderListAdapter.HorOrderListViewholder>{
    private Context context;
    private ArrayList<Goods> arrayList;

    public HorOrderListAdapter(Context context, ArrayList<Goods> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public HorOrderListViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HorOrderListViewholder(LayoutInflater.from(context).inflate(R.layout.hor_order_list_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(HorOrderListViewholder holder, int position) {
        Netroid.displayImage(arrayList.get(position).getCover(), holder.imageView);

    }

    @Override
    public int getItemCount() {
        if(arrayList == null){
            return 0;
        }
        return arrayList.size();
    }

    class HorOrderListViewholder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public HorOrderListViewholder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.iv_order);
        }
    }
    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener{
        void onItemClick();
    }
}
