package cn.yiya.shiji.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.ChooseGoodsActivity;
import cn.yiya.shiji.activity.ChoosePublishedGoodsActivity;
import cn.yiya.shiji.activity.ShowOrderActivity;
import cn.yiya.shiji.entity.PublishedGoodsInfo;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.TagAbleImageView;

/**
 * Created by Tom on 2015/12/9.
 */
public class PageFragmentItemAdapter extends RecyclerView.Adapter<PageFragmentItemAdapter.MyPageFragmentItemViewHolder> {
    private Context mContext;
    private ArrayList<PublishedGoodsInfo> mList;
    private int nType;

    public PageFragmentItemAdapter(ArrayList<PublishedGoodsInfo> mList, Context context, int type){
        this.mContext = context;
        this.mList = mList;
        this.nType = type;
    }

    @Override
    public MyPageFragmentItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyPageFragmentItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.page_fragment_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyPageFragmentItemViewHolder holder, int position) {
        final PublishedGoodsInfo item = mList.get(position);

        holder.tvGoodsPrice.setText(item.getPrice()+"");
        holder.tvGoods.setText(item.getBrand()+" "+item.getTitle());
        holder.tvGoodsBaner.setText(item.getSite());
        Netroid.displayImage(Util.transfer(item.getCover()), holder.ivGoods);

        holder.rltItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                String json = new Gson().toJson(item);
                if (nType > 0) {
                    Type type = new TypeToken<ArrayList<TagAbleImageView.TagInfo>>(){}.getType();
                    String tag = new Gson().toJson(hanlderJson(item), type);
                    intent.setClass(mContext, ShowOrderActivity.class);
                    intent.putExtra("TAG", tag);
                    intent.putExtra("AUTO", true);
                    intent.putExtra("cover", item.getCover());
                    intent.putExtra("goodsId", item.getGoods_id());
                    intent.putExtra("skip", true);
                    mContext.startActivity(intent);
                    ((ChooseGoodsActivity)mContext).finish();
                    return;
                }
                intent.putExtra("json", json);
                ((ChoosePublishedGoodsActivity)mContext).setResult(Activity.RESULT_OK, intent);
                ((ChoosePublishedGoodsActivity)mContext).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mList == null){
            return 0;
        }
        return mList.size();
    }

    private ArrayList<TagAbleImageView.TagInfo> hanlderJson(PublishedGoodsInfo item) {
        ArrayList<TagAbleImageView.TagInfo> tagInfos = new ArrayList<>();
        TagAbleImageView.TagInfo info1 = new TagAbleImageView.TagInfo();
        info1.setContent(item.getBrand());
        info1.setDetails(item.getTitle());
        info1.setType(1);
        tagInfos.add(info1);

        TagAbleImageView.TagInfo info2 = new TagAbleImageView.TagInfo();
        info2.setContent("柿集");
        info2.setDetails(item.getSite());
        info2.setType(3);
        tagInfos.add(info2);

        TagAbleImageView.TagInfo info3 = new TagAbleImageView.TagInfo();
        info3.setContent("人民币");
        info3.setDetails(item.getPrice());
        info3.setType(2);
        tagInfos.add(info3);

        return tagInfos;
    }

    class MyPageFragmentItemViewHolder extends RecyclerView.ViewHolder{
        ImageView ivGoods;
        TextView tvGoods;
        TextView tvGoodsBaner;
        TextView tvGoodsPrice;
        RelativeLayout rltItem;
        public MyPageFragmentItemViewHolder(View itemView) {
            super(itemView);
            ivGoods = (ImageView) itemView.findViewById(R.id.iv_goods);
            tvGoods = (TextView) itemView.findViewById(R.id.goods);
            tvGoodsBaner = (TextView) itemView.findViewById(R.id.goods_baner);
            tvGoodsPrice = (TextView) itemView.findViewById(R.id.goods_price);
            rltItem = (RelativeLayout) itemView.findViewById(R.id.rlt_item);
        }
    }
}

