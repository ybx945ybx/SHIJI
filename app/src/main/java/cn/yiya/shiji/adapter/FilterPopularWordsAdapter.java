package cn.yiya.shiji.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.yiya.shiji.R;

/**
 * Created by jerry on 2016/3/7.
 */
public class FilterPopularWordsAdapter extends BaseAdapter implements Filterable {
    public  HashMap<String,String[]>[] datas;
    private Context context;
    private int code;
    private LayoutInflater mLInflater;
    WordsFilter wordsFilter;
    public List<String> list;
    private ArrayList<String> arrayList;


    public FilterPopularWordsAdapter(HashMap<String, String[]>[] datas, Context context, int code, List<String> list) {
        this.datas = datas;
        this.context = context;
        this.code = code;
        this.list = list;
        mLInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if(list == null){
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
            convertView = mLInflater.inflate(R.layout.search_record_item,null);
            vh.tvWord = (TextView)convertView.findViewById(R.id.tv_record);
            vh.ivArrows = (ImageView)convertView.findViewById(R.id.iv_record);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder)convertView.getTag();
        }
        if(code == 1){
            vh.ivArrows.setVisibility(View.VISIBLE);
        } else {
            vh.ivArrows.setVisibility(View.INVISIBLE);
        }
        vh.tvWord.setText(list.get(position));
        vh.tvWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onClickListener(list.get(position));
                }
            }
        });
        return convertView;
    }

    private class ViewHolder{
        TextView tvWord;
        ImageView ivArrows;
    }
    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
    public interface  OnItemClickListener{
        public void onClickListener(String record);
    }
    @Override
    public Filter getFilter() {
        if(wordsFilter == null){
            wordsFilter = new WordsFilter();
        }
        return wordsFilter;
    }

    private class WordsFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if(arrayList == null){
                arrayList = new ArrayList<>();
            }
            if (constraint == null || constraint.length() == 0) {
                ArrayList<String> list = arrayList;
                results.values = list;
                results.count = list.size();
            } else {
                String word = constraint.toString().toUpperCase();
                ArrayList<String> mlist = arrayList;
                int count = mlist.size();
                ArrayList<String> newValues = new ArrayList<String>(count);
                newValues.clear();
                if (datas != null) {
                    for (int i = 0; i < datas.length; i++) {
                        Map map = datas[i];
                        Iterator iterator = map.entrySet().iterator();
                        while (iterator.hasNext()){
                            Map.Entry entry = (Map.Entry)iterator.next();
                            String localword = (String)entry.getKey();
                            String[] values = (String[])entry.getValue();
                            if(localword.toUpperCase().startsWith(word.toString().toUpperCase())){
                                newValues.add(localword);
                            }
                            if(values.length >0){
                                for(String words:values){
                                    if(words.toUpperCase().startsWith(word.toString().toUpperCase())){
                                        newValues.add(localword);
                                    }
                                }
                            }
                        }
                    }
                }
                results.values = newValues;
                results.count = newValues.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list = (ArrayList<String>)results.values;
            if(results.count > 0){
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

    public HashMap<String, String[]>[] getDatas() {
        return datas;
    }

    public void setDatas(HashMap<String, String[]>[] datas) {
        this.datas = datas;
    }
}
