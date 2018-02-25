package cn.yiya.shiji.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.RedPackageActivity;
import cn.yiya.shiji.entity.RedPackagrItem;
import cn.yiya.shiji.utils.Util;

/**
 * Created by tomyang on 2015/9/14.
 */
public class RedPackageAdapter extends RecyclerView.Adapter<RedPackageAdapter.RedPackageViewHolder> {
    private Context mContext;
    private ArrayList<RedPackagrItem> mList;
    private int type;
    public RedPackageAdapter(Context context,int type) {
        super();
        this.mContext = context;
        this.type = type;
    }

    public void setList(ArrayList<RedPackagrItem> mList) {
        this.mList = mList;
    }

    public ArrayList<RedPackagrItem> getList() {
        return mList;
    }

    @Override
    public RedPackageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RedPackageViewHolder(LayoutInflater.from(mContext).inflate(R.layout.red_package_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RedPackageViewHolder holder, int position) {
        final RedPackagrItem item = mList.get(position);

        holder.packageDes.setText(item.getDes());
        holder.handsel.setText(Util.FloatKeepZero(item.getHandsel()));
        holder.packageTimeLimit.setText(item.getStart_time() + "至 " + item.getEnd_time());
//        holder.packageSource.setText(item.getSource());
        holder.packageApply.setText(item.getApply());
        if(type == 2){
            holder.rltyRoot.setSelected(item.isSelected());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((RedPackageActivity)mContext).type == 2) {
                        Intent intent = new Intent();
                        String json = "";
                        if (!item.isSelected()) {
                            json = new Gson().toJson(item);
                        }
                        intent.putExtra("json",json);
                        ((RedPackageActivity)mContext).setResult(Activity.RESULT_OK, intent);
                        ((RedPackageActivity)mContext).finish();
                    }
                }
            });
        }
//        vh.ivSelected.setVisibility(item.isSelected()? View.VISIBLE : View.GONE);
//        vh.ivSelected.setVisibility(item.isClick()? View.VISIBLE : View.GONE);
//        if (item.isClick()) {
//            if(type == 1){
//                vh.ivSelected.setVisibility(View.INVISIBLE);
//            }else {
//                vh.ivSelected.setVisibility(View.VISIBLE);
//                vh.ivSelected.setSelected(item.isSelected());
//            }
//            vh.rltyRoot.setBackgroundResource(R.drawable.layout_selector_red);
//            vh.rmb.setTextColor(Color.parseColor("#ed5137"));
//            vh.handsel.setTextColor(Color.parseColor("#ed5137"));
//            convertView.setEnabled(true);
//            convertView.setClickable(true);
//            convertView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (((RedPackageActivity)mContext).type == 2) {
//                        Intent intent = new Intent();
//                        String json = "";
//                        if (!item.isSelected()) {
//                            json = new Gson().toJson(item);
//                        }
//                        intent.putExtra("json",json);
//                        ((RedPackageActivity)mContext).setResult(Activity.RESULT_OK, intent);
//                        ((RedPackageActivity)mContext).finish();
//                    }
//                }
//            });
//        } else {
//            vh.rltyRoot.setBackgroundResource(R.drawable.inselected_promo);
//            convertView.setEnabled(false);
//            convertView.setClickable(false);
//            vh.ivSelected.setVisibility(View.INVISIBLE);
//            vh.rmb.setTextColor(Color.parseColor("#c8c8c8"));
//            vh.handsel.setTextColor(Color.parseColor("#c8c8c8"));
//        }
    }

    @Override
    public int getItemCount() {
        if(mList == null){
            return 0;
        }
        return mList.size();
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder vh = null;
//        if(convertView == null){
//            convertView = View.inflate(parent.getContext(), R.layout.red_package_list_item,null);
//            vh = new ViewHolder();
//
//            convertView.setTag(vh);
//        }else{
//            vh = (ViewHolder)convertView.getTag();
//        }
//
//        return convertView;
//    }

    class RedPackageViewHolder extends RecyclerView.ViewHolder{

        TextView packageDes;
        TextView packageTimeLimit;
        //    TextView packageSource;
        TextView packageApply;
        TextView handsel;
        //    TextView rmb;
//    ImageView ivSelected;
        RelativeLayout rltyRoot;

        public RedPackageViewHolder(View itemView) {
            super(itemView);
            packageDes = (TextView)itemView.findViewById(R.id.package_des);
            packageTimeLimit = (TextView)itemView.findViewById(R.id.time_limit);
//        packageSource = (TextView)itemView.findViewById(R.id.package_source);
            packageApply = (TextView)itemView.findViewById(R.id.package_apply);
            handsel = (TextView)itemView.findViewById(R.id.handsel);
//        rmb = (TextView)itemView.findViewById(R.id.rmb);
//        ivSelected = (ImageView) itemView.findViewById(R.id.red_selected);
            rltyRoot = (RelativeLayout) itemView.findViewById(R.id.rlty_root);
        }
    }
}

