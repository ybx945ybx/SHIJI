package cn.yiya.shiji.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.ShoppingCartList;
import cn.yiya.shiji.views.FullyLinearLayoutManager;
import cn.yiya.shiji.views.SwipeItemLayout;
import cn.yiya.shiji.views.SwipeRecyclerView;

/**
 * 购物车
 * Created by Amy on 2016/11/4.
 */

public class NewShoppingCartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<ShoppingCartList> mList;

    private static final int LIST_TYPE = 1;
    private static final int BOTTOM = 2;

    public NewShoppingCartAdapter(Context context, OnClickListener onClickListener) {
        this.mContext = context;
        this.onClickListener = onClickListener;
    }

    public void setmList(ArrayList<ShoppingCartList> list) {
        this.mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case LIST_TYPE:
                return new SiteGoodsViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_shopping_cart_site, parent, false));
            case BOTTOM:
                return new BottomViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_shopping_cart_bottom, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder.getItemViewType() == LIST_TYPE) {
            final SiteGoodsViewHolder viewHolder = (SiteGoodsViewHolder) holder;
            final ShoppingCartList itemList = mList.get(position);

            if (itemList.isChecked()) {
                viewHolder.cbSite.setChecked(true);
            } else {
                viewHolder.cbSite.setChecked(false);
            }
            if (itemList.isSoldOut()) {
                viewHolder.llCheckSite.setEnabled(false);
            }

            viewHolder.ivSiteFlag.setImageURI(itemList.getCountry_flag());
            viewHolder.tvSiteName.setText(itemList.getSiteDes());

            viewHolder.rvGoodsList.setLayoutManager(new FullyLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            final ShoppingCartGoodsAdapter mAdapter = new ShoppingCartGoodsAdapter(mContext, itemList.getGoodses());
            viewHolder.rvGoodsList.setAdapter(mAdapter);


            setFullReduction(viewHolder, itemList);

            viewHolder.llSiteArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeAllSwipeItem(viewHolder.rvGoodsList);
                    if (onClickListener != null && !TextUtils.isEmpty(itemList.getSite_id())) {
                        onClickListener.gotoSiteMall(itemList.getSite_id());
                    }
                }
            });

            viewHolder.llCheckSite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.cbSite.toggle();
                    itemList.setChecked(viewHolder.cbSite.isChecked());
                    for (int i = 0; i < itemList.getGoodses().size(); i++) {
                        if (itemList.getGoodses().get(i).getStatus() == 1) {
                            itemList.getGoodses().get(i).setChecked(viewHolder.cbSite.isChecked());
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                    setFullReduction(viewHolder, itemList);
                    if (onClickListener != null) {
                        onClickListener.checkAll();
                    }
                }
            });


            mAdapter.setOnClickListener(new ShoppingCartGoodsAdapter.OnClickListener() {
                @Override
                public void checkGoods(int pos, boolean isChecked) {
                    if (!isChecked) {
                        //不选
                        itemList.setChecked(false);
                        viewHolder.cbSite.setChecked(false);
                    } else {
                        //选
                        boolean isAll = true;
                        for (int i = 0; i < itemList.getGoodses().size(); i++) {
                            if (itemList.getGoodses().get(i).getStatus() == 1 && !itemList.getGoodses().get(i).isChecked()) {
                                isAll = false;
                                break;
                            }
                        }
                        if (!isAll) {
                            itemList.setChecked(false);
                            viewHolder.cbSite.setChecked(false);
                        } else {
                            itemList.setChecked(true);
                            viewHolder.cbSite.setChecked(true);
                        }
                    }
                    setFullReduction(viewHolder, itemList);
                    if (onClickListener != null) {
                        onClickListener.checkAll();
                    }
                }

                @Override
                public void gotoCheckAll() {
                    if (mAdapter.getItemCount() == 0) {
                        mList.remove(position);
                        notifyItemRemoved(position);
                        if (mList.size() > 0) {
                            notifyItemRangeChanged(position, mList.size());
                        }
                    } else {
                        setFullReduction(viewHolder, itemList);
                    }
                    if (onClickListener != null) {
                        onClickListener.checkAll();
                    }
                }
            });

        }
    }

    private void closeAllSwipeItem(SwipeRecyclerView recyclerView) {
        final int count = recyclerView.getChildCount();
        for (int i = count - 1; i >= 0; i--) {
            final View child = recyclerView.getChildAt(i);
            if (child != null && child instanceof SwipeItemLayout) {
                ((SwipeItemLayout) child).close();
            }
        }
    }


    /**
     * 设置满减
     *
     * @param viewHolder
     * @param itemList
     */
    private void setFullReduction(SiteGoodsViewHolder viewHolder, ShoppingCartList itemList) {
        float singleTotalPrice = 0;
        int totalCount=0;
        int soldOut=0;
        for (int i = 0; i < itemList.getGoodses().size(); i++) {
            totalCount+=itemList.getGoodses().get(i).getNum();
            if (itemList.getGoodses().get(i).getStatus() == 1 && itemList.getGoodses().get(i).isChecked()) {
                singleTotalPrice += Float.parseFloat(itemList.getGoodses().get(i).getPrice()) *
                        itemList.getGoodses().get(i).getNum();
            }else if(itemList.getGoodses().get(i).getStatus() == 2){
                soldOut+=itemList.getGoodses().get(i).getNum();
            }
        }
        if (itemList.getDiscount_fee() != null) {
            viewHolder.rlFullReduction.setVisibility(View.VISIBLE);
            viewHolder.tvFullReduction.setText("满减优惠：满" + itemList.getDiscount_fee().getMoney() + "元减" +
                    itemList.getDiscount_fee().getDiscount() + "元");
            if (singleTotalPrice < Float.parseFloat(itemList.getDiscount_fee().getMoney())) {
                viewHolder.tvFullReduction.setTextColor(Color.parseColor("#50212121"));
                viewHolder.tvReductionFit.setText("未满足");
                viewHolder.tvReductionFit.setTextColor(Color.parseColor("#50212121"));
            } else {
                viewHolder.tvFullReduction.setTextColor(Color.parseColor("#212121"));
                viewHolder.tvReductionFit.setText("- ¥ " + itemList.getDiscount_fee().getDiscount());
                viewHolder.tvReductionFit.setTextColor(Color.parseColor("#212121"));
            }
        } else viewHolder.rlFullReduction.setVisibility(View.GONE);

        if(totalCount==soldOut){
            //全部售罄
            itemList.setChecked(false);
            itemList.setSoldOut(true);
            viewHolder.cbSite.setChecked(false);
            viewHolder.llCheckSite.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        if (mList == null || mList.isEmpty())
            return 0;
        else return mList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mList.size()) {
            return LIST_TYPE;
        } else return BOTTOM;
    }


    class SiteGoodsViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llCheckSite;
        private CheckBox cbSite;
        private SimpleDraweeView ivSiteFlag;
        private TextView tvSiteName;
        private LinearLayout llSiteArrow;

        private SwipeRecyclerView rvGoodsList;

        private RelativeLayout rlFullReduction;
        private TextView tvFullReduction;
        private TextView tvReductionFit;

        public SiteGoodsViewHolder(View itemView) {
            super(itemView);
            llCheckSite = (LinearLayout) itemView.findViewById(R.id.ll_check_site);
            cbSite = (CheckBox) itemView.findViewById(R.id.cb_site);
            ivSiteFlag = (SimpleDraweeView) itemView.findViewById(R.id.iv_site_flag);
            tvSiteName = (TextView) itemView.findViewById(R.id.tv_site_name);
            llSiteArrow = (LinearLayout) itemView.findViewById(R.id.ll_site_arrow);
            rvGoodsList = (SwipeRecyclerView) itemView.findViewById(R.id.rv_goods_list);
            rlFullReduction = (RelativeLayout) itemView.findViewById(R.id.rl_full_reduction);
            tvFullReduction = (TextView) itemView.findViewById(R.id.tv_full_reduction);
            tvReductionFit = (TextView) itemView.findViewById(R.id.tv_reduction_fit);

            rvGoodsList.setNestedScrollingEnabled(false);
        }
    }

    class BottomViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rlCustomService, rlReturnRules;

        public BottomViewHolder(View itemView) {
            super(itemView);
            rlCustomService = (RelativeLayout) itemView.findViewById(R.id.rl_custom_service);
            rlReturnRules = (RelativeLayout) itemView.findViewById(R.id.rl_return_rules);
            rlCustomService.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null) onClickListener.gotoCustomService();
                }
            });
            rlReturnRules.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null) onClickListener.gotoReturnRules();
                }
            });
        }
    }

    private OnClickListener onClickListener;

    public interface OnClickListener {
        //在线客服
        void gotoCustomService();

        //退换货政策
        void gotoReturnRules();

        //跳转至商城
        void gotoSiteMall(String siteId);

        void checkAll();

    }
}
