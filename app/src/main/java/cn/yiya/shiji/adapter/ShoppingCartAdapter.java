package cn.yiya.shiji.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.duowan.mobile.netroid.image.NetworkImageView;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.NewGoodsDetailActivity;
import cn.yiya.shiji.activity.NewMainActivity;
import cn.yiya.shiji.activity.SubmitOrderActivity;
import cn.yiya.shiji.entity.FreightInfo;
import cn.yiya.shiji.entity.ShoppingCarUpdataObject;
import cn.yiya.shiji.entity.ShoppingCartGoods;
import cn.yiya.shiji.entity.ShoppingCartGroup;
import cn.yiya.shiji.entity.ShoppingCartPost;
import cn.yiya.shiji.entity.ShoppingCartSku;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.MyPreference;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.AllListView;

/**
 * 购物车适配器
 * Created by yiya on 2015/9/2.
 */
public class ShoppingCartAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private ArrayList<ShoppingCartGroup> mGroup = null;
    private ArrayList<ArrayList<ShoppingCartGoods>> mChild = null;
    private int nType = 0;
    private CheckBox cbAllCheck;
    private updatePriceListener updateListener;

    private boolean isLogin;
    WeakReference<Activity> weak;
    private boolean directBuy;          // 是否是商品详情界面直接购买跳转过来结算的

    public ShoppingCartAdapter(Context ctx, ArrayList<ShoppingCartGroup> strGroup, ArrayList<ArrayList<ShoppingCartGoods>> lists, int type, CheckBox cb
            , updatePriceListener listener, boolean directBuy) {
        mContext = ctx;
        mGroup = strGroup;
        mChild = lists;
        nType = type;
        cbAllCheck = cb;
        this.updateListener = listener;
        weak = new WeakReference<Activity>((Activity) ctx);
        isLogin = Util.isLogin();
        this.directBuy = directBuy;
    }

    @Override
    public int getGroupCount() {
        return mGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (mChild.get(groupPosition) == null) {
            return 0;
        }
        return mChild.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroup.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChild.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder vHolder = null;
        if (convertView == null) {
            if (mContext != null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.expand_shop_group, parent, false);
            } else {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expand_shop_group, parent, false);
            }
            vHolder = new ViewHolder();
            vHolder.cbGroup = (CheckBox) convertView.findViewById(R.id.shop_group_cb);
            vHolder.ivFrom = (ImageView) convertView.findViewById(R.id.shop_group_image);
            vHolder.tvFrom = (TextView) convertView.findViewById(R.id.shop_group_from);
//            vHolder.vBack =  convertView.findViewById(R.id.shop_back);
//            vHolder.tvTip = (TextView) convertView.findViewById(R.id.shop_tip_txt);
//            vHolder.tvFeeType = (TextView) convertView.findViewById(R.id.shop_group_channel_type);
//            vHolder.tvFeeDesc =  (TextView) convertView.findViewById(R.id.shop_group_channel_fee);
//            vHolder.tvForeignFee = (TextView) convertView.findViewById(R.id.shop_group_channel_fee);
            convertView.setTag(vHolder);
        } else {
            vHolder = (ViewHolder) convertView.getTag();
        }
        if (this.nType > 0) {
            vHolder.cbGroup.setVisibility(View.GONE);
//            vHolder.tvTip.setVisibility(View.GONE);
        } else {
            vHolder.cbGroup.setVisibility(View.VISIBLE);
//            vHolder.tvTip.setVisibility(View.VISIBLE);
        }
//        if (groupPosition == 0) {
//            vHolder.vBack.setVisibility(View.GONE);
//        }else {
//            vHolder.vBack.setVisibility(View.VISIBLE);
//        }

        final ShoppingCartGroup item = mGroup.get(groupPosition);
        if (!TextUtils.isEmpty(item.getCountry_flag())) {
            Netroid.displayImage(Util.transfer(item.getCountry_flag()), vHolder.ivFrom);
        }
        vHolder.cbGroup.setChecked(item.getChecked());
        vHolder.cbGroup.setOnClickListener(new Group_CheckBox_Click(groupPosition));
        vHolder.tvFrom.setText(item.getSiteDes());
//        vHolder.tvFeeType.setText("[ " + item.getDelivery_des() +  " ]");

//        String freefee = item.getFree_fee();
//        if (!TextUtils.isEmpty(freefee)) {
//            freefee = "   (" + freefee + ")";
//        }

