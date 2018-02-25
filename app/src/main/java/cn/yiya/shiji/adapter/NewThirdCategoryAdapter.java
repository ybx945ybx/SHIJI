package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.NewGoodsListActivity;
import cn.yiya.shiji.entity.HotCategoryObject;

/**
 * Created by Amy on 2016/6/20.
 */
public class NewThirdCategoryAdapter extends RecyclerView.Adapter<NewThirdCategoryAdapter.ThirdViewHolder> {
    private List<HotCategoryObject.SecondItem.ThirdItem> mList;
    private Context mContext;
    private int secondId;
    private String typeName;

    public NewThirdCategoryAdapter(Context mContext, List<HotCategoryObject.SecondItem.ThirdItem> mList, int secondId, String typeName) {
        this.mContext = mContext;
        this.mList = mList;
        this.secondId = secondId;
        this.typeName = typeName;
    }


    @Override
    public ThirdViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ThirdViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_third_category, parent, false));
    }

    @Override
    public void onBindViewHolder(ThirdViewHolder holder, int position) {
        final HotCategoryObject.SecondItem.ThirdItem item = mList.get(position);
        holder.tvThirdCategory.setText(item.getName());
        holder.rlThirdCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NewGoodsListActivity.class);
                intent.putExtra("source", 1002);
                intent.putExtra("id", secondId);
                intent.putExtra("title", item.getName());
                intent.putExtra("typeName", typeName);
                if (item.getName().equals("全部"))
                    intent.putExtra("categoryid", "");
                else
                    intent.putExtra("categoryid", String.valueOf(item.getId()));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class ThirdViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rlThirdCategory;
        private TextView tvThirdCategory;

        public ThirdViewHolder(View itemView) {
            super(itemView);
            tvThirdCategory = (TextView) itemView.findViewById(R.id.tv_third_category);
            rlThirdCategory = (RelativeLayout) itemView.findViewById(R.id.rl_third_category);
        }
    }
}
