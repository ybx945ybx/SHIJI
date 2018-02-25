package cn.yiya.shiji.views;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.Random;

/**
 * Created by f.laurent on 21/11/13.
 */
public class KenBurnsSupportView extends ImageView {

    private static final String TAG = "KenBurnsView";

    private final Handler mHandler;
    private int[] mResourceIds;
    private ImageView[] mImageViews;
    private int mActiveImageIndex = -1;

    private final Random random = new Random();
    private int mSwapMs = 10000;
    private int mFadeInOutMs = 400;

    private float maxScaleFactor = 1.5F;
    private float minScaleFactor = 1.2F;

    private Runnable mSwapImageRunnable = new Runnable() {
        @Override
        public void run() {
            swapImage();
            mHandler.postDelayed(mSwapImageRunnable, mSwapMs - mFadeInOutMs * 2);
        }
    };

    public KenBurnsSupportView(Context context) {
        this(context, null);
    }

    public KenBurnsSupportView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KenBurnsSupportView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mHandler = new Handler();
    }

//    public void setResourceIds(int... resourceIds) {
//        mResourceIds = resourceIds;
//        fillImageViews();
//    }
    public void setStart() {
        startKenBurnsAnimation();
    }

    private void swapImage() {
        if(mActiveImageIndex == -1) {
            mActiveImageIndex = 0;
            animate(this);
            return;
        }

        int inactiveIndex = mActiveImageIndex;
//        mActiveImageIndex = (1 + mActiveImageIndex) % mImageViews.length;

//        final ImageView activeImageView = mImageViews[mActiveImageIndex];
        ViewHelper.setAlpha(this, 0.0f);
//        ImageView inactiveImageView = mImageViews[inactiveIndex];
        ViewHelper.setAlpha(this, 0.6f);

        animate(this);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(2000);
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(this, "alpha", 1.0f, 0.6f),
                ObjectAnimator.ofFloat(this, "alpha", 0.8f, 1.0f)
        );
        animatorSet.start();
    }

    private void start(View view, long duration, float fromScale, float toScale, float fromTranslationX, float fromTranslationY, float toTranslationX, float toTranslationY) {
        ViewHelper.setScaleX(view, fromScale);
        ViewHelper.setScaleY(view, fromScale);
        ViewHelper.setTranslationX(view, fromTranslationX);
        ViewHelper.setTranslationY(view, fromTranslationY);
        ViewPropertyAnimator propertyAnimator = ViewPropertyAnimator
                .animate(view)
                .translationX(toTranslationX)
                .translationY(toTranslationY)
                .scaleX(toScale)
                .scaleY(toScale)
                .setDuration(duration);

        propertyAnimator.start();
    }

    private float pickScale() {
        return this.minScaleFactor + this.random.nextFloat() * (this.maxScaleFactor - this.minScaleFactor);
    }

    private float pickTranslation(int value, float ratio) {
        return value * (ratio - 1.0f) * (this.random.nextFloat() - 0.5f);
    }

    public void animate(View view) {
        float fromScale = pickScale();
        float toScale = pickScale();
        float fromTranslationX = pickTranslation(view.getWidth(), fromScale);
        float fromTranslationY = pickTranslation(view.getHeight(), fromScale);
        float toTranslationX = pickTranslation(view.getWidth(), toScale);
        float toTranslationY = pickTranslation(view.getHeight(), toScale);
        start(view, mSwapMs - mFadeInOutMs*2, fromScale, toScale, fromTranslationX, fromTranslationY, toTranslationX, toTranslationY);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
//        startKenBurnsAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacks(mSwapImageRunnable);
    }

    private void startKenBurnsAnimation() {
        mHandler.post(mSwapImageRunnable);
    }

//    @Override
//    protected void onFinishInflate() {
//        super.onFinishInflate();
//        View view = inflate(getContext(), R.layout.view_kenburns, this);
//
//        mImageViews = new ImageView[1];
//        mImageViews[0] = (ImageView) view.findViewById(R.id.image0);
////        mImageViews[1] = (ImageView) view.findViewById(R.id.image1);
//    }

//    private void fillImageViews() {
//        for (int i = 0; i < mImageViews.length; i++) {
//            mImageViews[i].setImageResource(mResourceIds[i]);
//
//        }
//    }
}
