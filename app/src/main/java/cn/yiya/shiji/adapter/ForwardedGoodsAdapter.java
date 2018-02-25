package cn.yiya.shiji.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.BaseAppCompatActivity;
import cn.yiya.shiji.activity.NewGoodsDetailActivity;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MapRequest;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.GoodsDetailEntity;
import cn.yiya.shiji.entity.goodes.NewGoodsItem;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.GoodsBuyDialog;

/**
 * Created by Tom on 2016/12/1.
 */

public class ForwardedGoodsAdapter extends RecyclerView.Adapter<ForwardedGoodsAdapter.ForwardedGoodsViewHolder>{

    private Context mContext;
    private ArrayList<NewGoodsItem> mList = new ArrayList<>();

    public ForwardedGoodsAdapter(Context mContext){
        this.mContext = mContext;
    }

    public ArrayList<NewGoodsItem> getmList() {
        return mList;
    }

    public void setmList(ArrayList<NewGoodsItem> mList) {
        this.mList = mList;
    }

    @Override
    public ForwardedGoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ForwardedGoodsViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_shop_recommend_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ForwardedGoodsViewHolder holder, int position) {
        final NewGoodsItem item = mList.get(position);

        holder.ivGoodsImage.setImageURI(Util.transferImage(item.getCover(), SimpleUtils.dp2px(mContext, 120)));
        holder.tvBrand.setText(item.getBrand());
        holder.tvTitle.setText(item.getTitle());
        holder.tvPrice.setText("￥" + item.getPrice());
        holder.tvCommission.setText("佣金：￥147");
        holder.tvCommissionRatio.setText("佣金比例：10%");

        holder.ivGoodsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoGoodsDetail(item.getId());
            }
        });

        holder.tvBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGoodsDetail(item.getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    private void gotoGoodsDetail(final String goodsId) {
        Intent intent = new Intent(mContext, NewGoodsDetailActivity.class);
        intent.putExtra("goodsId", goodsId);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mContext.startActivity(intent);
    }

    // 获取商品详情信息
    private void getGoodsDetail(String goodsId) {
        new RetrofitRequest<JsonObject>(ApiRequest.getApiShiji().getGoodsDetailJson(MapRequest.setGoodsDetailMap(goodsId))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    JsonObject goodsJson = (JsonObject) msg.obj;
                    showBuyPopWindow(goodsJson);
                } else {
//                    hidePreDialog();
                }
            }
        });
    }

    // 下单，直接购买
    private void showBuyPopWindow(JsonObject goodsJson) {
        Dialog goodsBuyDialog = new GoodsBuyDialog((BaseAppCompatActivity) mContext, goodsJson, null , 1, false).build();
        goodsBuyDialog.show();
    }

    class ForwardedGoodsViewHolder extends RecyclerView.ViewHolder{
        private SimpleDraweeView ivGoodsImage;
        private TextView tvBrand, tvTitle, tvPrice, tvCommission, tvCommissionRatio, tvRecommended, tvBuy;

        public ForwardedGoodsViewHolder(View itemView) {
            super(itemView);
            ivGoodsImage = (SimpleDraweeView) itemView.findViewById(R.id.iv_goods_image);
            tvBrand = (TextView) itemView.findViewById(R.id.item_brand);
            tvTitle = (TextView) itemView.findViewById(R.id.item_title);
            tvPrice = (TextView) itemView.findViewById(R.id.item_price);
            tvCommission = (TextView) itemView.findViewById(R.id.tv_commission);
            tvCommissionRatio = (TextView) itemView.findViewById(R.id.tv_commission_ratio);
            tvRecommended = (TextView) itemView.findViewById(R.id.tv_recommended);
            tvBuy = (TextView) itemView.findViewById(R.id.tv_buy);
            tvBuy.setVisibility(View.VISIBLE);

            itemView.findViewById(R.id.view_line_top).setVisibility(View.VISIBLE);
            itemView.findViewById(R.id.view_line).setVisibility(View.GONE);
        }
    }
}
