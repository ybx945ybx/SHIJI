package cn.yiya.shiji.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.yiya.shiji.R;
import cn.yiya.shiji.utils.SimpleUtils;

/**
 * Created by chenjian on 2015/9/18.
 */
public class TagView extends RelativeLayout {

    public Animation blackAnimation1;
    public Animation blackAnimation2;
    public Animation whiteAnimation;
    public TagAbleImageView.TagInfo tagInfo;
    public boolean isShow = false;
    private Handler handler = new Handler();
    public ImageView blackIcon1;             // 黑色圆圈View
    public ImageView blackIcon2;             // 黑色圆圈View
    protected ImageView brandIcon;           // 白色圆圈View
    protected ImageView geoIcon;             // 白色定位圆圈View

    private TextView tvCountry;
    private TextView tvMoney;
    private LinearLayout llytFooter;

    private int nDrawCount = 20;

    private int nDur = 0;

    private TextView tvTitle;

    private Handler mHandler;

    private final static String X_KEY = "Xpos";
    private final static String Y_KEY = "Ypos";

    private int mX = 0;
    private int mY = 0;

    private int titleWidth = 0;
    private int footWidth = 0;
    private int lineWidth = 0;
    private int lineHeight = 0;
    private int Textwidht = 0;

    private static final int STATR_DRAW = 0;       // 开始绘制
    private static final int HOR_DRAW = 1;         // 水平线绘制
    private static final int END_DRAW = 2;         // 绘制完成

    private Context mContext;
    private boolean bTag = false;

    private List<Map<String, Integer>> mListPoint = new ArrayList<Map<String,Integer>>();
    private boolean bStart = false;
    private boolean bNoDraw = false;

    Paint mPaint = new Paint();

    private int nCount = 0;
    private int nDefaultCount = 2;

    private FrameLayout flytRootView;

    private TranslateAnimation mShowFooterAction;
    private AnimationSet leftAnimationSet;
    private AnimationSet rightAnimationSet;

    private long nTextDur = 0;

    LineView lineView;

    private int nDirection = 0;

    public TagView(Context context) {
        this(context, null);
    }

    public void setAnimationCount(int count){
        this.nDefaultCount = count;
    }

    public void setDirection(int direction){
        nDirection = direction;
    }

    public void setNoDraw(boolean bDraw) {
        bStart = bNoDraw;
    }

