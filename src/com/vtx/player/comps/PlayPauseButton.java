package com.vtx.player.comps;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.vtx.player.R;

public class PlayPauseButton extends RelativeLayout {


	private ImageButton playButton;
	private ImageButton pauseButton;
	
	
	public PlayPauseButton(Context context) {
		super(context);
		initView(context);
	}

	// this is called.
	public PlayPauseButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public PlayPauseButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	
	private void initView(Context context)
	{
		addView(inflate(context, R.layout.button_play_pause, null));
		
		playButton = (ImageButton) findViewById(R.id.play_button_);
		playButton.setOnClickListener(onPlayButtonClick);
		
		pauseButton = (ImageButton) findViewById(R.id.pause_button_);
		pauseButton.setOnClickListener(onPauseButtonClick);
	}
	
	
	public void setPlayState(Boolean isPlay)
	{
		if(isPlay)
			setStatePlay();
		else
			setStatePause();
	}
	
	
	private OnClickListener onPlayButtonClick = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			mlistener.onClick(playButton, "play");
			setStatePlay();
		}
	};
	private void setStatePlay()
	{
		playButton.setVisibility(INVISIBLE);
		pauseButton.setVisibility(VISIBLE);
	}
	
	
	private OnClickListener onPauseButtonClick = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			mlistener.onClick(pauseButton, "pause");
			setStatePause();
		}
	};
	private void setStatePause()
	{
		playButton.setVisibility(VISIBLE);
		pauseButton.setVisibility(INVISIBLE);
	}
	
	
	
	private OnPlayButtonStateChangeListener mlistener;
	public void setOnPlayButtonStateChangeListener(OnPlayButtonStateChangeListener listener)
	{
		mlistener = listener;
	}
	
	public interface OnPlayButtonStateChangeListener{
		void onClick(View v, String state);
	}
}
