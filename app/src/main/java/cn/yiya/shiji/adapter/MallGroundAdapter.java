package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.ImageViewZoomActivity;
import cn.yiya.shiji.entity.navigation.PlanesInfo;

/**
 * Created by Tom on 2016/4/13.
 */
public class MallGroundAdapter  extends RecyclerView.Adapter<MallGroundAdapter.MallGroundAdapterViewHolder>{
    private Context mContext;
    private ArrayList<PlanesInfo> mlist;

    public MallGroundAdapter(Context mContext ){
        this.mContext = mContext;
    }

    public ArrayList<PlanesInfo> getMlist() {
        return mlist;
    }

    public void setMlist(ArrayList<PlanesInfo> mlist) {
        this.mlist = mlist;
    }

    @Override
    public MallGroundAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MallGroundAdapterViewHolder(LayoutInflater.from(mContext).inflate(R.layout.mall_ground_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MallGroundAdapterViewHolder holder, int position) {
       final PlanesInfo info = mlist.get(position);

        holder.tvGround.setText(info.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ImageViewZoomActivity.class);
                intent.putExtra("imageUrl", info.getImage());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mlist == null){
            return 0;
        }else {
            return mlist.size();
        }
    }


    class MallGroundAdapterViewHolder extends RecyclerView.ViewHolder{
        TextView tvGround;
        public MallGroundAdapterViewHolder(View itemView) {
            super(itemView);
            tvGround = (TextView) itemView.findViewById(R.id.tv_ground);
        }
    }

}
