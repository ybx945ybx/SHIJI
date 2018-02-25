package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.NewGoodsDetailActivity;
import cn.yiya.shiji.entity.goodes.NewGoodsItem;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;

/**
 * Created by Tom on 2017/1/5.
 */

public class ShopRecommendHAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<NewGoodsItem> mList = new ArrayList<>();

    private int type; // 1 large  2 small
    public final static int GOODS = 111;
    public final static int MORE = 112;

    private OnClickListener onClickListener;

    public interface OnClickListener {
        void delete(int position);
    }

    public ShopRecommendHAdapter(Context mContext, int type) {
        this.mContext = mContext;
        this.type = type;
    }

    public ArrayList<NewGoodsItem> getmList() {
        return this.mList;
    }

    public void setmList(ArrayList<NewGoodsItem> mList) {
        this.mList = (mList == null ? new ArrayList<NewGoodsItem>() : mList);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(type == 1){// large
            LargeViewHolder largeViewHolder = null;
            if(convertView == null){
                largeViewHolder = new LargeViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_shop_recommend_large_list, parent, false);
                largeViewHolder.ivGoodsImage = (SimpleDraweeView) convertView.findViewById(R.id.iv_goods_image);
                largeViewHolder.ivDelete = (ImageView) convertView.findViewById(R.id.iv_delete);
                largeViewHolder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
                largeViewHolder.tvCommission = (TextView) convertView.findViewById(R.id.tv_commission);
                largeViewHolder.tvCommissionRatio = (TextView) convertView.findViewById(R.id.tv_commission_ratio);
            }else {
                largeViewHolder = (LargeViewHolder) convertView.getTag();
            }

            final NewGoodsItem item = mList.get(position);
            largeViewHolder.ivGoodsImage.setImageURI(Util.transferImage(item.getCover(), SimpleUtils.dp2px(mContext, 120)));
            largeViewHolder.tvPrice.setText("¥ " + item.getPrice());
            largeViewHolder.tvCommission.setText("佣金：¥" + item.getFor_seller());
            largeViewHolder.tvCommissionRatio.setText("佣金比例：" + item.getShare_profit());
            largeViewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null) onClickListener.delete(position);
                }
            });
            largeViewHolder.ivGoodsImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoGoodsDetail(item.getId());
                }
            });


        }else {// small
            SmallViewHolder smallViewHolder = null;
            if(convertView == null){
                smallViewHolder = new SmallViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_shop_recommend_small_list, parent, false);
                smallViewHolder.ivGoodsImage = (SimpleDraweeView) convertView.findViewById(R.id.iv_goods_image);
                smallViewHolder.ivDelete = (ImageView) convertView.findViewById(R.id.iv_delete);
            }else {
                smallViewHolder = (SmallViewHolder) convertView.getTag();
            }

            final NewGoodsItem item = mList.get(position);
            smallViewHolder.ivGoodsImage.setImageURI(Util.transferImage(item.getCover(), SimpleUtils.dp2px(mContext, 80)));
            smallViewHolder.ivGoodsImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoGoodsDetail(item.getId());
                }
            });
            smallViewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null) onClickListener.delete(position);
                }
            });

        }
        return convertView;
    }

    /**
     * 跳转到商品详情
     *
     * @param goodsId
     */
    private void gotoGoodsDetail(final String goodsId) {
        Intent intent = new Intent(mContext, NewGoodsDetailActivity.class);
        intent.putExtra("goodsId", goodsId);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mContext.startActivity(intent);
    }

    class LargeViewHolder{
        SimpleDraweeView ivGoodsImage;
        ImageView ivDelete;
        TextView tvPrice, tvCommission, tvCommissionRatio;
    }

    class SmallViewHolder{
        SimpleDraweeView ivGoodsImage;
        ImageView ivDelete;
    }
}
