package cn.yiya.shiji.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.goodes.NewGoodsItem;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.Util;

/**
 * Created by jerryzhang on 2015/9/15.
 * 热门单品适配器
 */
public class HotSingleAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater mLInflater;
    private ArrayList<NewGoodsItem> mLists;
    public HotSingleAdapter(Context context, ArrayList<NewGoodsItem> lists) {
        this.context = context;
        this.mLists = lists;
        this.mLInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if(mLists == null){
            return 0;
        }
        return  mLists.size();
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
        ViewHolder vh;
        if (convertView == null){
            convertView = mLInflater.inflate(R.layout.recommend_item,null);
            vh = new ViewHolder();
            vh.ivGoods = (ImageView)convertView.findViewById(R.id.imageview);
            vh.tvDetails = (TextView)convertView.findViewById(R.id.textview);
            vh.tvLeft = (TextView)convertView.findViewById(R.id.textleft);
            vh.tvRight = (TextView)convertView.findViewById(R.id.textright);
            vh.tvRight.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG );
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        final NewGoodsItem item = mLists.get(position);
        vh.tvLeft.setText("￥" + item.getPrice());
        vh.tvRight.setText("￥" + item.getList_price());
        vh.tvDetails.setText(item.getTitle());
        setImageLayoutParams(vh.ivGoods, item.getCover());
//        if (!TextUtils.isEmpty(item.getCover())) {
//            Netroid.displayImage(Util.transfer(item.getCover()),vh.ivGoods);
//        }

        return convertView;
    }

    public void setImageLayoutParams(ImageView imageView, String imagesUrl) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout. LayoutParams.MATCH_PARENT);
        layoutParams.width = displayMetrics.widthPixels * 3 / 8;
        layoutParams.height = (int) (layoutParams.width);
        imageView.setLayoutParams(layoutParams);
        if (!TextUtils.isEmpty(imagesUrl)) {
            Netroid.displayImage(Util.transfer(imagesUrl), imageView);
        }
    }
    class ViewHolder {
        ImageView ivGoods;
        TextView tvDetails,tvLeft,tvRight;
    }
}
