package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.NewSingleBrandActivity;
import cn.yiya.shiji.entity.HotBrandsItem;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.SquareImageView;

/**
 * Created by tomyang on 2015/9/10.
 * 热门品牌适配器
 */
public class BransItemAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<HotBrandsItem> zlist;
    private LayoutInflater mInflater = null;
    private String types = "";
    private PopularBrandsAdapter mPopularBrandsAdapter;


    public BransItemAdapter(Context context){
        zlist = new ArrayList<>();
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (zlist == null) {
            return 0;
        }
        return zlist.size();
    }

    @Override
    public Object getItem(int position) {
        return zlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        // 设置布局管理器

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.hot_banner_item, null);
            vh = new ViewHolder();
            vh.brandsName = (TextView)convertView.findViewById(R.id.brands_name);
            vh.brandsType1 = (TextView)convertView.findViewById(R.id.brands_type1);
            vh.brandsImg = (SquareImageView)convertView.findViewById(R.id.brands_image);
            vh.linearLayout = (LinearLayout)convertView.findViewById(R.id.linearlayout);
            vh.tvcnname = (TextView)convertView.findViewById(R.id.cn_name);
            vh.mRecyclerView = (RecyclerView)convertView.findViewById(R.id.recyclerview_horizontal);
            vh.mRecyclerView.setLayoutManager(layoutManager);
            vh.linearLayout.setVisibility(View.GONE);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final HotBrandsItem item = zlist.get(position);
        vh.brandsName.setText(item.getName());
        if(item.getTypes() != null && item.getTypes().length>0){
            for(int i=0;i<item.getTypes().length-1;i++){
                types = types+ (item.getTypes()[i]+",   ");
            }
            vh.brandsType1.setText(types+item.getTypes()[item.getTypes().length-1]);
            types = "";
        } else {
                types = "";
                vh.brandsType1.setVisibility(View.GONE);
        }
        if(item.getMlist() == null){
            vh.mRecyclerView.setVisibility(View.GONE);
        }else{
            mPopularBrandsAdapter = new PopularBrandsAdapter(context, item.getMlist());
            vh.mRecyclerView.setAdapter(mPopularBrandsAdapter);
            mPopularBrandsAdapter.setOnItemClickListener(new PopularBrandsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick() {
                    Intent intent = new Intent(context, NewSingleBrandActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("brand_id", item.getId());
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });

            if(item.getCn_name() != null){
                vh.tvcnname.setText(item.getCn_name());
            }
        }
        Netroid.displayImage(Util.transfer(item.getLogo()),vh.brandsImg);
        return convertView;

    }
    class ViewHolder {

        TextView brandsName,tvcnname;
        TextView brandsType1;
        SquareImageView brandsImg;
        LinearLayout linearLayout;
        RecyclerView mRecyclerView;
    }
    public ArrayList<HotBrandsItem> getHotList(){
        return zlist;
    }
    public void setHotList(ArrayList<HotBrandsItem> list){
        zlist =list;
    }
}
