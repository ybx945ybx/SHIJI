package cn.yiya.shiji.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.BaseAppCompatActivity;
import cn.yiya.shiji.activity.NewGoodsDetailActivity;
import cn.yiya.shiji.activity.NewShoppingCartActivity;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MapRequest;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.ShoppingCartGoods;
import cn.yiya.shiji.entity.ShoppingCartSku;
import cn.yiya.shiji.utils.DebugUtil;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.FullyLinearLayoutManager;
import cn.yiya.shiji.views.GoodsBuyDialog;
import cn.yiya.shiji.views.ProgressDialog;
import cn.yiya.shiji.views.SwipeItemLayout;

/**
 * 购物车站点下商品列表
 * Created by Amy on 2016/11/7.
 */

public class ShoppingCartGoodsAdapter extends RecyclerView.Adapter<ShoppingCartGoodsAdapter.GoodsViewHolder> {
    private Context mContext;
    private ArrayList<ShoppingCartGoods> mList;
    private OnClickListener clickListener;
    private Dialog dialog;

    public ShoppingCartGoodsAdapter(Context context, ArrayList<ShoppingCartGoods> list) {
        this.mContext = context;
        this.mList = list;

    }

    public void setOnClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public GoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GoodsViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_shopping_cart_goods, parent, false));
    }

    @Override
    public void onBindViewHolder(final GoodsViewHolder holder, final int position) {

        final ShoppingCartGoods item = mList.get(position);
        if (item.isChecked()) {
            holder.cbGoods.setChecked(true);
        } else {
            holder.cbGoods.setChecked(false);
        }

        if (item.getStatus() == 1) {
            holder.llCheckGoods.setEnabled(true);
            holder.ivSoldOut.setVisibility(View.GONE);
        } else {
            //售罄
            holder.llCheckGoods.setEnabled(false);
            holder.ivSoldOut.setVisibility(View.VISIBLE);
        }

        if ((Float.parseFloat(item.getList_price()) - Float.parseFloat(item.getPrice())) > 0) {
            holder.disLinearLayout.setVisibility(View.VISIBLE);
            holder.discount_tv.setText(Util.FloatKeepZero(
                    Float.parseFloat(item.getList_price()) - Float.parseFloat(item.getPrice())));
        }

        holder.ivGoodsImage.setImageURI(Util.ScaleImageGoodes(item.getCover(), SimpleUtils.dp2px(mContext, 135)));
        holder.tvGoodsName.setText(item.getTitle());

        ArrayList<String[]> paramsList = new ArrayList<>();
        String[] param = new String[]{"数量", String.valueOf(item.getNum())};
        paramsList.add(param);

        ArrayList<ShoppingCartSku> skus = item.getSku();
        for (int i = 0; i < skus.size(); i++) {
            param = new String[]{skus.get(i).getKey(), skus.get(i).getValue()};
            paramsList.add(param);
        }
        holder.rvParams.setAdapter(new GoodsDescParamsAdapter(mContext, paramsList));

        holder.tvGoodsPrice.setText(item.getPrice());
        if (item.getNum() > 1) {
            holder.tvSubtoatlPrice.setText(Util.subZeroAndDot(String.valueOf(Float.parseFloat(item.getPrice()) * item.getNum())));
            holder.llSubtoatlPrice.setVisibility(View.VISIBLE);
        } else {
            holder.llSubtoatlPrice.setVisibility(View.GONE);
        }

        holder.llCheckGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.cbGoods.toggle();
                item.setChecked(holder.cbGoods.isChecked());
                if (clickListener != null) {
                    clickListener.checkGoods(position, holder.cbGoods.isChecked());
                }
            }
        });
        holder.llGoodsEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  添加购物车
                if (((NewShoppingCartActivity) mContext).isEffectClick()) {
                    if (dialog != null && dialog.isShowing()) {
                        return;
                    }
                    gotoGoodsBuy(item);
                }
            }
        });
        holder.rlGoodsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NewGoodsDetailActivity.class);
                intent.putExtra("goodsId", item.getGoodsId());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
            }
        });

        holder.btnWish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((SwipeItemLayout) holder.itemView).isOpen()) {
                    ((BaseAppCompatActivity) mContext).showPreDialog("正在移入收藏夹");
                    WishGoods(item.getGoodsId(), item.getCartId(), position);
                }
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((SwipeItemLayout) holder.itemView).isOpen()) {
                    Util.showCustomDialog(mContext, "确认删除？", new ProgressDialog.DialogClick() {
                        @Override
                        public void OkClick() {
                            ((BaseAppCompatActivity) mContext).showPreDialog("正在删除");
                            deleteGoods(item.getCartId(), position);
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

    class GoodsViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout llCheckGoods;
        private CheckBox cbGoods;
        private LinearLayout llGoodsEdit;

        private RelativeLayout rlGoodsImage;
        private SimpleDraweeView ivGoodsImage;
        private ImageView ivSoldOut;

        private TextView tvGoodsName;
        private RecyclerView rvParams;

        private TextView tvGoodsPrice;
        private LinearLayout llSubtoatlPrice;
        private TextView tvSubtoatlPrice;

        private LinearLayout disLinearLayout;
        private TextView discount_tv;

        private Button btnWish;
        private Button btnDelete;

        public GoodsViewHolder(View itemView) {
            super(itemView);
            llCheckGoods = (LinearLayout) itemView.findViewById(R.id.ll_check_goods);
            cbGoods = (CheckBox) itemView.findViewById(R.id.cb_goods);
            llGoodsEdit = (LinearLayout) itemView.findViewById(R.id.ll_goods_edit);
            rlGoodsImage = (RelativeLayout) itemView.findViewById(R.id.rl_goods_image);
            ivGoodsImage = (SimpleDraweeView) itemView.findViewById(R.id.iv_goods_image);
            ivSoldOut = (ImageView) itemView.findViewById(R.id.iv_sold_out);
            tvGoodsName = (TextView) itemView.findViewById(R.id.tv_goods_name);

            rvParams = (RecyclerView) itemView.findViewById(R.id.rv_params);
            rvParams.setLayoutManager(new FullyLinearLayoutManager(mContext));
            rvParams.setHasFixedSize(true);
            rvParams.setNestedScrollingEnabled(false);

            tvGoodsPrice = (TextView) itemView.findViewById(R.id.tv_goods_price);
            llSubtoatlPrice = (LinearLayout) itemView.findViewById(R.id.ll_subtotal_price);
            tvSubtoatlPrice = (TextView) itemView.findViewById(R.id.tv_subtotal_price);
            btnWish = (Button) itemView.findViewById(R.id.btn_wish);
            btnDelete = (Button) itemView.findViewById(R.id.btn_delete);

            disLinearLayout = (LinearLayout) itemView.findViewById(R.id.linearlayout);
            discount_tv = (TextView) itemView.findViewById(R.id.discount_tv);
        }
    }

    /**
     * 将商品移入购物车
     *
     * @param goodsId
     * @param cartId
     * @param position
     */
    private void WishGoods(String goodsId, final String cartId, final int position) {
        new RetrofitRequest<>(ApiRequest.getApiShiji().followGoods(goodsId)).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    deleteGoods(cartId, position);
                }
            }
        });
    }

    /**
     * 删除购物车中商品
     *
     * @param cartId
     * @param position
     */
    private void deleteGoods(String cartId, final int position) {
        new RetrofitRequest<>(ApiRequest.getApiShiji().deleteShoppingCart(cartId))
                .handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        ((BaseAppCompatActivity) mContext).hidePreDialog();
                        if (msg.isSuccess()) {
                            mList.remove(position);
                            notifyItemRemoved(position);
                            if (mList.size() > 0) {
                                notifyItemRangeChanged(position, mList.size());
                            }
                            if (clickListener != null) clickListener.gotoCheckAll();
                        } else {
                            Util.toast(mContext, "删除失败", true);
                        }
                    }
                });
    }

    public interface OnClickListener {
        void checkGoods(int pos, boolean isChecked);

        void gotoCheckAll();
    }

    /**
     * 购物车弹层
     */
    private void gotoGoodsBuy(final ShoppingCartGoods item) {
        DebugUtil.e("=======goodsid:" + item.getGoodsId());
        new RetrofitRequest<JsonObject>(ApiRequest.getApiShiji().getGoodsDetailJson(MapRequest.setGoodsDetailMap(item.getGoodsId()))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    JsonObject goodsJson = (JsonObject) msg.obj;
                    ((NewShoppingCartActivity) mContext).showPreDialog("正在加载");
                    dialog = new GoodsBuyDialog((NewShoppingCartActivity) mContext, goodsJson, item, ((NewShoppingCartActivity) mContext).cartsList).build();
                    ((NewShoppingCartActivity) mContext).hidePreDialog();
                    dialog.show();
                }
            }
        });
    }
}
