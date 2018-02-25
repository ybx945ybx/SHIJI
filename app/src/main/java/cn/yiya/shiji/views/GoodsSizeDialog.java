package cn.yiya.shiji.views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.GoodsSizeListAdapter;
import cn.yiya.shiji.entity.GoodsDetailEntity;

/**
 * Created by Amy on 2016/11/14.
 */

public class GoodsSizeDialog extends Dialog {
    private Context mContext;
    private List<GoodsDetailEntity.SizeEntity.TablesEntity> mTables;
    private int matchPos = -1;
    private View mView;

    private RecyclerView recyclerView;
    private ImageView ivClose;

    public GoodsSizeDialog(Context context, List<GoodsDetailEntity.SizeEntity.TablesEntity> tables, int matchPos) {
        super(context, R.style.ActionSheetDialogStyle);
        this.mContext = context;
        this.mTables = tables;
        this.matchPos = matchPos;
    }

    @SuppressLint({"RtlHardcoded", "InflateParams"})
    public Dialog build() {

        initView();

        setContentView(mView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);

        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);
        return this;
    }


    private void initView() {
        mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_goods_buy_size, null);

        recyclerView = (RecyclerView) mView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(new GoodsSizeListAdapter(mContext, mTables, matchPos));

        ivClose = (ImageView) mView.findViewById(R.id.iv_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


}