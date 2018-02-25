package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.NewMatchDetailActivity;
import cn.yiya.shiji.entity.WorkItem;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;

/**
 * Created by Amy on 2016/7/22.
 */
public class MineMatchGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<WorkItem> mList = new ArrayList<>();
    private AddNewWorkListener addNewWorkListener;
    private boolean isCustom;                            // 主客态  true是主态false是客态
    public static final int ADD_TYPE = 111;
    public static final int LIST_TYPE = 222;

    public MineMatchGridAdapter(Context context, boolean isCustom) {
        super();
        this.mContext = context;
        this.isCustom = isCustom;
    }

    public MineMatchGridAdapter(Context context, ArrayList<WorkItem> mList) {
        super();
        this.mContext = context;
        this.mList = mList;

    }

    public MineMatchGridAdapter(Context context, ArrayList<WorkItem> mList, boolean isCustom) {
        super();
        this.mContext = context;
        this.mList = mList;
        this.isCustom = isCustom;

    }

    public ArrayList<WorkItem> getmList() {
        return mList;
    }

    public void setmList(ArrayList<WorkItem> mList) {
        this.mList = mList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isCustom) {
            if (viewType == ADD_TYPE) {
                return new WorkAddViewHolder(LayoutInflater.from(mContext).inflate(R.layout.work_add_item, parent, false));
            } else if (viewType == LIST_TYPE) {
                return new WorkListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.match_list_item, parent, false));
            }
        } else {
            return new WorkListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.match_list_item, parent, false));
        }
        return null;
    }

    @Override
    public int getItemCount() {
        if (isCustom) {
            if (mList == null) {
                return 1;
            } else {
                return mList.size() + 1;
            }
        } else {
            if (mList == null) {
                return 0;
            } else {
                return mList.size();
            }
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == ADD_TYPE) {
            WorkAddViewHolder workAddViewHolder = (WorkAddViewHolder) holder;
            workAddViewHolder.tvAddWork.setText("添加搭配");
            GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) workAddViewHolder.itemView.getLayoutParams();
            layoutParams.width = (SimpleUtils.getScreenWidth(mContext) - SimpleUtils.dp2px(mContext, 30)) / 2;
            layoutParams.height = layoutParams.width * 263 / 173;
            workAddViewHolder.itemView.setLayoutParams(layoutParams);
            workAddViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (addNewWorkListener != null) {
                        addNewWorkListener.AddNewWork();
                    }
                }
            });
        } else if (getItemViewType(position) == LIST_TYPE) {
            if (mList.size() == 0) {
                return;
            }
            final WorkItem item;
            if (isCustom) {
                item = mList.get(position - 1);
            } else {
                item = mList.get(position);
            }

            WorkListViewHolder workListViewHolder = (WorkListViewHolder) holder;
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) workListViewHolder.image.getLayoutParams();
            layoutParams.width = (SimpleUtils.getScreenWidth(mContext) - SimpleUtils.dp2px(mContext, 30)) / 2;
            layoutParams.height = layoutParams.width * 263 / 173;
            workListViewHolder.image.setLayoutParams(layoutParams);
            if (!TextUtils.isEmpty(item.getImages().get(0).getUrl())) {
                workListViewHolder.image.setImageURI(Util.transfer(item.getImages().get(0).getUrl()));
            }
            workListViewHolder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToDetail(position, item);
                }
            });
        }

    }

    /**
     * 跳转到详情页
     *
     * @param position
     * @param item
     */
    private void goToDetail(final int position, final WorkItem item) {
        Intent intent = new Intent(mContext, NewMatchDetailActivity.class);
        intent.putExtra("work_id", item.getWork_id());
        intent.putExtra("position", position);
        if (addNewWorkListener != null) addNewWorkListener.goToDetail(intent);
    }

    @Override
    public int getItemViewType(int position) {
        if (isCustom) {
            if (position == 0) {
                return ADD_TYPE;
            } else {
                return LIST_TYPE;
            }

        } else {
            return LIST_TYPE;
        }
    }

    public interface AddNewWorkListener {
        void AddNewWork();

        void goToDetail(Intent intent);
    }

    public void setAddNewWorkListener(AddNewWorkListener addNewWorkListener) {
        this.addNewWorkListener = addNewWorkListener;
    }

    class WorkListViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView image;

        public WorkListViewHolder(View itemView) {
            super(itemView);
            image = (SimpleDraweeView) itemView.findViewById(R.id.grid_item_match_image);
        }
    }

    class WorkAddViewHolder extends RecyclerView.ViewHolder {
        TextView tvAddWork;

        public WorkAddViewHolder(View itemView) {
            super(itemView);
            tvAddWork = (TextView) itemView.findViewById(R.id.tv_work_add);
        }
    }
}
