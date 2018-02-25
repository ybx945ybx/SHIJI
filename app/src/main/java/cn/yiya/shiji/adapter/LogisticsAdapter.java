package cn.yiya.shiji.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.OrderSubInfo;
import cn.yiya.shiji.netroid.Netroid;

/**
 * Created by chenjian on 2016/3/9.
 */
public class LogisticsAdapter extends BaseAdapter {

    private Context mContext;
    private List<OrderSubInfo.LogisticInfo.LogisticPointInfo> mLists;

    public LogisticsAdapter(Context mContext, List<OrderSubInfo.LogisticInfo.LogisticPointInfo> mLists) {
        this.mContext = mContext;
        this.mLists = mLists;
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public Object getItem(int position) {
        return mLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_logistics, parent, false);
            vh = new ViewHolder();
            vh.ivLogo = (ImageView) convertView.findViewById(R.id.logistics_logo);
            vh.tvTips = (TextView) convertView.findViewById(R.id.logistics_tips);
            vh.tvTime = (TextView) convertView.findViewById(R.id.logistics_time);
            vh.vHorLine = convertView.findViewById(R.id.logistics_line);
            vh.rlytLatest = (RelativeLayout) convertView.findViewById(R.id.logistics_latest_line);
            vh.rlytNormal = (RelativeLayout) convertView.findViewById(R.id.logistics_normal_line);
            vh.ivPoint = (ImageView) convertView.findViewById(R.id.logistics_point_img);
            vh.vVerLine = convertView.findViewById(R.id.logistics_ver_line);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder)convertView.getTag();
        }

        OrderSubInfo.LogisticInfo.LogisticPointInfo mInfo = mLists.get(position);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) vh.vVerLine.getLayoutParams();

        if (position == 0) {
            vh.rlytLatest.setVisibility(View.VISIBLE);
            vh.rlytNormal.setVisibility(View.GONE);
            vh.tvTips.setTextColor(Color.parseColor("#ee7829"));
            vh.tvTime.setTextColor(Color.parseColor("#ee7829"));
        } else {
            vh.rlytLatest.setVisibility(View.GONE);
            vh.rlytNormal.setVisibility(View.VISIBLE);
            if (mInfo.getStage() > 0) {
                vh.ivPoint.setImageResource(R.mipmap.icon_logistics_light);
                vh.tvTips.setTextColor(Color.parseColor("#3c3c3c"));
                vh.tvTime.setTextColor(Color.parseColor("#3c3c3c"));
            } else {
                vh.ivPoint.setImageResource(R.mipmap.icon_logistics_normal);
                vh.tvTips.setTextColor(Color.parseColor("#aaaaaa"));
                vh.tvTime.setTextColor(Color.parseColor("#aaaaaa"));
            }
        }

        vh.tvTips.setText(mInfo.getDesc());
        vh.tvTime.setText(mInfo.getTime());

        if (!TextUtils.isEmpty(mInfo.getLogo())) {
            vh.ivLogo.setVisibility(View.VISIBLE);
            Netroid.displayImage(mInfo.getLogo(), vh.ivLogo);
        } else {
            vh.ivLogo.setVisibility(View.GONE);
        }

        if (position == mLists.size() - 1) {
            vh.vHorLine.setVisibility(View.INVISIBLE);
            params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.logistics_point_img);
        } else {
            vh.vHorLine.setVisibility(View.VISIBLE);
            params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.logistics_line);
        }

        vh.vVerLine.setLayoutParams(params);

        return convertView;
    }

    class ViewHolder {
        ImageView ivLogo;
        TextView tvTips;
        TextView tvTime;
        View vHorLine;
        RelativeLayout rlytLatest;
        RelativeLayout rlytNormal;
        ImageView ivPoint;
        View vVerLine;
    }
}
