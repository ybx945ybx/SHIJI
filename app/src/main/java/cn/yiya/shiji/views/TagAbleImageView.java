package cn.yiya.shiji.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

import cn.yiya.shiji.utils.SimpleUtils;

public class TagAbleImageView extends FrameLayout implements View.OnClickListener{

	public static final int MODE_VIEW = 0; // 浏览模式
	public static final int MODE_EDITE = 1; // 编辑模式
	public static final int wChild = 20; // Tag的宽
	public static final int hChild = 20;// Tag的高
	public static final int ClickTarget = 200; // 点击 ms
	public static final int LongPressTarget = 2000; // 长按 ms
	public static final int DoubleClickDuration = 1000; // 双击 ms
	public static final int TAGSIZE = 4; // 添加TAG的最多数目 从0开始计
	private int downX = -100, downY = -100;
    private int nCycleX, nCycleY;
    private int firstX = 0, firstY = 0;
	private long downTime = 0;
	private int screenWidth=0;
	private int leftOffset = 0;
	private int topOffset = 0;
    int x = 0;
    int y = 0;
	public static final String Diraction = "Diraction";
	/**
	 * 模式
	 */
	private int mode = 0;
	/**
	 * 是否隐藏标签
	 */
	private boolean isTagHidden = false;
	/**
	 * 是否已经设置好图片
	 */
	private boolean isImageOK = false;

    private int nDirection = 0;

	private ImageView imageBackground;
	private TagView touchView;
	private ArrayList<View> tagViews;
	private PopupWindow popw;
    private Context mContext;
    private Runnable mLongPressRunnable;

    // 是否移动了
    private boolean isMoved;
    // 是否释放了
    private boolean isReleased;
    // 是否第一次添加
    private boolean isFirst = true;

	@SuppressLint("NewApi")
	public TagAbleImageView(Context context, AttributeSet attrs,
                            int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
		init();
	}

	public TagAbleImageView(Context context, AttributeSet attrs,
                            int defStyleAttr) {
		super(context, attrs, defStyleAttr);
        mContext = context;
		init();
	}

