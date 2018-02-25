package cn.yiya.shiji.adapter;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.search.NewSearchEntity;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.FullyLinearLayoutManager;

/**
 * Created by Tom on 2016/8/22.
 */
public class NewSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private final static int ACTIVITY = 0000;
    private final static int BRAND_LOGO = 1111;
    private final static int BRAND = 2222;
    private final static int SITE = 3333;
    private final static int CATEGROY = 4444;

    SearchTypeListBrandAdapter brandLogoAdapter;
    SearchTypeListTextAdapter brandAdapter;
    SearchTypeListTextAdapter siteAdapter;
    SearchTypeListTextAdapter categoryAdapter;

    private OnItemClickListener onItemClickListen;


    private NewSearchEntity searchEntity = new NewSearchEntity();
    public NewSearchAdapter(Context mContext){
        this.mContext = mContext;
    }

    public NewSearchEntity getSearchEntity() {
        return searchEntity;
    }

    public void setSearchEntity(NewSearchEntity searchEntity) {
        this.searchEntity = searchEntity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case ACTIVITY:
                return new ActViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item_activity, parent, false));
            case BRAND_LOGO:
                return new BrandLogoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.search_type_item, parent, false));
            case BRAND:
                return new BrandViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.search_type_item, parent, false));
            case SITE:
                return new SitiViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.search_type_item, parent, false));
            case CATEGROY:
                return new CategroyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.search_type_item, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case ACTIVITY:
                ((ActViewHolder)holder).bindActData(searchEntity.getShop_act_list());
                break;
            case BRAND_LOGO:
                ((BrandLogoViewHolder)holder).bindBrandLogoData(searchEntity.getBrand_list());
                break;
            case BRAND:
                ((BrandViewHolder)holder).bindBrandData(searchEntity.getBrand_list());
                break;
            case SITE:
                ((SitiViewHolder)holder).bindSiteData(searchEntity.getSite_list());
                break;
            case CATEGROY:
                ((CategroyViewHolder)holder).bindCategroyData(searchEntity.getGoods_type_list());
                break;

        }

    }

    @Override
    public int getItemCount() {
        return 5;
//        if(searchEntity.getGoods_type_list() == null){
//            return 4;
//        }else {
//            return searchEntity.getGoods_type_list().size() + 4;
//        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return ACTIVITY;
        }else if (position == 1){
            return BRAND_LOGO;
        }else if (position == 2){
            return BRAND;
        }else if (position == 3){
            return SITE;
        }else {
            return CATEGROY;
        }
    }

    class ActViewHolder extends RecyclerView.ViewHolder{

        ImageView ivAct;
        TextView tvAct;

        public ActViewHolder(View itemView) {
            super(itemView);
            ivAct = (ImageView) itemView.findViewById(R.id.iv_act);
            tvAct = (TextView) itemView.findViewById(R.id.tv_act);

        }

        public void bindActData(final List<NewSearchEntity.ShopActListEntity> actList){
            if(actList == null || actList.size() == 0){
//                itemView.setVisibility(View.GONE);
                ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
                layoutParams.height = 0;
                itemView.setLayoutParams(layoutParams);
                return;
            }
//            itemView.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = SimpleUtils.dp2px(mContext, 105);
            itemView.setLayoutParams(layoutParams);

//            Netroid.displayImage(Util.transferCropImage(actList.get(0).getCover(), SimpleUtils.dp2px(mContext, 158)), ivAct, R.mipmap.work_default);
//            BitmapTool.clipShowImageView(actList.get(0).getCover(), ivAct, R.mipmap.work_default, SimpleUtils.dp2px(mContext, 160), SimpleUtils.dp2px(mContext, 81));
//            Util.ClipImageBannerView(info.getWordless_cover());
            Netroid.displayImage(Util.ClipImageBannerView(actList.get(0).getCover()), ivAct);
            tvAct.setText(actList.get(0).getTitle());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListen != null){
                        onItemClickListen.OnActItemClick(actList.get(0));
                    }
                }
            });
        }
    }

    class BrandLogoViewHolder extends RecyclerView.ViewHolder{

        private RecyclerView rycvBrandLogo;

        public BrandLogoViewHolder(View itemView) {
            super(itemView);
            itemView.findViewById(R.id.tv_type_title).setVisibility(View.GONE);
            rycvBrandLogo = (RecyclerView) itemView.findViewById(R.id.rycv_search_type_list);
            rycvBrandLogo.setItemAnimator(new DefaultItemAnimator());
            rycvBrandLogo.setLayoutManager(new FullyLinearLayoutManager(mContext));
            brandLogoAdapter = new SearchTypeListBrandAdapter(mContext);
            rycvBrandLogo.setAdapter(brandLogoAdapter);

            brandLogoAdapter.setOnItemClickListen(new SearchTypeListBrandAdapter.OnItemClickListener() {
                @Override
                public void OnBrandLogoItemClick(NewSearchEntity.BrandListEntity brandListEntity) {
                    if(onItemClickListen != null){
                        onItemClickListen.OnBrandItemClick(brandListEntity);
                    }
                }
            });

        }

        public void bindBrandLogoData(List<NewSearchEntity.BrandListEntity> brandList){
            if(brandList == null || brandList.size() == 0){
                ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
                layoutParams.height = 0;
                itemView.setLayoutParams(layoutParams);
                return;
            }
            ArrayList<NewSearchEntity.BrandListEntity> brandLogoList = new ArrayList<>();
            for (int i = 0; i < brandList.size(); i ++ ){
                if(!TextUtils.isEmpty(brandList.get(i).getLogo())){
                    brandLogoList.add(brandList.get(i));
                }
            }
            if(brandLogoList.size() == 0){
                ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
                layoutParams.height = 0;
                itemView.setLayoutParams(layoutParams);
                return;
            }
            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            itemView.setLayoutParams(layoutParams);
            brandLogoAdapter.setmBrandList(brandLogoList);
            brandLogoAdapter.notifyDataSetChanged();

        }
    }

    class BrandViewHolder extends RecyclerView.ViewHolder{
        TextView tvTypeTitle;
        RecyclerView rycvBrand;

        public BrandViewHolder(View itemView) {
            super(itemView);
            tvTypeTitle = (TextView) itemView.findViewById(R.id.tv_type_title);
            tvTypeTitle.setText("品牌");
            rycvBrand = (RecyclerView) itemView.findViewById(R.id.rycv_search_type_list);

            rycvBrand.setItemAnimator(new DefaultItemAnimator());
            rycvBrand.setLayoutManager(new FullyLinearLayoutManager(mContext));
            brandAdapter = new SearchTypeListTextAdapter(mContext, 1);
            rycvBrand.setAdapter(brandAdapter);

            brandAdapter.setOnItemClickListen(new SearchTypeListTextAdapter.OnItemClickListener() {
                @Override
                public void OnBrandItemClick(NewSearchEntity.BrandListEntity brandListEntity) {
                    if(onItemClickListen != null){
                        onItemClickListen.OnBrandItemClick(brandListEntity);
                    }
                }

                @Override
                public void OnSiteItemClick(NewSearchEntity.SiteListEntity siteListEntity) {

                }

                @Override
                public void OnCategroyItemClick(NewSearchEntity.GoodsTypeListEntity goodsTypeListEntity) {

                }
            });

        }

        public void bindBrandData(List<NewSearchEntity.BrandListEntity> brandList){
            if(brandList == null || brandList.size() == 0){
                ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
                layoutParams.height = 0;
                itemView.setLayoutParams(layoutParams);
                return;
            }
            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            itemView.setLayoutParams(layoutParams);
            brandAdapter.setmBrandist(brandList);
            brandAdapter.notifyDataSetChanged();
        }
    }

    class SitiViewHolder extends RecyclerView.ViewHolder{
        TextView tvTypeTitle;
        RecyclerView rycvSite;

        public SitiViewHolder(View itemView) {
            super(itemView);
            tvTypeTitle = (TextView) itemView.findViewById(R.id.tv_type_title);
            tvTypeTitle.setText("商城");
            rycvSite = (RecyclerView) itemView.findViewById(R.id.rycv_search_type_list);

            rycvSite.setItemAnimator(new DefaultItemAnimator());
            rycvSite.setLayoutManager(new FullyLinearLayoutManager(mContext));
            siteAdapter = new SearchTypeListTextAdapter(mContext, 2);
            rycvSite.setAdapter(siteAdapter);

            siteAdapter.setOnItemClickListen(new SearchTypeListTextAdapter.OnItemClickListener() {
                @Override
                public void OnBrandItemClick(NewSearchEntity.BrandListEntity brandListEntity) {

                }

                @Override
                public void OnSiteItemClick(NewSearchEntity.SiteListEntity siteListEntity) {
                    if(onItemClickListen != null){
                        onItemClickListen.OnSiteItemClick(siteListEntity);
                    }

                }

                @Override
                public void OnCategroyItemClick(NewSearchEntity.GoodsTypeListEntity goodsTypeListEntity) {

                }
            });

        }

        public void bindSiteData(List<NewSearchEntity.SiteListEntity> siteList){
            if(siteList == null || siteList.size() == 0){
                ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
                layoutParams.height = 0;
                itemView.setLayoutParams(layoutParams);
                return;
            }
            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            itemView.setLayoutParams(layoutParams);
            siteAdapter.setmSiteList(siteList);
            siteAdapter.notifyDataSetChanged();
        }
    }

    class CategroyViewHolder extends RecyclerView.ViewHolder{

        TextView tvTypeTitle;
        RecyclerView rycvCategory;

        public CategroyViewHolder(View itemView) {
            super(itemView);
            tvTypeTitle = (TextView) itemView.findViewById(R.id.tv_type_title);
            tvTypeTitle.setText("分类");
            rycvCategory = (RecyclerView) itemView.findViewById(R.id.rycv_search_type_list);

            rycvCategory.setItemAnimator(new DefaultItemAnimator());
            rycvCategory.setLayoutManager(new FullyLinearLayoutManager(mContext));
            categoryAdapter = new SearchTypeListTextAdapter(mContext, 3);
            rycvCategory.setAdapter(categoryAdapter);

            categoryAdapter.setOnItemClickListen(new SearchTypeListTextAdapter.OnItemClickListener() {
                @Override
                public void OnBrandItemClick(NewSearchEntity.BrandListEntity brandListEntity) {

                }

                @Override
                public void OnSiteItemClick(NewSearchEntity.SiteListEntity siteListEntity) {

                }

                @Override
                public void OnCategroyItemClick(NewSearchEntity.GoodsTypeListEntity goodsTypeListEntity) {
                    if(onItemClickListen != null){
                        onItemClickListen.OnCategroyItemClick(goodsTypeListEntity);
                    }

                }
            });
        }

        public void bindCategroyData(List<NewSearchEntity.GoodsTypeListEntity> categoryList){
            if(categoryList == null || categoryList.size() == 0){
                ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
                layoutParams.height = 0;
                itemView.setLayoutParams(layoutParams);
                return;
            }
            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            itemView.setLayoutParams(layoutParams);
            categoryAdapter.setmCategoryList(categoryList);
            categoryAdapter.notifyDataSetChanged();
        }
    }

    public interface OnItemClickListener{
        void OnActItemClick(NewSearchEntity.ShopActListEntity shopActListEntity);
        void OnBrandItemClick(NewSearchEntity.BrandListEntity brandListEntity);
        void OnSiteItemClick(NewSearchEntity.SiteListEntity siteListEntity);
        void OnCategroyItemClick(NewSearchEntity.GoodsTypeListEntity goodsTypeListEntity);
    }

    public void setOnItemClickListen(OnItemClickListener onItemClickListen){
        this.onItemClickListen = onItemClickListen;

    }
}
