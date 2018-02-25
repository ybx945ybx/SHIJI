package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.NewWorkDetailsActivity;
import cn.yiya.shiji.entity.TagProduceItem;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;

/**
 * Created by chenjian on 2015/10/18.
 */
public class BrandsWorkAdapter extends RecyclerView.Adapter<BrandsWorkAdapter.WorkViewHolder> {

    private ArrayList<TagProduceItem> mList;
    private Context mContext;

    public BrandsWorkAdapter(ArrayList<TagProduceItem> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    public ArrayList<TagProduceItem> getmList() {
        return mList;
    }


    @Override
    public WorkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WorkViewHolder(LayoutInflater.from(mContext).inflate(R.layout.work_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(WorkViewHolder holder, int position) {

        final TagProduceItem info = mList.get(position);

        holder.tvContent.setText(info.getContent());
        holder.tvLike.setText("" + info.getLike_count());
        holder.tvComment.setText("" + info.getComment_count());

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.ivPhoto.getLayoutParams();
        layoutParams.width = (SimpleUtils.getScreenWidth(mContext) - SimpleUtils.dp2px(mContext, 30)) / 2;
        layoutParams.height = layoutParams.width;
        holder.ivPhoto.setLayoutParams(layoutParams);

        if (!TextUtils.isEmpty(info.getImage())) {
            holder.ivPhoto.setImageURI(Util.transferCropImage(info.getImage(), layoutParams.width));
//            Netroid.displayImage(Util.transferCropImage(info.getImage(), layoutParams.width), holder.ivPhoto);
        }

        holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NewWorkDetailsActivity.class);
                intent.putExtra("work_id", info.getWork_id());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class WorkViewHolder extends RecyclerView.ViewHolder {

        TextView tvContent, tvLike, tvComment;
        SimpleDraweeView ivPhoto;

        public WorkViewHolder(View itemView) {
            super(itemView);
            tvContent = (TextView) itemView.findViewById(R.id.grid_item_work_content);
            tvLike = (TextView) itemView.findViewById(R.id.grid_item_work_like);
            tvComment = (TextView) itemView.findViewById(R.id.grid_item_work_comment);
            ivPhoto = (SimpleDraweeView) itemView.findViewById(R.id.grid_item_work_image);
        }
    }
}

