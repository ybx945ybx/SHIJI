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

import java.util.ArrayList;
import java.util.Locale;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.BrandsSortItem;
import cn.yiya.shiji.entity.CharacterParser;
import cn.yiya.shiji.entity.Item;
import cn.yiya.shiji.views.PinnedSectionListView;

/**
 * Created by Amy on 2016/6/30.
 */
public class NewBrandSortAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {

    private Context mContext;
    private CharacterParser characterParser = CharacterParser.getInstance();
    private ArrayList<BrandsSortItem> mList = new ArrayList<>();


    public NewBrandSortAdapter(Context context, ArrayList<BrandsSortItem> list) {
        mContext = context;
        mList = list;

    }

    public int getPositionForSection(int section) {
        for (int i = 0; i < mList.size(); i++) {
            char firstChar = mList.get(i).getName().toUpperCase(Locale.ENGLISH).charAt(0);
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
            BrandsSortItem section = new BrandsSortItem();
            section.setType(Item.SECTION);
            section.setText(String.valueOf((char) ('A' + i)));
            section.sectionPosition = sectionPosition;
            section.listPosition = listPosition++;
            onSectionAdded(section, sectionPosition);
            mList.add(section);

            final int itemsNumber = (int) Math.abs((Math.cos(2f * Math.PI / 3f * sectionsNumber / (i + 1f)) * 25f));
            for (int j = 0; j < itemsNumber; j++) {
//                Item item = new Item();
                BrandsSortItem item = new BrandsSortItem();
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

    protected void onSectionAdded(BrandsSortItem section, int sectionPosition) {
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_brand, parent, false);
            vHolder = new ViewHolder();
            vHolder.tvBrand = (TextView) convertView.findViewById(R.id.tv_brand);
            vHolder.rlBrand = (RelativeLayout) convertView.findViewById(R.id.rl_brand);
            convertView.setTag(vHolder);
        } else {
            vHolder = (ViewHolder) convertView.getTag();
        }
        vHolder.tvBrand.setTextColor(Color.DKGRAY);
        final BrandsSortItem mitem = mList.get(position);
        vHolder.tvBrand.setText(mitem.getName() + "  " + mitem.getCn_name());                                 // 设置商城名称
        if (mitem.type == Item.SECTION) {             // 当字母第一次出现时，隐藏图片
            vHolder.rlBrand.setBackgroundColor(Color.parseColor("#F0F0F0"));    // 设置首字母item背景
            AbsListView.LayoutParams relativeLayoutParams = (AbsListView.LayoutParams) vHolder.rlBrand.getLayoutParams();
            relativeLayoutParams.height = 80;
            vHolder.rlBrand.setLayoutParams(relativeLayoutParams);
            int section = getSectionForPosition(position);
        }
        return convertView;
    }

    public class ViewHolder {
        TextView tvBrand;
        RelativeLayout rlBrand;
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
        return characterParser.getSelling(mList.get(position).getName()).charAt(0);

    }

}
