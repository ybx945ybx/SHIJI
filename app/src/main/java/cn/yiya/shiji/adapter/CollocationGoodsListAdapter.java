package cn.yiya.shiji.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.duowan.mobile.netroid.image.NetworkImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.CollocationGoodsOrderActivity;
import cn.yiya.shiji.entity.goodes.NewGoodsItem;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.MyPreference;

/**
 * Created by Tom on 2016/7/19.
 */
public class CollocationGoodsListAdapter extends RecyclerView.Adapter<CollocationGoodsListAdapter.CollocationGoodsListViewHolder> implements CollocationGoodsOrderActivity.ItemTouchHelperAdapter {

    private Context mContext;
    private ArrayList<NewGoodsItem> mList;
    private boolean isOrder;
    private boolean preView;
    private OnDeleteListener onDeleteListener;

    public CollocationGoodsListAdapter(Context mContext, boolean isOrder, boolean preView){
        this.mContext = mContext;
        this.isOrder = isOrder;
        this.preView = preView;
    }

    public ArrayList<NewGoodsItem> getmList() {
        return mList;
    }

    public void setmList(ArrayList<NewGoodsItem> mList) {
        this.mList = mList;
    }

    @Override
    public CollocationGoodsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CollocationGoodsListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.collocation_goods_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final CollocationGoodsListViewHolder holder, final int position) {
        NewGoodsItem info = mList.get(position);

        if(isOrder){
            holder.ivGoodsDelete.setVisibility(View.GONE);
            holder.ivGoodsOrder.setVisibility(View.VISIBLE);
            //给列表第一个添加引导
            if(position == 0){
                if (!MyPreference.takeSharedPreferences(mContext, MyPreference.COLLOCATION_GOODS_ORDER)) {
                    AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                    alphaAnimation.setFillAfter(true);
                    alphaAnimation.setDuration(1000);
                    alphaAnimation.setStartOffset(500);
                    holder.llytTips.setVisibility(View.VISIBLE);
                    holder.llytTips.setAnimation(alphaAnimation);
                    alphaAnimation.startNow();
                    MyPreference.saveSharedPreferences(mContext, MyPreference.COLLOCATION_GOODS_ORDER, true);
                    final Handler mHandler = new Handler(){
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            switch (msg.what) {
                                case 10:
                                    if(holder.llytTips.getVisibility() == View.VISIBLE) {
                                        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
                                        alphaAnimation.setFillAfter(true);
                                        alphaAnimation.setDuration(1000);
                                        holder.llytTips.setVisibility(View.INVISIBLE);
                                        holder.llytTips.setAnimation(alphaAnimation);
                                        alphaAnimation.startNow();
                                    }
                                    break;
                            }
                        }
                    };

                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        public void run() {
                            mHandler.sendEmptyMessage(10);
                        }
                    }, 3000);
                }
            }
//            holder.itemView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    switch (event.getAction()){
//                        case MotionEvent.ACTION_DOWN:
//                            holder.rlytItem.setBackgroundColor(Color.parseColor("#f0f0f0"));
//                            break;
//                        case MotionEvent.ACTION_UP:
//                            holder.rlytItem.setBackgroundColor(Color.parseColor("#ffffff"));
//                            break;
//                        case MotionEvent.ACTION_CANCEL:
//                            holder.rlytItem.setBackgroundColor(Color.parseColor("#ffffff"));
//                            break;
//                        default:
//                            holder.rlytItem.setBackgroundColor(Color.parseColor("#ffffff"));
//                    }
//                    return false;
//                }
//            });
        }else if(preView){
            holder.ivGoodsDelete.setVisibility(View.GONE);
            holder.ivGoodsOrder.setVisibility(View.GONE);
        }else {
            holder.ivGoodsDelete.setVisibility(View.VISIBLE);
            holder.ivGoodsOrder.setVisibility(View.GONE);
            holder.ivGoodsDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onDeleteListener != null){
                        onDeleteListener.onDelete(position);
                    }
                }
            });
        }


        // TODO: 2016/7/20

        Netroid.displayImage(info.getCover(), holder.ivGoods);
        holder.tvGoodsName.setText(info.getTitle());
        holder.tvGoodsBrand.setText(info.getBrand());
        holder.tvGoodsPrice.setText(info.getPrice());
    }

    @Override
    public int getItemCount() {
        if(mList == null){
            return 0;
        }else {
            return mList.size();
        }
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if(!isOrder || preView){
            return;
        }
        Collections.swap(mList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        if(!isOrder || preView){
            return;
        }
    }

    public interface OnDeleteListener{
        void onDelete(int position);
    }

    public void setOnDeleteListener(OnDeleteListener onDeleteListener){
        this.onDeleteListener = onDeleteListener;
    }

    class CollocationGoodsListViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout rlytItem;
        NetworkImageView ivGoods;
        TextView tvGoodsName;
        TextView tvGoodsBrand;
        TextView tvGoodsPrice;
        ImageView ivGoodsDelete;
        ImageView ivGoodsOrder;

        LinearLayout llytTips;
        public CollocationGoodsListViewHolder(View itemView) {
            super(itemView);
            rlytItem = (RelativeLayout) itemView.findViewById(R.id.rlyt_item);
            ivGoods = (NetworkImageView) itemView.findViewById(R.id.iv_goods_image);
            tvGoodsName = (TextView) itemView.findViewById(R.id.tv_goods_name);
            tvGoodsBrand = (TextView) itemView.findViewById(R.id.tv_goods_brand);
            tvGoodsPrice = (TextView) itemView.findViewById(R.id.tv_goods_price);
            ivGoodsDelete = (ImageView) itemView.findViewById(R.id.iv_delete);
            ivGoodsOrder = (ImageView) itemView.findViewById(R.id.iv_order);

            llytTips = (LinearLayout) itemView.findViewById(R.id.llyt_tips);

        }
    }
}
