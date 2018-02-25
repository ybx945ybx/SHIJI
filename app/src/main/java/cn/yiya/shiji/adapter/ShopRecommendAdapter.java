package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.linearlistview.LinearListView;

import java.util.ArrayList;
import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.HomeIssueActivity;
import cn.yiya.shiji.activity.NewGoodsDetailActivity;
import cn.yiya.shiji.activity.NewLocalWebActivity;
import cn.yiya.shiji.activity.SellerRecommendedActivity;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MapRequest;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.ActivityListObject;
import cn.yiya.shiji.entity.HtmlThematicInfo;
import cn.yiya.shiji.entity.MallGoodsDetailObject;
import cn.yiya.shiji.entity.goodes.NewGoodsItem;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.FullyLinearLayoutManager;
import cn.yiya.shiji.views.PagerContainer;

/**
 * Created by Amy on 2016/11/29.
 */

public class ShopRecommendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<NewGoodsItem> mList = new ArrayList<>();
    private ArrayList<NewGoodsItem> mHotList = new ArrayList<>();
    private ArrayList<NewGoodsItem> mTopList = new ArrayList<>();

    private static final int TOPIC_TYPE = 1;
    private static final int HOT_TYPE = 2;
    private static final int TITLE_TYPE = 3;
    private static final int LIST_TYPE = 4;
    private static final int TOP_TYPE = 5;

    private HotAdapter hotAdapter;
    private ShopRecommendHAdapter largeAdapter;
    private OnLargeClickListener onLargeClickListener;

    public ShopRecommendAdapter(Context mContext) {
        this.mContext = mContext;
//        this.mList = mList;
        hotAdapter = new HotAdapter(mHotList);
        largeAdapter = new ShopRecommendHAdapter(mContext, 1);
        largeAdapter.setOnClickListener(new ShopRecommendHAdapter.OnClickListener() {
            @Override
            public void delete(int position) {
                if(onLargeClickListener != null){
                    onLargeClickListener.onLargeClick(position);
                }
            }
        });
    }

    public void notifyHotDelete(String goodsId){
        if(mHotList != null && mHotList.size() > 0){
            for (int i = 0; i < mHotList.size(); i++){
                if(mHotList.get(i).getId().equals(goodsId)){
                    mHotList.get(i).setIs_recommend(false);
                    hotAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    public void notifyDelete(String goodsId){
        if(mList != null && mList.size() > 0){
            for (int i = 0; i < mList.size(); i++){
                if(mList.get(i).getId().equals(goodsId)){
                    mList.get(i).setIs_recommend(false);
                    notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    public void notifyHotAdapter(){
        new RetrofitRequest<MallGoodsDetailObject>(ApiRequest.getApiShiji().getShopTopList())
                .handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            MallGoodsDetailObject obj = (MallGoodsDetailObject) msg.obj;
                            mHotList = obj.getList();
                            hotAdapter.setItemList(mHotList);
                            hotAdapter.notifyDataSetChanged();
                        } else {
                        }
                    }
                });
    }

    public void notifyTopAdapter(final View itemView){
        new RetrofitRequest<MallGoodsDetailObject>(ApiRequest.getApiShiji().getRecommendGoods(MapRequest.setMapTwenty(0)))
                .handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            MallGoodsDetailObject object = (MallGoodsDetailObject) msg.obj;

                            if (object.getList() != null && object.getList().size() > 0) {

                                setViewHolder(itemView, SimpleUtils.dp2px(mContext, 230));
                            } else {
                                setViewHolder(itemView, 0);
                            }
                            largeAdapter.setmList(object.getList());
                            largeAdapter.notifyDataSetChanged();
                            notifyDataSetChanged();
                        } else {
                        }
                    }
                });
    }

    public ArrayList<NewGoodsItem> getmList() {
        return mList;
    }

    public void setmList(ArrayList<NewGoodsItem> mList) {
        this.mList = mList;
    }

    public ArrayList<NewGoodsItem> getmTopList() {
        return mTopList;
    }

    public void setmTopList(ArrayList<NewGoodsItem> mTopList) {
        this.mTopList = mTopList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TOP_TYPE:
                return new TopViewHolder(LayoutInflater.from(mContext).inflate(R.layout.shop_recommend_top_item, parent, false));
            case TOPIC_TYPE:
                return new TopicViewHolder(LayoutInflater.from(mContext).inflate(R.layout.main_item_top_brand, parent, false));
            case HOT_TYPE:
                return new HotViewHolder(LayoutInflater.from(mContext).inflate(R.layout.main_item_top_in, parent, false));
            case TITLE_TYPE:
                return new TitleViewHolder(LayoutInflater.from(mContext).inflate(R.layout.main_item_thematic_title, parent, false));
            case LIST_TYPE:
                return new ListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_shop_recommend_list, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == LIST_TYPE) {
            ListViewHolder viewHolder = (ListViewHolder) holder;
            final NewGoodsItem item = mList.get(position - 4);

            viewHolder.ivGoodsImage.setImageURI(Util.transferImage(item.getCover(), SimpleUtils.dp2px(mContext, 120)));
            viewHolder.tvBrand.setText(item.getBrand());
            viewHolder.tvTitle.setText(item.getTitle());
            viewHolder.tvPrice.setText("￥" + item.getPrice());
            viewHolder.tvCommission.setText("佣金：￥" + item.getFor_seller());
            viewHolder.tvCommissionRatio.setText("佣金比例：" + item.getShare_profit());

            viewHolder.tvRecommended.setVisibility(item.is_recommend() ? View.VISIBLE : View.GONE);

            viewHolder.ivGoodsImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoGoodsDetail(item.getId());
                }
            });
        }else if(holder.getItemViewType() == TOP_TYPE){
            TopViewHolder viewHolder = (TopViewHolder) holder;
            viewHolder.getRecommend();
        }else if (holder.getItemViewType() == HOT_TYPE){
            HotViewHolder viewHolder = (HotViewHolder) holder;
            viewHolder.getShopTopList();
        }
    }

    @Override
    public int getItemCount() {
        return (mList == null || mList.size() == 0) ? 3 : mList.size() + 4;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TOP_TYPE;
        } else if (position == 1) {
            return TOPIC_TYPE;
        } else if (position == 2) {
            return HOT_TYPE;
        } else if (position == 3) {
            return TITLE_TYPE;
        }else return LIST_TYPE;
    }


    /*====================================精选专题推荐====================================================*/
    class TopicViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout llTopBrand;
        private TextView tvTitle;
        private PagerContainer container;

        public TopicViewHolder(final View itemView) {
            super(itemView);
            llTopBrand = (RelativeLayout) itemView.findViewById(R.id.ll_top_brand);
            final ViewGroup.LayoutParams layoutParams = llTopBrand.getLayoutParams();

            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvTitle.setText("精选专题推荐");

            container = (PagerContainer) itemView.findViewById(R.id.main_container);
            container.setHasAlpha(false);
            container.setMinAlpha(0.95f);
            container.setMinScale(0.9f);
//            container.setMargin(6);
            final ViewPager pager = container.getViewPager();

            new RetrofitRequest<ActivityListObject>(ApiRequest.getApiShiji().getShopRecommendActivity())
                    .handRequest(new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            if (msg.isSuccess()) {
                                ActivityListObject obj = (ActivityListObject) msg.obj;
                                if (obj == null || obj.getList() == null || obj.list.isEmpty()) {
                                    layoutParams.height = 0;
                                    llTopBrand.setLayoutParams(layoutParams);
                                    itemView.findViewById(R.id.viewline).setVisibility(View.GONE);
                                    return;
                                }

                                List<ActivityListObject.ActivityListItem> itemList = obj.list;
                                pager.setAdapter(new CoverFlowAdapter(itemList));
                                pager.setClipChildren(false);
                                pager.setCurrentItem(itemList.size() * 100 / 2, false);
                                pager.setOffscreenPageLimit(3);
                            } else {
                                layoutParams.height = 0;
                                llTopBrand.setLayoutParams(layoutParams);
                                itemView.findViewById(R.id.viewline).setVisibility(View.GONE);
                            }
                        }
                    });

        }
    }

    class CoverFlowAdapter extends PagerAdapter {
        private List<ActivityListObject.ActivityListItem> itemList;

        public CoverFlowAdapter(List<ActivityListObject.ActivityListItem> itemList) {
            this.itemList = itemList;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.main_item_top_brand_list, null);
            final int nPosition = position % itemList.size();
            final ActivityListObject.ActivityListItem actItem = itemList.get(nPosition);

            SimpleDraweeView ivBrand = (SimpleDraweeView) view.findViewById(R.id.iv_brand);
            TextView tvTopic = (TextView) view.findViewById(R.id.tv_topic);
            view.findViewById(R.id.iv_brand_logo).setVisibility(View.GONE);
            view.findViewById(R.id.btn_follow).setVisibility(View.GONE);

//            if (!TextUtils.isEmpty(item.getTitle())) {
//                tvTopic.setText(item.getTitle());
//                tvTopic.setVisibility(View.VISIBLE);
//            }
            ivBrand.setImageURI(Util.ClipImageFlashSaleView(actItem.getCover()));

            ivBrand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, HomeIssueActivity.class);
                    intent.putExtra("activityId", String.valueOf(actItem.getId()));
                    intent.putExtra("menuId", 7);
                    mContext.startActivity(intent);
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return itemList.size() * 100;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }
    }

    /*====================================热门推荐====================================================*/
    class HotViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout llTopIn;
        RecyclerView recyclerView;
        TextView tvTitle;

        public HotViewHolder(final View itemView) {
            super(itemView);
            llTopIn = (RelativeLayout) itemView.findViewById(R.id.ll_top_in);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvTitle.setText("热门推荐");



            recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerview);
            recyclerView.setLayoutManager(new FullyLinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));



        }

        private void getShopTopList(){
            new RetrofitRequest<MallGoodsDetailObject>(ApiRequest.getApiShiji().getShopTopList())
                    .handRequest(new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) llTopIn.getLayoutParams();
                            if (msg.isSuccess()) {
                                MallGoodsDetailObject obj = (MallGoodsDetailObject) msg.obj;
                                if (obj == null || obj.getList() == null || obj.getList().isEmpty()) {
                                    layoutParams.height = 0;
                                    llTopIn.setLayoutParams(layoutParams);
                                    itemView.findViewById(R.id.viewline).setVisibility(View.GONE);
                                    return;
                                }
                                mHotList = obj.getList();
                                hotAdapter.setItemList(mHotList);
                                recyclerView.setAdapter(hotAdapter);
                                hotAdapter.notifyDataSetChanged();

                            } else {
                                layoutParams.height = 0;
                                llTopIn.setLayoutParams(layoutParams);
                                itemView.findViewById(R.id.viewline).setVisibility(View.GONE);
                            }
                        }
                    });
        }
    }

    class HotAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<NewGoodsItem> itemList;

        public HotAdapter(List<NewGoodsItem> itemList) {
            this.itemList = itemList;
        }

        public List<NewGoodsItem> getItemList() {
            return itemList;
        }

        public void setItemList(List<NewGoodsItem> itemList) {
            this.itemList = itemList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new HotItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_top_in_list, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            HotItemViewHolder viewHolder = (HotItemViewHolder) holder;
            final NewGoodsItem item = itemList.get(position);

            viewHolder.ivGoodsImage.setImageURI(Util.transferCropImage(item.getCover(), SimpleUtils.dp2px(mContext, 80)));
            viewHolder.tvTop.setText("TOP " + (position + 1));
            viewHolder.tvPrice.setText("¥ " + item.getPrice());
            viewHolder.tvBrand.setText(item.getBrand());


            viewHolder.tvRecommended.setVisibility(item.is_recommend() ? View.VISIBLE : View.GONE);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoGoodsDetail(item.getId());
                }
            });
        }

        @Override
        public int getItemCount() {
            return itemList == null ? 0 : itemList.size();
        }

        class HotItemViewHolder extends RecyclerView.ViewHolder {
            SimpleDraweeView ivGoodsImage;
            TextView tvTop;
            TextView tvRecommended;
            TextView tvPrice;
            TextView tvDesc;
            TextView tvBrand;

            public HotItemViewHolder(View itemView) {
                super(itemView);
                ivGoodsImage = (SimpleDraweeView) itemView.findViewById(R.id.iv_goods_image);
                tvTop = (TextView) itemView.findViewById(R.id.tv_top);
                tvRecommended = (TextView) itemView.findViewById(R.id.tv_recommended);
                tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
                tvDesc = (TextView) itemView.findViewById(R.id.tv_desc);
                tvDesc.setVisibility(View.GONE);
                tvBrand = (TextView) itemView.findViewById(R.id.tv_brand);
                tvBrand.setTextColor(Color.parseColor("#9b9b9b"));
            }
        }
    }

    /*====================================精选商品推荐标题====================================================*/
    class TitleViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;

        public TitleViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvTitle.setText("精选商品推荐");
        }
    }

    /*====================================精选商品推荐列表====================================================*/
    class ListViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView ivGoodsImage;
        private TextView tvBrand, tvTitle, tvPrice, tvCommission, tvCommissionRatio, tvRecommended;

        public ListViewHolder(View itemView) {
            super(itemView);
            ivGoodsImage = (SimpleDraweeView) itemView.findViewById(R.id.iv_goods_image);
            tvBrand = (TextView) itemView.findViewById(R.id.item_brand);
            tvTitle = (TextView) itemView.findViewById(R.id.item_title);
            tvPrice = (TextView) itemView.findViewById(R.id.item_price);
            tvCommission = (TextView) itemView.findViewById(R.id.tv_commission);
            tvCommissionRatio = (TextView) itemView.findViewById(R.id.tv_commission_ratio);
            tvRecommended = (TextView) itemView.findViewById(R.id.tv_recommended);
        }
    }


    /**
     * 跳转到商品详情
     *
     * @param goodsId
     */
    private void gotoGoodsDetail(final String goodsId) {
        Intent intent = new Intent(mContext, NewGoodsDetailActivity.class);
        intent.putExtra("goodsId", goodsId);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mContext.startActivity(intent);
    }

    class TopViewHolder extends RecyclerView.ViewHolder{
        HorizontalScrollView rvLarge;
        LinearListView llvLarge;

        LinearLayout llytLargeMore;
        TextView tvNull;

        public TopViewHolder(View itemView) {
            super(itemView);
            rvLarge = (HorizontalScrollView) itemView.findViewById(R.id.rv_large);
            llvLarge = (LinearListView) itemView.findViewById(R.id.horizontal_list_large);
//            largeAdapter = new ShopRecommendHAdapter(mContext, 1);
            llvLarge.setAdapter(largeAdapter);
            llytLargeMore = (LinearLayout) itemView.findViewById(R.id.tv_more_large) ;
            llytLargeMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, SellerRecommendedActivity.class);
                    mContext.startActivity(intent);
                }
            });
            tvNull = (TextView) itemView.findViewById(R.id.tv_null_recommend);

