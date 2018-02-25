package cn.yiya.shiji.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.yiya.shiji.R;

public class SystemAlertDialogBuilder extends Dialog implements DialogInterface {

    private final String defTextColor="#333333";

    private final String defDividerColor="#11000000";

    private final String defMsgColor="#333333";

    private final String defDialogColor="#ffffff";


    private static Context tmpContext;


    private Effectstype type=null;

    private LinearLayout mLinearLayoutView;

    private RelativeLayout mRelativeLayoutView;

    private LinearLayout mLinearLayoutMsgView;

    private LinearLayout mLinearLayoutTopView;

    private FrameLayout mFrameLayoutCustomView;

    private View mDialogView;

    private View mDivider;

    private TextView mTitle;

    private TextView mMessage;

    private ImageView mIcon;

    private Button mButton1;

    private Button mButton2;

    private int mDuration = -1;

    private static  int mOrientation=1;

    private boolean isCancelable=true;

    private static SystemAlertDialogBuilder instance;

    public SystemAlertDialogBuilder(Context context) {
        super(context);
        init(context);

    }
    public SystemAlertDialogBuilder(Context context, int theme) {
        super(context, theme);
        init(context);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width  = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes((WindowManager.LayoutParams) params);
        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
    }
    
    public static SystemAlertDialogBuilder getInstance(Context context) {

        if (instance == null || !tmpContext.equals(context)) {
            synchronized (SystemAlertDialogBuilder.class) {
                if (instance == null || !tmpContext.equals(context)) {
                    instance = new SystemAlertDialogBuilder(context, R.style.dialog_untran);
                }
            }
        }
        tmpContext = context;
        return instance;

    }
    
