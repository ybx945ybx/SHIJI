package cn.yiya.shiji.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.CollocationGoodsListAdapter;
import cn.yiya.shiji.entity.goodes.NewGoodsItem;
import cn.yiya.shiji.utils.BitmapTool;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.views.FullyLinearLayoutManager;

/**
 * Created by Tom on 2016/7/20.
 */
public class CollocationGoodsOrderActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private TextView tvRight;

    private RelativeLayout rlytExpand;
    private ImageView ivExpand;

    private ImageView ivCollocation;

    private LinearLayout llytAddTips;
    private RecyclerView rycvGoods;
    private CollocationGoodsListAdapter collocationGoodsListAdapter;
    private ArrayList<NewGoodsItem> mList = new ArrayList<>();

    private String mPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collocation_add_goods);
        initIntent();
        initViews();
        initEvents();
        init();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if(intent != null){
            mPath = getIntent().getStringExtra("path");

            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonArray Jarray = parser.parse(intent.getStringExtra("list")).getAsJsonArray();

            for(JsonElement obj : Jarray ){
                NewGoodsItem cse = gson.fromJson( obj , NewGoodsItem.class);
                mList.add(cse);
            }
        }
    }

    @Override
    protected void initViews() {
        findViewById(R.id.title_back).setVisibility(View.GONE);
        ((TextView)findViewById(R.id.title_txt)).setText("添加商品");
        tvRight = (TextView) findViewById(R.id.title_right);
        tvRight.setText("完成排序");
        tvRight.setVisibility(View.VISIBLE);

        rlytExpand = (RelativeLayout) findViewById(R.id.rlyt_expand);
        ivExpand = (ImageView) findViewById(R.id.iv_expand);
        ivExpand.setImageResource(R.mipmap.xiala);

        ivCollocation = (ImageView) findViewById(R.id.iv_collocation);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.width = SimpleUtils.getScreenWidth(this) - SimpleUtils.dp2px(this, 136);
        layoutParams.height = layoutParams.width * 360/239;
        layoutParams.topMargin = SimpleUtils.dp2px(this, 26);
        layoutParams.bottomMargin = SimpleUtils.dp2px(this, 26);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        ivCollocation.setLayoutParams(layoutParams);
//        ivCollocation.setImageBitmap(BaseApplication.getInstance().collocation);
        BitmapTool.disaplayImage("file://" + mPath, ivCollocation, null);
        ivCollocation.setVisibility(View.GONE);

        llytAddTips = (LinearLayout) findViewById(R.id.llyt_add_tips);
        rycvGoods = (RecyclerView) findViewById(R.id.rycv_goods);
        rycvGoods.setItemAnimator(new DefaultItemAnimator());
        rycvGoods.setLayoutManager(new FullyLinearLayoutManager(this));
        collocationGoodsListAdapter = new CollocationGoodsListAdapter(this, true, false);
        rycvGoods.setAdapter(collocationGoodsListAdapter);
        llytAddTips.setVisibility(View.GONE);
        rycvGoods.setVisibility(View.VISIBLE);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(collocationGoodsListAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rycvGoods);

        findViewById(R.id.rlyt_bottom).setVisibility(View.GONE);
        findViewById(R.id.nscrollview).setPadding(0, 0, 0, 0);
    }

    @Override
    protected void initEvents() {
        tvRight.setOnClickListener(this);
        rlytExpand.setOnClickListener(this);
    }

    @Override
    protected void init() {
        collocationGoodsListAdapter.setmList(mList);
        collocationGoodsListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_right:
                Intent intent = new Intent();
                intent.putExtra("list", new Gson().toJson(mList));
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.rlyt_expand:
                if(ivCollocation.getVisibility() == View.GONE){
                    ivCollocation.setVisibility(View.VISIBLE);
                    ivExpand.setImageResource(R.mipmap.shousuo);
                }else {
                    ivCollocation.setVisibility(View.GONE);
                    ivExpand.setImageResource(R.mipmap.xiala);
                }
                break;
        }

    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback{
        private ItemTouchHelperAdapter mAdapter;

        public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter){
            mAdapter = adapter;
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return false;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP|ItemTouchHelper.DOWN;
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if(actionState != ItemTouchHelper.ACTION_STATE_IDLE){
                viewHolder.itemView.setBackgroundColor(Color.parseColor("#e5ffffff"));
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

    public interface ItemTouchHelperAdapter {

        void onItemMove(int fromPosition, int toPosition);

        void onItemDismiss(int position);
    }

}
