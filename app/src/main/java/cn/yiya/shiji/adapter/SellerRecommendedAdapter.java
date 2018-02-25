package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.BaseAppCompatActivity;
import cn.yiya.shiji.activity.NewGoodsDetailActivity;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MapRequest;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.goodes.NewGoodsItem;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.ProgressDialog;
import cn.yiya.shiji.views.SwipeItemLayout;

/**
 * Created by Tom on 2016/12/5.
 */

public class SellerRecommendedAdapter extends RecyclerView.Adapter<SellerRecommendedAdapter.SellerRecommendedViewHolder>{

    private Context mContext;
    private List<NewGoodsItem> mList;

    public SellerRecommendedAdapter(Context mContext){
        this.mContext = mContext;
        mList = new ArrayList<>();
    }

    public List<NewGoodsItem> getmList() {
        return mList;
    }

    public void setmList(List<NewGoodsItem> mList) {
        this.mList = mList;
    }

    @Override
    public SellerRecommendedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SellerRecommendedViewHolder(LayoutInflater.from(mContext).inflate(R.layout.seller_recommended_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final SellerRecommendedViewHolder holder, final int position) {
        final NewGoodsItem item = mList.get(position);

        holder.ivGoodsImage.setImageURI(Util.transferImage(item.getCover(), SimpleUtils.dp2px(mContext, 120)));
        holder.ivGoodsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NewGoodsDetailActivity.class);
                intent.putExtra("goodsId", item.getId());
                mContext.startActivity(intent);
            }
        });
        holder.tvBrand.setText(item.getBrand());
        holder.tvTitle.setText(item.getTitle());
        holder.tvPrice.setText("￥" + item.getPrice());
        holder.tvCommission.setText("佣金：￥" + item.getFor_seller());
        holder.tvCommissionRatio.setText("佣金比例：" + item.getShare_profit());

        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((SwipeItemLayout) holder.itemView).isOpen()) {
                    Util.showCustomDialog(mContext, "确认删除？", new ProgressDialog.DialogClick() {
                        @Override
                        public void OkClick() {
                            ((BaseAppCompatActivity) mContext).showPreDialog("正在删除");
                            deleteGoods(item.getId(), position,holder);
                        }

                        @Override
                        public void CancelClick() {

                        }
                    });
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class SellerRecommendedViewHolder extends RecyclerView.ViewHolder{

        private SimpleDraweeView ivGoodsImage;
        private TextView tvBrand, tvTitle, tvPrice, tvCommission, tvCommissionRatio, tvDelete;

        public SellerRecommendedViewHolder(View itemView) {
            super(itemView);
            ivGoodsImage = (SimpleDraweeView) itemView.findViewById(R.id.iv_goods_image);
            tvBrand = (TextView) itemView.findViewById(R.id.item_brand);
            tvTitle = (TextView) itemView.findViewById(R.id.item_title);
            tvPrice = (TextView) itemView.findViewById(R.id.item_price);
            tvCommission = (TextView) itemView.findViewById(R.id.tv_commission);
            tvCommissionRatio = (TextView) itemView.findViewById(R.id.tv_commission_ratio);
            tvDelete = (TextView) itemView.findViewById(R.id.tv_delete);
        }
    }

    /**
     * 删除已推荐商品
     *
     * @param goodsId
     * @param position
     */
    private void deleteGoods(String goodsId, final int position, final SellerRecommendedViewHolder holder) {
        new RetrofitRequest<>(ApiRequest.getApiShiji().getRecommendGoodsEdit(MapRequest.setRecommendGoodsAddMap(goodsId, 2)))
                .handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        ((BaseAppCompatActivity) mContext).hidePreDialog();
                        if (msg.isSuccess()) {
                            ((SwipeItemLayout) holder.itemView).close();
                            mList.remove(position);
                            notifyItemRemoved(position);
                            if (mList.size() > 0) {
                                notifyItemRangeChanged(position, mList.size());
                            } else {
                                if (onItemClickListener != null) {
                                    onItemClickListener.deleteAll();
                                }
                            }

                        } else {
                            Util.toast(mContext, "删除失败", true);
                        }
                    }
                });
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void deleteAll();
    }
}
