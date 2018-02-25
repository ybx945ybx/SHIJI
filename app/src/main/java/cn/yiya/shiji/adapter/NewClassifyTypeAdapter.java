package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.NewCategoryActivity;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.entity.HotCategoryObject;
import cn.yiya.shiji.entity.LogstashReport.CategoryLogstashItem;
import cn.yiya.shiji.netroid.Netroid;

/**
 * Created by Amy on 2016/6/22.
 */
public class NewClassifyTypeAdapter extends RecyclerView.Adapter<NewClassifyTypeAdapter.TypeViewHolder> {
    private Context mContext;
    private List<HotCategoryObject.SecondItem> mList;

    public NewClassifyTypeAdapter(Context mContext) {
        this.mContext = mContext;
        this.mList = new ArrayList<>();
    }

    public void setList(List<HotCategoryObject.SecondItem> mList) {
        this.mList = mList;
    }

    @Override
    public TypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TypeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_classify_type, parent, false));
    }

    @Override
    public void onBindViewHolder(TypeViewHolder holder, int position) {
        final HotCategoryObject.SecondItem item = mList.get(position);
        Netroid.displayImage(item.getIcon(), holder.ivType);
        holder.ivType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打点事件
                CategoryLogstashItem categoryLogstashItem = new CategoryLogstashItem();
                categoryLogstashItem.setId(String.valueOf(item.getId()));
                categoryLogstashItem.setUser_id(BaseApplication.getInstance().readUserId());
                categoryLogstashItem.setName(item.getName());
                new RetrofitRequest<>(ApiRequest.getApiShijiLogstash().reportCategoryEvent(categoryLogstashItem)).handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {

                    }
                });

                Intent intent = new Intent(mContext, NewCategoryActivity.class);
                intent.putExtra("typeId", item.getId());//一级分类编号
                intent.putExtra("typeName", item.getName());//一级分类名称
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class TypeViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivType;

        public TypeViewHolder(View itemView) {
            super(itemView);
            ivType = (ImageView) itemView.findViewById(R.id.iv_type);
        }
    }
}

