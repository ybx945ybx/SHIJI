package cn.yiya.shiji.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.ForwardGoodsEntity;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;

/**
 * Created by Tom on 2016/11/29.
 */

public class SelectForwardGoodsAdapter extends RecyclerView.Adapter<SelectForwardGoodsAdapter.SelectForwardGoodsViewHolder> {

    private Context mContext;
    private OnItemSelectedListener onItemSelectedListener;
    private List<ForwardGoodsEntity> mList;

    public SelectForwardGoodsAdapter(Context mContext){
        this.mContext = mContext;
        this.mList = new ArrayList<>();
    }

    public List<ForwardGoodsEntity> getmList() {
        return mList;
    }

    public void setmList(List<ForwardGoodsEntity> mList) {
        this.mList = mList;
    }

    @Override
    public SelectForwardGoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelectForwardGoodsViewHolder(LayoutInflater.from(mContext).inflate(R.layout.select_forward_goods_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final SelectForwardGoodsViewHolder holder, final int position) {
        final ForwardGoodsEntity entity = mList.get(position);
        int width = (SimpleUtils.getScreenWidth(mContext) - SimpleUtils.dp2px(mContext, 44))/3;
        holder.sdvGoods.setImageURI(Util.transferImage(entity.getUrl(), width), R.mipmap.work_default);
        holder.ivSelected.setSelected(entity.isSelected());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemSelectedListener != null){
                    if(entity.isSelected()){
                        entity.setSelected(false);
                    }else {
                        entity.setSelected(true);
                    }
                    holder.ivSelected.setSelected(entity.isSelected());
                    onItemSelectedListener.OnItemSelected(position, entity.isSelected());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public interface OnItemSelectedListener{
        void OnItemSelected(int position, boolean selected);
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener){
        this.onItemSelectedListener = onItemSelectedListener;
    }

    class SelectForwardGoodsViewHolder extends RecyclerView.ViewHolder{
        SimpleDraweeView sdvGoods;
        ImageView ivSelected;

        public SelectForwardGoodsViewHolder(View itemView) {
            super(itemView);
            sdvGoods = (SimpleDraweeView) itemView.findViewById(R.id.iv_goods);
            ivSelected = (ImageView) itemView.findViewById(R.id.iv_selected);
        }
    }
}
