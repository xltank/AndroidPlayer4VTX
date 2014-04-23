package com.vtx.player.comps;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class FloatTabPanel extends LinearLayout {

	public FloatTabPanel(Context context) 
	{
		super(context);
	}

	public FloatTabPanel(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
	}

	public FloatTabPanel(Context context, AttributeSet attrs, int defStyle) 
	{
		super(context, attrs, defStyle);
	}

	
	@Override
	public void setVisibility(int visibility)
	{
		super.setVisibility(visibility);
		
		if(visibility == View.INVISIBLE)
		{
			
		}
	}
}
