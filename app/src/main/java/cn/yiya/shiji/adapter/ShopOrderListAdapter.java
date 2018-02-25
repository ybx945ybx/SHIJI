package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.ShopOrderDetailActivity;
import cn.yiya.shiji.entity.OrderListInfo;
import cn.yiya.shiji.utils.Util;

/**
 * 店铺订单列表Adapter
 * Created by Amy on 2016/10/19.
 */

public class ShopOrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<OrderListInfo> mList;

    public ShopOrderListAdapter(Context context, ArrayList<OrderListInfo> list) {
        this.mContext = context;
        this.mList = list;
    }

    public  ArrayList<OrderListInfo> getmList(){
        return mList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StoreOrderViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_shop_order, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        StoreOrderViewHolder viewHolder = (StoreOrderViewHolder) holder;
        final OrderListInfo item = mList.get(position);
        viewHolder.tvDateTime.setText(item.getCreate_time().trim().substring(0, 16));
        viewHolder.ivGoodsImage.setImageURI(item.getGoods().get(0).getCover());
        viewHolder.tvReceiver.setText(item.getConsignee_name());
        viewHolder.tvGoodsFee.setText("¥ " + Util.FloatKeepTwo(Float.valueOf(item.getCash_amount())));
        viewHolder.tvGoodsMoney.setText("¥ " + Util.FloatKeepTwo(Float.valueOf(item.getAmount())));
        viewHolder.tvGoodsCount.setText(item.getGoods_num()+"");
        viewHolder.tvStatus.setText(item.getGroup_info().getGroupDes());
        switch (item.getGroup_info().getGroup()) {
            //1:待付款，2:待缴税，3:运输中，4:完成，5:失效
            case 1:
                viewHolder.tvStatus.setTextColor(Color.parseColor("#ed5137"));
                break;
            case 3:
            case 4:
            case 5:
                viewHolder.tvStatus.setTextColor(Color.parseColor("#212121"));
                break;
            default:
                break;
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转至订单详情
                Intent intent = new Intent(mContext, ShopOrderDetailActivity.class);
                intent.putExtra("order_number",item.getOrder_number());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class StoreOrderViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDateTime;
        private SimpleDraweeView ivGoodsImage;
        private TextView tvReceiver;
        private TextView tvGoodsFee;
        private TextView tvGoodsMoney;
        private TextView tvGoodsCount;
        private TextView tvStatus;

        public StoreOrderViewHolder(View itemView) {
            super(itemView);
            tvDateTime = (TextView) itemView.findViewById(R.id.tv_datetime);
            ivGoodsImage = (SimpleDraweeView) itemView.findViewById(R.id.iv_goods_image);
            tvReceiver = (TextView) itemView.findViewById(R.id.tv_receiver);
            tvGoodsFee = (TextView) itemView.findViewById(R.id.tv_goods_fee);
            tvGoodsMoney = (TextView) itemView.findViewById(R.id.tv_goods_money);
            tvGoodsCount = (TextView) itemView.findViewById(R.id.tv_goods_count);
            tvStatus = (TextView) itemView.findViewById(R.id.tv_status);
        }
    }

}