//        freefee = item.getFee() + freefee;
//        vHolder.tvFeeDesc.setText(freefee);
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, GoodsListActivity.class);
//                intent.putExtra("id", Integer.parseInt(item.getSite_id()));
//                intent.putExtra("type", Configration.HOT_GOODS);
//                mContext.startActivity(intent);
//            }
//        });

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expand_shop_item, parent, false);
            holder = new ViewHolder();
            holder.cbChild = (CheckBox) convertView.findViewById(R.id.shop_child_cb);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.shop_title);
            holder.tvColor = (TextView) convertView.findViewById(R.id.shop_color);
            holder.tvSize = (TextView) convertView.findViewById(R.id.shop_size);
            holder.tvCount = (TextView) convertView.findViewById(R.id.shop_count);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.shop_price);
            holder.ivShopIcon = (NetworkImageView) convertView.findViewById(R.id.shop_image);
            holder.ivSoldout = (ImageView) convertView.findViewById(R.id.shop_image_sold_out);
            holder.vDidver = (View) convertView.findViewById(R.id.shop_didver);
            holder.vDiverOther = (View) convertView.findViewById(R.id.shop_didver_other);
            holder.llytFee = (LinearLayout) convertView.findViewById(R.id.shop_fee_layout);
            holder.tvFullCut = (TextView) convertView.findViewById(R.id.shop_item_fullcut);
            holder.tvFullCutTxt = (TextView) convertView.findViewById(R.id.shop_item_fullcut_txt);
            holder.llytAddCount = (LinearLayout) convertView.findViewById(R.id.count_edit_layout);
            holder.tvAdd = (TextView) convertView.findViewById(R.id.count_add);
            holder.tvMinus = (TextView) convertView.findViewById(R.id.count_minus);
            holder.etCount = (EditText) convertView.findViewById(R.id.count_edit);
            holder.tvWidth = (TextView) convertView.findViewById(R.id.shop_width);
            holder.tvColorTxt = (TextView) convertView.findViewById(R.id.shop_color_txt);
            holder.tvSizeTxt = (TextView) convertView.findViewById(R.id.shop_size_txt);
            holder.tvWidthTxt = (TextView) convertView.findViewById(R.id.shop_width_txt);
            holder.disLinearLayout = (LinearLayout) convertView.findViewById(R.id.linearlayout);
            holder.discount_tv = (TextView) convertView.findViewById(R.id.discount_tv);
            holder.tvOfficialFee = (TextView) convertView.findViewById(R.id.shop_official_fee);
//            holder.tvNationalFee = (TextView) convertView.findViewById(R.id.shop_national_fee);
//            holder.tvWeightDesc = (TextView) convertView.findViewById(R.id.shop_fee_desc);
//            holder.rlytFreight = (RelativeLayout) convertView.findViewById(R.id.shop_weight_fee_layout);
//            holder.mListChannel = (AllListView) convertView.findViewById(R.id.shop_fee_list);
            holder.tvGoodsNum = (TextView) convertView.findViewById(R.id.shop_freight_num);
            holder.tvGoodsTotal = (TextView) convertView.findViewById(R.id.shop_freight_total);
