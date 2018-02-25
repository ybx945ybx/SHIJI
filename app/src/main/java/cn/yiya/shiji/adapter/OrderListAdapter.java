package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.NewShoppingCartActivity;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.OrderListInfo;
import cn.yiya.shiji.views.InterceptLinearLayout;
import cn.yiya.shiji.views.ProgressDialog;

/**
 * 订单列表Adapter
 * Created by Amy on 2016/10/26.
 */

public class OrderListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    public ArrayList<OrderListInfo> mList;
    private int status; //0:全部,1:待付款,2:在途,3:待缴税,4:完成,5:失效

    private SimpleDateFormat mTimeFormat;
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final int ALL_WAITPAY = 1001; //全部 未付款
    private final int ALL_TRANS = 1002; //全部 运输中
    private final int ALL_DONE = 1004; //全部 已完成
    private final int ALL_INVAILD = 1005; //全部 已失效
    private final int WAITPAY = 1; //未付款
    private final int TRANS = 2; //运输中
    private final int DONE = 4; //已完成
    private final int INVAILD = 5; //已失效

    private static final String REFUNDTAG = "14";               // 退款后失效标签

    private static final int INTERVAL = 1000;                   // 倒计时间隔(1000毫秒)
    public final int OTHER = 2001;
    public final int SHARE_ORDER = 2002; //去晒单

    public OrderListAdapter(Context context, int status) {
        this.mContext = context;
        this.status = status;
        mTimeFormat = new SimpleDateFormat("mm:ss");
    }

    public void setmList(ArrayList<OrderListInfo> list) {
        if (list != null) {
            this.mList = list;
        } else {
            this.mList = new ArrayList<>();
        }
    }

    public ArrayList<OrderListInfo> getmList() {
        return mList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrderListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.order_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        OrderListViewHolder viewHolder = (OrderListViewHolder) holder;
        final OrderListInfo item = mList.get(position);

        int count = 0;
        for (int i = 0; i < item.getGoods().size(); i++) {
            count += item.getGoods().get(i).getNum();
        }
        SpannableString spannableString = new SpannableString("共 " + count + " 件");
        spannableString.setSpan(new ForegroundColorSpan(0xFF3c3c3c), 2, spannableString.length() - 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(14, true), 2, spannableString.length() - 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 2, spannableString.length() - 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.tvTotal.setText(spannableString);

        String str = "订单金额:  ¥";
        if (item.getGroup_info().getGroup() == 1) {
            str = "应付金额:  ¥";
        }
        SpannableString spannable = new SpannableString(str + item.getAmount());
        spannable.setSpan(new ForegroundColorSpan(0xFFed5137), 7, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new AbsoluteSizeSpan(14, true), 5, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new StyleSpan(Typeface.BOLD), 5, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.tvAmount.setText(spannable);

        viewHolder.tvDate.setText(item.getCreate_time().substring(0, 10));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        viewHolder.recyclerView.setLayoutManager(linearLayoutManager);

        HorOrderListAdapter horOrderListAdapter = new HorOrderListAdapter(mContext, item.getGoods());
        viewHolder.recyclerView.setAdapter(horOrderListAdapter);

        viewHolder.llGoods.setOnClickingListener(new InterceptLinearLayout.OnClickingListener() {
            @Override
            public void OnClickingListener() {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(item.getOrder_number(), item.getSub_order_number(), OTHER);
                }
            }
        });

        if (item.getGroup_info().getGroup() == 1) {
            waitToPay(viewHolder, item);
        } else {
            setOtherStatus(viewHolder, item);
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (status == 0) {
            switch (mList.get(position).getGroup_info().getGroup()) {
                case 1:
                    return ALL_WAITPAY;
                case 3:
                    return ALL_TRANS;
                case 4:
                    return ALL_DONE;
                case 5:
                    return ALL_INVAILD;
                default:
                    return 0;
            }
        }
        return status;
    }

    class OrderListViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderNum, tvAmount, tvTotal, tvPay, tvDate;
        RelativeLayout relativeLayout;
        RecyclerView recyclerView;
        TextView tvDes;
        LinearLayout llTv;
        InterceptLinearLayout llGoods;

        public OrderListViewHolder(View itemView) {
            super(itemView);
            tvOrderNum = (TextView) itemView.findViewById(R.id.tvorder);
            tvAmount = (TextView) itemView.findViewById(R.id.tv_amount);
            tvTotal = (TextView) itemView.findViewById(R.id.tv_total);
            tvPay = (TextView) itemView.findViewById(R.id.tv_pay);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.rl_order);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recyler_view);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvDes = (TextView) itemView.findViewById(R.id.tv_des);
            llTv = (LinearLayout) itemView.findViewById(R.id.ll_tv);
            llGoods = (InterceptLinearLayout) itemView.findViewById(R.id.linearlayout);
        }
    }

    private void waitToPay(final OrderListViewHolder holder, final OrderListInfo item) {
        long diff = 0;
        CountDownTimer timer;
        try {
            Date d1 = df.parse(item.getCurrent_time());
            Date d2 = df.parse(item.getClose_time());
            if (item.getDiff() == 0) {
                diff = d2.getTime() - d1.getTime();
            } else {
                diff = item.getDiff();
            }

        } catch (Exception e) {

        }
        if (holder.getItemViewType() == ALL_WAITPAY) {
            holder.tvOrderNum.setText("等待付款");
            holder.tvOrderNum.setTextColor(0xFFed5137);
            holder.tvDes.setText(R.string.go_to_pay);
            holder.tvPay.setVisibility(View.VISIBLE);

            timer = new CountDownTimer(diff, INTERVAL) {
                @Override
                public void onTick(long millisUntilFinished) {
                    holder.tvPay.setText(mTimeFormat.format(new Date(millisUntilFinished)));
                    item.setDiff(millisUntilFinished);
                }

                @Override
                public void onFinish() {
                    holder.tvOrderNum.setText("等待付款");
                    holder.tvOrderNum.setTextColor(0xFF9e9e9e);
                    holder.tvPay.setText("");
                    holder.tvDes.setText(R.string.go_back_shopping_cart);
                }
            };
            timer.start();

        } else if (holder.getItemViewType() == WAITPAY) {
            holder.tvOrderNum.setText("订单号 :" + item.getOrder_number());
            holder.tvDes.setText(R.string.go_to_pay);
            holder.tvPay.setVisibility(View.VISIBLE);
            timer = new CountDownTimer(diff, INTERVAL) {
                @Override
                public void onTick(long millisUntilFinished) {
                    holder.tvPay.setText(mTimeFormat.format(new Date(millisUntilFinished)));
                    if (millisUntilFinished != 0) {
                        item.setDiff(millisUntilFinished);
                    }
                }

                @Override
                public void onFinish() {                                   // 倒计时结束的操作
                    holder.llTv.setBackgroundColor(0xFFd8d8d8);
                }
            };
            timer.start();
        }

        initEvent(holder.llTv, item.getOrder_number(), item.getSub_order_number(), OTHER);
    }

    private void setOtherStatus(final OrderListViewHolder holder, final OrderListInfo item) {
        switch (holder.getItemViewType()) {
            case ALL_TRANS: //全部 运输中
                holder.tvOrderNum.setText("运输中");
                holder.tvOrderNum.setTextColor(Color.parseColor("#0c0c0c"));
                holder.tvPay.setText("");
                holder.tvDes.setText(R.string.goto_Share);
                initEvent(holder.llTv, item.getOrder_number(), item.getSub_order_number(), SHARE_ORDER);
                break;
            case ALL_DONE://全部 已完成
                holder.tvOrderNum.setText("已完成");
                holder.tvPay.setText("");
                holder.tvOrderNum.setTextColor(Color.parseColor("#0c0c0c"));
                holder.tvDes.setText(R.string.goto_Share);
                initEvent(holder.llTv, item.getOrder_number(), item.getSub_order_number(), SHARE_ORDER);
                break;
            case ALL_INVAILD://全部 已失效
                holder.tvOrderNum.setText("已失效");
                holder.tvOrderNum.setTextColor(Color.parseColor("#9b9b9b"));
                holder.tvPay.setText("");
                holder.tvDes.setText(R.string.go_back_shopping_cart);
                if (item.getStatus().equals(REFUNDTAG)) {
                    holder.llTv.setVisibility(View.GONE);
                }
                holder.llTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gobackShoppingCar(item.getOrder_number());
                    }
                });

                break;
            case TRANS://运输中
                holder.tvOrderNum.setText("订单号 :" + item.getOrder_number());
                holder.tvPay.setText("");
                holder.tvDes.setText(R.string.goto_Share);
                initEvent(holder.llTv, item.getOrder_number(), item.getSub_order_number(), SHARE_ORDER);
                break;
            case DONE://已完成
                holder.tvOrderNum.setText("订单号:" + item.getOrder_number());
                holder.tvPay.setText("");
                holder.tvDes.setText(R.string.goto_Share);
                initEvent(holder.llTv, item.getOrder_number(), item.getSub_order_number(), SHARE_ORDER);
                break;
            case INVAILD://已失效
                holder.tvOrderNum.setText("订单号 :" + item.getOrder_number());
                holder.tvPay.setText("");
                holder.tvDes.setText(R.string.go_back_shopping_cart);
                holder.llTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gobackShoppingCar(item.getOrder_number());
                    }
                });
                break;
            default:
                break;
        }
    }


    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(String orderno, String suborderno, int type);
    }

    private void initEvent(View view, final String orderNum, final String SubOrderNum, final int status) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(orderNum, SubOrderNum, status);
                }
            }
        });
    }

    private void gobackShoppingCar(String orderNum) {
        new RetrofitRequest<>(ApiRequest.getApiShiji().gobackShoppingCar(orderNum)).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    ProgressDialog.showCommonScrollDialogCancel(mContext, "提示", "您已成功将订单内的商品加回购物车", "返回", "查看购物车",
                            new ProgressDialog.DialogClick() {
                                @Override
                                public void OkClick() {
                                    Intent intent = new Intent(mContext, NewShoppingCartActivity.class);
                                    mContext.startActivity(intent);
                                }

                                @Override
                                public void CancelClick() {

                                }
                            });
                }
            }
        });
    }
}
