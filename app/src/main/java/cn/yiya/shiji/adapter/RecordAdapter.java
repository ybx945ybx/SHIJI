package cn.yiya.shiji.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.yiya.shiji.R;

/**
 * Created by jerry on 2015/12/28.
 */
public class RecordAdapter extends BaseAdapter {
    private List<String> list;
    private Context context;
    private LayoutInflater mLInflater;
    private int code;
    public RecordAdapter(List<String> list, Context context, int code) {
        this.list = list;
        this.context = context;
        this.code = code;
        mLInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if(list == null ){
            return 0;
        }
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if(convertView == null){
            vh = new ViewHolder();
            convertView = mLInflater.inflate(R.layout.search_record_item, null);
            vh.mTextView = (TextView)convertView.findViewById(R.id.tv_record);
            vh.imageView = (ImageView)convertView.findViewById(R.id.iv_record);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder)convertView.getTag();
        }
        if(code == 1){
            vh.imageView.setVisibility(View.VISIBLE);
        } else {
            vh.imageView.setVisibility(View.INVISIBLE);
        }
        vh.mTextView.setText(list.get(position));
        vh.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onClickListener(list.get(position));
                }
            }
        });
        return convertView;
    }
    class ViewHolder{
        TextView mTextView;
        ImageView imageView;
    }
    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
    public interface  OnItemClickListener{
        public void onClickListener(String record);
    }
    public void setList(List<String> list){
        this.list = list;
    }
}
