package cn.yiya.shiji.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Tom on 2016/11/9.
 */
public class FullyStaggeredGridLayoutManager extends StaggeredGridLayoutManager {
    public FullyStaggeredGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public FullyStaggeredGridLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
    }

    private int[] mMeasuredDimension = new int[2];

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        super.onMeasure(recycler, state, widthSpec, heightSpec);
        final int widthMode = View.MeasureSpec.getMode(widthSpec);
        final int heightMode = View.MeasureSpec.getMode(heightSpec);
        final int widthSize = View.MeasureSpec.getSize(widthSpec);
        final int heightSize = View.MeasureSpec.getSize(heightSpec);

        int width = 0;
        int height = 0;
        int heightLeft = 0;
        int heightRight = 0;
        int count = getItemCount();
        int span = getSpanCount();

        for (int i = 0; i < count; i++) {
            measureScrapChild(recycler,i, View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED), mMeasuredDimension);

            if(getOrientation() == HORIZONTAL){
                if(i % span == 0){
                    width = width + mMeasuredDimension[0];
                }
                if(i == 0){
                    height = mMeasuredDimension[1];
                }
            }else {
                if(i % span == 0){
                    heightLeft = heightLeft + mMeasuredDimension[1];
                }else {
                    heightRight = heightRight + mMeasuredDimension[1];
                }
                if (i == 0){
                    width = mMeasuredDimension[0];
                }
            }
        }

        if(getOrientation() == VERTICAL){
            if(heightLeft >= heightRight){
                height = heightLeft;
            }else {
                height = heightRight;
            }
        }

        switch (widthMode){
            case View.MeasureSpec.EXACTLY:
                width = widthSize;
                break;
            case View.MeasureSpec.AT_MOST:
                break;
            case View.MeasureSpec.UNSPECIFIED:
                break;
        }

        switch (heightMode){
            case View.MeasureSpec.EXACTLY:
                height = heightSize;
                break;
            case View.MeasureSpec.AT_MOST:
                break;
            case View.MeasureSpec.UNSPECIFIED:
                break;
        }

        setMeasuredDimension(width,height);

    }

    private void measureScrapChild(RecyclerView.Recycler recycler, int position, int widthSpec, int heightSpec, int[] measuredDimension){
        if(position<getItemCount()) {
            try {
                View view = recycler.getViewForPosition(0);
                if (view != null) {
                    RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();
                    int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec, getPaddingLeft() + getPaddingRight(), p.width);
                    int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec, getPaddingTop() + getPaddingBottom(), p.height);
                    view.measure(childWidthSpec, childHeightSpec);
                    measuredDimension[0] = view.getMeasuredWidth() + p.leftMargin + p.rightMargin;
                    measuredDimension[1] = view.getMeasuredHeight() + p.topMargin + p.bottomMargin;
                    recycler.recycleView(view);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
