package cn.yiya.shiji.adapter;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.CashEntity;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.FullyLinearLayoutManager;

/**
 * Created by Tom on 2016/9/30.
 */
public class CashAccountListAdapter extends RecyclerView.Adapter<CashAccountListAdapter.CashAccountListViewHolder> {

    private Context mContext;
    private List<CashEntity.ListEntity> mList = new ArrayList<>();
    private int type;

    public CashAccountListAdapter(Context mContext, int type){
        this.mContext = mContext;
        this.type = type;
    }

    public List<CashEntity.ListEntity> getmList() {
        return mList;
    }

    public void setmList(List<CashEntity.ListEntity> mList) {
        this.mList = mList;
    }

    @Override
    public CashAccountListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CashAccountListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.cash_account_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(CashAccountListViewHolder holder, int position) {
        CashEntity.ListEntity entity = mList.get(position);

        holder.tvMonth.setText(entity.getMonth() + "月");

        List<CashEntity.ListEntity.CashInfoEntity> monthList = entity.getCash_info();
        float monthSum = 0;
        for(int i = 0; i < monthList.size(); i++){
            if(type == 8){
                if(monthList.get(i).getAdd_sub() == 1){
                    monthSum -= monthList.get(i).getAmount();
                }else {
                    monthSum += monthList.get(i).getAmount();
                }
            }else {
                monthSum += monthList.get(i).getAmount();
            }
        }
        String monthSumValue;
        if(type == 5 || type == 8 || type == 9){
            monthSumValue = "-" + Util.FloatKeepTwo(monthSum);
        }else {
            monthSumValue = "+" + Util.FloatKeepTwo(monthSum);
        }
        holder.tvMonthTotalSum.setText(monthSumValue);
        holder.rycvMonth.setItemAnimator(new DefaultItemAnimator());
        holder.rycvMonth.setLayoutManager(new FullyLinearLayoutManager(mContext));
        holder.rycvMonth.setAdapter(new CashAccountMonthListAdapter(mContext, monthList, type));
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class CashAccountListViewHolder extends RecyclerView.ViewHolder{

        TextView tvMonth;
        TextView tvMonthTotalSum;
        RecyclerView rycvMonth;

        public CashAccountListViewHolder(View itemView) {
            super(itemView);
            tvMonth = (TextView) itemView.findViewById(R.id.tv_month);
            tvMonthTotalSum = (TextView) itemView.findViewById(R.id.tv_month_total_sum);
            rycvMonth = (RecyclerView) itemView.findViewById(R.id.rycv_month);
        }
    }
}
