package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.NewMallGoodsActivity;
import cn.yiya.shiji.activity.NewSingleBrandActivity;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.entity.BrandsItem;
import cn.yiya.shiji.entity.HotMallItem;
import cn.yiya.shiji.entity.LogstashReport.CategoryBrandLogstashItem;
import cn.yiya.shiji.entity.LogstashReport.CategorySiteLogstashItem;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.Util;

/**
 * Created by Amy on 2016/7/11.
 */
public class NewBrandOrMallAdapter extends RecyclerView.Adapter<NewBrandOrMallAdapter.BrandOrMallViewHolder> {
    private Context mContext;
    private ArrayList<BrandsItem> brandsList;
    private ArrayList<HotMallItem> mallsList;
    private int type;

    public NewBrandOrMallAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setBrandsList(ArrayList<BrandsItem> mList) {
        this.brandsList = mList;
        this.type = 1;
    }

    public void setMallsList(ArrayList<HotMallItem> mList) {
        this.mallsList = mList;
        this.type = 2;
    }

    @Override
    public BrandOrMallViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BrandOrMallViewHolder(LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false));
    }

    @Override
    public void onBindViewHolder(BrandOrMallViewHolder holder, int position) {
        if (type == 1) {
            final BrandsItem item = brandsList.get(position);
            if (!TextUtils.isEmpty(item.getLogo())) {
                Netroid.displayImage(Util.transfer(item.getLogo()), holder.imageView);
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 打点事件
                        CategoryBrandLogstashItem categoryBrandLogstashItem = new CategoryBrandLogstashItem();
                        categoryBrandLogstashItem.setBrand_id(String.valueOf(item.getId()));
                        categoryBrandLogstashItem.setUser_id(BaseApplication.getInstance().readUserId());
                        new RetrofitRequest<>(ApiRequest.getApiShijiLogstash().reportCategoryBrandEvent(categoryBrandLogstashItem)).handRequest(new MsgCallBack() {
                            @Override
                            public void onResult(HttpMessage msg) {

                            }
                        });

                        Intent intent = new Intent(mContext, NewSingleBrandActivity.class);
                        intent.putExtra("brand_id", item.getId());
                        mContext.startActivity(intent);
                    }
                });
            }
        } else if (type == 2) {
            final HotMallItem item = mallsList.get(position);
            if (item.getLogo() != null) {
                Netroid.displayImage(Util.transfer(item.getLogo()), holder.imageView);
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 打点事件
                        CategorySiteLogstashItem categorySiteLogstashItem = new CategorySiteLogstashItem();
                        categorySiteLogstashItem.setId(String.valueOf(item.getId()));
                        categorySiteLogstashItem.setUser_id(BaseApplication.getInstance().readUserId());
                        new RetrofitRequest<>(ApiRequest.getApiShijiLogstash().reportCategorySiteEvent(categorySiteLogstashItem)).handRequest(new MsgCallBack() {
                            @Override
                            public void onResult(HttpMessage msg) {

                            }
                        });

                        Intent intent = new Intent(mContext, NewMallGoodsActivity.class);
                        intent.putExtra("id", item.getId());
                        mContext.startActivity(intent);
                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        if (type == 1) {
            return brandsList == null ? 0 : brandsList.size();
        } else if (type == 2) {
            return mallsList == null ? 0 : mallsList.size();
        } else return 0;
    }

    class BrandOrMallViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public BrandOrMallViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.hotimage);
        }
    }
}
