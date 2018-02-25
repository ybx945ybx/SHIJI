package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.UserTagWorkActivity;
import cn.yiya.shiji.entity.TagItem;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.SimpleUtils;

/**
 * 我的标签适配器
 * tomyang 2016-6-23
 */

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.MyTagViewHolder> {
    private Context mContext;
    private ArrayList<TagItem> mList;
    private int user_id;
    private String user_name;

    public TagAdapter(Context context, int user_id, String user_name) {
        super();
        this.mContext = context;
        this.mList = new ArrayList<>();
        this.user_id = user_id;
        this.user_name = user_name;
    }

    public void setImageLayoutParams(ImageView imageView, String imagesUrl) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParams.width = (SimpleUtils.getScreenWidth(mContext) - SimpleUtils.dp2px(mContext, 30)) / 2;
        layoutParams.height = layoutParams.width;
        imageView.setLayoutParams(layoutParams);
        Netroid.displayImage(imagesUrl, imageView);
    }

    public void setImageArrayParams(ImageView[] imageViews, List<TagItem.Work> works) {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        LinearLayout.LayoutParams layoutParams0 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams0.width = (int) (displayMetrics.widthPixels - 12 * 3 * displayMetrics.density) / 2;
        layoutParams0.height = layoutParams0.width;

        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams1.width = (int) (layoutParams0.width - 4 * 2 * displayMetrics.density) / 3 - 10;
        layoutParams1.height = layoutParams1.width;

//        if(works.size()==1){
//            layoutParams0.height = layoutParams0.width-layoutParams1.width;
//
//            imageViews[0].setLayoutParams(layoutParams0);
//            Netroid.displayImage(works.get(0).getImage(), imageViews[0]);
//            for(int i=1;i<4;i++){
//                layoutParams1.rightMargin= (int) (4*displayMetrics.density);
//                imageViews[i].setLayoutParams(layoutParams1);
//                imageViews[i].setVisibility(View.VISIBLE);
//            }
//        }else if(works == null || works.size() == 0) {
//            for(int i=0 ; i < 4 ;i++){
//                imageViews[i].setVisibility(View.GONE);
//            }
//        }else{
//            layoutParams0.height = layoutParams0.width-layoutParams1.width;
//            imageViews[0].setLayoutParams(layoutParams0);
//            Netroid.displayImage(works.get(0).getImage(), imageViews[0]);
//            for(int i=1;i<4;i++){
//                if(i<3)
//                    layoutParams1.rightMargin= (int) (4*displayMetrics.density);
//                imageViews[i].setLayoutParams(layoutParams1);
//                imageViews[i].setVisibility(View.VISIBLE);
//                if(works!=null&&works.size()>i){
//                    Netroid.displayImage(Util.transfer(works.get(i).getImage()), imageViews[i]);
//                }
//            }
//
//        }
    }

    public void setList(ArrayList<TagItem> mList) {
        this.mList = mList;
    }

    public ArrayList<TagItem> getList() {
        return mList;
    }

    @Override
    public MyTagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyTagViewHolder(LayoutInflater.from(mContext).inflate(R.layout.grid_item_tag, parent, false));
    }

    @Override
    public void onBindViewHolder(MyTagViewHolder holder, int position) {
        final TagItem item = mList.get(position);

        holder.name.setText(item.getContent());

        if (item.getWorks() != null && item.getWorks().size() > 0) {
            setImageLayoutParams(holder.image, item.getWorks().get(0).getImage());
        }

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(mContext, UserTagWorkActivity.class);
                intent1.putExtra("tag_id", item.getTag_id());
                intent1.putExtra("tag_name", item.getContent());
                intent1.putExtra("tag_image", item.getImage());
                intent1.putExtra("user_id", user_id);
                intent1.putExtra("user_name", user_name);
                mContext.startActivity(intent1);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

    class MyTagViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;

        public MyTagViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.grid_item_tag_name);
            image = (ImageView) itemView.findViewById(R.id.grid_item_tag_image0);
        }
    }

}

