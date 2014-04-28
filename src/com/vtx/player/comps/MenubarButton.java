package com.vtx.player.comps;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

public class MenubarButton extends Button {

	public MenubarButton(Context context) {
		super(context);
		init();
	}

	public MenubarButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MenubarButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init()
	{
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		setLayoutParams(lp);
		setBackgroundColor(0x00000000);
		setGravity(Gravity.CENTER);
		setTextSize(12);
		
	}

}
