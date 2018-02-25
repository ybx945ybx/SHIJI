package cn.yiya.shiji.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.entity.navigation.CountryListInfo;
import cn.yiya.shiji.entity.navigation.CouponDetailInfo;
import cn.yiya.shiji.utils.SimpleUtils;

/**
 * Created by Tom on 2016/4/6.
 */
public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.CouponViewHolder> {
    private Context mContext;
    private ArrayList<CountryListInfo> mList;
    private ArrayList<CouponDetailInfo> countryCouponList;
    private OnItemClickListener onItemClickListener;

    public CouponAdapter(Context mContext, ArrayList<CouponDetailInfo> countryCouponList) {
        this.mContext = mContext;
        this.countryCouponList = countryCouponList;
    }

    public ArrayList<CountryListInfo> getmList() {
        return mList;
    }

    public void setmList(ArrayList<CountryListInfo> mList) {
        this.mList = mList;
    }

    @Override
    public CouponViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CouponViewHolder(LayoutInflater.from(mContext).inflate(R.layout.coupon_item, parent, false));
    }

    @Override
    public void onBindViewHolder(CouponViewHolder holder, int position) {
        final CouponDetailInfo info = countryCouponList.get(position);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)holder.ivCouponContent.getLayoutParams();
        float scale = 2.3f;
        int width = SimpleUtils.getScreenWidth(mContext) - SimpleUtils.dp2px(mContext,32);
        layoutParams.height = (int) (width / scale);
        holder.ivCouponContent.setLayoutParams(layoutParams);

        String imgPath = Configration.COUPON_PATH + "/" + info.getId() + ".png";
        info.setCover(imgPath);

        holder.tvBrief.setText(info.getBrief());
        holder.tvStoreName.setText(info.getStore_name());
        holder.tvRange.setText(info.getRange());
        holder.tvIndate.setText(info.getStart_time() + " - " + info.getEnd_time());
        holder.tvDes.setText(info.getDes());
        holder.tvTitle.setText(info.getStore_name());
        holder.relativelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.OnItemClick(info);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (countryCouponList == null) {
            return 0;
        } else {
            return countryCouponList.size();
        }
    }

    public ArrayList<CouponDetailInfo> getCountryCouponList() {
        return countryCouponList;
    }

    public void setCountryCouponList(ArrayList<CouponDetailInfo> countryCouponList) {
        this.countryCouponList = countryCouponList;
    }

    public interface OnItemClickListener {
        void OnItemClick(CouponDetailInfo info);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class CouponViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvBrief, tvRange, tvIndate, tvDes, tvStoreName;
        RelativeLayout relativelayout;
        ImageView ivCouponContent;

        public CouponViewHolder(View itemView) {
            super(itemView);
            relativelayout = (RelativeLayout) itemView.findViewById(R.id.relativelayout);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_coupon_title);
            tvDes = (TextView) itemView.findViewById(R.id.tv_des);
            tvIndate = (TextView) itemView.findViewById(R.id.tv_indate);
            tvRange = (TextView) itemView.findViewById(R.id.tv_range);
            tvStoreName = (TextView) itemView.findViewById(R.id.tv_store_name);
            tvBrief = (TextView) itemView.findViewById(R.id.tv_brief);
            ivCouponContent = (ImageView) itemView.findViewById(R.id.coupon_content);
        }

    }
}
