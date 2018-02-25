package cn.yiya.shiji.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.duowan.mobile.netroid.image.NetworkImageView;

import java.util.ArrayList;
import java.util.Locale;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.HotMallSortItem;
import cn.yiya.shiji.entity.Item;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.PinnedSectionListView;

/**
 * Created by yiya on 2015/9/9.
 */
public class MallsListAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {

    private Context mContext;
    //2015-09-16
    private ArrayList<HotMallSortItem> mList = new ArrayList<>();

    public MallsListAdapter(Context context, ArrayList<HotMallSortItem> list) {
        mContext = context;
        mList = list;

    }

    public int getPositionForSection(int section) {
        for (int i = 0; i < mList.size(); i++) {
            char firstChar = mList.get(i).getTitle().toUpperCase(Locale.ENGLISH).charAt(0);
            int se = firstChar;
            if (se == section) {
                return i;
            }
        }

        return -1;
    }

    public void generateDataset(char from, char to, boolean clear) {


        final int sectionsNumber = to - from + 1;
        prepareSections(sectionsNumber);

        int sectionPosition = 0, listPosition = 0;
        for (char i = 0; i < sectionsNumber; i++) {
            HotMallSortItem section = new HotMallSortItem();
            section.setType(Item.SECTION);
            section.setText(String.valueOf((char) ('A' + i)));
            section.sectionPosition = sectionPosition;
            section.listPosition = listPosition++;
            onSectionAdded(section, sectionPosition);
            mList.add(section);

            final int itemsNumber = (int) Math.abs((Math.cos(2f * Math.PI / 3f * sectionsNumber / (i + 1f)) * 25f));
            for (int j = 0; j < itemsNumber; j++) {
                HotMallSortItem item = new HotMallSortItem();
                item.setType(Item.ITEM);
                item.setText(section.getText().toUpperCase(Locale.ENGLISH) + "-" + j);
                item.sectionPosition = sectionPosition;
                item.listPosition = listPosition++;
                mList.add(item);
            }

            sectionPosition++;
        }
    }

    protected void prepareSections(int sectionsNumber) {
    }

    protected void onSectionAdded(HotMallSortItem section, int sectionPosition) {
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_mall_sort_item, parent, false);
            vHolder = new ViewHolder();
            vHolder.tv = (TextView) convertView.findViewById(R.id.txt);
            vHolder.iv = (NetworkImageView) convertView.findViewById(R.id.imageview);
            vHolder.rl = (RelativeLayout) convertView.findViewById(R.id.relativelayout);
            vHolder.view = (View) convertView.findViewById(R.id.view);
            convertView.setTag(vHolder);
        } else {
            vHolder = (ViewHolder) convertView.getTag();
        }
        vHolder.tv.setTextColor(Color.DKGRAY);
        final HotMallSortItem mitem = mList.get(position);
        vHolder.tv.setText(mitem.getTitle());                                 // 设置商城名称
        Netroid.displayImage(Util.transfer(mitem.getLogo()), vHolder.iv);      // 加载商城图片
        if (mitem.type == Item.SECTION) {
            vHolder.iv.setVisibility(View.GONE);                                                // 当字母第一次出现时，隐藏图片
            vHolder.rl.setBackgroundColor(Color.parseColor("#F0F0F0"));    // 设置首字母item背景
            AbsListView.LayoutParams relativeLayoutParams = (AbsListView.LayoutParams) vHolder.rl.getLayoutParams();
            relativeLayoutParams.height = 80;
            vHolder.rl.setLayoutParams(relativeLayoutParams);

            int section = getSectionForPosition(position);
        }
        return convertView;
    }

    public class ViewHolder {
        TextView tv;
        NetworkImageView iv;
        RelativeLayout rl;
        View view;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getType();
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == Item.SECTION;
    }

    @Override
    public int getCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return mList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    public int getSectionForPosition(int position) {
        return mList.get(position).getTitle().charAt(0);
    }

}
