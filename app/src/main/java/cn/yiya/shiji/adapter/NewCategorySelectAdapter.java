package cn.yiya.shiji.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.HotCategoryObject;

/**
 * Created by Amy on 2016/6/23.
 */
public class NewCategorySelectAdapter extends RecyclerView.Adapter<NewCategorySelectAdapter.TypeViewHolder> {

    private Context mContext;
    private List<HotCategoryObject.SecondItem> mList;
    private OnItemClickListener mListener;

    public NewCategorySelectAdapter(Context mContext, List<HotCategoryObject.SecondItem> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public TypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TypeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_category_select, parent, false));
    }

    public void setOnItemClickListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onBindViewHolder(TypeViewHolder holder, int position) {
        final HotCategoryObject.SecondItem item = mList.get(position);
        holder.tvType.setText(item.getName());
        holder.llType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.OnItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class TypeViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llType;
        private TextView tvType;

        public TypeViewHolder(View itemView) {
            super(itemView);
            llType = (LinearLayout) itemView.findViewById(R.id.ll_type);
            tvType = (TextView) itemView.findViewById(R.id.tv_type);
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(HotCategoryObject.SecondItem item);
    }
}
