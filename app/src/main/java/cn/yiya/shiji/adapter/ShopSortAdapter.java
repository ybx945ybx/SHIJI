package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.TravelStoreActivity;
import cn.yiya.shiji.entity.navigation.StoreShortInfo;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;

/**
 * Created by Tom on 2016/4/7.
 */
public class ShopSortAdapter extends RecyclerView.Adapter<ShopSortAdapter.ShopSortAdapterViewHolder> {
    private Context mContext;
    private ArrayList<StoreShortInfo> mlist;

    public ShopSortAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public ArrayList<StoreShortInfo> getMlist() {
        return mlist;
    }

    public void setMlist(ArrayList<StoreShortInfo> mlist) {
        this.mlist = mlist;
    }

    @Override
    public ShopSortAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ShopSortAdapterViewHolder(LayoutInflater.from(mContext).inflate(R.layout.shop_sort_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ShopSortAdapterViewHolder holder, int position) {
        final StoreShortInfo info = mlist.get(position);

        holder.tvShopName.setText(info.getName());
        Netroid.displayImage(Util.clipImageViewByWH(info.getCover(), SimpleUtils.dp2px(mContext,100),
                SimpleUtils.dp2px(mContext,100)),holder.ivShopCover, R.mipmap.travel_default);
        if (TextUtils.isEmpty(info.getRate())) {
            holder.ratingBar.setRating(0f);
        } else {
            holder.ratingBar.setRating(Float.parseFloat(info.getRate()));
        }
        holder.tvShopCategroy.setText(info.getCategory());
        holder.tvShopAddress.setText(info.getAddress());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TravelStoreActivity.class);
                intent.putExtra("id", info.getId());
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        if (mlist == null) {
            return 0;
        } else {
            return mlist.size();
        }
    }

    class ShopSortAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView tvShopName;
        ImageView ivShopCover;
        RatingBar ratingBar;
        TextView tvShopAddress;
        TextView tvShopCategroy;

        public ShopSortAdapterViewHolder(View itemView) {
            super(itemView);
            tvShopName = (TextView) itemView.findViewById(R.id.shop_name);
            ivShopCover = (ImageView) itemView.findViewById(R.id.shop_cover);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rb_rating);
            tvShopAddress = (TextView) itemView.findViewById(R.id.shop_address);
            tvShopCategroy = (TextView) itemView.findViewById(R.id.shop_category);

        }
    }
}

