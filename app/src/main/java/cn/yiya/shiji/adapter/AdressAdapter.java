package cn.yiya.shiji.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.AddressListItem;

/**
 * Created by yiya on 2015/9/7.
 */
public class AdressAdapter extends BaseAdapter {
    private Context mContext;
    private List<AddressListItem> mList;

    public AdressAdapter(Context mContext, List<AddressListItem> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        if (mList == null)
            return 0;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_account, null);
            vHolder = new ViewHolder();
            vHolder.tvName = (TextView) convertView.findViewById(R.id.account_name);
            vHolder.tvMobile = (TextView) convertView.findViewById(R.id.account_mobile);
            vHolder.tvOrderNo = (TextView) convertView.findViewById(R.id.account_orderno);
            vHolder.tvCity = (TextView) convertView.findViewById(R.id.account_city);
            vHolder.tvAddress = (TextView) convertView.findViewById(R.id.account_adress);
            vHolder.rlytAdd = (RelativeLayout) convertView.findViewById(R.id.add_address);
            vHolder.rlytAddress = (RelativeLayout) convertView.findViewById(R.id.address_layout);
            vHolder.ivSelected = (ImageView) convertView.findViewById(R.id.selected_arrow);
            convertView.setTag(vHolder);
        } else
            vHolder = (ViewHolder)convertView.getTag();

        vHolder.rlytAdd.setVisibility(View.GONE);
        vHolder.rlytAddress.setVisibility(View.VISIBLE);

        AddressListItem info = mList.get(position);
        vHolder.tvName.setText(info.getRecipient());
        vHolder.tvMobile.setText(info.getMobile());
        vHolder.tvOrderNo.setText(info.getIdentity_number());
        vHolder.tvCity.setText(info.getProvince() + info.getCity() + info.getDistrict());
        vHolder.tvAddress.setText(info.getAddress());
        if (info.isSelected()) {
            vHolder.ivSelected.setVisibility(View.VISIBLE);
        }else {
            vHolder.ivSelected.setVisibility(View.GONE);
        }
        return convertView;
    }

    public class ViewHolder {
        TextView tvName;
        TextView tvMobile;
        TextView tvOrderNo;
        TextView tvCity;
        TextView tvAddress;
        View vBack;
        RelativeLayout rlytAddress;
        RelativeLayout rlytAdd;
        ImageView ivSelected;
    }
}
