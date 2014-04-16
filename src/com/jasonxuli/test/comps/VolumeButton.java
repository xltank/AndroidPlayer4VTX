package com.jasonxuli.test.comps;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.jasonxuli.test.R;

public class VolumeButton extends RelativeLayout {

	private ImageButton volumeMuted;
	private ImageButton volumeLow;
	private ImageButton volumeMid;
	private ImageButton volumeHigh;
	
	private List<ImageButton> buttons = new ArrayList<ImageButton>(); 
	
	
	public VolumeButton(Context context) {
		super(context);
		initView(context);
	}

	public VolumeButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public VolumeButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	
	private void initView(Context context)
	{
		addView(inflate(context, R.layout.button_volume, null));
		
		volumeMuted = (ImageButton) findViewById(R.id.volume_muted);
		volumeLow = (ImageButton) findViewById(R.id.volume_low);
		volumeMid = (ImageButton) findViewById(R.id.volume_mid);
		volumeHigh = (ImageButton) findViewById(R.id.volume_high);
		
		buttons.add(volumeMuted);
		buttons.add(volumeLow);
		buttons.add(volumeMid);
		buttons.add(volumeHigh);
	}
	
	/**
	 * @param volume 0-100
	 */
	public void setVolume(int volume)
	{
		for (ImageButton btn : buttons) {
			btn.setVisibility(INVISIBLE);
		}
		if(volume <= 0)
			volumeMuted.setVisibility(VISIBLE);
		else if(volume > 0 && volume < 33)
			volumeLow.setVisibility(VISIBLE);
		else if(volume >= 33 && volume < 67)
			volumeMid.setVisibility(VISIBLE);
		else if(volume >= 67)
			volumeHigh.setVisibility(VISIBLE);
	}
	
}
