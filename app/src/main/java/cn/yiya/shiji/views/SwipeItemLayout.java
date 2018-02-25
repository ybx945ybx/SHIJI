package cn.yiya.shiji.views;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 使用ViewRragHelper实现侧滑效果功能
 * Created by Amy on 2016/11/8.
 */

public class SwipeItemLayout extends FrameLayout {
    private final ViewDragHelper dragHelper;
    //菜单布局
    private View menu;
    //内容布局
    private View content;

    private boolean isOpen;
    private int currentState;

    public SwipeItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        dragHelper = ViewDragHelper.create(this, dragCalback);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        menu = getChildAt(0);
        content = getChildAt(1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return dragHelper.shouldInterceptTouchEvent(ev);
    }

    private ViewDragHelper.Callback dragCalback = new ViewDragHelper.Callback() {

        /**
         * 表示尝试捕获子view，返回值可以决定一个parentview中哪个子view可以拖动
         * @param child 当前被拖拽的view
         * @param pointerId 区分多点触摸的id
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return content == child;
        }

        /**
         * 水平方向移动
         * @param left  代表你将要移动到的位置的坐标,返回值就是最终确定的移动的位置,
         *              判断如果这个坐标在layout之内,那我们就返回这个坐标值，
         *              不能让他超出这个范围, 除此之外就是如果你的layout设置了padding的话，
         *              让子view的活动范围在padding之内的
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return left > 0 ? 0 : left < -menu.getWidth() ? -menu.getWidth() : left;
        }

        /**
         * 当拖拽的子View，手势释放的时候回调的方法， 然后根据左滑或者右滑的距离进行判断打开或者关闭
         * @param releasedChild
         * @param xvel 水平方向的速度  向右为正
         * @param yvel 竖直方向的速度  向下为正
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            // x轴移动速度大于菜单宽度，或者已经移动到菜单的一半之后，展开菜单
            if (isOpen) {
                if (xvel > menu.getWidth() || -content.getLeft() < menu.getWidth() / 2) {
                    close();
                } else {
                    open();
                }
            } else {
                if (-xvel > menu.getWidth() || -content.getLeft() > menu.getWidth() / 2) {
                    open();
                } else {
                    close();
                }
            }
        }

        /**
         *  要返回一个大于0的数，才会在在垂直方向上对触摸到的View进行拖动
         * 返回拖拽的范围，不对拖拽进行真正的限制，仅仅决定了动画执行速度
         */
        @Override
        public int getViewHorizontalDragRange(View child) {
            return 1;
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return 0;
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            currentState = state;
        }
    };

    public void close() {
        dragHelper.smoothSlideViewTo(content, 0, 0);
        isOpen = false;
        invalidate();
    }

    public void open() {
        dragHelper.smoothSlideViewTo(content, -menu.getWidth(), 0);
        isOpen = true;
        invalidate();
    }

    public boolean isOpen() {
        return isOpen;
    }

    public int getState() {
        return currentState;
    }

    private Rect outRect = new Rect();

    public Rect getMenuRect() {
        menu.getHitRect(outRect);
        return outRect;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (dragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        content.setOnClickListener(l);
    }

}