    private void init(Context context) {
        
        mDialogView = View.inflate(context, R.layout.dialog_layout, null);
        
        mLinearLayoutView=(LinearLayout)mDialogView.findViewById(R.id.parentPanel);
        mRelativeLayoutView=(RelativeLayout)mDialogView.findViewById(R.id.main);
        mLinearLayoutTopView=(LinearLayout)mDialogView.findViewById(R.id.topPanel);
        mLinearLayoutMsgView=(LinearLayout)mDialogView.findViewById(R.id.contentPanel);
        mFrameLayoutCustomView=(FrameLayout)mDialogView.findViewById(R.id.customPanel);
        
        mTitle = (TextView) mDialogView.findViewById(R.id.alertTitle);
        mMessage = (TextView) mDialogView.findViewById(R.id.message);
        mIcon = (ImageView) mDialogView.findViewById(R.id.icon);
        mDivider = mDialogView.findViewById(R.id.titleDivider);
        mButton1=(Button)mDialogView.findViewById(R.id.button1);
        mButton2=(Button)mDialogView.findViewById(R.id.button2);
        
        setContentView(mDialogView);
        
        this.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                
                mLinearLayoutView.setVisibility(View.VISIBLE);
                if(type==null){
                    type=Effectstype.Slidetop;
                }
                start(type);
                
                
            }
        });
        mRelativeLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCancelable)dismiss();
            }
        });
    }
    
    public void toDefault(){
        mTitle.setTextColor(Color.parseColor(defTextColor));
        mDivider.setBackgroundColor(Color.parseColor(defDividerColor));
        mMessage.setTextColor(Color.parseColor(defMsgColor));
        mLinearLayoutView.setBackgroundColor(Color.parseColor(defDialogColor));
    }
    
    public SystemAlertDialogBuilder withDividerColor(String colorString) {
        mDivider.setBackgroundColor(Color.parseColor(colorString));
        return this;
    }
    public SystemAlertDialogBuilder withDividerColor(int color) {
        mDivider.setBackgroundColor(color);
        return this;
    }
    
    
    public SystemAlertDialogBuilder withTitle(CharSequence title) {
        toggleView(mLinearLayoutTopView,title);
        mTitle.setText(title);
        return this;
    }
    
    public SystemAlertDialogBuilder withTitleColor(String colorString) {
        mTitle.setTextColor(Color.parseColor(colorString));
        return this;
    }
    
    public SystemAlertDialogBuilder withTitleColor(int color) {
        mTitle.setTextColor(color);
        return this;
    }
    
    public SystemAlertDialogBuilder withMessage(int textResId) {
        toggleView(mLinearLayoutMsgView,textResId);
        mMessage.setText(textResId);
        return this;
    }
    
    public SystemAlertDialogBuilder withMessage(CharSequence msg) {
        toggleView(mLinearLayoutMsgView,msg);
        mMessage.setText(msg);
        return this;
    }
    public SystemAlertDialogBuilder withMessageColor(String colorString) {
        mMessage.setTextColor(Color.parseColor(colorString));
        return this;
    }
    public SystemAlertDialogBuilder withMessageColor(int color) {
        mMessage.setTextColor(color);
        return this;
    }
    
    public SystemAlertDialogBuilder withDialogColor(String colorString) {
        mLinearLayoutView.getBackground().setColorFilter(ColorUtils.getColorFilter(Color.parseColor(colorString)));
        return this;
    }
    
    public SystemAlertDialogBuilder withDialogColor(int color) {
        mLinearLayoutView.getBackground().setColorFilter(ColorUtils.getColorFilter(color));
        return this;
    }
    
    public SystemAlertDialogBuilder withIcon(int drawableResId) {
        mIcon.setImageResource(drawableResId);
        return this;
    }
    
    public SystemAlertDialogBuilder withIcon(Drawable icon) {
        mIcon.setImageDrawable(icon);
        return this;
    }
    
    public SystemAlertDialogBuilder withDuration(int duration) {
        this.mDuration=duration;
        return this;
    }
    
    public SystemAlertDialogBuilder withEffect(Effectstype type) {
        this.type=type;
        return this;
    }
    
    public SystemAlertDialogBuilder withButtonDrawable(int resid) {
        mButton1.setBackgroundResource(resid);
        mButton2.setBackgroundResource(resid);
        return this;
    }
    public SystemAlertDialogBuilder withButton1Text(CharSequence text) {
        mButton1.setVisibility(View.VISIBLE);
        mButton1.setText(text);
        
        return this;
    }
    public SystemAlertDialogBuilder withButton2Text(CharSequence text) {
        mButton2.setVisibility(View.VISIBLE);
        mButton2.setText(text);
        return this;
    }
    public SystemAlertDialogBuilder setButton1Click(View.OnClickListener click) {
        mButton1.setOnClickListener(click);
        return this;
    }
    
    public SystemAlertDialogBuilder setButton2Click(View.OnClickListener click) {
        mButton2.setOnClickListener(click);
        return this;
    }
    
    
    public SystemAlertDialogBuilder setCustomView(int resId, Context context) {
        View customView = View.inflate(context, resId, null);
        if (mFrameLayoutCustomView.getChildCount()>0){
            mFrameLayoutCustomView.removeAllViews();
        }
        mFrameLayoutCustomView.addView(customView);
        return this;
    }
    
    public SystemAlertDialogBuilder setCustomView(View view, Context context) {
        if (mFrameLayoutCustomView.getChildCount()>0){
            mFrameLayoutCustomView.removeAllViews();
        }
        mFrameLayoutCustomView.addView(view);
        
        return this;
    }
    public SystemAlertDialogBuilder isCancelableOnTouchOutside(boolean cancelable) {
        this.isCancelable=cancelable;
        this.setCanceledOnTouchOutside(cancelable);
        return this;
    }
    
    public SystemAlertDialogBuilder isCancelable(boolean cancelable) {
        this.isCancelable=cancelable;
        this.setCancelable(cancelable);
        return this;
    }
    
    private void toggleView(View view, Object obj){
        if (obj==null){
            view.setVisibility(View.GONE);
        }else {
            view.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void show() {
        super.show();
    }
    
    private void start(Effectstype type){
        BaseEffects animator = type.getAnimator();
        if(mDuration != -1){
            animator.setDuration(Math.abs(mDuration));
        }
        animator.start(mRelativeLayoutView);
    }
    
    @Override
    public void dismiss() {
        super.dismiss();
        mButton1.setVisibility(View.GONE);
        mButton2.setVisibility(View.GONE);
    }
}
