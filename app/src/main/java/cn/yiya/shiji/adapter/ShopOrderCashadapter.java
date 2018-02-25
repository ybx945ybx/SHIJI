package cn.yiya.shiji.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.OrderCashInfo;
import cn.yiya.shiji.utils.Util;

/**
 * 店铺单个订单佣金列表
 * Created by Amy on 2016/10/20.
 */

public class ShopOrderCashadapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<OrderCashInfo.GoodsListEntity> mList;

    public ShopOrderCashadapter(Context context, List<OrderCashInfo.GoodsListEntity> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_shop_order_cash_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ListViewHolder viewHolder = (ListViewHolder) holder;
        OrderCashInfo.GoodsListEntity item = mList.get(position);
        viewHolder.tvGoodsName.setText(item.getTitle());
        String strPrice = "¥ " + Util.FloatKeepTwo(item.getPrice()) + " × " + item.getNum();
        String strCash = "¥ " + Util.FloatKeepTwo(item.getGoods_cash()) + " × " + item.getNum() + " = " + (item.getGoods_cash() * item.getNum());
        viewHolder.tvGoodsPrice.setText(strPrice);
        viewHolder.tvGoodsCash.setText(strCash);

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    class ListViewHolder extends RecyclerView.ViewHolder {
        private TextView tvGoodsName;
        private TextView tvGoodsPrice;
        private TextView tvGoodsCash;

        public ListViewHolder(View itemView) {
            super(itemView);
            tvGoodsName = (TextView) itemView.findViewById(R.id.tv_goods_name);
            tvGoodsPrice = (TextView) itemView.findViewById(R.id.tv_goods_price);
            tvGoodsCash = (TextView) itemView.findViewById(R.id.tv_goods_cash);
        }
    }
}