//            holder.ivFeeHelp = (ImageView) convertView.findViewById(R.id.shop_weight_fee_help);
            holder.tvTriff = (TextView) convertView.findViewById(R.id.shop_fee_tariff_txt);
            holder.tvTriffFee = (TextView) convertView.findViewById(R.id.shop_fee_tariff);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ViewHolder mHolder = holder;

        ShoppingCartGoods info = mChild.get(groupPosition).get(childPosition);
        if (info.getStatus() == 1) {
            holder.ivSoldout.setVisibility(View.INVISIBLE);
            holder.cbChild.setEnabled(true);
        } else {
            holder.ivSoldout.setVisibility(View.VISIBLE);
            holder.cbChild.setEnabled(false);
        }

        ArrayList<ShoppingCartSku> skus = info.getSku();
        ArrayList<String> dimensions = info.getDimensions();
        boolean bColor = false;
        boolean bSize = false;
        boolean bWidth = false;
        if (setShowDimension(dimensions, "color")) {
            holder.tvColor.setVisibility(View.VISIBLE);
            holder.tvColorTxt.setVisibility(View.VISIBLE);
        } else {
            holder.tvColor.setVisibility(View.GONE);
            holder.tvColorTxt.setVisibility(View.GONE);
            bColor = true;
        }

        if (setShowDimension(dimensions, "size")) {
            holder.tvSize.setVisibility(View.VISIBLE);
            holder.tvSizeTxt.setVisibility(View.VISIBLE);
        } else {
            holder.tvSize.setVisibility(View.GONE);
            holder.tvSizeTxt.setVisibility(View.GONE);
            bSize = true;
        }

        if (setShowDimension(dimensions, "width")) {
            holder.tvWidth.setVisibility(View.VISIBLE);
            holder.tvWidthTxt.setVisibility(View.VISIBLE);
        } else {
            holder.tvWidth.setVisibility(View.GONE);
            holder.tvWidthTxt.setVisibility(View.GONE);
            bWidth = true;
        }

        int size = skus.size();
        for (int i = 0; i < size; i++) {
            ShoppingCartSku sku = skus.get(i);
            if (sku.getKey().equals("color")) {
                holder.tvColor.setText(": " + sku.getValue());
            }

            if (sku.getKey().equals("size")) {
                holder.tvSize.setText(": " + sku.getValue());
            }

            if (sku.getKey().equals("width")) {
                holder.tvWidth.setText(": " + sku.getValue());
            }
        }

        holder.cbChild.setChecked(info.getChecked());
        holder.cbChild.setOnClickListener(new Child_CheckBox_Click(groupPosition, childPosition));

        holder.tvTitle.setText(info.getTitle());
        Netroid.displayImage(Util.transfer(info.getCover()), holder.ivShopIcon);

        holder.etCount.setText(String.valueOf(info.getNum()));

        if (info.getIsEdit()) {
            if (info.getStatus() == 1) {
                editViewShow(holder, true, false, false, false);
                holder.etCount.setText(String.valueOf(info.getNum()));
                holder.tvAdd.setOnClickListener(new Count_Edit_Click(holder.etCount, info, holder.tvCount, holder.tvPrice, info.getCartId()));
                holder.tvMinus.setOnClickListener(new Count_Edit_Click(holder.etCount, info, holder.tvCount, holder.tvPrice, info.getCartId()));
            } else {
                editViewShow(holder, false, bColor, bSize, bWidth);
            }

        } else {
            editViewShow(holder, false, bColor, bSize, bWidth);
        }

        holder.tvCount.setText("" + info.getNum());
        holder.tvPrice.setText(info.getPrice());
        if(!directBuy) {
            if ((Float.parseFloat(info.getList_price()) - Float.parseFloat(info.getPrice())) > 0) {
                holder.disLinearLayout.setVisibility(View.VISIBLE);
                holder.discount_tv.setText(Util.FloatKeepZero(Float.parseFloat(info.getList_price()) - Float.parseFloat(info.getPrice())));
            }
        }

        if (this.nType > 0) {
            holder.cbChild.setVisibility(View.GONE);
            holder.vDidver.setVisibility(View.GONE);
            holder.vDiverOther.setVisibility(View.VISIBLE);
            if (childPosition == mChild.get(groupPosition).size() - 1) {
                holder.llytFee.setVisibility(View.VISIBLE);
                final ShoppingCartGroup group = mGroup.get(groupPosition);
                if (group.getFee() != null) {

                    float singletotal = 0;
                    int num = 0;
                    for (int i = 0; i < mChild.get(groupPosition).size(); i++) {
                        singletotal += Float.parseFloat(mChild.get(groupPosition).get(i).getPrice()) *
                                mChild.get(groupPosition).get(i).getNum();
                        num += mChild.get(groupPosition).get(i).getNum();
                    }

                    holder.tvGoodsNum.setText("" + num);

                    if (group.getDiscount_fee() == null) {
                        holder.tvFullCut.setVisibility(View.GONE);
                        holder.tvFullCutTxt.setVisibility(View.GONE);
                    } else {
                        holder.tvFullCut.setVisibility(View.VISIBLE);
                        holder.tvFullCutTxt.setVisibility(View.VISIBLE);
                        holder.tvFullCutTxt.setText("满减优惠：满" + group.getDiscount_fee().getMoney() + "元减" +
                                group.getDiscount_fee().getDiscount() + "元");

                        if (singletotal >= Float.parseFloat(group.getDiscount_fee().getMoney())) {
                            holder.tvFullCutTxt.setTextColor(Color.parseColor("#212121"));
                            holder.tvFullCut.setTextColor(Color.parseColor("#212121"));
                            holder.tvFullCut.setText("-￥" + group.getDiscount_fee().getDiscount());
                        } else {
                            holder.tvFullCut.setTextColor(Color.parseColor("#50212121"));
                            holder.tvFullCut.setText("未满足");
                            holder.tvFullCutTxt.setTextColor(Color.parseColor("#50212121"));
                        }
                    }

                    holder.tvOfficialFee.setText("￥" + group.getTotal_fee());
//                    if (group.getDelivery() == 1) {
//                        holder.rlytFreight.setVisibility(View.VISIBLE);
//                        String strXml = mContext.getResources().getString(R.string.shop_weight_desc);
//                        String strDesc = String.format(strXml, group.getWeight() / 1000);
//                        holder.tvWeightDesc.setText(strDesc);
//                        holder.tvNationalFee.setText("￥" + Util.FloatKeepTwo(group.getFees().getChannelFee()));
//
//                        holder.ivFeeHelp.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent intentNation = new Intent(mContext, NationalFeeDetail.class);
//                                intentNation.putExtra("weight", Util.FloatKeepThree(group.getWeight() / 1000));
//                                mContext.startActivity(intentNation);
//                            }
//                        });
//
//                        if(!MyPreference.takeSharedPreferences(mContext,MyPreference.FREE_DETAIL)){
//                            addGuideImage(holder.ivFeeHelp, null, R.layout.guide_tips_up, 0, 0, "点这里可以查看运费税费具体详情", "", MyPreference.FREE_DETAIL);
//                        }
//
//                    } else if (group.getDelivery() == 2) {
//                        holder.rlytFreight.setVisibility(View.GONE);
//                    }

                    final float total = group.getFees().getServiceFee() + group.getFees().getForeignFee();
                    int nChannel = 0;
                    for (int i = 0; i < group.getTaxs().size(); i++) {
                        if (group.getTaxs().get(i).isSelect()) {
                            nChannel = i;
                            break;
                        }
                    }

                    FreightInfo fInfo = group.getTaxs().get(nChannel);
                    if (fInfo.getDelivery() == 1) {
                        if (Float.parseFloat(fInfo.getTax_fee()) > 0 && fInfo.isShow()) {
                            holder.tvTriff.setVisibility(View.VISIBLE);
                            holder.tvTriff.setTextColor(Color.parseColor("#ffffff"));
                            holder.tvTriff.setBackgroundColor(Color.parseColor("#ed5137"));
                            holder.tvTriff.setText(fInfo.getFee_des());
                            holder.tvTriffFee.setText("￥0");
                        } else {
                            holder.tvTriff.setVisibility(View.GONE);
                            holder.tvTriffFee.setText("￥" + fInfo.getTax_fee());
                        }
                    } else if (fInfo.getDelivery() == 2) {
                        holder.tvTriff.setVisibility(View.VISIBLE);
                        holder.tvTriff.setTextColor(Color.parseColor("#212121"));
                        holder.tvTriff.setBackgroundColor(Color.parseColor("#ffffff"));
                        holder.tvTriff.setText(fInfo.getTax_des());
                        holder.tvTriffFee.setText("￥" + fInfo.getTax_fee());
                    }


                    if (group.getDelivery() == 1) {
                        holder.tvGoodsTotal.setText(Util.changeTextStytle(Util.FloatKeepZero(singletotal + total + group.getFees().getChannelFee())
                                , 1));
                    } else {
                        holder.tvGoodsTotal.setText(Util.changeTextStytle(Util.FloatKeepZero(singletotal + total), 1));
                    }

                }
            } else {
                holder.llytFee.setVisibility(View.GONE);
            }
        } else {
            holder.cbChild.setVisibility(View.VISIBLE);
            holder.llytFee.setVisibility(View.GONE);
            holder.vDiverOther.setVisibility(View.GONE);
            if (childPosition == mChild.get(groupPosition).size() - 1) {
                holder.vDidver.setVisibility(View.GONE);
            } else {
                holder.vDidver.setVisibility(View.VISIBLE);
            }
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mChild.get(groupPosition).get(childPosition).getIsEdit()) {
                    return;
                }

                if (nType == 0) {
                    Intent intent = new Intent(mContext, NewGoodsDetailActivity.class);
                    intent.putExtra("goodsId", mChild.get(groupPosition).get(childPosition).getGoodsId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivity(intent);

//                    GoodsIdInfo idInfo = new GoodsIdInfo();
//                    idInfo.setGoodsId(mChild.get(groupPosition).get(childPosition).getGoodsId());
//                    idInfo.setGoodsId(mChild.get(groupPosition).get(childPosition).getRecommend());
//                    Intent intent = new Intent(mContext, GoodsDetailActivity.class);
//                    intent.putExtra("data", new Gson().toJson(idInfo));
//                    mContext.startActivity(intent);
                }
            }
        });

        return convertView;
    }

    /**
     * group选中事件，并保证child的跟随选中
     */
    class Group_CheckBox_Click implements View.OnClickListener {
        private int nGropPosition;

        public Group_CheckBox_Click(int nGropPosition) {
            this.nGropPosition = nGropPosition;
        }

        @Override
        public void onClick(View v) {
            mGroup.get(nGropPosition).toggle();
            int childCount = mChild.get(nGropPosition).size();
            boolean groupIsChecked = mGroup.get(nGropPosition).getChecked();
            for (int i = 0; i < childCount; i++)
                if (mChild.get(nGropPosition).get(i).getStatus() == 1)
                    mChild.get(nGropPosition).get(i).setChecked(groupIsChecked);

            if (cbAllCheck != null)
                checkAllChecked();
            notifyDataSetChanged();
        }
    }

    /**
     * child选中事件，保持与Group一致
     */
    class Child_CheckBox_Click implements View.OnClickListener {
        private int groupPosition;
        private int childPosition;

        public Child_CheckBox_Click(int groupPosition, int childPosition) {
            this.groupPosition = groupPosition;
            this.childPosition = childPosition;
        }

        @Override
        public void onClick(View v) {
            mChild.get(groupPosition).get(childPosition).toggle();
            int childCount = mChild.get(groupPosition).size();
            boolean childAllIsCheck = true;
            for (int i = 0; i < childCount; i++) {
                if (mChild.get(groupPosition).get(i).getStatus() == 1) {
                    if (!mChild.get(groupPosition).get(i).getChecked()) {
                        childAllIsCheck = false;
                    }
                }
            }

            mGroup.get(groupPosition).setChecked(childAllIsCheck);
            if (cbAllCheck != null)
                checkAllChecked();
            notifyDataSetChanged();
        }
    }

    /**
     * 全选和反全选功能
     */
    public void allChecked(boolean isChecked) {
        int nGroupCount = mGroup.size();
        for (int i = 0; i < nGroupCount; i++) {
            mGroup.get(i).setChecked(isChecked);
            int nChildCount = mChild.get(i).size();
            for (int j = 0; j < nChildCount; j++) {
                if (mChild.get(i).get(j).getStatus() == 1)
                    mChild.get(i).get(j).setChecked(isChecked);
            }
        }
        if (cbAllCheck != null)
            checkAllChecked();
        notifyDataSetChanged();
    }

    /**
     * 判断是否全选中和不全选中
     */
    public void checkAllChecked() {
        boolean allIsChecked = true;
        float price = 0;
        int count = 0;
        int nGroupCount = mGroup.size();
        for (int i = 0; i < nGroupCount; i++) {
            int nChildCount = mChild.get(i).size();
            for (int j = 0; j < nChildCount; j++) {
                if (mChild.get(i).get(j).getStatus() == 1) {
                    if (!mChild.get(i).get(j).getChecked())
                        allIsChecked = false;
                    else {
                        price += Float.parseFloat(mChild.get(i).get(j).getPrice()) * mChild.get(i).get(j).getNum();
                        count++;
                    }
                }
            }
        }

        if (updateListener != null) {
            updateListener.updatePrice(price, count);
        }

        cbAllCheck.setChecked(allIsChecked);
    }

    /**
     * 计算修改数量后的总价钱
     *
     * @return
     */
    public String getTotalPrice() {
        float nPrice = 0;
        int nGroupCount = mGroup.size();
        for (int i = 0; i < nGroupCount; i++) {
            int nChildCount = mChild.get(i).size();
            for (int j = 0; j < nChildCount; j++) {
                if (mChild.get(i).get(j).getStatus() == 1) {
                    if (mChild.get(i).get(j).getChecked())
                        nPrice += Float.parseFloat(mChild.get(i).get(j).getPrice()) * mChild.get(i).get(j).getNum();
                }
            }
        }
        return "商品总价：￥" + Util.FloatKeepTwo(nPrice);
    }

    /**
     * 得到售罄商品个数
     *
     * @return
     */
    public int getSoldCount() {
        int nCount = 0;
        int nGroupCount = mGroup.size();
        for (int i = 0; i < nGroupCount; i++) {
            int nChildCount = mChild.get(i).size();
            for (int j = 0; j < nChildCount; j++) {
                if (mChild.get(i).get(j).getStatus() != 1) {
                    nCount++;
                }
            }
        }
        return nCount;
    }

    // 获取将要提交订单的购物车商品
    public ShoppingCartPost[] getCartList() {

        int nGroupCount = mGroup.size();
        ArrayList<ShoppingCartPost> listCart = new ArrayList<>();
        for (int i = 0; i < nGroupCount; i++) {
            int nChildCount = mChild.get(i).size();
            for (int j = 0; j < nChildCount; j++) {
                if (mChild.get(i).get(j).getChecked() && mChild.get(i).get(j).getStatus() == 1) {
                    ShoppingCartPost info = new ShoppingCartPost();
                    info.setNum(mChild.get(i).get(j).getNum());
                    info.setSkuId(mChild.get(i).get(j).getSkuId());
                    info.setGoodsId(mChild.get(i).get(j).getGoodsId());
                    listCart.add(info);
                }
            }
        }
        ShoppingCartPost[] arrCart = new ShoppingCartPost[listCart.size()];
        for (int i = 0; i < listCart.size(); i++) {
            arrCart[i] = listCart.get(i);
        }

        return arrCart;
    }

    // 获取未被选中的购物车商品
    public ShoppingCartPost[] getUnCartList() {

        int nGroupCount = mGroup.size();
        ArrayList<ShoppingCartPost> listCart = new ArrayList<>();
        for (int i = 0; i < nGroupCount; i++) {
            int nChildCount = mChild.get(i).size();
            for (int j = 0; j < nChildCount; j++) {
                if (!mChild.get(i).get(j).getChecked()) {
                    ShoppingCartPost info = new ShoppingCartPost();
                    info.setNum(mChild.get(i).get(j).getNum());
                    info.setSkuId(mChild.get(i).get(j).getSkuId());
                    info.setGoodsId(mChild.get(i).get(j).getGoodsId());
                    listCart.add(info);
                }
            }
        }
        ShoppingCartPost[] arrUnCart = new ShoppingCartPost[listCart.size()];
        for (int i = 0; i < listCart.size(); i++) {
            arrUnCart[i] = listCart.get(i);
        }

        return arrUnCart;
    }

    public String getCartIds() {
        String cartIds = "";
        int nGroupCount = mGroup.size();
        for (int i = 0; i < nGroupCount; i++) {
            int nChildCount = mChild.get(i).size();
            for (int j = 0; j < nChildCount; j++) {
                if (mChild.get(i).get(j).getChecked()) {
                    cartIds += mChild.get(i).get(j).getCartId() + ",";
                }
            }
        }
        return (cartIds.substring(0, cartIds.length() - 1));
    }

    private boolean setShowDimension(ArrayList<String> list, String str) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(str)) {
                return true;
            }
        }

        return false;
    }

    public void editViewShow(ViewHolder viewHolder, boolean bEdit, boolean bColor, boolean bSize, boolean bWidth) {
        if (bEdit) {
            viewHolder.tvColor.setVisibility(View.GONE);
            viewHolder.tvSize.setVisibility(View.GONE);
            viewHolder.tvWidth.setVisibility(View.GONE);
            viewHolder.llytAddCount.setVisibility(View.VISIBLE);
            viewHolder.tvColorTxt.setVisibility(View.GONE);
            viewHolder.tvSizeTxt.setVisibility(View.GONE);
            viewHolder.tvWidthTxt.setVisibility(View.GONE);
        } else {
            viewHolder.tvColor.setVisibility(bColor ? View.GONE : View.VISIBLE);
            viewHolder.tvSize.setVisibility(bSize ? View.GONE : View.VISIBLE);
            viewHolder.llytAddCount.setVisibility(View.GONE);
            viewHolder.tvColorTxt.setVisibility(bColor ? View.GONE : View.VISIBLE);
            viewHolder.tvSizeTxt.setVisibility(bSize ? View.GONE : View.VISIBLE);
            viewHolder.tvWidthTxt.setVisibility(bWidth ? View.GONE : View.VISIBLE);
            viewHolder.tvWidth.setVisibility(bWidth ? View.GONE : View.VISIBLE);
        }
    }

    private class Count_Edit_Click implements View.OnClickListener {

        private EditText etCount;
        private ShoppingCartGoods mInfo;
        private TextView tvCount;
        private TextView tvPrice;
        private String cartId;

        public Count_Edit_Click(EditText editText, ShoppingCartGoods info, TextView count, TextView price, String cartId) {
            this.etCount = editText;
            this.mInfo = info;
            this.tvCount = count;
            this.tvPrice = price;
            this.cartId = cartId;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.count_add:
                    int nCountAdd = Integer.parseInt(etCount.getText().toString());
                    nCountAdd++;
                    etCount.setText(String.valueOf(nCountAdd));
                    mInfo.setNum(nCountAdd);
                    tvCount.setText("" + nCountAdd);
                    ShoppingCarUpdataObject updata = new ShoppingCarUpdataObject();
                    carts = updata.new Carts();
                    carts.setNum("" + nCountAdd);
                    carts.setId(cartId);
                    mlist.add(carts);
                    updata.setCarts(mlist);
                    json = new Gson().toJson(updata);
                    if (isLogin)
                        shoppingCarUpdata();
                    break;
                case R.id.count_minus:
                    int nCountMinus = Integer.parseInt(etCount.getText().toString());
                    nCountMinus--;
                    if (nCountMinus < 1) {
                        nCountMinus = 1;
                    }
                    tvCount.setText("" + nCountMinus);
                    etCount.setText(String.valueOf(nCountMinus));
                    mInfo.setNum(nCountMinus);
                    ShoppingCarUpdataObject updataMinus = new ShoppingCarUpdataObject();
                    carts = updataMinus.new Carts();
                    carts.setNum("" + nCountMinus);
                    carts.setId(cartId);
                    mlist.add(carts);
                    updataMinus.setCarts(mlist);
                    json = new Gson().toJson(updataMinus);
                    if (isLogin)
                        shoppingCarUpdata();
                    break;
            }

        }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public class ViewHolder {
        CheckBox cbGroup;
        CheckBox cbChild;
        ImageView ivFrom;
        TextView tvFrom;
        NetworkImageView ivShopIcon;
        ImageView ivSoldout;
        TextView tvTitle;
        TextView tvColorTxt;
        TextView tvSizeTxt;
        TextView tvWidthTxt;
        TextView tvColor;
        TextView tvSize;
        TextView tvCount;
        TextView tvWidth;
        TextView tvPrice;
        View vDidver;
        View vBack;
        TextView tvTip;
        LinearLayout llytFee;
        TextView tvFullCut;
        TextView tvFullCutTxt;
        View vDiverOther;
        LinearLayout llytAddCount;
        TextView tvAdd;
        TextView tvMinus;
        EditText etCount;
        LinearLayout disLinearLayout;
        TextView discount_tv;
        TextView tvFeeType;
        TextView tvFeeDesc;
        TextView tvWeightDesc;
        TextView tvOfficialFee;
        TextView tvNationalFee;
        RelativeLayout rlytFreight;
        AllListView mListChannel;
        TextView tvGoodsNum;
        TextView tvGoodsTotal;
        ImageView ivFeeHelp;
        TextView tvTriff;
        TextView tvTriffFee;
    }

    public interface updatePriceListener {
        public void updatePrice(float price, int count);
    }

    public void setUpdateListener(updatePriceListener updateListener) {
        this.updateListener = updateListener;
    }

    String json;
    ShoppingCarUpdataObject.Carts carts;
    ArrayList<ShoppingCarUpdataObject.Carts> mlist = new ArrayList<>();

    //修改购物车中的商品
    private void shoppingCarUpdata() {
//        HttpRequest.getInstance().shoppingCarUpdata(activity.mHandler, json, new MsgCallBack() {
//            @Override
//            public void onResult(HttpMessage msg) {
//                if(msg.isSuccess()){
//
//                }
//            }
//        });
    }

    //添加操作引导
    public void addGuideImage(final View referenceViewUp, final View referenceViewDown, int upLayoutId, int downLayoutId, final int type, final String tipsUp, final String tipsDown, String key) {
        View view = null;
        DisplayMetrics metric = new DisplayMetrics();

        if (mContext instanceof SubmitOrderActivity) {
            view = ((SubmitOrderActivity) mContext).getWindow().getDecorView().findViewById(R.id.layout_root);
            ((SubmitOrderActivity) mContext).getWindowManager().getDefaultDisplay().getMetrics(metric);               //查找通过setContentView上的根布局
        } else if (mContext instanceof NewMainActivity) {
        }

        final int mScreenWidth = metric.widthPixels;
        final int mScreenHeight = metric.heightPixels;
        if (view == null) return;
        if (MyPreference.takeSharedPreferences(mContext, key)) {
            //引导过了
            return;
        }
        ViewParent viewParent = view.getParent();
        if (viewParent instanceof FrameLayout) {
            final FrameLayout frameLayout = (FrameLayout) viewParent;
            LayoutInflater inflater = LayoutInflater.from(mContext);
            final RelativeLayout guideView = (RelativeLayout) inflater.inflate(R.layout.guide_tips, null);
            final View tipsUpView;
            final ImageView ivArrowUp;
            final TextView tvTipsUp;
            //获取参照物控件的位置
            if (referenceViewUp != null && upLayoutId != 0) {
                ViewTreeObserver vtoUp = referenceViewUp.getViewTreeObserver();
                tipsUpView = inflater.inflate(upLayoutId, null);
                ivArrowUp = (ImageView) tipsUpView.findViewById(R.id.guide_arrow);
                tvTipsUp = (TextView) tipsUpView.findViewById(R.id.guide_tips);
                guideView.addView(tipsUpView);
                vtoUp.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        referenceViewUp.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        tvTipsUp.setText(tipsUp);
                        int[] location = new int[2];
                        referenceViewUp.getLocationInWindow(location);
                        int x = location[0];
                        int y = location[1];
                        int widt = referenceViewUp.getWidth();
                        int heigh = referenceViewUp.getHeight();
                        int tipsWidth = tvTipsUp.getWidth();
                        LinearLayout.LayoutParams llparamsArrow = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        llparamsArrow.leftMargin = x + 40;
                        ivArrowUp.setLayoutParams(llparamsArrow);

                        LinearLayout.LayoutParams llparamsTips = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        llparamsTips.gravity = Gravity.CENTER_HORIZONTAL;
                        tvTipsUp.setLayoutParams(llparamsTips);

                        RelativeLayout.LayoutParams reparams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        reparams.topMargin = (int) y + heigh / 2;
                        tipsUpView.setLayoutParams(reparams);

                        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                        alphaAnimation.setFillAfter(true);
                        alphaAnimation.setDuration(1000);
                        alphaAnimation.setStartOffset(500);
                        tipsUpView.setVisibility(View.VISIBLE);
                        tipsUpView.setAnimation(alphaAnimation);
                        alphaAnimation.startNow();
                    }
                });
            }

            guideView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (guideView.getVisibility() == View.VISIBLE) {
                        AlphaAnimation alphaAnimation1 = new AlphaAnimation(1, 0);
                        alphaAnimation1.setFillAfter(true);
                        alphaAnimation1.setDuration(1000);
                        guideView.setVisibility(View.INVISIBLE);
                        guideView.setAnimation(alphaAnimation1);
                        alphaAnimation1.startNow();

                        frameLayout.removeView(guideView);
                    }
                }
            });
            frameLayout.addView(guideView);//添加引导图片
            MyPreference.saveSharedPreferences(mContext, key, true);
            final Handler mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case 0:
                            if (guideView.getVisibility() == View.VISIBLE) {
                                AlphaAnimation alphaAnimation1 = new AlphaAnimation(1, 0);
                                alphaAnimation1.setFillAfter(true);
                                alphaAnimation1.setDuration(1000);
                                guideView.setVisibility(View.INVISIBLE);
                                guideView.setAnimation(alphaAnimation1);
                                alphaAnimation1.startNow();

                                frameLayout.removeView(guideView);
                            }
                            break;
                    }
                }
            };

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    mHandler.sendEmptyMessage(0);
                }
            }, 3000);

        }
    }
}
