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
 * Created by Tom on 2016/6/23.
 */
public class CommonTagAdapter extends RecyclerView.Adapter<CommonTagAdapter.CommonTagViewHolder> {

    private Context mContext;
    private ArrayList<TagProduceItem> mList;

    public CommonTagAdapter(Context mContext){
        this.mContext = mContext;
    }

    public ArrayList<TagProduceItem> getmList() {
        return mList;
    }

    public void setmList(ArrayList<TagProduceItem> mList) {
        this.mList = mList;
    }

    @Override
    public CommonTagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommonTagViewHolder(LayoutInflater.from(mContext).inflate(R.layout.work_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(CommonTagViewHolder holder, int position) {
        final TagProduceItem item = mList.get(position);

        holder.content.setText(item.getContent());
        holder.like.setText("" + item.getLike_count());
        holder.comment.setText("" + item.getComment_count());
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.image.getLayoutParams();
        layoutParams.width = (SimpleUtils.getScreenWidth(mContext) - SimpleUtils.dp2px(mContext, 30))/2;
        layoutParams.height = layoutParams.width;
        holder.image.setLayoutParams(layoutParams);
        if (!TextUtils.isEmpty(item.getImage())) {
//            Netroid.displayImage(Util.transfer(item.getImage()), holder.image);
            holder.image.setImageURI(Util.transfer(item.getImage()));
        }
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NewWorkDetailsActivity.class);
                intent.putExtra("work_id",item.getWork_id());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mList == null){
            return 0;
        }else {
            return mList.size();
        }
    }

    class CommonTagViewHolder extends RecyclerView.ViewHolder{
        TextView content, like, comment;
        SimpleDraweeView image;

        public CommonTagViewHolder(View itemView) {
            super(itemView);
            image = (SimpleDraweeView) itemView.findViewById(R.id.grid_item_work_image);
            content = (TextView) itemView.findViewById(R.id.grid_item_work_content);
            like = (TextView) itemView.findViewById(R.id.grid_item_work_like);
            comment = (TextView) itemView.findViewById(R.id.grid_item_work_comment);
        }
    }
}


