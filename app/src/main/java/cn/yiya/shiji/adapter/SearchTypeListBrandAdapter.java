package cn.yiya.shiji.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.duowan.mobile.netroid.image.NetworkImageView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.search.NewSearchEntity;
import cn.yiya.shiji.netroid.Netroid;

/**
 * Created by Tom on 2016/8/22.
 */
public class SearchTypeListBrandAdapter extends RecyclerView.Adapter<SearchTypeListBrandAdapter.SearchTypeListViewHolder> {

    private Context mContext;
    private ArrayList<NewSearchEntity.BrandListEntity> mBrandList;
    private OnItemClickListener onItemClickListen;

    public SearchTypeListBrandAdapter(Context mContext, ArrayList<NewSearchEntity.BrandListEntity> mBrandList){
        this.mContext = mContext;
        this.mBrandList = mBrandList;
    }

    public SearchTypeListBrandAdapter(Context mContext){
        this.mContext = mContext;
    }

    public ArrayList<NewSearchEntity.BrandListEntity> getmBrandList() {
        return mBrandList;
    }

    public void setmBrandList(ArrayList<NewSearchEntity.BrandListEntity> mBrandList) {
        this.mBrandList = mBrandList;
    }

    @Override
    public SearchTypeListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchTypeListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.search_item_brand, parent, false));
    }

    @Override
    public void onBindViewHolder(SearchTypeListViewHolder holder, int position) {
        final NewSearchEntity.BrandListEntity info = mBrandList.get(position);

        Netroid.displayImage(info.getLogo(), holder.ivBrandLogo, R.mipmap.work_default);
        holder.tvBrandName.setText(info.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListen != null){
                    onItemClickListen.OnBrandLogoItemClick(info);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if(mBrandList == null){
            return 0;
        }else {
            return mBrandList.size();
        }
    }

    public interface OnItemClickListener{
        void OnBrandLogoItemClick(NewSearchEntity.BrandListEntity brandListEntity);
    }

    public void setOnItemClickListen(OnItemClickListener onItemClickListen){
        this.onItemClickListen = onItemClickListen;

    }

    class SearchTypeListViewHolder extends RecyclerView.ViewHolder{

        NetworkImageView ivBrandLogo;
        TextView tvBrandName;

        public SearchTypeListViewHolder(View itemView) {
            super(itemView);
            ivBrandLogo = (NetworkImageView) itemView.findViewById(R.id.iv_brand_logo);
            tvBrandName = (TextView) itemView.findViewById(R.id.tv_brand_name);

        }
    }

}

