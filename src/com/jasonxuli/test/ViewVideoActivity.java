package com.jasonxuli.test;

import java.io.IOException;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnTimedTextListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.media.TimedText;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.jasonxuli.test.comps.CustomRelativeLayout;
import com.jasonxuli.test.constants.MessageConstant;
import com.jasonxuli.test.control.ImageManager;
import com.jasonxuli.test.utils.CommonUtil;
import com.jasonxuli.test.utils.VideoUtil;
import com.jasonxuli.test.vo.ThumbnailsVTX;
import com.jasonxuli.test.vo.VideoInfo;

public class ViewVideoActivity extends Activity 
	implements Callback, OnPreparedListener, OnBufferingUpdateListener, OnVideoSizeChangedListener,
	OnCompletionListener, OnSeekCompleteListener, OnErrorListener, OnInfoListener{

	private final String LOG_TAG = "ViewVideoActivity";
	private final boolean AUTO_HIDE = true;
	private final int AUTO_HIDE_DELAY_MILLIS = 3000;
	
	private SurfaceView playerView;
	private MediaPlayer player;
	
	private VideoInfo videoInfo;
	private String videoUrl;
	
	private Timer timer;

	private CustomRelativeLayout playerViewContainer;
	private LinearLayout controlBar;
	private ImageView snapshot;
	private TextView timeLabel;
	private SeekBar seekBar;
	private ImageButton playButton;
	private ImageButton pauseButton;
	private ImageButton fullScreenButton;
	private ImageButton fullScreenExitButton;
	private TextView videoTitle;
	private TextView videoDesc;
	
	private int bufferPercent;
	private int duration;
	private int videoWidth;
	private int videoHeight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.w(LOG_TAG, "ViewVideoActivity  onCreate");
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		setContentView(R.layout.activity_view_video);
//		setupActionBar();
		
		Intent intent = getIntent();
		String videoJSON = intent.getStringExtra(MessageConstant.VIDEO_INFO_JSON);
		videoInfo = VideoUtil.parseVideoInfoJSON(videoJSON);
		videoUrl = videoInfo.renditions.get(0).getUrl();
		
		// should we remove all listeners? 
		//or we need not to do that because they are all in the same activity?
		initListeners(); 
		initPlayer();
	}
	
	private void initListeners()
	{
		playerViewContainer = (CustomRelativeLayout) findViewById(R.id.playerContainer);
		playerViewContainer.setOnSizeChangedListener(onPlayerViewContainerSizeChcanged);
		
		playerViewContainer.setOnTouchListener(onPlayerViewContainerTouchListener);
		playerViewContainer.setOnClickListener(onPlayerViewContainerClickListener);
		
		controlBar = (LinearLayout) findViewById(R.id.controlBar);
		
		timeLabel = (TextView) findViewById(R.id.time);
		
		seekBar = (SeekBar) findViewById(R.id.seekBar);
		seekBar.setOnSeekBarChangeListener(onSeekBarChange);
		
		playButton = (ImageButton) findViewById(R.id.play_button);
		playButton.setOnClickListener(onPlayButtonClick);
		
		pauseButton = (ImageButton) findViewById(R.id.pause_button);
		pauseButton.setOnClickListener(onPauseButtonClick);
		
		fullScreenButton = (ImageButton) findViewById(R.id.fullscreen_button);
		fullScreenButton.setOnClickListener(onFullScreenButtonClick);
		
		fullScreenExitButton = (ImageButton) findViewById(R.id.fullscreen_exit_button);
		fullScreenExitButton.setOnClickListener(onFullScreenExitButtonClick);
		
		videoTitle = (TextView) findViewById(R.id.video_title);
		videoTitle.setText(videoInfo.title);
		
		videoDesc = (TextView) findViewById(R.id.video_desc);
		videoDesc.setText(videoInfo.description);
	}
	
	private void initPlayer()
	{
		setSnapshot();
		
		playerView = (SurfaceView) findViewById(R.id.playerView);
		playerView.getHolder().addCallback(this);
		
		player = new MediaPlayer();
		player.setOnPreparedListener(this);
		player.setOnBufferingUpdateListener(this);
		player.setOnVideoSizeChangedListener(this);
		player.setOnCompletionListener(this);
		player.setOnSeekCompleteListener(this);
		player.setOnErrorListener(this);
		player.setOnInfoListener(this);
		
		try {
			player.setDataSource(videoUrl);
			player.prepare();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setSnapshot()
	{
		snapshot = (ImageView) findViewById(R.id.snapshot);
		String snapshotUrl ="";
		Collections.sort(videoInfo.thumbnails);
		int containerW = playerViewContainer.getWidth();
		int containerH = playerViewContainer.getHeight();
		for(ThumbnailsVTX t : videoInfo.thumbnails)
		{
			if(t.width < containerW && t.height < containerH)
				continue;
			else
				snapshotUrl = t.url;
		}
		ImageManager.ins().loadImage(snapshotUrl, snapshot, 1);
	}
	
	
	////////////// player event listeners
	
	public void onPrepared(MediaPlayer mp) {
//		Log.w(LOG_TAG, "onPrepared");
		duration = player.getDuration();
		videoWidth = player.getVideoWidth();
		videoHeight = player.getVideoHeight();
		layoutVideo();
	}
	
	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
	}
	
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
//		Log.w(LOG_TAG, "onBufferingUpdate : " + percent);
		bufferPercent = percent;
	}

	public boolean onInfo(MediaPlayer mp, int what, int extra) {
//		Log.w(LOG_TAG, "onInfo : " + what + " , "  + extra);
		switch (what) {
		case MediaPlayer.MEDIA_INFO_UNKNOWN :
			break;
		case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING :
			break;
		case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START :
			Log.w(LOG_TAG, "onInfo : MEDIA_INFO_VIDEO_RENDERING_START");
			break;
		case MediaPlayer.MEDIA_INFO_BUFFERING_START :
			Log.w(LOG_TAG, "onInfo : MEDIA_INFO_BUFFERING_START");
			break;
		case MediaPlayer.MEDIA_INFO_BUFFERING_END :
			Log.w(LOG_TAG, "onInfo : MEDIA_INFO_BUFFERING_END");
			break;
		case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING :
			break;
		case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE :
			break;
		case MediaPlayer.MEDIA_INFO_METADATA_UPDATE :
			Log.w(LOG_TAG, "onInfo : MEDIA_INFO_METADATA_UPDATE");
			break;
		case MediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE :
			break;
		case MediaPlayer.MEDIA_INFO_SUBTITLE_TIMED_OUT :
			break;
		}
		return false;
	}

	public boolean onError(MediaPlayer mp, int what, int extra) {
		Log.w(LOG_TAG, "onError : " + what + " , "  + extra);
		return false;
	}

	public void onSeekComplete(MediaPlayer mp) {
		Log.w(LOG_TAG, "onSeekComplete : " + mp.getCurrentPosition());
	}

	public void onCompletion(MediaPlayer mp) {

		setPlayingState(false);
	}


	////////// SurfaceView implements
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		Log.w(LOG_TAG, "surface Changed");
	}
	// when activity is paused, surface is destroyed.
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.w(LOG_TAG, "surface Destroyed");
	}
	// when activity is started/restarted, surface is created.
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.w(LOG_TAG, "surface Created");
		startPlaying();
	}

	private void startPlaying()
	{
		player.setDisplay(playerView.getHolder());
		player.start();
		snapshot.setVisibility(View.INVISIBLE);// 
		
		setPlayingState(true);
		
		startTimer();
	}
	
	private void startTimer()
	{
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() 
		{
			@Override
			public void run() {
				if(player != null && player.isPlaying())
				{
					timeLabel.post(new Runnable() 
					{	
						@Override
						public void run() {
							playProgressUpdate();
						}
					});
				}
			}
		}, 0, 200);
	}
	
	private void playProgressUpdate()
	{
		if(player == null)
			return ;
			
		if(timeLabel != null)
			timeLabel.setText(player.getCurrentPosition()+"");
		
		if(seekBar != null)
		{
			seekBar.setMax(player.getDuration());
			seekBar.setProgress(player.getCurrentPosition());
			seekBar.setSecondaryProgress((int) (bufferPercent * 0.01 * duration));
			
			String time = CommonUtil.formatDuration(player.getCurrentPosition());
			String duration = CommonUtil.formatDuration(player.getDuration());
			timeLabel.setText(time+"/"+duration);
		}
	}
	
	
	final CustomRelativeLayout.OnSizeChangedListener onPlayerViewContainerSizeChcanged = new CustomRelativeLayout.OnSizeChangedListener() 
	{
		private int oldW;
		private int oldH;
		@Override
		public void onEvent() 
		{
			int w = playerViewContainer.getWidth();
			int h = playerViewContainer.getHeight();
			if(oldW != w || oldH != h)
			{
				oldW = w;
				oldH = h;
				layoutVideo();
			}
		}
	};
	
	private void layoutVideo()
	{
		if(player == null || playerView == null)
			return ;
		
		if(videoWidth == 0 || videoHeight == 0)
			return ;
		
		View playerViewParent = (View) playerView.getParent();
		int containerW = playerViewParent.getWidth();
		int containerH = playerViewParent.getHeight();
		if(containerW == 0 || containerH == 0)
			return ;
		
		Point newSize = CommonUtil.getSuitableSize(videoWidth, videoHeight, containerW, containerH);
		Log.w(LOG_TAG, newSize.x + "," + newSize.y);
		// !!! when setLayoutParams is called rapidly, video will not show.
		playerView.setLayoutParams(new RelativeLayout.LayoutParams(newSize.x, newSize.y));
		
		playerView.setX((containerW - newSize.x)/2);
		playerView.setY((containerH - newSize.y)/2);
	}
	
	
	//////////////// play control
	
	private OnClickListener onPlayButtonClick = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			player.start();
			setPlayingState(true);
		}
	};
	
	private OnClickListener onPauseButtonClick = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			player.pause();
			setPlayingState(false);
		}
	};
	
	private void setPlayingState(Boolean playing)
	{
		playButton.setVisibility(playing ? View.INVISIBLE : View.VISIBLE);
		pauseButton.setVisibility(playing ? View.VISIBLE : View.INVISIBLE);
	}
	
	private OnClickListener onFullScreenButtonClick = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			toFullScreen();
		}
	};
	
	private void toFullScreen()
	{
		player.pause();
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		fullScreenButton.setVisibility(View.INVISIBLE);
		fullScreenExitButton.setVisibility(View.VISIBLE);
		findViewById(R.id.gap).setVisibility(View.GONE);
		findViewById(R.id.video_info).setVisibility(View.GONE);
		
		WindowManager.LayoutParams attrs = getWindow().getAttributes();
		attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
		getWindow().setAttributes(attrs); 

		layoutVideo();
		player.start();
	}
	
	private OnClickListener onFullScreenExitButtonClick = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			player.pause();
			
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			fullScreenButton.setVisibility(View.VISIBLE);
			fullScreenExitButton.setVisibility(View.INVISIBLE);
			findViewById(R.id.gap).setVisibility(View.VISIBLE);
			findViewById(R.id.video_info).setVisibility(View.VISIBLE);

			WindowManager.LayoutParams attrs = getWindow().getAttributes();
			attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
			getWindow().setAttributes(attrs);
			
			layoutVideo();
			player.start();
		}
	};
	
	
	private final int LEFT = 1;
	private final int RIGHT = 2;
	private final int TOP = 3;
	private final int BOTTOM = 4;
	final OnTouchListener onPlayerViewContainerTouchListener = new OnTouchListener() 
	{	
		int type=0; // progress=1, volume=2, brightness=3;
		int direction=0; // left=1, right=2, top=3, bottom=4;
		int area=0; // left part=1, right part=2;
		float value=0;
		@Override
		public boolean onTouch(View v, MotionEvent event) 
		{
			int action = event.getActionMasked();
			switch(action) {
		        case (MotionEvent.ACTION_DOWN) :
		            Log.w(LOG_TAG,"ACTION_DOWN");
		        	//TODO: show indicator(play time, volume, brightness)
		        
		        	//TODO: judge brightness(left) or volume(right)
		        	int rectW = playerViewContainer.getWidth();
	        		area = event.getX()*2 < rectW ? 1 : 2;
		            return true;
		        case (MotionEvent.ACTION_MOVE) :
		        	float xValue = event.getX()-event.getHistoricalX(event.getHistorySize());
		        	float yValue = event.getY()-event.getHistoricalY(event.getHistorySize());
		        	Log.w(LOG_TAG,"ACTION_MOVE" + xValue + "," + yValue);
		        	
		        	if(direction == 0) // get type(direction)
		        	{
			        	// 5 : minimal x/y value to trigger this kind of quick operation for the first move.
			        	if(Math.abs(xValue) < 5 || Math.abs(yValue) < 5)
			        		return true;
			        	
			        	float diff = Math.abs(xValue) - Math.abs(yValue);
			        	if(diff > 0)
			        		direction = xValue > 0 ? RIGHT : LEFT;
		        		else
		        			direction = yValue > 0 ? BOTTOM : TOP;
		        	}
		        	//TODO: calculate real value.
		        	switch (direction) {
						case LEFT:
							value = xValue;
							break;
						case RIGHT:
							value = xValue;
							break;
						case TOP:
							
							break;
						case BOTTOM:
							
							break;
					}
		        	
		            
		            return true;
		        case (MotionEvent.ACTION_UP) :
		            Log.w(LOG_TAG,"ACTION_UP");
		        	direction = 0;
		        	type = 0;
		            return true;
		        case (MotionEvent.ACTION_CANCEL) :
		            Log.w(LOG_TAG,"ACTION_CANCEL");
		            return true;
		        case (MotionEvent.ACTION_OUTSIDE) :
		            Log.w(LOG_TAG,"ACTION_OUTSIDE");
			        direction = 0;
		        	type = 0;
		            return true;      
		    }      
			// TODO: left->rewind, right->fast forward, left part->brightness, right part->volume.
			
			return false;
		}
	};
	
	final OnClickListener onPlayerViewContainerClickListener = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			controlBar.setVisibility(View.VISIBLE);
			if (AUTO_HIDE) 
			{
				mHideHandler.removeCallbacks(mHideRunnable);
				mHideHandler.postDelayed(mHideRunnable, AUTO_HIDE_DELAY_MILLIS);
			}
		}
	};
	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			controlBar.setVisibility(View.INVISIBLE);
		}
	};
	
	
	
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		
		if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
		{
			
		}else if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
		{
			
		}
	}
	
	
	private OnSeekBarChangeListener onSeekBarChange = new OnSeekBarChangeListener() 
	{
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) 
		{
			if(!fromUser) return ;
			
			player.seekTo(progress);
		}
		
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}
	};

	
	@Override
	protected void onStart()
	{
		super.onStart();
		Log.w(LOG_TAG, "ViewVideoActivity  onStart");
	}
	
	/**
	 * Called as part of the activity lifecycle when an activity is going into
     * the background, but has not (yet) been killed.
	 */
	@Override
	protected void onPause()
	{
		super.onPause();
		if(player != null && player.isPlaying())
		{
			if(timer != null)
			{
				timer.cancel();
				timer.purge();
			}
			
			player.pause();
		}
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		Log.w(LOG_TAG, "ViewVideoActivity  onResume");
		
		startTimer();
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		Log.w(LOG_TAG, "ViewVideoActivity  onStop");
		if(player != null && player.isPlaying())
		{
			if(timer != null)
			{
				timer.cancel();
				timer.purge();
			}
			
			player.release();
			player = null;
		}
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		Log.w(LOG_TAG, "ViewVideoActivity  onDestroy");
	}
	
	@Override
	protected void onRestart()
	{
		super.onRestart();
		Log.w(LOG_TAG, "ViewVideoActivity  onRestart");
	}
	

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_video, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
