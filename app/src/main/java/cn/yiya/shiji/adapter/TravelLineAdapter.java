package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.HotLineActivity;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.entity.navigation.HotLineInfo;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.utils.SimpleUtils;

/**
 * Created by Tom on 2016/3/22.
 */
public class TravelLineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private ArrayList<HotLineInfo> mLineList;

    public TravelLineAdapter(Context mContext, ArrayList<HotLineInfo> list){
        this.mContext = mContext;
        this.mLineList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TravelLineViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((TravelLineViewHolder)holder).bindData(mLineList.get(position));
    }

    @Override
    public int getItemCount() {
        if(mLineList == null){
            return 0;
        }
        return mLineList.size();
    }

    protected class TravelLineViewHolder extends RecyclerView.ViewHolder{
        ImageView ivLine;
        TextView tvLineBrief;

        public TravelLineViewHolder(ViewGroup group) {
            super(LayoutInflater.from(group.getContext()).inflate(R.layout.travle_line_item, group, false));

            ivLine = (ImageView) itemView.findViewById(R.id.iv_line);
            tvLineBrief = (TextView) itemView.findViewById(R.id.tv_line_brief);
        }

        public void bindData(final HotLineInfo info) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.width = SimpleUtils.getScreenWidth(mContext) - SimpleUtils.dp2px(mContext,32);
            layoutParams.height = layoutParams.width * 191/288;
            ivLine.setLayoutParams(layoutParams);
            ivLine.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Netroid.displayImage(info.getCover(), ivLine);
            tvLineBrief.setText(info.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!NetUtil.IsInNetwork(mContext)){
                        Toast.makeText(mContext, Configration.OFF_LINE_TIPS, Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        Intent intent = new Intent(mContext, HotLineActivity.class);
                        String lineInfo = new Gson().toJson(info);
                        intent.putExtra("url", info.getUrl());
                        intent.putExtra("name", info.getName());
                        intent.putExtra("lineInfo", lineInfo);
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }
}

