package cn.yiya.shiji.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.Goods;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.Util;

/**
 * Created by jerry on 2016/1/18.
 */
public class PopularBrandsAdapter extends RecyclerView.Adapter<PopularBrandsAdapter.PopularBrandsViewHolder> {
    private Context mContext;
    private ArrayList<Goods> mlist;
    private OnItemClickListener mOnItemClickListener;

    public PopularBrandsAdapter(Context mContext, ArrayList<Goods> mlist) {
        this.mContext = mContext;
        this.mlist = mlist;
    }

    @Override
    public PopularBrandsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PopularBrandsViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_leftgallery_item, parent, false));
    }

    @Override
    public void onBindViewHolder(PopularBrandsViewHolder holder, int position) {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        Goods item = mlist.get(position);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParams.width = (int) ((displayMetrics.widthPixels - 60) / 3.5f + 0.5f);
        layoutParams.height = layoutParams.width;
        holder.ivgallery.setLayoutParams(layoutParams);

        if (!TextUtils.isEmpty(item.getCover())) {
            holder.ivgallery.setImageURI(Util.transferCropImage(item.getCover(), layoutParams.width));
        }
        holder.ivgallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mlist == null) {
            return 0;
        }
        return mlist.size();
    }

    class PopularBrandsViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView ivgallery;
        RelativeLayout mRelativeLayout;

        public PopularBrandsViewHolder(View itemView) {
            super(itemView);
            ivgallery = (SimpleDraweeView) itemView.findViewById(R.id.image);
            mRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativelayout);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick();
    }
    public void setMlist(ArrayList<Goods> mlist){
        this.mlist = mlist;
    }
}
