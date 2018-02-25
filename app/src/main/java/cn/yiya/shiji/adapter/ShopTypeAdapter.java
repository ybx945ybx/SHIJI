package cn.yiya.shiji.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.duowan.mobile.netroid.image.NetworkImageView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.ShopTypeEntity;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.BitmapTool;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;

/**
 * Created by Tom on 2016/10/19.
 */
public class ShopTypeAdapter extends RecyclerView.Adapter<ShopTypeAdapter.ShopTypeViewHolder>{

    private Context mContext;
    private List<ShopTypeEntity.ListEntity> mList = new ArrayList<>();
    private OnTypeSelectedListener onTypeSelectedListener;
    private int selectedPosition = 0;

    public ShopTypeAdapter(Context mContext){
        this.mContext = mContext;
    }

    public List<ShopTypeEntity.ListEntity> getmList() {
        return mList;
    }

    public void setmList(List<ShopTypeEntity.ListEntity> mList) {
        this.mList = mList;
    }

    @Override
    public ShopTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ShopTypeViewHolder(LayoutInflater.from(mContext).inflate(R.layout.shop_type_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ShopTypeViewHolder holder, final int position) {
        final ShopTypeEntity.ListEntity entity = mList.get(position);

        if(position == selectedPosition){
            holder.ivSelected.setVisibility(View.VISIBLE);
            holder.ivShade.setVisibility(View.VISIBLE);
        }else {
            holder.ivSelected.setVisibility(View.GONE);
            holder.ivShade.setVisibility(View.INVISIBLE);
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)holder.ivType.getLayoutParams();
        layoutParams.width = SimpleUtils.getScreenWidth(mContext) * 240/375;
        layoutParams.height = layoutParams.width * 427/240;
        holder.ivType.setLayoutParams(layoutParams);
        holder.ivShade.setLayoutParams(layoutParams);
        holder.ivType.setImageURI(Util.transferCropImage(entity.getCover(), layoutParams.width), R.mipmap.work_default);
        holder.tvType.setText(entity.getName());
        holder.tvShopSimpGuide.setText(entity.getDesc_title());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedPosition == position){
                    return;
                }
                if(onTypeSelectedListener != null){
                    onTypeSelectedListener.onTypeSelected(entity.getDesc(), entity.getId(), entity.getPrice());
                }
                selectedPosition = position;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public interface OnTypeSelectedListener{
        void onTypeSelected(String desc, int typeId, String price);
    }

    public void setOnTypeSelectedListener(OnTypeSelectedListener onTypeSelectedListener){
        this.onTypeSelectedListener = onTypeSelectedListener;
    }

    class ShopTypeViewHolder extends RecyclerView.ViewHolder{

        SimpleDraweeView ivType;
        ImageView ivShade;
        TextView tvType;
        TextView tvShopSimpGuide;
        ImageView ivSelected;

        public ShopTypeViewHolder(View itemView) {
            super(itemView);
            ivType = (SimpleDraweeView) itemView.findViewById(R.id.image);
            ivShade = (ImageView) itemView.findViewById(R.id.iv_shade);
            tvType = (TextView) itemView.findViewById(R.id.tv_shop_type);
            tvShopSimpGuide = (TextView) itemView.findViewById(R.id.tv_shop_introduce);
            ivSelected = (ImageView) itemView.findViewById(R.id.iv_selected);
        }
    }
}
