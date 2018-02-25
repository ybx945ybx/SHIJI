package cn.yiya.shiji.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class AllListView extends ListView {

	public AllListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public AllListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AllListView(Context context) {
		super(context);
	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
	
}
