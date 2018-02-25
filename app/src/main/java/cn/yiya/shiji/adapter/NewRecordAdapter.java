package cn.yiya.shiji.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.yiya.shiji.R;

/**
 * Created by Amy on 2016/6/17.
 */
public class NewRecordAdapter extends BaseAdapter {
    private List<String> list;
    private Context context;
    private LayoutInflater mLInflater;
    public NewRecordAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
        mLInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (list == null) {
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
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = mLInflater.inflate(R.layout.list_item_new_search_record, null);
            vh.mTextView = (TextView) convertView.findViewById(R.id.tv_record);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.mTextView.setText(list.get(position));
        return convertView;
    }

    class ViewHolder {
        TextView mTextView;
    }
}
