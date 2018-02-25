package cn.yiya.shiji.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

/**
 * Created by jerry on 2016/3/2.
 */
public class InterceptLinearLayout extends LinearLayout {
    private boolean mScrolling;
    private float touchDownY;
    private float Y;

    public InterceptLinearLayout(Context context) {
        super(context);
    }

    public InterceptLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InterceptLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDownY = event.getY();
                mScrolling = false;
                break;
            case MotionEvent.ACTION_MOVE:
                mScrolling = false;
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(touchDownY - event.getY()) >= ViewConfiguration.get(
                        getContext()).getScaledTouchSlop()) {
                    mScrolling = false;
                } else {
                    mScrolling = true;
                    if (onClickingListener != null) {
                        onClickingListener.OnClickingListener();
                    }
                }
                break;
        }
        return mScrolling;
    }

    private OnClickingListener onClickingListener;

    public void setOnClickingListener(OnClickingListener onClickingListener) {
        this.onClickingListener = onClickingListener;
    }

    public interface OnClickingListener {
        void OnClickingListener();
    }

}
