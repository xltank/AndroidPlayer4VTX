package com.jasonxuli.test.comps;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class CustomRelativeLayout extends RelativeLayout {

	public CustomRelativeLayout(Context context) {
		super(context);
	}

	public CustomRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomRelativeLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	
	OnSizeChangedListener onSizeChangedListener;
	
	public void setOnSizeChangedListener(OnSizeChangedListener listener)
	{
		onSizeChangedListener = listener;
	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		if(onSizeChangedListener != null)
			onSizeChangedListener.onEvent();
	}
	
	public interface OnSizeChangedListener
	{
		public void onEvent();
	}
}
