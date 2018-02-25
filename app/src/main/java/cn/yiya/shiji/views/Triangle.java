package cn.yiya.shiji.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Amy on 2016/9/27.
 */

public class Triangle extends View {
    private Paint paint;
    private Path path;

    private int mWidth;
    private int mHeight;

    private final int DefaultSize = 60;

    public Triangle(Context context) {
        super(context);
        init();
    }

    public Triangle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Triangle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        drawTriangle(canvas);
    }

    //完成相关参数初始化
    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0xFFdedede);

        path = new Path();
    }

    //重写测量大小的onMeasure方法和绘制View的核心方法onDraw()
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getRealSize(DefaultSize, widthMeasureSpec);
        mHeight = getRealSize(DefaultSize, heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    public int getRealSize(int defaultsize, int measureSpec) {
        int result = defaultsize;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        switch (mode) {
            case MeasureSpec.EXACTLY:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(size, defaultsize);
                break;
            case MeasureSpec.UNSPECIFIED:
                result = defaultsize;
                break;
        }
        return result;
    }

    private void drawTriangle(Canvas canvas) {
        //用路径来画出这个三角形
        path.moveTo(0, 0);//起始点
        path.lineTo(mWidth, 0);//从起始点画一根线到 相应的坐标
        path.lineTo((float) mWidth / 2, mHeight);
        path.close();//闭合
        canvas.drawPath(path, paint);
    }
}
