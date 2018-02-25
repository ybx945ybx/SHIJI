package cn.yiya.shiji.adapter;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.HotCategoryObject;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.views.FullyLinearLayoutManager;

/**
 * Created by Amy on 2016/6/20.
 */
public class NewSecondCategoryAdapter extends RecyclerView.Adapter<NewSecondCategoryAdapter.SecondViewHolder> {
    private List<HotCategoryObject.SecondItem> mList;
    private Context mContext;
    private String typeName;
    private int clickPosition = -1;
    private int lastClickPosition = -1;

    private boolean[] bVisable;

    public NewSecondCategoryAdapter(Context mContext, String typeName) {
        this.mContext = mContext;
        this.mList = new ArrayList<>();
        this.typeName = typeName;
    }

    public void setList(List<HotCategoryObject.SecondItem> mList) {
        this.mList = mList;
        bVisable = new boolean[mList.size()];
    }

    @Override
    public SecondViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SecondViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_second_category, parent, false));
    }

    @Override
    public void onBindViewHolder(final SecondViewHolder holder, final int position) {

        final HotCategoryObject.SecondItem item = mList.get(position);
        Netroid.displayImage(item.getIcon(), holder.ivSecondCategory, R.drawable.backgroud_white);
        holder.rvThirdCategory.setLayoutManager(new FullyLinearLayoutManager(mContext));
        List<HotCategoryObject.SecondItem.ThirdItem> thirdlist = item.getList();
        NewThirdCategoryAdapter adapter = new NewThirdCategoryAdapter(mContext, thirdlist, item.getId(),typeName);
        holder.rvThirdCategory.setAdapter(adapter);
        holder.rvThirdCategory.setItemAnimator(new DefaultItemAnimator());
        if (position == clickPosition) {
            //三级分类可见
            if (bVisable[position]) {
                holder.rvThirdCategory.setVisibility(View.GONE);
                bVisable[position] = false;
            } else {
                holder.rvThirdCategory.setVisibility(View.VISIBLE);
                bVisable[position] = true;
            }

        } else {
            //收起三级分类
            bVisable[position] = false;
            holder.rvThirdCategory.setVisibility(View.GONE);
        }
        holder.ivSecondCategory.setTag(position);
        holder.ivSecondCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                lastClickPosition = clickPosition;
                clickPosition = (Integer) view.getTag();
                if (bVisable[clickPosition]) {
                    if (mlistener != null) mlistener.OnCollapse(position);
                } else {
                    if (mlistener != null) mlistener.OnExpand(position);
                }
                holder.ivSecondCategory.post(new Runnable() {
                    @Override
                    public void run() {
                        if (clickPosition == lastClickPosition) {
                            notifyItemChanged(clickPosition);
                        } else {
                            notifyItemChanged(clickPosition);
                            if (lastClickPosition != -1) {
                                holder.ivSecondCategory.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        notifyItemChanged(lastClickPosition);
                                    }
                                });

                            }
                        }
                    }
                });
            }
        });


    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class SecondViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivSecondCategory;
        private RecyclerView rvThirdCategory;

        public SecondViewHolder(View itemView) {
            super(itemView);
            ivSecondCategory = (ImageView) itemView.findViewById(R.id.iv_second_category);
            rvThirdCategory = (RecyclerView) itemView.findViewById(R.id.rv_third_category);
        }
    }

    private ExpandCollapseListener mlistener;

    public void setExpandCollapseListener(ExpandCollapseListener mlistener) {
        this.mlistener = mlistener;
    }

    public interface ExpandCollapseListener {
        void OnExpand(int position);

        void OnCollapse(int position);
    }
}
