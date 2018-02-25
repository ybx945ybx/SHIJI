package cn.yiya.shiji.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import cn.yiya.shiji.R;

/**
 * Created by Amy on 2016/6/21.
 */
public class NewBrandLetterAdapter extends RecyclerView.Adapter<NewBrandLetterAdapter.BrandLetterViewHolder> {
    private Context mContext;
    private String[] letters;
    private int clickPosition;
    private OnItemClickListener mListener;

    public NewBrandLetterAdapter(Context mContext, String[] letters) {
        this.mContext = mContext;
        this.letters = letters;
    }

    public void setOnItemClickListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    public void setClickPosition(int position) {
        this.clickPosition = position;
        notifyDataSetChanged();
    }

    @Override
    public BrandLetterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BrandLetterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_new_brand_letter, parent, false));
    }

    @Override
    public void onBindViewHolder(final BrandLetterViewHolder holder, final int position) {
        holder.rbLetter.setText(letters[position]);
        if (position == clickPosition) {
            holder.rbLetter.setChecked(true);
            holder.rbLetter.setTextColor(mContext.getResources().getColor(R.color.new_white_color));
        } else {
            holder.rbLetter.setChecked(false);
            holder.rbLetter.setTextColor(mContext.getResources().getColor(R.color.new_black_color));
        }
        holder.rbLetter.setTag(position);
        holder.rbLetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPosition = (Integer) v.getTag();
                notifyDataSetChanged();
                if (mListener != null) mListener.OnItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return letters.length;
    }

    class BrandLetterViewHolder extends RecyclerView.ViewHolder {

        private RadioButton rbLetter;

        public BrandLetterViewHolder(View itemView) {
            super(itemView);
            rbLetter = (RadioButton) itemView.findViewById(R.id.rb_letter);
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(int position);
    }
}
