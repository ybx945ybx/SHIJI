package cn.yiya.shiji.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.BannerItem;
import cn.yiya.shiji.utils.BitmapTool;

public class BannerAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<BannerItem> mList;
    private int type;                           //1是coupon 2是首页
    private ImageLoader mImageLoader;
    private DisplayImageOptions mDisplayImageOptions;

    public BannerAdapter(Context context) {
        super();
        this.mContext = context;
        this.mList = new ArrayList<BannerItem>();
    }
    public BannerAdapter(Context context, ArrayList<BannerItem> list, int type) {
        super();
        this.mContext = context;
        this.mList = list;
        this.type = type;
    }
    public BannerAdapter(Context context, ArrayList<BannerItem> list) {
        super();
        this.mContext = context;
        this.mList = list;
    }

    public void setImageLayoutParams(ImageView imageView, String imagesUrl) {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        Gallery.LayoutParams layoutParams = new Gallery.LayoutParams(Gallery.LayoutParams.MATCH_PARENT, Gallery. LayoutParams.MATCH_PARENT);
        layoutParams.width = displayMetrics.widthPixels;
        if(type == 1){
            layoutParams.height = (int) (layoutParams.width / 3.2f);
        }else {
            layoutParams.height = (int) (layoutParams.width * 0.45f);
        }
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        BitmapTool.showImageView(imagesUrl, imageView);
//        Netroid.displayImage(imagesUrl, imageView);
    }
    public void setList(ArrayList<BannerItem> mList) {
        this.mList = mList;
    }

    public ArrayList<BannerItem> getList() {
        return mList;
    }

    @Override
    public int getCount() {
        if(mList.size() == 0){
            return 0;
        }else {
            int maxValue = Integer.MAX_VALUE;
            return maxValue;
        }
    }

    @Override
    public BannerItem getItem(int position) {
        if (mList.size() == 0) {
            return null;
        }
        return mList.get(position%mList.size());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(),
                    R.layout.gallery_item_banner, null);
            vh = new ViewHolder();
            vh.image = (ImageView) convertView.findViewById(R.id.gallery_item_banner_image);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        BannerItem item = getItem(position);
        setImageLayoutParams(vh.image, item.getImage());
        return convertView;
    }

    class ViewHolder {
        ImageView image;
    }
}