	public TagAbleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
        mContext = context;
		init();
	}

	public TagAbleImageView(Context context) {
		super(context);
        mContext = context;
		init();
	}

	public void setEditable(boolean able){
		if(able){
			mode = MODE_EDITE;
		}else{
			mode = MODE_VIEW;
		}
	}

    public void setNoDraw(boolean bDraw) {
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            if (v instanceof TagView){
                ((TagView)v).setNoDraw(bDraw);
            }
        }
    }
	
	/**
	 * 初始化ImageView
	 */
	private void initImageView() {
		imageBackground = new ImageView(getContext());
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		imageBackground.setPadding(0,0,0,0);
		imageBackground.setScaleType(ScaleType.FIT_CENTER);
		addView(imageBackground, lp);
	}

	/**
	 * 初始化控件
	 */
	private void init() {
		setClickable(true);
		tagViews = new ArrayList<View>();
		initImageView();
		screenWidth = SimpleUtils.getScreenWidth(getContext()) - SimpleUtils.dp2px(mContext, 32);
	}

	public void setImageBitmap(Bitmap bmp) {
		imageBackground.setImageBitmap(bmp);
		isImageOK = true;
	}

	/**
	 * 获取ImageView 方便定制 或者 使用第三方库加载图片等
	 * 
	 * @return
	 */
	public ImageView getImageView() {
		isImageOK = true;
		return imageBackground;
	}

	/**
	 * 判断点击的位置是否有子View
	 * 
	 * @param x
	 * @param y
	 * @return boolean
	 */
	private boolean hasView(int x, int y) {
		// 循环获取子view，判断xy是否在子view上，即判断是否按住了子view
		for (int index = this.getChildCount() - 1; index >= 0; index--) {
			View view = this.getChildAt(index);
			int left = view.getLeft();
			int top = view.getTop();
			int right = view.getRight();
			int bottom = view.getBottom();
			Rect rect = new Rect(left, top, right, bottom);
			boolean contains = rect.contains(x, y);
			// 如果是与子view重叠则返回真,表示已经有了view不需要添加新view了
			if (contains) {
				if (view == imageBackground) {
					break;
				}
				touchView = (TagView)view;
//				touchView.bringToFront();
				return true;
			}
		}
		touchView = null;
		return false;
	}

    // 判断点击位置是否处于标签上
    private int onClickView(int x, int y) {
        for (int index = this.getChildCount() - 1; index >= 0; index--) {
            View view = this.getChildAt(index);
            if (view instanceof TagView){
                int count = ((TagView) view).getChildCount();
                for (int i = 0; i < count; i++) {
                    View childView = ((TagView) view).getChildAt(i);
                    if (childView instanceof TextView) {
                        int left = view.getLeft() + childView.getLeft();
                        int top = view.getTop() + childView.getTop();
                        int right = left + childView.getMeasuredWidth();
                        int bottom = top + childView.getMeasuredHeight();
                        Rect rect = new Rect(left, top, right, bottom);
                        boolean contains = rect.contains(x, y);
                        if (contains)
                            return 1;
                    }

                    if (childView instanceof LinearLayout) {
                        int left = view.getLeft() + childView.getLeft();
                        int top = view.getTop() + childView.getTop();
                        int right = left + childView.getMeasuredWidth();
                        int bottom = top + childView.getMeasuredHeight();
                        Rect rect = new Rect(left, top, right, bottom);
                        boolean contains = rect.contains(x, y);
                        if (contains)
                            return 2;
                    }
                }

//                int left = view.getLeft();
//                int top = view.getTop();
//                int right = left + ((TagView) view).getViewWidth();
//                int bottom = top + ((TagView) view).getViewHeight();
//                Rect rect = new Rect(left, top, right, bottom);
//                boolean contains = rect.contains(x, y);
//                if (contains) {
//                    return true;
//                }
            }
        }
        return 0;
    }

	/**
	 * 添加一个标签
	 */
	public void addTag(ArrayList<TagInfo> tagInfos, int count) {
		mTags = tagInfos;
        int w = screenWidth;
        int h = w;
        String title = "";
        String country = "";
        String money = "";
        int nDirection = 0;

        TagView view = new TagView(mContext);

        for (TagInfo tag : tagInfos){
                if (tag.getType() == 1) {
                    String strType1 = "";
                    if (!TextUtils.isEmpty(tag.getContent())){
                        strType1 = tag.getContent() + " ";
                    }
                    title = strType1 + setNullText(tag.getDetails());
                }else {
                    if (tag.getType() == 3) {
                        String strType2 = "";
                        if (!TextUtils.isEmpty(tag.getContent())) {
                            strType2 = tag.getContent() + " ";
                        }
                        country = strType2 + setNullText(tag.getDetails());
                    }

                    if (tag.getType() == 2) {
                        String strType3 = "";
                        if (!TextUtils.isEmpty(tag.getContent())) {
                            strType3 = tag.getContent() + " ";
                        }
                        money = strType3 + setNullText(tag.getDetails());
                    }
            }
            if (!(tag.getX() == 0 && tag.getY() == 0)) {
                downX = (int) (w * tag.getX());
                downY = (int) (h * tag.getY());
            } else {
                if (tag.getX() == 0) {
                    downX = wChild;
                    if (tag.getY() > 0)
                        downY = (int) (h * tag.getY());
                }
                if (tag.getY() == 0) {
                    if (tag.getX() > 0)
                        downX = (int) (w * tag.getX());
                    downY = hChild;
                }
            }
        }
        if (downX <= w/2 && downY <= w/2){
            nDirection = 0;
        } else if (downX > w/2 && downY < w/2){
            nDirection = 1;
        } else if (downX < w/2 && downY > w/2){
            nDirection = 2;
        } else if (downX >= w/2 && downY >= w/2){
            nDirection = 3;
        } else {
            nDirection = 0;
        }
        view.setTitleText(title);
        view.setFooterText(country, money);
        view.setDirection(nDirection);

        view.setAnimationCount(count);


        int left = downX;
        int top = downY;

		if (downX + wChild > w) {
			left = w - wChild;
		} else {
			left = downX;
		}

		if (downY + hChild > w) {
			top = w - hChild;
		} else {
			top = downY;
		}

        if (downX <= 0 && downY <= 0) {
            left = 20;
            top = 20;
        }

		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.width = w;

        switch (nDirection){
            case 0:
                lp.leftMargin = left - SimpleUtils.dp2px(mContext, 10);
                lp.topMargin = top - SimpleUtils.dp2px(mContext, 10);
                if (lp.leftMargin + view.getViewWidth() > w) {
                    lp.rightMargin = w;
                }
                break;
            case 1:
                lp.leftMargin = left - view.getViewWidth() - SimpleUtils.dp2px(mContext, 10);
                lp.topMargin = top - - SimpleUtils.dp2px(mContext, 10);
                if (view.getViewWidth() - left  > 0) {
                    lp.leftMargin = 0;
                }
                break;
            case 2:
                lp.leftMargin = left - SimpleUtils.dp2px(mContext, 10);
                lp.topMargin = top - view.getViewHeight() - - SimpleUtils.dp2px(mContext, 10);
                if (lp.leftMargin + view.getViewWidth() > w) {
                    lp.rightMargin = w;
                }
                break;
            case 3:
                lp.leftMargin = left - view.getViewWidth() - SimpleUtils.dp2px(mContext, 10);
                lp.topMargin = top - view.getViewHeight() - SimpleUtils.dp2px(mContext, 10);
                if (view.getViewWidth() - left > 0) {
                    lp.leftMargin = 0;
                }
                break;
        }

        view.setVisible();
        view.setLayoutParams(lp);

		tagViews.add(view);
        if (TextUtils.isEmpty(title) && TextUtils.isEmpty(country) && TextUtils.isEmpty(money)){

        }else {
            addView(view, lp);
        }

	}

    public String setNullText(String str) {
        if (str == null) {
            return "";
        }
        return str;
    }

    public int getCycleX() {
        return nCycleX;
    }

    public int getCycleY() {
        return nCycleY;
    }

    private ArrayList<TagInfo> mTags = new ArrayList<>();
	/**
	 * 初始化显示一组标签
	 */
	public void initTags(ArrayList<TagInfo> tags, int count) {
        isFirst = true;
		if(tagViews != null){
			for(View v : tagViews){
				removeView(v);
			}
		}
		if(tags == null)
            return;

        addTag(tags, count);

	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev);
	}

	@SuppressLint("ClickableViewAccessibility") @Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if(popw!=null && popw.isShowing()){
				popw.dismiss();
			}
            x = (int) event.getX();
            y = (int) event.getY();
			downX = x;
			downY = y;

            if (isFirst) {
                getCycle();
            }
			downTime = System.currentTimeMillis();

			touchView = null;
            if (hasView(x, y)) {
            }

            isReleased = false;
            isMoved = false;

            postDelayed(mLongPressRunnable, ViewConfiguration.getLongPressTimeout());// 按下 3秒后调用线程
            mLongPressRunnable = new Runnable() {
                @Override
                public void run() {
                    if (isReleased || isMoved)
                        return;
                    if(customEventListener != null && touchView != null){
                        customEventListener.onTagLongClick(touchView);
                        return;
                    }
                }
            };
			break;
		case MotionEvent.ACTION_MOVE:

            int fx = (int)event.getX();
            int fy = (int)event.getY();
			if(mode == MODE_EDITE) {
                if (Math.abs(x - fx)> 50 || Math.abs(y - fy) > 50) {
                    moveView(fx, fy);
                    isMoved = true;
                    removeCallbacks(mLongPressRunnable);
                }
            }
			break;
		case MotionEvent.ACTION_UP:
            isReleased = true;
            removeCallbacks(mLongPressRunnable);
            if(imageBackground.getDrawable() == null){
				if(mode==MODE_EDITE){
					if(customEventListener!=null){
//						customEventListener.onAddImage();
					}
				}
				break;
			}

			if(isClick((int) event.getX(), (int) event.getY())){
				if(mode==MODE_EDITE){
					if(touchView == null && tagViews.size()< 5){
						if(customEventListener!=null){
							customEventListener.onAddTag();
						}
						break;
					} else {
                        if(customEventListener!=null&&touchView!=null){
//                            customEventListener.onTagClick(touchView);
                        }
                    }
					if(touchView==null) break;

				}else{
                    // 如果当前点击区域在标签，则响应标签点击时间，反之响应图片点击事件
                    if (onClickView(x, y) == 1) {
                        if(customEventListener != null && touchView != null)
                           customEventListener.onTagTextClick(touchView);
                    } else if (onClickView(x, y) == 2) {
                        if(customEventListener != null && touchView != null)
                            customEventListener.onTagLinearClick(touchView);
                    }else{
                        if(onClickListener != null)
                           onClickListener.onClick(this);
                    }
				}
			}
            break;
		}
		return true;

	}

    private void getCycle() {
        nCycleX = downX;
        nCycleY = downY;



        if (downX < screenWidth/2 && downY < screenWidth/2){
            nDirection = 0;
        } else if (downX > screenWidth/2 && downY < screenWidth/2){
            nDirection = 1;
        } else if (downX < screenWidth/2 && downY > screenWidth/2){
            nDirection = 2;
        } else if (downX > screenWidth/2 && downY > screenWidth/2){
            nDirection = 3;
        } else {
            nDirection = 0;
        }

        isFirst = false;
    }

	private void moveView(int fx, int fy) {
		if (touchView == null || mode == MODE_VIEW)
			return;

        int left = fx - touchView.getViewWidth() / 2;
        int top = fy - touchView.getViewHeight() / 2;

        if (left < 0) {
            left = 0;
        }

        if (top < 0) {
            top = 0;
        }

        int sWidth = SimpleUtils.getScreenWidth(mContext);

        int width = left + touchView.getViewWidth();
        int height = top + touchView.getViewHeight();

        if (width > sWidth) {
            width = sWidth;
            left = sWidth - touchView.getViewWidth();
        }
        if (height > sWidth){
            height = sWidth;
            top = sWidth - touchView.getViewHeight();
        }

        touchView.layout(left, top, width, height);

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        if (left == 0 && top <= sWidth / 2) {
            lp.leftMargin = 0;
            lp.topMargin = top;
            nDirection = 0;
            touchView.setDirection(nDirection);
            touchView.setLayoutParams(lp);
            touchView.setStartDraw(nDirection);
            nCycleX = 0; nCycleY = top;
        } else if (width == sWidth && top <= sWidth / 2) {
            lp.leftMargin = sWidth - touchView.getViewWidth();
            lp.topMargin = top;
            nDirection = 1;
            touchView.setDirection(nDirection);
            touchView.setLayoutParams(lp);
            touchView.setStartDraw(nDirection);
            nCycleX = sWidth; nCycleY = lp.topMargin;
        } else if (left == 0 && top > sWidth / 2){
            lp.leftMargin = 0;
            lp.topMargin = top;
            nDirection = 2;
            touchView.setDirection(nDirection);
            touchView.setLayoutParams(lp);
            touchView.setStartDraw(nDirection);
            nCycleX = 0; nCycleY = lp.topMargin + touchView.getViewHeight();
        } else if (width == sWidth && top > sWidth / 2) {
            lp.leftMargin = sWidth - touchView.getViewWidth();
            lp.topMargin = top;
            nDirection = 3;
            touchView.setDirection(nDirection);
            touchView.setLayoutParams(lp);
            touchView.setStartDraw(nDirection);
            nCycleX = sWidth; nCycleY = lp.topMargin + touchView.getViewHeight();
        } else {
            switch (nDirection) {
                case 0:
                    nCycleX = left;
                    nCycleY = top;
                    break;
                case 1:
                    nCycleX = left + touchView.getViewWidth();
                    nCycleY = top;
                    break;
                case 2:
                    nCycleX = left;
                    nCycleY = top + touchView.getViewHeight();
                    break;
                case 3:
                    nCycleX = left + touchView.getViewWidth();
                    nCycleY = top + touchView.getViewHeight();
                    break;
            }
        }

        for (int i = 0; i < mTags.size(); i++) {
            TagInfo info = mTags.get(i);
            info.setX((float)nCycleX / (float)sWidth);
            info.setY((float)nCycleY / (float)sWidth);
        }

        invalidate();
	}

	/**
	 * 判断是否是单击
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean isClick(int x, int y){
		if(System.currentTimeMillis() - downTime > ClickTarget){
			return false;
		}
		if(Math.abs(x-downX)>10 || Math.abs(y-downY)>10){
			return false;
		}
		
		return true;
	}
	
	@Override
	public void onClick(View v) {
		if(touchView==null){ 
			if(popw!=null){
				popw.dismiss();
			}
			return;
		}
		tagViews.remove(touchView);
		removeView(touchView);
		popw.dismiss();
	}
	/**
	 * tag的信息
	 */
	public static class TagInfo{
		public static final int Left = 0;
		public static final int Right = 1;
		public static final int Top = 2;
		public static final int Bottom = 3;
		/**
		 *  0<= top <=1 
		 */
		private float y;
		/**
		 *  0<= top <=1 
		 */
		private float x;
        private String details;
		private String content;
		private int direction = 0;
		private int type = 0;
        private int group_id;
		public float getY() {
			return y;
		}
		public void setY(float top) {
			if(top<0 || top >1){
				return;
			}
			y = top;
		}
		public float getX() {
			return x;
		}
		public void setX(float left) {
			if(left<0 || left >1){
				return;
			}
			x = left;
		}

		public int getType(){
			return  type;
		}
		public void setType(int type){
			this.type = type;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String tag) {
			this.content = tag;
		}
		public int getDirection() {
			return direction;
		}

        public String getDetails() {
            if(details==null) return "";
            return details;
        }

        public int getGroup_id() {
            return group_id;
        }

        public void setGroup_id(int group_id) {
            this.group_id = group_id;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public void setDirection(int direction) {
			this.direction = direction;
		}
	}
	
	public ArrayList<TagInfo> getTagsInfo(){
		return mTags;
	}

	private CustomEventListener  customEventListener;

	public void setCustomEventListener(CustomEventListener listener){
		customEventListener = listener;
	}
	public interface CustomEventListener{
		public void onAddTag();
		public void onTagLinearClick(TagView tagView);
        public void onTagTextClick(TagView tagView);
        public void onTagLongClick(TagView tagView);
	}

	private OnClickListener onClickListener;

	@Override
	public void setOnClickListener(OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}
}
