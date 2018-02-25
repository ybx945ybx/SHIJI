package cn.yiya.shiji.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.duowan.mobile.netroid.image.NetworkImageView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.OrderSubInfo;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.Util;

/**
 * Created by chenjian on 2016/3/4.
 */
public class OrderGoodesAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<OrderSubInfo.OrderGoodesInfo> mLists;

    public OrderGoodesAdapter(Context mContext, ArrayList<OrderSubInfo.OrderGoodesInfo> mLists) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_package_goodes, parent, false);
            vh = new ViewHolder();
            vh.ivGoodes = (NetworkImageView) convertView.findViewById(R.id.shop_image);
            vh.tvTitle = (TextView) convertView.findViewById(R.id.shop_title);
            vh.tvColorTxt = (TextView) convertView.findViewById(R.id.shop_color_txt);
            vh.tvSizeTxt = (TextView) convertView.findViewById(R.id.shop_size_txt);
            vh.tvWidthTxt = (TextView) convertView.findViewById(R.id.shop_width_txt);
            vh.tvColor = (TextView) convertView.findViewById(R.id.shop_color);
            vh.tvSize = (TextView) convertView.findViewById(R.id.shop_size);
            vh.tvWidth = (TextView) convertView.findViewById(R.id.shop_width);
            vh.tvRefund = (TextView) convertView.findViewById(R.id.shop_refund);
            vh.tvCount = (TextView) convertView.findViewById(R.id.shop_count);
            vh.tvPrice = (TextView) convertView.findViewById(R.id.shop_price);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder)convertView.getTag();
        }

        OrderSubInfo.OrderGoodesInfo mInfo = mLists.get(position);
        Netroid.displayImage(mInfo.getCover(), vh.ivGoodes);
        vh.tvTitle.setText(mInfo.getTitle());
        vh.tvCount.setText(mInfo.getNum() + "");
        vh.tvPrice.setText(Util.FloatKeepTwo(mInfo.getPrice()));

        ArrayList<OrderSubInfo.OrderGoodesInfo.OrderSkuInfo> skus = mInfo.getSku();

        int nSize =  skus.size();
        ArrayList<String> skuKeys = new ArrayList<>();
        for (int i = 0; i < nSize; i++) {
            skuKeys.add(skus.get(i).getKey());
        }

        if (setShowDimension(skuKeys, "color")) {
            vh.tvColor.setVisibility(View.VISIBLE);
            vh.tvColorTxt.setVisibility(View.VISIBLE);
        } else {
            vh.tvColor.setVisibility(View.GONE);
            vh.tvColorTxt.setVisibility(View.GONE);
        }

        if (setShowDimension(skuKeys, "size")) {
            vh.tvSize.setVisibility(View.VISIBLE);
            vh.tvSizeTxt.setVisibility(View.VISIBLE);
        } else {
            vh.tvSize.setVisibility(View.GONE);
            vh.tvSizeTxt.setVisibility(View.GONE);
        }

        if (setShowDimension(skuKeys, "width")) {
            vh.tvWidth.setVisibility(View.VISIBLE);
            vh.tvWidthTxt.setVisibility(View.VISIBLE);
        } else {
            vh.tvWidth.setVisibility(View.GONE);
            vh.tvWidthTxt.setVisibility(View.GONE);
        }

        for (int i = 0; i < nSize; i++) {
            OrderSubInfo.OrderGoodesInfo.OrderSkuInfo sku = skus.get(i);
            if (sku.getKey().equals("color")) {
                vh.tvColor.setText(": " + sku.getValue());
            }

            if (sku.getKey().equals("size")) {
                vh.tvSize.setText(": " + sku.getValue());
            }

            if (sku.getKey().equals("width")) {
                vh.tvWidth.setText(": " + sku.getValue());
            }
        }

        vh.tvRefund.setVisibility(mInfo.getStatus() == 2 ? View.VISIBLE : View.GONE);

        return convertView;
    }

    private boolean setShowDimension(ArrayList<String> list, String str) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(str)) {
                return true;
            }
        }

        return false;
    }

    class ViewHolder {
        NetworkImageView ivGoodes;
        TextView tvTitle;
        TextView tvSizeTxt;
        TextView tvColorTxt;
        TextView tvWidthTxt;
        TextView tvSize;
        TextView tvColor;
        TextView tvWidth;
        TextView tvRefund;
        TextView tvCount;
        TextView tvPrice;
    }
}
