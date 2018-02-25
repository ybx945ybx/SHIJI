package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.TravelStoreActivity;
import cn.yiya.shiji.entity.navigation.StoreShortInfo;
import cn.yiya.shiji.utils.BitmapTool;
import cn.yiya.shiji.utils.SimpleUtils;

/**
 * Created by Tom on 2016/3/31.
 */
public class HotShopItemAdapter extends RecyclerView.Adapter<HotShopItemAdapter.HotShopViewHolder> {
    private Context mContext;
    private ArrayList<StoreShortInfo> mlist;
    private String countryId;
    private String cityId;

    public HotShopItemAdapter(Context mContext, String countryId, String cityId){
        this.mContext = mContext;
        this.countryId = countryId;
        this.cityId = cityId;
    }

    public ArrayList<StoreShortInfo> getMlist() {
        return mlist;
    }

    public void setMlist(ArrayList<StoreShortInfo> mlist) {
        this.mlist = mlist;
    }

    @Override
    public HotShopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HotShopViewHolder(LayoutInflater.from(mContext).inflate(R.layout.hot_shop_item, parent, false));
    }

    @Override
    public void onBindViewHolder(HotShopViewHolder holder, int position) {
        final StoreShortInfo info = mlist.get(position);

//        holder.ivShopCover.setImageResource(info.getImageResource());
//        Netroid.displayImage(info.getCover(), holder.ivShopCover);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)holder.ivShopCover.getLayoutParams();
        layoutParams.width = (SimpleUtils.getScreenWidth(mContext) - SimpleUtils.dp2px(mContext,64))/3;
        layoutParams.height = layoutParams.width;
        holder.ivShopCover.setLayoutParams(layoutParams);
        BitmapTool.clipShowImageView(info.getCover(), holder.ivShopCover, R.mipmap.travel_default, layoutParams.width, layoutParams.height);
//        Netroid.displayImage(Util.clipImageViewByWH(info.getCover(), layoutParams.width, layoutParams.height), holder.ivShopCover, R.drawable.travel_default);

        holder.tvShopName.setText(info.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TravelStoreActivity.class);
                intent.putExtra("id", info.getId());
                intent.putExtra("countryId", countryId);
                intent.putExtra("cityId", cityId);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mlist == null){
            return  0;
        }else {
            return mlist.size();
        }
    }

    class HotShopViewHolder extends RecyclerView.ViewHolder{
        ImageView ivShopCover;
        TextView tvShopName;

        public HotShopViewHolder(View itemView) {
            super(itemView);
            ivShopCover = (ImageView) itemView.findViewById(R.id.iv_shop_cover);
            tvShopName = (TextView) itemView.findViewById(R.id.tv_shop_name);
        }
    }
}


