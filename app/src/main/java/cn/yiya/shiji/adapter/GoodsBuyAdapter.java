package cn.yiya.shiji.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.GoodsDetailEntity;
import cn.yiya.shiji.entity.GoodsParamsInfo;
import cn.yiya.shiji.entity.GoodsSortInfo;
import cn.yiya.shiji.entity.SizeTableEntity;
import cn.yiya.shiji.views.FullyGridLayoutManager;
import cn.yiya.shiji.views.FullyLinearLayoutManager;
import cn.yiya.shiji.views.GoodsSizeDialog;

/**
 * 加入购物车弹层 商品参数 Adapter
 * Created by Amy on 2016/11/17.
 */

public class GoodsBuyAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<GoodsParamsInfo> mList;

    public GoodsBuyAdapter(Context mContext, ArrayList<GoodsParamsInfo> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_goods_check_params, null);
            holder = new ViewHolder();

            holder.tvParams = (TextView) convertView.findViewById(R.id.tv_params);
            holder.tflParams = (TagFlowLayout) convertView.findViewById(R.id.tfl_params);
            holder.rlCheckSize = (RelativeLayout) convertView.findViewById(R.id.rl_check_size);
            holder.rvSize = (RecyclerView) convertView.findViewById(R.id.rv_size);
            holder.rvSize.setLayoutManager(new FullyLinearLayoutManager(mContext));
            holder.rvSize.setNestedScrollingEnabled(false);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final GoodsParamsInfo paramsInfo = mList.get(position);
        holder.tvParams.setText(paramsInfo.getKey());
        holder.rvSize.setVisibility(View.GONE);
        if (paramsInfo.getKey().toLowerCase().equals("size") && paramsInfo.getSize() != null) {
            holder.rlCheckSize.setVisibility(View.VISIBLE);
        } else {
            holder.rlCheckSize.setVisibility(View.GONE);
        }

        com.zhy.view.flowlayout.TagAdapter mAdapter = null;

        if (paramsInfo.getKey().toLowerCase().equals("color")) {
            final ViewHolder finalHolder1 = holder;
            mAdapter = new com.zhy.view.flowlayout.TagAdapter<GoodsSortInfo>(paramsInfo.getValueList()) {
                @Override
                public View getView(FlowLayout parent, int pos, GoodsSortInfo info) {
                    View view = LayoutInflater.from(mContext).inflate(R.layout.goods_cart_tagflow_color_item, finalHolder1.tflParams, false);
                    SimpleDraweeView ivColorCover = (SimpleDraweeView) view.findViewById(R.id.iv_color_cover);
                    TextView tvColor = (TextView) view.findViewById(R.id.tv_color);
                    ivColorCover.setImageURI(info.getCover());
                    tvColor.setText(info.getTitle());
                    view.setTag(tvColor);
                    if (TextUtils.isEmpty(info.getCover())) {
                        ivColorCover.setVisibility(View.GONE);
                    }
                    return view;
                }
            };

        } else {
            final ViewHolder finalHolder = holder;
            mAdapter = new com.zhy.view.flowlayout.TagAdapter<GoodsSortInfo>(paramsInfo.getValueList()) {
                @Override
                public View getView(FlowLayout parent, int pos, GoodsSortInfo info) {
                    TextView tvText = (TextView) LayoutInflater.from(mContext).inflate(R.layout.goods_cart_tagflow_item, finalHolder.tflParams, false);
                    tvText.setText(info.getTitle());
                    if (paramsInfo.getKey().toLowerCase().equals("size") && info.isChecked()) {
                        showMatchSize(finalHolder, info.getSizemap());
                    }
                    return tvText;
                }
            };
        }
        holder.tflParams.setAdapter(mAdapter);
        setTagState(holder, paramsInfo.getValueList(), paramsInfo.getKey().toLowerCase().equals("color"));

        holder.tflParams.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int pos, FlowLayout parent) {
                //无效 不点击
                if (!paramsInfo.getValueList().get(pos).isValid()) return false;

                //对点击的选项的checked值取反
                for (int i = 0; i < paramsInfo.getValueList().size(); i++) {
                    paramsInfo.getValueList().get(i).setChecked(i == pos ? !paramsInfo.getValueList().get(i).isChecked() : false);

                }
                if (checkListener != null) checkListener.onCheck(position, pos);
                return false;
            }
        });

        //查看尺码表
        holder.rlCheckSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSizeTable(paramsInfo.getSize().getTables(), paramsInfo.getSize_match_pos());
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tvParams;
        TagFlowLayout tflParams;

        RelativeLayout rlCheckSize;
        RecyclerView rvSize;
    }

    private void setTagState(ViewHolder holder, ArrayList<GoodsSortInfo> paramList, boolean isColor) {
        for (int i = 0; i < paramList.size(); i++) {
            if (paramList.get(i).isValid()) {
                //有效
                holder.tflParams.getChildAt(i).setEnabled(true);
                holder.tflParams.getChildAt(i).setSelected(paramList.get(i).isChecked());
                if (isColor) {
                    ((ViewGroup) ((com.zhy.view.flowlayout.TagView) holder.tflParams.getChildAt(i)).getTagView()).getChildAt(1).setEnabled(true);
                }

            } else {
                //无效
                holder.tflParams.getChildAt(i).setEnabled(false);
                paramList.get(i).setChecked(false);
                holder.tflParams.getChildAt(i).setSelected(false);
                if (isColor) {
                    ((ViewGroup) ((com.zhy.view.flowlayout.TagView) holder.tflParams.getChildAt(i)).getTagView()).getChildAt(1).setEnabled(false);
                }
            }
        }
    }

    /**
     * 查看尺码表
     */
    private void checkSizeTable(List<GoodsDetailEntity.SizeEntity.TablesEntity> tables, int matchPos) {
        new GoodsSizeDialog(mContext, tables, matchPos).build().show();
    }

    /**
     * 显示匹配的尺码表
     *
     * @param holder
     * @param sizeMap
     */
    private void showMatchSize(ViewHolder holder, final ArrayList<SizeTableEntity> sizeMap) {
        if (sizeMap == null || sizeMap.size() == 0) {
            holder.rvSize.setVisibility(View.GONE);
        } else {
            FullyGridLayoutManager layoutManager = new FullyGridLayoutManager(mContext, getGridSpan(sizeMap));
            holder.rvSize.setLayoutManager(layoutManager);
            holder.rvSize.setAdapter(new SizeAdapter(sizeMap));
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    String key = sizeMap.get(position).getName().replaceAll("\\(+?", "").replaceAll("\\)+?", "");
                    return key.length() % 2 == 0 ? key.length() / 2 : key.length() / 2 + 1;
                }
            });
            holder.rvSize.setVisibility(View.VISIBLE);
        }
    }

    private int getGridSpan(ArrayList<SizeTableEntity> list) {
        int gridSpan;
        String headerAppend = "";
        for (int i = 0; i < list.size(); i++) {
            headerAppend += list.get(i).getName().replaceAll("\\(+?", "").replaceAll("\\)+?", "");
        }
        gridSpan = headerAppend.length() % 2 == 0 ? headerAppend.length() / 2 : (headerAppend.length() / 2 + 1);
        return gridSpan;
    }

    /**
     * 匹配尺码表Adapter
     */
    class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.SizeViewHolder> {
        ArrayList<SizeTableEntity> maps;

        public SizeAdapter(ArrayList<SizeTableEntity> maps) {
            this.maps = maps;
        }

        @Override
        public SizeAdapter.SizeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SizeAdapter.SizeViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_goods_buy_size, parent, false));
        }

        @Override
        public void onBindViewHolder(SizeAdapter.SizeViewHolder holder, int position) {
            if (maps.get(position).isMark()) {
                holder.itemView.setBackgroundColor(Color.parseColor("#ebebeb"));
            }
            holder.tvTitle.setText(maps.get(position).getName());
            holder.tvSize.setText(maps.get(position).getValue());
        }

        @Override
        public int getItemCount() {
            return maps == null ? 0 : maps.size();
        }

        class SizeViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle;
            TextView tvSize;

            public SizeViewHolder(View itemView) {
                super(itemView);
                tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
                tvSize = (TextView) itemView.findViewById(R.id.tv_size);
            }
        }
    }

    OnCheckListener checkListener;

    public void setOnCheckListener(OnCheckListener checkListener) {
        this.checkListener = checkListener;
    }

    public interface OnCheckListener {
        void onCheck(int paramPosition, int checkPosition);
    }
}