    // 设置头标签
    public void setTitleText(String text) {

        tvTitle = new TextView(mContext);
        tvTitle.setText(text);
        tvTitle.setShadowLayer(4,4,4, Color.BLACK);//添加阴影
        tvTitle.setBackgroundResource(R.color.tag_view_text_bg);
        tvTitle.setPadding(SimpleUtils.dp2px(mContext, 4), 0, 0, 0);
        tvTitle.setTextSize(11);
        tvTitle.setMaxWidth(SimpleUtils.dp2px(mContext, 160));
        tvTitle.setSingleLine(true);
        tvTitle.setEllipsize(TextUtils.TruncateAt.END);
        tvTitle.setTextColor(mContext.getResources().getColor(android.R.color.white));
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tvTitle.setGravity(Gravity.LEFT);
        tvTitle.setLayoutParams(params);
        tvTitle.setVisibility(INVISIBLE);

        TextPaint paint = tvTitle.getPaint();
        titleWidth = (int)paint.measureText(text) + SimpleUtils.dp2px(mContext, 4);
        if (titleWidth > SimpleUtils.dp2px(mContext, 160)) {
            titleWidth = SimpleUtils.dp2px(mContext, 160);
        }
        if (titleWidth > SimpleUtils.getScreenWidth(mContext) - SimpleUtils.dp2px(mContext, 60)) {
            titleWidth = SimpleUtils.getScreenWidth(mContext) - SimpleUtils.dp2px(mContext, 60);
        }

        if (TextUtils.isEmpty(text)) {
            tvTitle.setVisibility(GONE);
        }

        this.addView(tvTitle);

    }
    // 设置脚标签
    public void setFooterText(String country, String money) {

        if (TextUtils.isEmpty(country) && TextUtils.isEmpty(money)) {
            llytFooter.setVisibility(GONE);
        }else {
            llytFooter.setVisibility(INVISIBLE);
            if (TextUtils.isEmpty(country)) {
                tvCountry.setVisibility(GONE);
            }
            if (TextUtils.isEmpty(money)) {
                tvMoney.setVisibility(GONE);
            }
            tvCountry.setText(country);
            tvCountry.setShadowLayer(4,4,4, Color.BLACK);//添加阴影
            tvMoney.setText(money);
            tvMoney.setShadowLayer(4,4,4, Color.BLACK);//添加阴影
        }

        TextPaint paint1 = tvCountry.getPaint();
        int tvWidth1 = (int)paint1.measureText(country);
        TextPaint paint2 = tvMoney.getPaint();
        int tvWidth2 = (int)paint2.measureText(money);
        if (TextUtils.isEmpty(country) || TextUtils.isEmpty(money)) {
            lineHeight = SimpleUtils.dp2px(mContext, 11 + 11);
        } else {
            lineHeight = SimpleUtils.dp2px(mContext, 11 + 22);
        }
        footWidth = SimpleUtils.dp2px(mContext, 10) + (tvWidth1 > tvWidth2 ? tvWidth1 : tvWidth2);

        if (footWidth > titleWidth) {
            lineWidth = footWidth;
        }else {
            lineWidth = titleWidth;
            llytFooter.setMinimumWidth(lineWidth);
        }
        Textwidht = lineHeight + lineWidth;
    }

    public int getViewWidth() {
        return Textwidht + flytRootView.getWidth() / 2;
    }

    public int getViewHeight() {
        return lineHeight + SimpleUtils.dp2px(mContext, 14) + flytRootView.getHeight() / 2 + 8;
    }

    public TagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        LayoutInflater.from(context).inflate(R.layout.tag_view_point, this);

        this.blackIcon1 = ((ImageView) findViewById(R.id.blackIcon1));
        this.blackIcon2 = ((ImageView) findViewById(R.id.blackIcon2));
        this.brandIcon = ((ImageView) findViewById(R.id.brandIcon));
        this.geoIcon = ((ImageView) findViewById(R.id.geoIcon));
        flytRootView = (FrameLayout) findViewById(R.id.tagview_point_layout);

        this.blackAnimation1 = AnimationUtils.loadAnimation(context, R.anim.black_anim);
        this.blackAnimation2 = AnimationUtils.loadAnimation(context, R.anim.black_anim);
        this.whiteAnimation = AnimationUtils.loadAnimation(context, R.anim.white_anim);

        View v = LayoutInflater.from(context).inflate(R.layout.tag_view_text, null);
        llytFooter = (LinearLayout)v.findViewById(R.id.tag_view_layout);
        tvCountry = (TextView)v.findViewById(R.id.tag_view_country);
        tvMoney = (TextView)v.findViewById(R.id.tag_view_money);
        this.addView(v);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);

        TranslateAnimation mShowTitleLeftAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);

        TranslateAnimation mShowTitleRightAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);

        leftAnimationSet = new AnimationSet(true);
        leftAnimationSet.setInterpolator(new AccelerateInterpolator());
        leftAnimationSet.addAnimation(mShowTitleLeftAction);
        leftAnimationSet.addAnimation(alphaAnimation);

        rightAnimationSet = new AnimationSet(true);
        rightAnimationSet.setInterpolator(new AccelerateInterpolator());
        rightAnimationSet.addAnimation(mShowTitleRightAction);
        rightAnimationSet.addAnimation(alphaAnimation);

        mShowFooterAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowFooterAction.setDuration(200);

        lineView = new LineView(context);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case STATR_DRAW:
