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

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.SortInfo;

/**
 * Created by jerry on 2016/3/7.
 */
public class FilterBrandsAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private LayoutInflater mLInflater;
    private ArrayList<SortInfo> mList;
    private WordsFilter wordsFilter;
    private ArrayList<SortInfo> mBrandsData;

    public FilterBrandsAdapter(Context context, ArrayList<SortInfo> mBrandsData) {
        this.context = context;
        this.mBrandsData = mBrandsData;
        mLInflater = LayoutInflater.from(context);
        mList = new ArrayList<>();
    }


    @Override
    public int getCount() {
        if(mList == null){
            return 0;
        }
        return mList.size();
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
        vh.tvWord.setText(mList.get(position).getName());
        vh.tvWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onClickListener(mList.get(position));
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
        void onClickListener(SortInfo sortInfo);
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
            String word = constraint.toString().toUpperCase();
            ArrayList<SortInfo> newValues = new ArrayList<>();
            newValues.clear();
            if (mBrandsData != null) {
                for (int i = 0; i < mBrandsData.size(); i++) {
                    String brand = mBrandsData.get(i).getName();
                    String[] alils = mBrandsData.get(i).getAlias();
                    if(brand.toUpperCase().startsWith(word.toString().toUpperCase())){
                        newValues.add(mBrandsData.get(i));
                    }else {
                        if(alils != null && alils.length > 0){
                            for (int j = 0; j < alils.length; j++) {
                                if (alils[j].toUpperCase().startsWith(word.toString().toUpperCase())) {
                                    newValues.add(mBrandsData.get(i));
                                }
                            }
                        }
                    }
                }
            }
            results.values = newValues;
            results.count = newValues.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mList = (ArrayList<SortInfo>)results.values;
            if(results.count > 0){
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

}