//            getRecommend();

        }
        private void getRecommend(){
            if (mTopList != null && mTopList.size() > 0) {
                largeAdapter.setmList(mTopList);
                largeAdapter.notifyDataSetChanged();
                setViewHolder(itemView, SimpleUtils.dp2px(mContext, 240));
            } else {
                setViewHolder(itemView, 0);
            }
//            new RetrofitRequest<MallGoodsDetailObject>(ApiRequest.getApiShiji().getRecommendGoods(MapRequest.setMapTwenty(0)))
//                    .handRequest(new MsgCallBack() {
//                        @Override
//                        public void onResult(HttpMessage msg) {
//                            if (msg.isSuccess()) {
//                                MallGoodsDetailObject object = (MallGoodsDetailObject) msg.obj;
//                                if (object.getList() != null && object.getList().size() > 0) {
//                                    largeAdapter.setmList(object.getList());
//                                    largeAdapter.notifyDataSetChanged();
//                                    setViewHolder(itemView, SimpleUtils.dp2px(mContext, 230));
//                                } else {
//                                    setViewHolder(itemView, 0);
//                                }
//                            } else {
//                            }
//                        }
//                    });
        }

    }

    public interface OnLargeClickListener{
        void onLargeClick(int position);
    }

    public void setOnLargeClickListener(OnLargeClickListener onLargeClickListener){
        this.onLargeClickListener = onLargeClickListener;
    }


    private void setViewHolder(View itemView, int height){
        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
//        layoutParams.width = SimpleUtils.getScreenWidth(mContext);
        layoutParams.height = height;
        itemView.setLayoutParams(layoutParams);
    }
}
