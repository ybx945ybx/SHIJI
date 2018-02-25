package cn.yiya.shiji.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.CashEntity;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.Util;

/**
 * Created by Tom on 2016/9/30.
 */
public class CashAccountMonthListAdapter extends RecyclerView.Adapter<CashAccountMonthListAdapter.CashAccountMonthListViewHolder> {

    private Context mContext;
    private List<CashEntity.ListEntity.CashInfoEntity> mList;
    private int type;

    public CashAccountMonthListAdapter(Context mContext, List<CashEntity.ListEntity.CashInfoEntity> mList, int type){
        this.mContext = mContext;
        this.mList = mList;
        this.type = type;
    }

    public List<CashEntity.ListEntity.CashInfoEntity> getmList() {
        return mList;
    }

    public void setmList(List<CashEntity.ListEntity.CashInfoEntity> mList) {
        this.mList = mList;
    }

    @Override
    public CashAccountMonthListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CashAccountMonthListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.cash_account_month_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(CashAccountMonthListViewHolder holder, int position) {
        CashEntity.ListEntity.CashInfoEntity entity = mList.get(position);

        holder.ivIcon.setImageURI(entity.getIcon(), R.mipmap.work_default);

        if(type == 9){
            holder.tvContentTxt.setVisibility(View.VISIBLE);
            holder.tvContentTxt.setText(entity.getDesc());
            holder.tvContent.setText(entity.getDesc_ext());
        }else {
            holder.tvContentTxt.setVisibility(View.GONE);
            holder.tvContent.setText(entity.getDesc());
        }

        holder.tvDate.setText(entity.getTime());
        float sum = entity.getAmount();
        String sumValue;
        if(type == 2 || type == 5 || type == 9) {
            sumValue = "-" + Util.FloatKeepTwo(sum);
        }else if(type == 8){
            if(entity.getAdd_sub() == 1){
                sumValue = "+" + Util.FloatKeepTwo(sum);
            }else {
                sumValue = "-" + Util.FloatKeepTwo(sum);
            }
        }else{
            sumValue = "+" + Util.FloatKeepTwo(sum);
        }
        holder.tvSum.setText(sumValue);
        if(type == 1 || type == 2 || type == 3 || type == 5 || type == 8 || type == 9){
            holder.tvSum.setTextColor(Color.parseColor("#212121"));
        }else {
            holder.tvSum.setTextColor(Color.parseColor("#40ba3a"));
        }
        if(!TextUtils.isEmpty(entity.getStatus())){
            holder.tvTips.setText(entity.getStatus());
            holder.tvTips.setVisibility(View.VISIBLE);
        }else {
            holder.tvTips.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class CashAccountMonthListViewHolder extends RecyclerView.ViewHolder{

        SimpleDraweeView ivIcon;
        TextView tvContent;
        TextView tvContentTxt;
        TextView tvDate;
        TextView tvSum;
        TextView tvTips;

        public CashAccountMonthListViewHolder(View itemView) {
            super(itemView);
            ivIcon = (SimpleDraweeView) itemView.findViewById(R.id.iv_icon);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            tvContentTxt = (TextView) itemView.findViewById(R.id.tv_content_txt);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvSum = (TextView) itemView.findViewById(R.id.tv_sum);
            tvTips = (TextView) itemView.findViewById(R.id.tv_tip);
        }
    }
}
