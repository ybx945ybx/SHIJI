package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.TravelCityActivity;
import cn.yiya.shiji.entity.navigation.CityInfo;
import cn.yiya.shiji.utils.BitmapTool;
import cn.yiya.shiji.views.joinable.ParallaxViewHolder;

/**
 * Created by Tom on 2016/3/28.
 */
public class HotCityAdapter extends RecyclerView.Adapter<HotCityAdapter.HotCityViewHolder>{
    private ArrayList<CityInfo> mList;
    private Context mContext;

    private String countryId;
    public HotCityAdapter(Context mContext, ArrayList<CityInfo> mList, String countryId){
        this.mContext = mContext;
        this.mList = mList;
        this.countryId = countryId;
    }

    @Override
    public HotCityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.hot_city_item, parent, false);
        return new HotCityAdapter.HotCityViewHolder(v);
    }

    @Override
    public void onBindViewHolder(HotCityViewHolder holder, int position) {
        final CityInfo info  = mList.get(position);

        holder.tvName.setText(info.getName());
        holder.tvCnName.setText(info.getCn_name());
        BitmapTool.showFullImageView(mContext, info.getCover(), holder.getBackgroundImage());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TravelCityActivity.class);
                intent.putExtra("id", info.getId());
                intent.putExtra("countryId", countryId);
                mContext.startActivity(intent);
            }
        });

        holder.itemView.setTag(holder);
    }

    @Override
    public int getItemCount() {
        if (mList == null) {
            return 0;
        } else {
            return mList.size();
        }
    }

    public static class HotCityViewHolder extends ParallaxViewHolder {
        public TextView tvCnName;
        public TextView tvName;

        @Override
        public int getParallaxImageId() {
            return R.id.city_backgroud;
        }

        public HotCityViewHolder(View parent) {
            super(parent);
            tvCnName = (TextView) itemView.findViewById(R.id.cn_name);
            tvName = (TextView) itemView.findViewById(R.id.name);
        }
    }
}

