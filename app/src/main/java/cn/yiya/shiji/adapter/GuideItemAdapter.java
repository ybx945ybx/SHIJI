package cn.yiya.shiji.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.duowan.mobile.netroid.image.NetworkImageView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.GuideItem;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;

/**
 * Created by Tom on 2015/12/21.
 */
public class GuideItemAdapter extends RecyclerView.Adapter<GuideItemAdapter.GuideItemViewHolder> {
    private ArrayList<GuideItem> mList;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private int type;                   //  1是品牌 2是品类
    private int selectedNumber;
    private boolean isEdit;

    public GuideItemAdapter(Context context, int type, int selectedNumber, boolean isEdit){
        this.mContext =context;
        this.type = type;
        this.selectedNumber = selectedNumber;
        this.isEdit = isEdit;
    }

    public ArrayList<GuideItem> getmList() {
        return mList;
    }

    public void setmList(ArrayList<GuideItem> mList) {
        this.mList = mList;
    }

    @Override
    public GuideItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GuideItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.guide_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(final GuideItemViewHolder holder, int position) {
        final GuideItem item = mList.get(position);

        GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams)holder.rltyItem.getLayoutParams();
        layoutParams.height = SimpleUtils.getScreenWidth(mContext)/3;
        layoutParams.width = SimpleUtils.getScreenWidth(mContext)/3;
        holder.rltyItem.setLayoutParams(layoutParams);
        if(type == 1){
            holder.tvName.setVisibility(View.GONE);
            if(TextUtils.isEmpty(item.getLogo())){
                holder.rltyHasLog.setVisibility(View.INVISIBLE);
                holder.tvNoLog.setVisibility(View.VISIBLE);
                holder.tvNoLog.setText(item.getName());

            }else {
                holder.rltyHasLog.setVisibility(View.VISIBLE);
                holder.tvNoLog.setVisibility(View.INVISIBLE);
                Netroid.displayImage(Util.transfer(item.getLogo()), holder.ivLogo);
            }
        }else {
            holder.rltyHasLog.setVisibility(View.VISIBLE);
            holder.tvNoLog.setVisibility(View.INVISIBLE);
            holder.tvName.setVisibility(View.VISIBLE);
            holder.tvName.setText(item.getName());
            Netroid.displayImage(Util.transfer(item.getLogo()), holder.ivLogo);

        }
        if(item.getFollowed() == 1 || item.isSelected()){
            item.setIsSelected(true);
            holder.ivSelected.setVisibility(View.VISIBLE);
            holder.ivBackground.setBackgroundColor(Color.parseColor("#19000000"));
        }else {
            item.setIsSelected(false);
            holder.ivSelected.setVisibility(View.INVISIBLE);
            holder.ivBackground.setBackgroundColor(Color.parseColor("#00ffffff"));
        }

        holder.rltyItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.isSelected()){
                    item.setIsSelected(false);
                    item.setFollowed(0);
                    selectedNumber -= 1;
                    holder.ivSelected.setVisibility(View.INVISIBLE);
                    holder.ivBackground.setBackgroundColor(Color.parseColor("#00ffffff"));
                    if(isEdit){
                        unFollowed(type-1, item.getId(), item.getTag_id());
                    }
                }else {
                    item.setIsSelected(true);
                    item.setFollowed(1);
                    selectedNumber += 1;
                    holder.ivSelected.setVisibility(View.VISIBLE);
                    holder.ivBackground.setBackgroundColor(Color.parseColor("#19000000"));
                    if(isEdit){
                        followed(type-1, item.getId(), item.getTag_id());
                    }
                }
                if(mOnItemClickListener != null){
                    mOnItemClickListener.onItemClick(selectedNumber);
                    mOnItemClickListener.onItemClick(selectedNumber,type - 1);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mList == null){
            return 0;
        }
        return mList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(int snb);
        void onItemClick(int snb, int pagePosition);
    }

    //批量关注  position 0是品牌1是品类
    private void followed(int position, String id, String tag_id){
        if(position == 0){
            new RetrofitRequest<>(ApiRequest.getApiShiji().postFollowBrands(tag_id)).handRequest(new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    if(msg.isSuccess()){
                    }else {
                        Toast.makeText(mContext, msg.message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            new RetrofitRequest<>(ApiRequest.getApiShiji().postFollowCategorys(id)).handRequest(new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    if(msg.isSuccess()){
                    }else {
                        Toast.makeText(mContext, msg.message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    //取消关注
    private void unFollowed(int position, String id, String tag_id){
        if(position == 0){
            new RetrofitRequest<>(ApiRequest.getApiShiji().postUnFollowBrands(tag_id)).handRequest(new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    if(msg.isSuccess()){
                    }else {
                        Toast.makeText(mContext, msg.message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            new RetrofitRequest<>(ApiRequest.getApiShiji().postUnFollowCategorys(id)).handRequest(new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    if(msg.isSuccess()){
                    }else {
                        Toast.makeText(mContext, msg.message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    class GuideItemViewHolder extends RecyclerView.ViewHolder{
        NetworkImageView ivLogo;
        TextView tvName;
        TextView tvNoLog;
        ImageView ivSelected;
        RelativeLayout rltyItem;
        RelativeLayout rltyHasLog;
        ImageView ivBackground;

        public GuideItemViewHolder(View itemView) {
            super(itemView);
            ivLogo = (NetworkImageView) itemView.findViewById(R.id.image_logo);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvNoLog = (TextView) itemView.findViewById(R.id.tv_log_null);
            ivSelected = (ImageView) itemView.findViewById(R.id.iv_guid_item_selected);
            rltyItem = (RelativeLayout) itemView.findViewById(R.id.rlty_item);
            rltyHasLog = (RelativeLayout) itemView.findViewById(R.id.has_log_root);
            ivBackground = (ImageView) itemView.findViewById(R.id.iv_background);
        }
    }
}

