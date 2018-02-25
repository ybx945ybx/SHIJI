package cn.yiya.shiji.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.ShopCommissionInfo;

/**
 * 邀请开店卖家列表
 * Created by Amy on 2016/10/24.
 */

public class SellerCommissionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<ShopCommissionInfo.SellerEntity> mList;

    public SellerCommissionAdapter(Context context, List<ShopCommissionInfo.SellerEntity> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SellerViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_invite_shop, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SellerViewHolder viewHolder = (SellerViewHolder) holder;
        ShopCommissionInfo.SellerEntity item = mList.get(position);
        if (item.getShop_name().equals("暂无")) {
            viewHolder.tvSellerName.setTextColor(Color.parseColor("#9b9b9b"));
            viewHolder.tvSellerMobile.setTextColor(Color.parseColor("#9b9b9b"));
            viewHolder.tvSellerCommission.setTextColor(Color.parseColor("#9b9b9b"));
        }
        viewHolder.tvSellerName.setText(item.getShop_name());
        viewHolder.tvSellerMobile.setText(item.getMobile());
        viewHolder.tvSellerCommission.setText(item.getCommission());
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class SellerViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSellerName, tvSellerMobile, tvSellerCommission;

        public SellerViewHolder(View itemView) {
            super(itemView);
            tvSellerName = (TextView) itemView.findViewById(R.id.tv_seller_name);
            tvSellerMobile = (TextView) itemView.findViewById(R.id.tv_seller_mobile);
            tvSellerCommission = (TextView) itemView.findViewById(R.id.tv_seller_commission);
        }
    }
}
