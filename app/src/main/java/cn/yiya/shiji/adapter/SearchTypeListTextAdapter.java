package cn.yiya.shiji.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.search.NewSearchEntity;

/**
 * Created by Tom on 2016/8/22.
 */
public class SearchTypeListTextAdapter extends RecyclerView.Adapter<SearchTypeListTextAdapter.SearchTypeListTextViewHolder> {
    private Context mContext;
    private int type;           // 1 品牌无logo  2 商城  3 类别

    private List<NewSearchEntity.SiteListEntity> mSiteList = new ArrayList<>();
    private List<NewSearchEntity.BrandListEntity> mBrandist = new ArrayList<>();
    private List<NewSearchEntity.GoodsTypeListEntity> mCategoryList = new ArrayList<>();

    private OnItemClickListener onItemClickListen;

    public SearchTypeListTextAdapter(Context mContext, int type){
        this.mContext = mContext;
        this.type = type;
    }

    public List<NewSearchEntity.SiteListEntity> getmSiteList() {
        return mSiteList;
    }

    public void setmSiteList(List<NewSearchEntity.SiteListEntity> mSiteList) {
        this.mSiteList = mSiteList;
    }

    public List<NewSearchEntity.BrandListEntity> getmBrandist() {
        return mBrandist;
    }

    public void setmBrandist(List<NewSearchEntity.BrandListEntity> mBrandist) {
        this.mBrandist = mBrandist;
    }

    public List<NewSearchEntity.GoodsTypeListEntity> getmCategoryList() {
        return mCategoryList;
    }

    public void setmCategoryList(List<NewSearchEntity.GoodsTypeListEntity> mCategoryList) {
        this.mCategoryList = mCategoryList;
    }

    @Override
    public SearchTypeListTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchTypeListTextViewHolder(LayoutInflater.from(mContext).inflate(R.layout.search_item_text, parent, false));
    }

    @Override
    public void onBindViewHolder(SearchTypeListTextViewHolder holder, int position) {
        if(type == 1){
            final NewSearchEntity.BrandListEntity brandInfo = mBrandist.get(position);
            holder.tvName.setText(brandInfo.getName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListen != null){
                        onItemClickListen.OnBrandItemClick(brandInfo);
                    }
//                    Intent intent = new Intent(mContext, NewSingleBrandActivity.class);
//                    intent.putExtra("brand_id", brandInfo.getId());
//                    mContext.startActivity(intent);
                }
            });
        }else if (type == 2){
            final NewSearchEntity.SiteListEntity sitiInfo = mSiteList.get(position);
            holder.tvName.setText(sitiInfo.getName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListen != null){
                        onItemClickListen.OnSiteItemClick(sitiInfo);
                    }
//                    Intent intent = new Intent(mContext, GoodsListActivity.class);
//                    intent.putExtra("id", sitiInfo.getId());
//                    intent.putExtra("site_name", sitiInfo.getName());
//                    intent.putExtra("type", 1003);
//                    mContext.startActivity(intent);
                }
            });
        }else if (type == 3){
            final NewSearchEntity.GoodsTypeListEntity categroyInfo = mCategoryList.get(position);
            holder.tvName.setText(categroyInfo.getName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListen != null){
                        onItemClickListen.OnCategroyItemClick(categroyInfo);
                    }
//                    Intent intent = new Intent(mContext, GoodsListActivity.class);
//                    intent.putExtra("categoryid", categroyInfo.getId());
//                    intent.putExtra("categoryname", categroyInfo.getName());
//                    intent.putExtra("sourcce", 2);
//                    intent.putExtra("type", 1002);
//                    mContext.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if(type == 1){ // 品牌
            return mBrandist == null ? 0 : mBrandist.size();
        }else if (type == 2){ // 商城
            return mSiteList == null ? 0 : mSiteList.size();
        }else if (type == 3){
            return mCategoryList == null ? 0 : mCategoryList.size();
        }else {
            return 0;
        }
    }

    public interface OnItemClickListener{
        void OnBrandItemClick(NewSearchEntity.BrandListEntity brandListEntity);
        void OnSiteItemClick(NewSearchEntity.SiteListEntity siteListEntity);
        void OnCategroyItemClick(NewSearchEntity.GoodsTypeListEntity goodsTypeListEntity);
    }

    public void setOnItemClickListen(OnItemClickListener onItemClickListen){
        this.onItemClickListen = onItemClickListen;

    }

    class SearchTypeListTextViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        public SearchTypeListTextViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);

        }
    }

}
