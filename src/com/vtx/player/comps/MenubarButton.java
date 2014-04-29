package com.vtx.player.comps;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

import com.vtx.player.utils.GlobalData;

/**
 * !!!NOT USED
 * @author VTX
 *
 */
public class MenubarButton extends Button {

	private Context mContext;
	
	public MenubarButton(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public MenubarButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	public MenubarButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}
	
	private void init()
	{
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		setLayoutParams(lp);
		setBackgroundColor(0xFF9999);
		setGravity(Gravity.CENTER);
		setTextSize(12);
		setPadding(0, 0, 0, 0);
		
		setOnClickListener(onClickListener);
	}
	
	final OnClickListener onClickListener = new OnClickListener() 
	{		
		@Override
		public void onClick(View v) {
			Log.w(GlobalData.DEBUG_TAG, v.getWidth()+"");
		}
	};

}
