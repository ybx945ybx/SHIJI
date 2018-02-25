package cn.yiya.shiji.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.navigation.StoreShortInfo;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;

/**
 * Created by jerry on 2016/3/29.
 */
public class StoreListAdapter extends RecyclerView.Adapter<StoreListAdapter.StoreListViewHolder> {
    private Context context;
    private OnItemClickListener onItemClickListener;
    private ArrayList<StoreShortInfo> list;
    public StoreListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public StoreListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StoreListViewHolder(LayoutInflater.from(context).inflate(R.layout.store_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(StoreListViewHolder holder, final int position) {
        holder.tvTitle.setText(list.get(position).getName());
        if(!TextUtils.isEmpty(list.get(position).getRate())){
            holder.rbRating.setRating(Float.parseFloat(list.get(position).getRate()));
        } else {
            holder.rbRating.setRating(0.0f);
        }
        holder.tvCategory.setText(list.get(position).getCategory());
        holder.tvDes.setText(list.get(position).getAddress());
        holder.tvCoord.setText(list.get(position).getDistance());
        Drawable drawable = ContextCompat.getDrawable(context, R.mipmap.coord);
        holder.tvCoord.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
//        Netroid.displayImage(list.get(position).getCover(), holder.ivCover);
        Netroid.displayImage(Util.clipImageViewByWH(list.get(position).getCover(), SimpleUtils.dp2px(context, 100),
                SimpleUtils.dp2px(context, 100)),holder.ivCover, R.mipmap.travel_default);

        holder.ivCover.setScaleType(ImageView.ScaleType.CENTER_CROP);
        holder.rvStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(list.get(position).getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(list == null){
            return 0;
        } else {
            return list.size();
        }
    }


    class StoreListViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvCategory, tvDes, tvCoord;
        ImageView ivCover;
        RatingBar rbRating;
        CardView cvStore;
        RelativeLayout rvStore;
        public StoreListViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvCategory = (TextView) itemView.findViewById(R.id.tv_category);
            tvDes = (TextView) itemView.findViewById(R.id.tv_des);
            tvCoord = (TextView) itemView.findViewById(R.id.tv_coord);
            ivCover = (ImageView) itemView.findViewById(R.id.iv_cover);
            rbRating = (RatingBar) itemView.findViewById(R.id.rb_rating);
            cvStore = (CardView) itemView.findViewById(R.id.cv_store);
            rvStore = (RelativeLayout)itemView.findViewById(R.id.rv_store);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String id);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ArrayList<StoreShortInfo> getList() {
        return list;
    }

    public void setList(ArrayList<StoreShortInfo> list) {
        this.list = list;
    }
}
