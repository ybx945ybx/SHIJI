package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.HotMessageActivity;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.entity.navigation.NewsInfo;
import cn.yiya.shiji.entity.navigation.RecommendInfo;
import cn.yiya.shiji.utils.BitmapTool;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.utils.SimpleUtils;

/**
 * Created by Tom on 2016/4/20.
 */
public class CountryHotMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public static final int RECOMMEND_TYPE = 1111;
    public static final int LIST_TYPE = 2222;
    public static final int TXT_TYPE = 3333;

    private Context mContext;
    public ArrayList<NewsInfo> list;
    private ArrayList<RecommendInfo> mRecommendList;

    public CountryHotMessageAdapter(Context mContext, ArrayList<NewsInfo> list, ArrayList<RecommendInfo> mRecommendList){
        this.mContext = mContext;
        this.list = list;
        this.mRecommendList = mRecommendList;
        setHasStableIds(true);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case RECOMMEND_TYPE:
                return new RecommendViewHolder(parent);
            case LIST_TYPE:
                return new CountryHotMessageViewHolder(parent);
            case TXT_TYPE:
                return new CountryTxtViewHolder(parent);
            default:
                throw new RuntimeException("Unknown issue type");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case RECOMMEND_TYPE:
                ((RecommendViewHolder)holder).bindData(mRecommendList);
                break;
            case LIST_TYPE:
                final NewsInfo info;
                if (mRecommendList == null || mRecommendList.size() == 0) {
                    if (position - 1 > list.size()) {
                        return;
                    } else {
                        info = list.get(position - 2);
                    }
                } else {
                    if (position - 1 > list.size()) {
                        return;
                    } else {
                        info = list.get(position - 2);
                    }
                }
                CountryHotMessageViewHolder pHolder = (CountryHotMessageViewHolder)holder;
                BitmapTool.showImageView(info.getCover(), pHolder.ivHotMessage);
                pHolder.tvTitle.setText(info.getTitle());
                pHolder.tvContext.setText(info.getBrief());

                pHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!NetUtil.IsInNetwork(mContext)){
                            Toast.makeText(mContext, Configration.OFF_LINE_TIPS, Toast.LENGTH_SHORT).show();
                            return;
                        }else {
                            Intent intent = new Intent(mContext, HotMessageActivity.class);
                            intent.putExtra("id", info.getId());
                            mContext.startActivity(intent);
                        }
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        if(mRecommendList == null || mRecommendList.size() == 0){
            if (list == null || list.size() == 0)
                return 0;
            else {
                return list.size() + 2;
            }
        } else {
            if (list == null || list.size() == 0)
                return 1;
            else {
                return list.size() + 2;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) {
            return RECOMMEND_TYPE;
        } else if (position == 1){
            return TXT_TYPE;
        } else {
            return LIST_TYPE;
        }
    }

    protected class RecommendViewHolder extends RecyclerView.ViewHolder {

        public RecyclerView rycvRecommend;

        public RecommendViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.country_footer_hotmessage_head, parent, false));

        rycvRecommend = (RecyclerView) itemView.findViewById(R.id.recommend_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rycvRecommend.setLayoutManager(linearLayoutManager);
        rycvRecommend.setNestedScrollingEnabled(false);
        rycvRecommend.setItemAnimator(new DefaultItemAnimator());

        LinearLayout.LayoutParams rycvLayoutParams = (LinearLayout.LayoutParams)rycvRecommend.getLayoutParams();
        rycvLayoutParams.height = (SimpleUtils.getScreenWidth(mContext) - SimpleUtils.dp2px(mContext, 48))*2/5;
        rycvLayoutParams.width = SimpleUtils.getScreenWidth(mContext);
        rycvRecommend.setLayoutParams(rycvLayoutParams);
            if (mRecommendList.size() == 0) {
                itemView.setVisibility(View.GONE);
                rycvRecommend.setVisibility(View.GONE);
                return;
            }
        }

        public void bindData(ArrayList<RecommendInfo> mlist) {
            if (mRecommendList.size() == 0) {
                ViewGroup.LayoutParams lp = itemView.getLayoutParams();
                lp.height = 0;
                lp.width = 0;
                itemView.setLayoutParams(lp);
                return;
            }
            RecommendBuyAdapter recommendBuyAdapter = new RecommendBuyAdapter(mContext, 1, mlist);
            rycvRecommend.setAdapter(recommendBuyAdapter);
        }
    }

    public static class CountryHotMessageViewHolder extends RecyclerView.ViewHolder{
        ImageView ivHotMessage;
        TextView tvTitle;
        TextView tvContext;

        public CountryHotMessageViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.hot_message_item, parent, false));
            ivHotMessage = (ImageView) itemView.findViewById(R.id.iv_hotmessage);
            tvTitle = (TextView) itemView.findViewById(R.id.hotmessage_title);
            tvContext = (TextView) itemView.findViewById(R.id.hotmessage_context);
        }

    }

    public static class CountryTxtViewHolder extends RecyclerView.ViewHolder{
        TextView tvHotMessage;

        public CountryTxtViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.travel_txt_info, parent, false));
            tvHotMessage = (TextView) itemView.findViewById(R.id.travle_txt);
            tvHotMessage.setText("热门资讯");
        }

    }
}

