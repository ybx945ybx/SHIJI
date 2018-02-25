package cn.yiya.shiji.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.utils.SimpleUtils;

/**
 * 尺码表Adapter
 * Created by Amy on 2016/11/14.
 */

public class GoodsSizeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<List<String>> mList;
    private int matchPos = -1;
    private int mViewType;

    public static final int MATCH_TYPE = 1;
    public static final int WRAP_TYPE = 2;


    public GoodsSizeAdapter(Context context, List<List<String>> list, int matchPos, int viewtype) {
        this.mContext = context;
        this.mList = list;
        this.matchPos = matchPos;
        this.mViewType = viewtype;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case MATCH_TYPE:
                return new SizeMatchViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_goods_size_match, parent, false));
            case WRAP_TYPE:
                return new SizeWrapViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_goods_size_wrap, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        List<String> item = mList.get(position);

        if(holder.getItemViewType()==MATCH_TYPE){
            SizeMatchViewHolder viewHolder= (SizeMatchViewHolder) holder;
            if (position == 0) {
                viewHolder.llLayout.setBackgroundColor(Color.parseColor("#ebebeb"));
            } else {
                viewHolder.llLayout.setBackgroundColor(Color.parseColor("#ffffff"));
            }
            for (int i = 0; i < item.size(); i++) {
                viewHolder.txtList.get(i).setText(item.get(i));
                viewHolder.txtList.get(i).setVisibility(View.VISIBLE);
                if (matchPos >= 0 && i == matchPos) {
                    viewHolder.txtList.get(i).setVisibility(View.GONE);
                }
            }

        }else if(holder.getItemViewType()==WRAP_TYPE){
            SizeWrapViewHolder viewHolder= (SizeWrapViewHolder) holder;
            if (position == 0) {
                viewHolder.llLayout.setBackgroundColor(Color.parseColor("#ebebeb"));
            } else {
                viewHolder.llLayout.setBackgroundColor(Color.parseColor("#ffffff"));
            }
            for (int i = 0; i < item.size(); i++) {
                viewHolder.txtList.get(i).setText(item.get(i));
                viewHolder.txtList.get(i).setVisibility(View.VISIBLE);
                if (matchPos >= 0 && i == matchPos) {
                    viewHolder.txtList.get(i).setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mViewType;
    }


    class SizeMatchViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llLayout;
       TextView tvTxt1,tvTxt2,tvTxt3,tvTxt4,tvTxt5;
        List<TextView> txtList = new ArrayList<>();
        View viewline;

        public SizeMatchViewHolder(View itemView) {
            super(itemView);
            llLayout = (LinearLayout) itemView.findViewById(R.id.ll_layout);
            viewline = itemView.findViewById(R.id.viewline);
            tvTxt1 = (TextView) itemView.findViewById(R.id.tv_txt1);
            tvTxt2 = (TextView) itemView.findViewById(R.id.tv_txt2);
            tvTxt3 = (TextView) itemView.findViewById(R.id.tv_txt3);
            tvTxt4 = (TextView) itemView.findViewById(R.id.tv_txt4);
            tvTxt5 = (TextView) itemView.findViewById(R.id.tv_txt5);
            txtList.add(tvTxt1);
            txtList.add(tvTxt2);
            txtList.add(tvTxt3);
            txtList.add(tvTxt4);
            txtList.add(tvTxt5);
        }
    }

    class SizeWrapViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llLayout;
        List<TextView> txtList = new ArrayList<>();
        View viewline;

        public SizeWrapViewHolder(View itemView) {
            super(itemView);
            llLayout = (LinearLayout) itemView.findViewById(R.id.ll_layout);
            viewline = itemView.findViewById(R.id.viewline);

            for (int i = 0; i < mList.get(0).size(); i++) {
                TextView textView = new TextView(mContext);
                textView.setWidth(SimpleUtils.dp2px(mContext, 76));
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(Color.parseColor("#212121"));
                textView.setTextSize(10);
                textView.setVisibility(View.GONE);
                llLayout.addView(textView);
                txtList.add(textView);

            }
        }
    }
}
