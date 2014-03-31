package com.jasonxuli.test.comps;

import com.jasonxuli.test.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class VolumeButton extends RelativeLayout {

	private ImageButton volumeMuted;
	private ImageButton volumeLow;
	private ImageButton volumeMid;
	private ImageButton volumeHigh;
	
	
	public VolumeButton(Context context) {
		super(context);
		
		volumeMuted = (ImageButton) inflate(context, R.drawable.volume_muted_button, null);
		volumeLow = (ImageButton) inflate(context, R.drawable.volume_low_button, null);
		volumeMid = (ImageButton) inflate(context, R.drawable.volume_mid_button, null);
		volumeHigh = (ImageButton) inflate(context, R.drawable.volume_high_button, null);
		
		addView(volumeMuted);
		addView(volumeLow);
		addView(volumeMid);
		addView(volumeHigh);
	}

	public VolumeButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public VolumeButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

}
