package cn.yiya.shiji.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.BrandLetterInfo;

/**
 * Created by Tom on 2016/6/1.
 */
public class LetterAdapter extends RecyclerView.Adapter<LetterAdapter.LetterViewHolder>{

    private Context mContext;
    private ArrayList<BrandLetterInfo> mList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private int checkedPosition = 0;
//    private static final String[] letterArry = new String[]{"#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
//            "T", "U", "V", "W", "X", "Y", "Z", };

//    public LetterAdapter(Context mContext){
//        this.mContext = mContext;
//        initData();
//    }
    public LetterAdapter(Context mContext, ArrayList<BrandLetterInfo> mList){
        this.mContext = mContext;
        this.mList = mList;
    }

//    private void initData() {
//        String[] letterArry = new String[]{"#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
//                "T", "U", "V", "W", "X", "Y", "Z", };
//        for (int i = 0; i < letterArry.length; i++ ){
//            BrandLetterInfo brandLetterInfo = new BrandLetterInfo();
//            brandLetterInfo.setName(letterArry[i]);
//            if(i == 0) {
//                brandLetterInfo.setCheck(true);
//            }
//            mList.add(brandLetterInfo);
//        }
//    }

    public ArrayList<BrandLetterInfo> getmList() {
        return mList;
    }

    public void setmList(ArrayList<BrandLetterInfo> mList) {
        this.mList = mList;
    }

    @Override
    public LetterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LetterViewHolder(LayoutInflater.from(mContext).inflate(R.layout.letter_adapter_item, parent, false));
    }

    @Override
    public void onBindViewHolder(LetterViewHolder holder, int position) {
        final BrandLetterInfo info = mList.get(position);

        holder.tvLetter.setText(info.getName());


        if(info.isCheck()){
            holder.tvLetter.setSelected(true);
            checkedPosition = position;
        }else {
            holder.tvLetter.setSelected(false);
        }

//        if(!info.isClickable()){
////            holder.tvLetter.setClickable(true);
////        }else {
////            holder.tvLetter.setClickable(false);
//            holder.tvLetter.setTextColor(Color.parseColor("#3c212121"));
//        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(info.isCheck()){
                    return;
                }
                info.setCheck(true);
                mList.get(checkedPosition).setCheck(false);
                notifyDataSetChanged();
                if(onItemClickListener != null){
                    onItemClickListener.OnItemClick(info);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if(mList == null){
            return 0;
        }else {
            return mList.size();
        }
    }

    public interface OnItemClickListener{
        void OnItemClick(BrandLetterInfo brandLetterInfo);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    class LetterViewHolder extends RecyclerView.ViewHolder{
        TextView tvLetter;
        public LetterViewHolder(View itemView) {
            super(itemView);
            tvLetter = (TextView) itemView.findViewById(R.id.tv_letter);
        }
    }
}

