package cn.yiya.shiji.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.WorkImage;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;

/**
 * 搭配列表 右侧三个图
 * Created by Amy on 2016/7/14.
 */

public class NewMatchGoodsImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<WorkImage.GoodsEntity> mList;
    private OnItemClickListener listener;

    public NewMatchGoodsImageAdapter(Context mContext, List<WorkImage.GoodsEntity> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : 3;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View itemView, ViewGroup parent) {
        ViewHolder holder = null;
        if (itemView == null) {
            holder = new ViewHolder();
            itemView = LayoutInflater.from(mContext).inflate(R.layout.list_item_match_goods_image, parent, false);
            holder.ivGoodsImage = (SimpleDraweeView) itemView.findViewById(R.id.iv_goods_image);
            holder.layoutParams = (LinearLayout.LayoutParams) holder.ivGoodsImage.getLayoutParams();
            holder.layoutParams.width = (SimpleUtils.getScreenWidth(mContext) - SimpleUtils.dp2px(mContext, 2) * 3) / 3;
            holder.layoutParams.height = holder.layoutParams.width;
            holder.ivGoodsImage.setLayoutParams(holder.layoutParams);
            itemView.setTag(holder);
        } else {
            holder = (ViewHolder) itemView.getTag();
        }
        WorkImage.GoodsEntity item = null;
        if (mList.size() == 3) {
            item = mList.get(position);
            holder.ivGoodsImage.setImageURI(Util.transferCropImage(item.getCover(), holder.layoutParams.width));

        } else if (mList.size() == 2) {
            if (position == 0 || position == 1) {
                item = mList.get(position);
                holder.ivGoodsImage.setImageURI(Util.transferCropImage(item.getCover(), holder.layoutParams.width));
            }
        } else if (mList.size() == 1) {
            if (position == 1) {
                item = mList.get(0);
                holder.ivGoodsImage.setImageURI(Util.transferCropImage(item.getCover(), holder.layoutParams.width));
            }
        }
        final WorkImage.GoodsEntity finalItem = item;
        holder.ivGoodsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalItem == null) return;
                if (listener != null)
                    listener.OnItemClick(finalItem.getId(), finalItem.getRecommend());
            }
        });
        return itemView;
    }

    class ViewHolder {
        SimpleDraweeView ivGoodsImage;
        LinearLayout.LayoutParams layoutParams;
    }

    public interface OnItemClickListener {
        void OnItemClick(String goodsId, String recommend);
    }
}

