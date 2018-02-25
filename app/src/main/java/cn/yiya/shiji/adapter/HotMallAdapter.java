package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.CouponDetailActivity;
import cn.yiya.shiji.activity.TravelMallActivity;
import cn.yiya.shiji.entity.BannerItem;
import cn.yiya.shiji.entity.navigation.MallDetailInfo;
import cn.yiya.shiji.utils.BitmapTool;
import cn.yiya.shiji.utils.SimpleUtils;

/**
 * Created by Tom on 2016/3/30.
 */
public class HotMallAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    public static final int BANNER_TYPE = 111;
    public static final int LIST_TYPE = 222;
    public static final int TXT_TYPE = 333;

    private ArrayList<MallDetailInfo> mListData;
    ArrayList<BannerItem> mBannerData = new ArrayList<>();

    private String countryId;
    private String cityId = "";

    public HotMallAdapter(Context mContext, ArrayList<MallDetailInfo> mList,
                          ArrayList<BannerItem> mBannerData, String countryId, String cityId) {
        this.mContext = mContext;
        this.mListData = mList;
        this.mBannerData = mBannerData;
        this.cityId = cityId;
        this.countryId = countryId;
        setHasStableIds(true);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case BANNER_TYPE:
                return new BannerViewHolder(parent);
            case LIST_TYPE:
                return new HotMallViewHolder(parent);
            case TXT_TYPE:
                return new CityTxtViewHolder(parent);
            default:
                throw new RuntimeException("Unknown issue type");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case BANNER_TYPE:
                ((BannerViewHolder) holder).bindData(mBannerData);
                break;
            case LIST_TYPE:
                if (mListData.size() == 0) {
                    return;
                }
                HotMallViewHolder mHolder = (HotMallViewHolder)holder;
                final MallDetailInfo info;

                info = mListData.get(position - 2);

                BitmapTool.showImageView(info.getCover(), mHolder.ivMall, R.drawable.travel_background);
                if(info.getShop_num().equals("0") || TextUtils.isEmpty(info.getShop_num())){
                    mHolder.llytShopCount.setVisibility(View.INVISIBLE);
                }else {
                    mHolder.tvShopCount.setText(info.getShop_num() + "");
                    mHolder.llytShopCount.setVisibility(View.VISIBLE);
                }
                mHolder.tvName.setText(info.getName());
                mHolder.tvCnName.setText(info.getCn_name());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, TravelMallActivity.class);
                        intent.putExtra("id", info.getId());
                        intent.putExtra("countryId", countryId);
                        intent.putExtra("cityId", cityId);
                        mContext.startActivity(intent);
                    }
                });
                break;
        }

    }

    @Override
    public int getItemCount() {
        if(mListData == null){
            return 1;
        }else {
            return mListData.size() + 2;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) {
            return BANNER_TYPE;
        } else if (position == 1){
            return TXT_TYPE;
        } else {
            return LIST_TYPE;
        }
    }

    protected class BannerViewHolder extends RecyclerView.ViewHolder {

        public ConvenientBanner mGalleryTravle;

        public BannerViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.travle_head_banner, parent, false));
            mGalleryTravle = (ConvenientBanner) itemView.findViewById(R.id.travle_banner);
            mGalleryTravle.setPageIndicator(new int[]{R.mipmap.main_indictator_normal, R.mipmap.main_indictator_focused});
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mGalleryTravle.getLayoutParams();
            layoutParams.width = SimpleUtils.getScreenWidth(mContext);
            layoutParams.height = (int)(layoutParams.width / 3.2f);
            mGalleryTravle.setLayoutParams(layoutParams);
            mGalleryTravle.startTurning(4000);
            mGalleryTravle.setVisibility(View.VISIBLE);
            itemView.findViewById(R.id.view).setVisibility(View.VISIBLE);
        }

        public void bindData(ArrayList<BannerItem> mlist) {
            if(mlist == null){
                if (mlist == null || mlist.size() == 0) {
                    ViewGroup.LayoutParams lp = itemView.getLayoutParams();
                    lp.height = 0;
                    lp.width = 0;
                    itemView.setLayoutParams(lp);
                }
            }
            if(mlist != null){
                mGalleryTravle.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
                    @Override
                    public NetworkImageHolderView createHolder() {
                        return new NetworkImageHolderView();
                    }
                }, mlist);
            }

        }
    }

    class NetworkImageHolderView implements Holder<BannerItem> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return imageView;
        }

        @Override
        public void UpdateUI(final Context context, final int position, final BannerItem item) {
            int width = SimpleUtils.getScreenWidth(mContext);
            int height = (int) (width / 3.2f);
            BitmapTool.clipShowImageView(item.getImage(), imageView, R.drawable.user_dafault, width, height);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentBanner = new Intent(mContext, CouponDetailActivity.class);
                    intentBanner.putExtra("couponId", item.getCouponId());
                    intentBanner.putExtra("countryId", countryId);
                    intentBanner.putExtra("cityId", cityId);
                    mContext.startActivity(intentBanner);
                }
            });
        }
    }

    public class HotMallViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMall;
        TextView tvCnName;
        TextView tvName;
        TextView tvShopCount;
        LinearLayout llytShopCount;

        public HotMallViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.hot_mall_item, parent, false));
            ivMall = (ImageView) itemView.findViewById(R.id.image_background);
            tvCnName = (TextView) itemView.findViewById(R.id.tv_name_ch);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvShopCount = (TextView) itemView.findViewById(R.id.tv_shop_count);
            llytShopCount = (LinearLayout) itemView.findViewById(R.id.llyt_shop_count);
            if(mListData.size() == 0){
                itemView.setVisibility(View.GONE);
                ViewGroup.LayoutParams lp = itemView.getLayoutParams();
                lp.height = 0;
                lp.width = 0;
                itemView.setLayoutParams(lp);
                return;
            }
            ViewGroup.LayoutParams lp = itemView.getLayoutParams();
            lp.width = SimpleUtils.getScreenWidth(mContext) - SimpleUtils.dp2px(mContext, 32);
            lp.height = (int)(lp.width/1.83f);
            itemView.setLayoutParams(lp);
        }
    }

    public class CityTxtViewHolder extends RecyclerView.ViewHolder{
        TextView tvHotMessage;
        LinearLayout llytTxt;

        public CityTxtViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.travel_txt_info, parent, false));
            tvHotMessage = (TextView) itemView.findViewById(R.id.travle_txt);
            llytTxt = (LinearLayout) itemView.findViewById(R.id.txt_layout);
            if (mListData != null && mListData.size() > 0)
                tvHotMessage.setText("热门商城");

            if (mListData == null || mListData.size() == 0) {
                ViewGroup.LayoutParams lp = itemView.getLayoutParams();
                lp.height = 0;
                lp.width = 0;
                itemView.setLayoutParams(lp);
            }
        }

    }
}
