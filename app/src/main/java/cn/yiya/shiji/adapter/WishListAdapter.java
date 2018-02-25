package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.NewGoodsDetailActivity;
import cn.yiya.shiji.entity.goodes.NewGoodsItem;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;

/**
 * Created by Tom on 2016/8/17.
 */
public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.WishListViewHolder> {

    private Context mContext;
    private ArrayList<NewGoodsItem> mList = new ArrayList<>();
    private OnDeleteListener onDeleteListener;

    public WishListAdapter (Context mContext){
        this.mContext = mContext;
    }

    public ArrayList<NewGoodsItem> getmList() {
        return mList;
    }

    public void setmList(ArrayList<NewGoodsItem> mList) {
        this.mList = mList;
    }


    @Override
    public WishListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WishListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.wish_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(WishListViewHolder holder, final int position) {
        final NewGoodsItem item = mList.get(position);
        holder.ivGoods.setImageURI(Util.transferImage(item.getCover(), SimpleUtils.dp2px(mContext, 77)), R.mipmap.work_default);
        holder.tvBrand.setText(item.getBrand());
        holder.tvTitle.setText(item.getTitle());
        holder.tvPrice.setText(item.getPrice());
        holder.tvPrice.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/Helvetica Condensed Bold.ttf"));
        holder.tvFrom.setText(item.getSite());

        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onDeleteListener != null){
                    onDeleteListener.OnDelete(position);
                }
            }
        });

        holder.ivGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NewGoodsDetailActivity.class);
                intent.putExtra("goodsId", item.getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public interface OnDeleteListener{
        void OnDelete(int position);
    }

    public void setOnDeleteListener(OnDeleteListener onDeleteListener){
        this.onDeleteListener = onDeleteListener;
    }

    class WishListViewHolder extends RecyclerView.ViewHolder{
        SimpleDraweeView ivGoods;
        TextView tvBrand;
        TextView tvTitle;
        TextView tvPrice;
        TextView tvFrom;
        TextView tvDelete;
        LinearLayout llytGoods;

        public WishListViewHolder(View itemView) {
            super(itemView);
            ivGoods = (SimpleDraweeView) itemView.findViewById(R.id.item_img);
            tvBrand = (TextView) itemView.findViewById(R.id.item_brand);
            tvTitle = (TextView) itemView.findViewById(R.id.item_title);
            tvPrice = (TextView) itemView.findViewById(R.id.item_price);
            tvFrom = (TextView) itemView.findViewById(R.id.goods_from_text);
            tvDelete = (TextView) itemView.findViewById(R.id.tv_delete);
            llytGoods = (LinearLayout) itemView.findViewById(R.id.llyt_goods);
        }
    }
}
