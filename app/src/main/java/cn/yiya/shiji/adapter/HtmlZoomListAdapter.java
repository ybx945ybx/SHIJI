package cn.yiya.shiji.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.HtmlImageListItem;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;

/**
 * Created by jerry on 2016/1/22.
 */
public class HtmlZoomListAdapter extends BaseAdapter {
    private ArrayList<HtmlImageListItem> list;
    private Context mContext;

    public HtmlZoomListAdapter(ArrayList<HtmlImageListItem> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        int width = SimpleUtils.getScreenWidth(mContext);
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.html_image_item_layout, null);
            vh.ivGoods = (ImageView) convertView.findViewById(R.id.goods_image);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        Netroid.displayImage(Util.transferImage(list.get(position).getUrl(), width), vh.ivGoods);
        return convertView;
    }

    class ViewHolder {
        ImageView ivGoods;
    }
}