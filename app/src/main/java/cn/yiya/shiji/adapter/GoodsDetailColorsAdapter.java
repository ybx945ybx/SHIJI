package cn.yiya.shiji.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.GoodsDetailEntity;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;

/**
 * Created by Tom on 2016/11/7.
 */
public class GoodsDetailColorsAdapter extends RecyclerView.Adapter<GoodsDetailColorsAdapter.GoodsDetailColorsViewHolder>{

    private Context mContext;
    private List<GoodsDetailEntity.GoodsColorsEntity> mColors;
    private OnColorItemClickListener onColorItemClickListener;
    private int selectedPositon = 0;

    public GoodsDetailColorsAdapter(Context mContext, List<GoodsDetailEntity.GoodsColorsEntity> mColors){
        this.mContext = mContext;
        this.mColors = mColors;
    }

    @Override
    public GoodsDetailColorsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GoodsDetailColorsViewHolder(LayoutInflater.from(mContext).inflate(R.layout.goods_detail_colors_item, parent, false));
    }

    @Override
    public void onBindViewHolder(GoodsDetailColorsViewHolder holder, final int position) {
        GoodsDetailEntity.GoodsColorsEntity entity = mColors.get(position);

        if(position == selectedPositon){
            holder.ivColor.setSelected(true);
        }else {
            holder.ivColor.setSelected(false);
        }
        holder.ivColor.setImageURI(Util.transferImage(entity.getImages().get(0).getImage(), SimpleUtils.dp2px(mContext, 17)));
        holder.ivColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position == selectedPositon){
                    return;
                }
                if(onColorItemClickListener != null){
                    onColorItemClickListener.OnColorItemClick(position);
                    selectedPositon = position;
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mColors == null ? 0 : mColors.size();
    }

    public interface OnColorItemClickListener{
        void  OnColorItemClick(int position);
    }

    public void setOnColorItemClickListener(OnColorItemClickListener onColorItemClickListener){
        this.onColorItemClickListener = onColorItemClickListener;
    }

    class  GoodsDetailColorsViewHolder extends RecyclerView.ViewHolder{
        private  SimpleDraweeView ivColor;
        public GoodsDetailColorsViewHolder(View itemView) {
            super(itemView);
            ivColor = (SimpleDraweeView) itemView.findViewById(R.id.iv_color);
        }
    }
}
