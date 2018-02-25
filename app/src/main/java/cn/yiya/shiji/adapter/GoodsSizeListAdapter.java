package cn.yiya.shiji.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.GoodsDetailEntity;
import cn.yiya.shiji.views.FullyLinearLayoutManager;

/**
 * 尺码表 List Adapter
 * Created by Amy on 2016/11/14.
 */

public class GoodsSizeListAdapter extends RecyclerView.Adapter<GoodsSizeListAdapter.ListViewGolder> {

    private Context mContext;
    private List<GoodsDetailEntity.SizeEntity.TablesEntity> mTables;
    private int matchPos = -1;

    public static final int MATCH_TYPE = 1;
    public static final int WRAP_TYPE = 2;

    public GoodsSizeListAdapter(Context context, List<GoodsDetailEntity.SizeEntity.TablesEntity> list, int matchPos) {
        this.mContext = context;
        this.mTables = list;
        this.matchPos = matchPos;
    }

    @Override
    public ListViewGolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case MATCH_TYPE:
                return new ListViewGolder(LayoutInflater.from(mContext).inflate(R.layout.item_dialog_goods_size, parent, false));
            case WRAP_TYPE:
                return new ListViewGolder(LayoutInflater.from(mContext).inflate(R.layout.item_dialog_goods_size_wrap, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(ListViewGolder holder, int position) {
        GoodsDetailEntity.SizeEntity.TablesEntity table = mTables.get(position);
        holder.tvTitle.setText(table.getTitle());
        List<List<String>> sizeList = new ArrayList<>(table.getData());
        sizeList.add(0, table.getHeader());
        GoodsSizeAdapter sizeAdapter = new GoodsSizeAdapter(mContext, sizeList,matchPos,holder.getItemViewType());
        holder.rvSize.setAdapter(sizeAdapter);
    }

    @Override
    public int getItemCount() {
        return mTables == null ? 0 : mTables.size();
    }

    @Override
    public int getItemViewType(int position) {
        List<List<String>> sizeList = mTables.get(position).getData();
        int length = matchPos < 0 ? sizeList.get(0).size() : sizeList.get(0).size() - 1;
        if (length < 5) {
            return MATCH_TYPE;
        } else {
            return WRAP_TYPE;
        }
    }

    class ListViewGolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        RecyclerView rvSize;

        public ListViewGolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            rvSize = (RecyclerView) itemView.findViewById(R.id.rv_size);
            rvSize.setLayoutManager(new FullyLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            rvSize.setNestedScrollingEnabled(false);
        }
    }

}