//                        Util.v(msg.arg1 + "得到的坐标值" + msg.arg2 );
                        lineView.setLinePoint(msg.arg1, msg.arg2);
                        break;
                    case HOR_DRAW:
                        if (tvTitle.getVisibility() == INVISIBLE) {
                            if (nDirection == 0 || nDirection == 2) {
                                leftAnimationSet.setDuration(nTextDur);
                                tvTitle.setAnimation(leftAnimationSet);
                            }else {
                                rightAnimationSet.setDuration(nTextDur);
                                tvTitle.setAnimation(rightAnimationSet);
                            }
                            tvTitle.setVisibility(VISIBLE);
                        }
                        break;
                    case END_DRAW:
                        if (llytFooter.getVisibility() == INVISIBLE) {
                            llytFooter.setAnimation(mShowFooterAction);
                            llytFooter.setVisibility(VISIBLE);
                        }
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }

    public void setVisible() {
        clearAnim();
        show();
    }

    public final void clearAnim(){
        this.blackIcon1.clearAnimation();
        this.blackIcon2.clearAnimation();
        this.brandIcon.clearAnimation();
        this.isShow = false;
    }

    private class drawThread extends Thread {
        @Override
        public void run() {
            for (int index = 0; index < nDrawCount; index++)
            {
                Message message = new Message();
                message.what = STATR_DRAW;
                message.arg1 = mX;
                message.arg2 = mY;
                mHandler.sendMessage(message);

                try {
                    sleep(80);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                nDur = Textwidht / nDrawCount;
                Log.i("nDirection", "nDirection=" + nDirection);

                switch (nDirection){
                    case 0:
                        if (mY - lineHeight < nDur && mY - lineHeight >= 0) {
                            nTextDur = (long)((nDrawCount - index)*80);
                            mHandler.sendEmptyMessage(HOR_DRAW);
                        }

                        if (mY - lineHeight < nDur && mY - lineHeight >= 0) {
                            mX = lineHeight + flytRootView.getWidth() / 2;
                            mY = lineHeight + flytRootView.getHeight() / 2;
                        } else if (mY > lineHeight) {
                            mX += nDur;
                        }else {
                            mX += nDur;
                            mY += nDur;
                        }

                        if (index == nDrawCount - 2) {
                            mX = Textwidht + flytRootView.getWidth() / 2;
                        }
                        break;
                    case 1:
                        if (mY - lineHeight < nDur && mY - lineHeight >= 0) {
                            nTextDur = (long)((nDrawCount - index)*80);
                            mHandler.sendEmptyMessage(HOR_DRAW);
                        }

                        if (mY - lineHeight < nDur && mY - lineHeight >= 0) {
                            mX = lineWidth;
                            mY = lineHeight + flytRootView.getHeight() / 2;
                        } else if (mY > lineHeight) {
                            mX = mX - nDur;
                        }else {
                            mX = mX - nDur;
                            mY += nDur;
                        }

                        if (index == nDrawCount - 2) {
                            mX = 0;
                        }
                        break;
                    case 2:
                        int mD2y = getViewHeight() - lineHeight - flytRootView.getHeight() / 2;
                        if (mY - mD2y < nDur && mY >= mD2y) {
                            nTextDur = (long)((nDrawCount - index)*80);
                            mHandler.sendEmptyMessage(HOR_DRAW);
                        }

                        if (mY - mD2y < nDur && mY >= mD2y) {
                            mX = getViewWidth() - lineWidth;
                            mY = mD2y ;
                        } else if (mY == mD2y) {
                            mX += nDur;
                        }else {
                            mX += nDur;
                            mY -= nDur;
                        }

                        if (index == nDrawCount - 2) {
                            mX = Textwidht + flytRootView.getWidth() / 2;
                        }
                        break;
                    case 3:
                        int mD3y = getViewHeight() - lineHeight - flytRootView.getHeight() / 2;
                        if (mY - mD3y < nDur && mY >= mD3y) {
                            nTextDur = (long)((nDrawCount - index)*80);
                            mHandler.sendEmptyMessage(HOR_DRAW);
                        }

                        if (mY - mD3y < nDur && mY >= mD3y) {
                            mX = lineWidth;
                            mY = mD3y;
                        } else if (mY == mD3y) {
                            mX -= nDur;
                        }else {
                            mX -= nDur;
                            mY -= nDur;
                        }

                        if (index == nDrawCount - 2) {
                            mX = 0;
                        }
                        break;
                    default:
                        break;
                }

                if(index == nDrawCount - 1) {
                    mHandler.sendEmptyMessage(END_DRAW);
                }

            }
        }
    }

    public void setStartDraw(int type){
        bStart = true;
        removeView(lineView);
        LineView newLine = new LineView(getContext());
        switch (type) {
            case 0:
                newLine.setLinePoint(flytRootView.getWidth() / 2, flytRootView.getHeight() / 2);
                newLine.setLinePoint(lineHeight + flytRootView.getWidth() / 2, lineHeight + flytRootView.getHeight() / 2);
                newLine.setLinePoint(getViewWidth(), lineHeight + flytRootView.getHeight() / 2);

                break;
            case 1:
                newLine.setLinePoint(getViewWidth() - flytRootView.getWidth() / 2, flytRootView.getHeight() / 2);
                newLine.setLinePoint(lineWidth, lineHeight + flytRootView.getHeight() / 2);
                newLine.setLinePoint(0, lineHeight + flytRootView.getHeight() / 2);

                break;
            case 2:
                newLine.setLinePoint(flytRootView.getWidth() / 2, getViewHeight() - flytRootView.getHeight() / 2);
                newLine.setLinePoint(lineHeight + flytRootView.getWidth() / 2, getViewHeight() - lineHeight - flytRootView.getHeight() / 2);
                newLine.setLinePoint(getViewWidth(), getViewHeight() - lineHeight - flytRootView.getHeight() / 2);
                break;
            case 3:
                newLine.setLinePoint(getViewWidth() - flytRootView.getWidth() / 2, getViewHeight() - flytRootView.getHeight() / 2);
                newLine.setLinePoint(lineWidth, getViewHeight() - lineHeight - flytRootView.getHeight() / 2);
                newLine.setLinePoint(0, getViewHeight() - lineHeight - flytRootView.getHeight() / 2);
                break;
        }
        lineView = newLine;
        this.addView(lineView);
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (bNoDraw){
            return;
        }

        int s = getChildCount();

        for (int i = 0; i < s; i++) {
            View v = getChildAt(i);
            int width = flytRootView.getWidth() / 2;
            int height = flytRootView.getHeight() / 2;

            switch (nDirection) {
                case 0:
                    if (v instanceof LinearLayout) {
                        v.layout(lineHeight + width, height, lineHeight + lineWidth + width, lineHeight + height);
                    }
                    if (v instanceof TextView) {
                        v.layout(lineHeight + width, lineHeight + height + 8, lineHeight + lineWidth + width, lineHeight + v.getHeight() + height + 8);
                    }
                    if (v instanceof FrameLayout) {
                        v.layout(0, 0, 2 * width, 2 * height);
                    }

                    if (bStart && v instanceof LineView) {
                        v.layout(0, 0, getViewWidth(), lineHeight + height);
                    }

                    break;
                case 1:
                    if (v instanceof LinearLayout) {
                        v.layout(0, height, lineWidth, lineHeight + height);
                    }
                    if (v instanceof TextView) {
                        v.layout(0, height + lineHeight + 8, lineWidth, lineHeight + v.getHeight() + height + 8);
                    }
                    if (v instanceof FrameLayout) {
                        v.layout(Textwidht - width, 0, Textwidht + width, 2 * height);
                    }
                    if (bStart && v instanceof LineView) {
                        v.layout(0, 0, getViewWidth(), lineHeight + height);
                    }
                    break;
                case 2:
                    if (v instanceof LinearLayout) {
                        v.layout(lineHeight + width, getViewHeight() - lineHeight - height, lineHeight + lineWidth + width, getViewHeight() - height);
                    }
                    if (v instanceof TextView) {
                        v.layout(lineHeight + width, 0, getViewWidth(), getViewHeight() - lineHeight - height - 8);
                    }
                    if (v instanceof FrameLayout) {
                        v.layout(0, getViewHeight() - 2 * height, 2 * width, getViewHeight());
                    }
                    if (bStart && v instanceof LineView) {
                        v.layout(0, getViewHeight() - height - lineHeight, getViewWidth(), getViewHeight());
                    }
                    break;
                case 3:
                    if (v instanceof LinearLayout) {
                        v.layout(0, getViewHeight() - lineHeight - height, lineWidth, lineHeight + lineWidth + width);
                    }
                    if (v instanceof TextView) {
                        v.layout(0, 0, lineWidth, getViewHeight() - lineHeight - height - 8);
                    }
                    if (v instanceof FrameLayout) {
                        v.layout(Textwidht - width, getViewHeight() - 2 * height, Textwidht + width, getViewHeight());
                    }
                    if (bStart && v instanceof LineView) {
                        v.layout(0, getViewHeight() - height - lineHeight, getViewWidth(), getViewHeight());
                    }
                    break;
            }
            bStart = false;
        }

    }

    public void show() {
        if (this.isShow) {
            return;
        }
        this.blackIcon1.setVisibility(VISIBLE);
        this.blackIcon2.setVisibility(VISIBLE);
        this.isShow = true;
        startWhiteAnimation(brandIcon);
    }

    public void hide(){
        clearAnim();
        this.blackIcon1.setVisibility(GONE);
        this.blackIcon2.setVisibility(GONE);
    }

    public final void startWhiteAnimation(final View view) {
        whiteAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                nCount++;
                if (nDefaultCount > 0 && nCount > nDefaultCount){
                    hide();
                }else {
                    if (!isShow) {
                        return;
                    }
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            view.clearAnimation();
                            whiteAnimation.reset();
                            startBlackAnimation1(blackIcon1);
                            if (!bTag) {
                                switch (nDirection){
                                    case 0:
                                        mX =  flytRootView.getWidth() / 2;
                                        mY = flytRootView.getHeight() / 2;
                                        break;
                                    case 1:
                                        mX = getViewWidth() - flytRootView.getWidth() / 2;
                                        mY = flytRootView.getHeight() / 2;
                                        break;
                                    case 2:
                                        mX = flytRootView.getWidth() / 2;
                                        mY = getViewHeight() - flytRootView.getHeight() / 2;
                                        break;
                                    case 3:
                                        mX = getViewWidth() - flytRootView.getWidth() / 2;
                                        mY = getViewHeight() - flytRootView.getHeight() / 2;
                                        break;
                                }
                                RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(
                                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                                lineView.setLayoutParams(params);
                                addView(lineView);
                                new drawThread().start();
                                bTag = true;
                            }
                        }
                    }, 10);
                }
            }
        });
        view.clearAnimation();
        view.startAnimation(whiteAnimation);
    }

    public final void startBlackAnimation1(final View view) {
        blackAnimation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!isShow) {
                    return;
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.clearAnimation();
                        blackAnimation1.reset();
                        startBlackAnimation2(blackIcon2);
                    }
                }, 10);
            }
        });
        view.clearAnimation();
        view.startAnimation(blackAnimation1);
    }

    public final void startBlackAnimation2(final View view) {
        blackAnimation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!isShow) {
                    return;
                }
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        view.clearAnimation();
                        blackAnimation2.reset();
                        startWhiteAnimation(brandIcon);
                    }
                }, 10);
            }
        });
        view.clearAnimation();
        view.startAnimation(blackAnimation2);
    }
}
