package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.NewGoodsDetailActivity;
import cn.yiya.shiji.entity.goodes.NewGoodsItem;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;

/**
 * Created by Tom on 2016/11/9.
 */
public class GoodsDetailVisitAdapter extends RecyclerView.Adapter<GoodsDetailVisitAdapter.GoodsDetailVisitViewHolder> {

    private Context mContext;
    private List<NewGoodsItem> list;

    public GoodsDetailVisitAdapter(Context mContext, List<NewGoodsItem> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public GoodsDetailVisitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GoodsDetailVisitViewHolder(LayoutInflater.from(mContext).inflate(R.layout.goods_detail_visit_item, parent, false));
    }

    @Override
    public void onBindViewHolder(GoodsDetailVisitViewHolder holder, int position) {
        final NewGoodsItem item = list.get(position);

        holder.ivGoods.setImageURI(Util.transferImage(item.getCover(), SimpleUtils.dp2px(mContext, 160)), R.mipmap.work_default);

        holder.ivGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext, GoodsDetailActivity.class);
//                intent.putExtra("goodsId", item.getId());
//                mContext.startActivity(intent);
                Intent intent = new Intent(mContext, NewGoodsDetailActivity.class);
                intent.putExtra("goodsId", item.getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class GoodsDetailVisitViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView ivGoods;

        public GoodsDetailVisitViewHolder(View itemView) {
            super(itemView);
            ivGoods = (SimpleDraweeView) itemView.findViewById(R.id.iv_visit);
        }
    }
}
