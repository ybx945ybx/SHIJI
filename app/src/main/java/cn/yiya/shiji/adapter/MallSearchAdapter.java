package cn.yiya.shiji.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.duowan.mobile.netroid.image.NetworkImageView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.HotMallSortItem;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.Util;

/**
 * Created by jerryzhang  on 2015/10/29.
 */
public class MallSearchAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater mLInflaster;
    private ArrayList<HotMallSortItem> nlist;
    public MallSearchAdapter(Context context) {
        this.nlist = new ArrayList<>();
        this.context =context;
        this.mLInflaster = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if(nlist == null ){
            return  0;
        }
        return nlist.size();
    }

    @Override
    public Object getItem(int position) {
        return nlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vHolder =null ;
        if(convertView == null){
            convertView =mLInflaster.inflate(R.layout.layout_mall_sort_item, null);
            vHolder = new ViewHolder();
            vHolder.tv = (TextView)convertView.findViewById(R.id.txt);
            vHolder.iv = (NetworkImageView)convertView.findViewById(R.id.imageview);
            vHolder.rl = (RelativeLayout)convertView.findViewById(R.id.relativelayout);
            convertView.setTag(vHolder);
        } else {
            vHolder = (ViewHolder) convertView.getTag();
        }
        HotMallSortItem mitem = nlist.get(position);
        vHolder.tv.setText(mitem.getTitle());                                 // 设置商城名称
        Netroid.displayImage(Util.transfer(mitem.getLogo()), vHolder.iv);
        return convertView;
    }
    class ViewHolder{
        TextView tv;
        NetworkImageView iv;
        RelativeLayout rl;
    }
    public void setList(ArrayList<HotMallSortItem> list){
        this.nlist = list;
    }
    public ArrayList<HotMallSortItem> getList(){
        return nlist;
    }
    public void updateListView(ArrayList<HotMallSortItem> list){
        this.nlist = list;
        notifyDataSetChanged();
    }

}
