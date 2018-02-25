package cn.yiya.shiji.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.NotifyItem;

/**
 * Created by Tom on 2016/1/15.
 */
public class ExchangeCouponRecordAdapter extends RecyclerView.Adapter<ExchangeCouponRecordAdapter.ExchangeCouponRecordViewHolder>{
    private Context mContext;
    private ArrayList<NotifyItem> mList;

    public ExchangeCouponRecordAdapter(Context context){
        this.mContext = context;
    }

    public ArrayList<NotifyItem> getmList() {
        return mList;
    }

    public void setmList(ArrayList<NotifyItem> mList) {
        this.mList = mList;
    }


    @Override
    public ExchangeCouponRecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExchangeCouponRecordViewHolder(LayoutInflater.from(mContext).inflate(R.layout.exchange_coupon_record_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ExchangeCouponRecordViewHolder holder, int position) {
        NotifyItem item = mList.get(position);
        holder.tvContext.setText(item.getMsg());
    }

    @Override
    public int getItemCount() {
        if(mList == null){
            return 0;
        }
        return mList.size();
    }

    class ExchangeCouponRecordViewHolder extends RecyclerView.ViewHolder {
        TextView tvContext;

        public ExchangeCouponRecordViewHolder(View itemView) {
            super(itemView);
            tvContext = (TextView) itemView.findViewById(R.id.record_context);
        }
    }
}


