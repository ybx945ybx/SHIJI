package cn.yiya.shiji.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.AddressListItem;

/**
 * Created by tomyang on 2015/9/28.
 */
public class AddressItemAdapter extends RecyclerView.Adapter<AddressItemAdapter.AddressViewHolder> {
    private Context mContext;
    private ArrayList<AddressListItem> mlist;
    private OnItemClickListener onItemClickListener;
    private OnItemSelectListener onItemSelectListener;
    private boolean isSelect;

    public AddressItemAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public AddressItemAdapter(Context mContext, boolean isSelect) {
        this.mContext = mContext;
        this.isSelect = isSelect;
    }

    public ArrayList<AddressListItem> getMlist() {
        return mlist;
    }

    public void setMlist(ArrayList<AddressListItem> mlist) {
        this.mlist = mlist;
    }

    @Override
    public AddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AddressViewHolder(LayoutInflater.from(mContext).inflate(R.layout.address_item, parent, false));
    }

    @Override
    public void onBindViewHolder(AddressViewHolder holder, final int position) {
        final AddressListItem item = mlist.get(position);

        holder.tvReceiveName.setText(item.getRecipient());
        holder.tvReceivePhone.setText(item.getMobile());
        holder.tvIdentificationNumber.setText(item.getIdentity_number());
        holder.tvProvince.setText(item.getProvince());
        holder.tvCity.setText(item.getCity());
        holder.tvDistrict.setText(item.getDistrict());
        holder.tvDetailAddress.setText(item.getAddress());

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(item, position);
                }
            }
        });

        if(isSelect) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemSelectListener != null) {
                        onItemSelectListener.onItemSelect(item);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (mlist == null)
            return 0;
        return mlist.size();
    }

    public interface OnItemClickListener{
        void onItemClick(AddressListItem item, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemSelectListener{
        void onItemSelect(AddressListItem item);
    }

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener){
        this.onItemSelectListener = onItemSelectListener;
    }

    protected class AddressViewHolder extends RecyclerView.ViewHolder{

        TextView tvReceiveName;
        TextView tvReceivePhone;
        TextView tvIdentificationNumber;
        TextView tvProvince;
        TextView tvCity;
        TextView tvDistrict;
        TextView tvDetailAddress;
        ImageView ivEdit;

        public AddressViewHolder(View itemView) {
            super(itemView);
            tvReceiveName = (TextView) itemView.findViewById(R.id.tv_receive_name);
            tvReceivePhone = (TextView) itemView.findViewById(R.id.tv_receive_phone);
            tvIdentificationNumber = (TextView) itemView.findViewById(R.id.tv_identification_number);
            tvProvince = (TextView) itemView.findViewById(R.id.tv_province);
            tvCity = (TextView) itemView.findViewById(R.id.tv_city);
            tvDistrict = (TextView) itemView.findViewById(R.id.tv_district);
            tvDetailAddress = (TextView) itemView.findViewById(R.id.tv_detail_address);
            ivEdit = (ImageView) itemView.findViewById(R.id.iv_edit);
        }
    }
}


