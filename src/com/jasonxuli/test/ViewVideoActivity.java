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
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.support.v4.app.NavUtils;
import android.util.Log;
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
import android.view.WindowManager.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.jasonxuli.test.comps.CustomRelativeLayout;
import com.jasonxuli.test.comps.PlayPauseButton;
import com.jasonxuli.test.comps.PlayPauseButton.OnPlayButtonStateChangeListener;
import com.jasonxuli.test.comps.VolumeButton;
import com.jasonxuli.test.constants.APIConstant;
import com.jasonxuli.test.constants.MessageConstant;
import com.jasonxuli.test.control.Facade;
import com.jasonxuli.test.control.ImageManager;
import com.jasonxuli.test.utils.CommonUtil;
import com.jasonxuli.test.utils.VideoUtil;
import com.jasonxuli.test.vo.PlaylistInfo;
import com.jasonxuli.test.vo.ThumbnailsVTX;
import com.jasonxuli.test.vo.VideoInfo;

public class ViewVideoActivity extends Activity 
	implements Callback, OnPreparedListener, OnBufferingUpdateListener, OnVideoSizeChangedListener,
	OnCompletionListener, OnSeekCompleteListener, OnErrorListener, OnInfoListener{

	private final String LOG_TAG = "ViewVideoActivity";
	private final boolean AUTO_HIDE = true;
	private final int AUTO_HIDE_DELAY_MILLIS = 3000;
	
	private String curVideoInfoJSON ;
	private VideoInfo curVideoInfo; 
	
	private String curPlaylistInfoJSON;
	private PlaylistInfo curPlaylistInfo;
	
	private SurfaceView playerView;
	private MediaPlayer player;
	private AudioManager audioManager;
	
	private VideoInfo videoInfo;
	private String videoUrl;
	
	private Timer timer;

	private CustomRelativeLayout playerViewContainer;
	private LinearLayout controlBar;
	private ImageView snapshot;
	private View slideSeekHint;
	private TextView slideSeekTime;
	private View slideVolumeHint;
	private TextView slideVolume;
	private View slideBrightnessHint;
	private TextView slideBrightness;
	
	private TextView timeLabel;
	private SeekBar seekBar;
	private PlayPauseButton playPauseButton;
//	private ImageButton playButton;
//	private ImageButton pauseButton;
	private VolumeButton volumeButton;
	private ImageButton fullScreenButton;
	private ImageButton fullScreenExitButton;
	private TextView videoTitle;
	private TextView videoDesc;
	
	private int bufferPercent;
	private long duration;
	private int videoWidth;
	private int videoHeight;
	
	private int maxVolumeIndex;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		setContentView(R.layout.activity_view_video);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
//		setupActionBar();
		
		Intent intent = getIntent();
		String videoId = intent.getStringExtra(MessageConstant.VIDEO_ID);
		String playlistId = intent.getStringExtra(MessageConstant.PLAYLIST_ID);
		String publisherId = intent.getStringExtra(MessageConstant.PUBLISHER_ID);
		if(videoId != null && publisherId != null)
		{
			getVideoInfo(videoId, publisherId);
		}
		else if(playlistId != null && publisherId != null)
		{
			getPlaylistInfo(playlistId, publisherId);
		}
	}
	
	/////////// video
	private void getVideoInfo(String videoId, String publisherId)
	{
		Facade.ins().getVideoInfo(
				onVideoInfoHandler, 
				videoId, 
				publisherId, 
				APIConstant.DEFAULT_RESULT_FORMAT, 
				APIConstant.VIDEO_TYPE_MP4);
	}
	final Handler onVideoInfoHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg){
			super.handleMessage(msg);
			
			curVideoInfoJSON = msg.getData().getString("result");
			curVideoInfo = VideoUtil.parseVideoInfoJSON(curVideoInfoJSON);
			if(curVideoInfo==null || curVideoInfo.renditions.size() == 0)
			{
				Log.e(LOG_TAG, "ERROR: video info error or no playable rendition");
				return ;
			}

			videoInfo = VideoUtil.parseVideoInfoJSON(curVideoInfoJSON);
			videoUrl = videoInfo.renditions.get(0).getUrl();
			duration = videoInfo.renditions.get(0).getDuration();
			videoWidth = videoInfo.renditions.get(0).getWidth();
			videoHeight = videoInfo.renditions.get(0).getHeight();
			
			// should we remove all listeners? 
			//or we need not to do that because they are all in the same activity?
			initListeners(); 
			initPlayer();
		}
	};
	
	/////////// playlist
	private void getPlaylistInfo(String playlistId, String publisherId)
	{
		Facade.ins().getPlaylistInfo(
				onPlaylistInfoHandler, 
				playlistId, 
				publisherId, 
				APIConstant.DEFAULT_RESULT_FORMAT, 
				APIConstant.VIDEO_TYPE_MP4);
	}
	final Handler onPlaylistInfoHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg){
			super.handleMessage(msg);
			
			curPlaylistInfoJSON = msg.getData().getString("result");
			curPlaylistInfo = VideoUtil.parsePlaylistInfoJSON(curPlaylistInfoJSON);
			if(curPlaylistInfo==null || curPlaylistInfo.videos.size() == 0)
			{
				Log.e(LOG_TAG, "ERROR: video info error or no playable rendition");
				return ;
			}
			// TODO: codes for playing playlist. 
			
			initListeners(); 
			initPlayer();
		}
	};
	
	
	private void initListeners()
	{
		audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		maxVolumeIndex = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		
		playerViewContainer = (CustomRelativeLayout) findViewById(R.id.playerContainer);
		playerViewContainer.setOnSizeChangedListener(onPlayerViewContainerSizeChcanged);
		
		slideSeekHint = findViewById(R.id.slide_seek_hint);
		slideSeekTime = (TextView) findViewById(R.id.slide_seek_time);
		slideVolumeHint = findViewById(R.id.slide_volume_hint);
		slideVolume = (TextView) findViewById(R.id.slide_volume);
		slideBrightnessHint = findViewById(R.id.slide_brightness_hint);
		slideBrightness = (TextView) findViewById(R.id.slide_brightness);
		
		controlBar = (LinearLayout) findViewById(R.id.controlBar);
		
		timeLabel = (TextView) findViewById(R.id.time);
		
		seekBar = (SeekBar) findViewById(R.id.seekBar);
		seekBar.setOnSeekBarChangeListener(onSeekBarChange);
		
//		playButton = (ImageButton) findViewById(R.id.play_button);
//		playButton.setOnClickListener(onPlayButtonClick);
//		
//		pauseButton = (ImageButton) findViewById(R.id.pause_button);
//		pauseButton.setOnClickListener(onPauseButtonClick);
		
		playPauseButton = (PlayPauseButton) findViewById(R.id.play_pause_button);
		playPauseButton.setOnPlayButtonStateChangeListener(onPlayButtonStateChangeListener);
		
		volumeButton = (VolumeButton) findViewById(R.id.volume_button);
		
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
		if(duration <= 0)
			duration = player.getDuration();
		if(videoWidth <= 0)
			videoWidth = player.getVideoWidth();
		if(videoHeight <= 0)
			videoHeight = player.getVideoHeight();
		layoutVideo();
	}
	
	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
	}
	
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		bufferPercent = percent;
	}

	public boolean onInfo(MediaPlayer mp, int what, int extra) {
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

	public void onCompletion(MediaPlayer mp) 
	{
		playPauseButton.setPlayState(false);
//		setPlayingState(false);
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
		playerView.setOnTouchListener(null);
		playerView.setOnClickListener(null);
	}
	// when activity is started/restarted, surface is created.
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.w(LOG_TAG, "surface Created");
		playerView.setOnTouchListener(onPlayerViewContainerTouchListener);
		playerView.setOnClickListener(onPlayerViewContainerClickListener);
		startPlaying();
	}

	private void startPlaying()
	{
		player.setDisplay(playerView.getHolder());
		player.start();
		snapshot.setVisibility(View.INVISIBLE);// 
		
		playPauseButton.setPlayState(true);
//		setPlayingState(true);
		
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
				
				slideSeekHint.setX((w-slideSeekHint.getWidth())/2);
				slideSeekHint.setY((h-slideSeekHint.getHeight())/2);
				
				slideVolumeHint.setX((w-slideVolumeHint.getWidth())/2);
				slideVolumeHint.setY((h-slideVolumeHint.getHeight())/2);
				
				slideBrightnessHint.setX((w-slideBrightnessHint.getWidth())/2);
				slideBrightnessHint.setY((h-slideBrightnessHint.getHeight())/2);
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
//		Log.w(LOG_TAG, newSize.x + "," + newSize.y);
		// !!! when setLayoutParams is called rapidly, video will not show.
		playerView.setLayoutParams(new RelativeLayout.LayoutParams(newSize.x, newSize.y));
		
		playerView.setX((containerW - newSize.x)/2);
		playerView.setY((containerH - newSize.y)/2);
	}
	
	
	//////////////// play control
	
//	private OnClickListener onPlayButtonClick = new OnClickListener() 
//	{
//		@Override
//		public void onClick(View v) 
//		{
//			player.start();
//			setPlayingState(true);
//		}
//	};
//	
//	private OnClickListener onPauseButtonClick = new OnClickListener() 
//	{
//		@Override
//		public void onClick(View v) 
//		{
//			player.pause();
//			setPlayingState(false);
//		}
//	};
//	private void setPlayingState(Boolean playing)
//	{
//		playButton.setVisibility(playing ? View.INVISIBLE : View.VISIBLE);
//		pauseButton.setVisibility(playing ? View.VISIBLE : View.INVISIBLE);
//	}
	
	private OnPlayButtonStateChangeListener onPlayButtonStateChangeListener = new OnPlayButtonStateChangeListener() 
	{
		@Override
		public void onClick(View v, String state) 
		{
			if(state == "play")
			{
				player.start();
			}
			else if(state == "pause")
			{
				player.pause();
			}
		}
	};
	
	
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
//		int type=0; // progress=1, volume=2, brightness=3;
		int direction=0; // left=1, right=2, top=3, bottom=4;
		int area=0; // left part=1, right part=2;
		float value=0;
		float lastX=0;
		float lastY=0;
		float timeDelta=0;
		float timeTarget=0;
		float volumeDelta=0;
		float volumeTarget=0;
		float brightnessDelta=0;
		float brightnessTarget=0;
		
		@Override
		public boolean onTouch(View v, MotionEvent event) 
		{
			int action = event.getActionMasked();
			float curX = event.getX();
			float curY = event.getY();
			switch(action) {
		        case (MotionEvent.ACTION_DOWN) :
	        		area = event.getX()*2 < playerViewContainer.getWidth() ? 1 : 2;
		        	lastX = curX;
		        	lastY = curY;
		            return true;
		        case (MotionEvent.ACTION_MOVE) :
		        	// getHistoricalX()/getHistoricalY() is not perfect, for sometime getHistorySize() == 0.
		        	float xValue = curX - lastX;
		        	float yValue = curY - lastY;
		        	if(xValue == 0 && yValue == 0) // first move event.
		        		return true;
//		        	Log.w(LOG_TAG, (curX - lastX) + ", " + xValue);
		        	
		        	if(direction == 0)
		        	{
			        	float diff = Math.abs(xValue) - Math.abs(yValue);
			        	if(diff >= 0)
			        		direction = xValue > 0 ? RIGHT : LEFT;
		        		else
		        			direction = yValue > 0 ? BOTTOM : TOP;
		        	}
		        	if(direction == LEFT || direction == RIGHT)
						value = xValue;
		        	else // if(direction == TOP || direction == BOTTOM)
						value = -yValue;
//		        	Log.w(LOG_TAG, "direction: " + direction + "," + value + "," + (curX - lastX));
		        	
		        	if(direction == LEFT || direction == RIGHT) // progress
		        	{
		        		timeDelta += value * 50; // time delta = 1/20 * xValue.
		        		timeTarget = player.getCurrentPosition() + timeDelta;
		        		if(timeTarget < 0 || timeTarget > duration)
		        			return true; // TODO : or close this touch action.
		        		slideSeekTime.setText(CommonUtil.formatDuration((long)timeTarget)+"/"+CommonUtil.formatDuration((long)duration));
		        		autoHide(slideSeekHint, 1000);
		        	}
		        	else if(area == 1)  // brightness
		        	{
		        		brightnessDelta += value * 0.1;
		        		int curBrightness = 0;
						try {
							curBrightness = System.getInt(getContentResolver(), System.SCREEN_BRIGHTNESS);
							LayoutParams lp = getWindow().getAttributes();
							Log.w(LOG_TAG, "curBrightness " + curBrightness + ", " + lp.screenBrightness);
						}
						catch (SettingNotFoundException e) {
							e.printStackTrace();
						}
		        		brightnessTarget = curBrightness + brightnessDelta;
		        		if(brightnessTarget < 0 || brightnessTarget > 255)
		        			return true; // TODO : or close this touch action.
		        		Log.w(LOG_TAG, brightnessDelta + ", " + curBrightness + ", " + brightnessTarget);
		        		LayoutParams lp = getWindow().getAttributes();
		        		lp.screenBrightness = brightnessTarget/255;
		                getWindow().setAttributes(lp);
		                System.putInt(getContentResolver(), System.SCREEN_BRIGHTNESS, (int)brightnessTarget);
		                slideBrightness.setText((int)brightnessTarget*100/255 + "%");
		                autoHide(slideBrightnessHint, 1000);
		        	}
		        	else if(area == 2)  // volume
		        	{
		        		// TODO: get explicit volume value. Now this method returns volume grade index.
//		        		int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//		        		volumeDelta += value * 0.0004;
//		        		volumeTarget = 0 + volumeDelta;
//		        		Log.w(LOG_TAG, "Volume: " + volumeDelta + "," + curVolume + "," + volumeTarget);
//		        		if(volumeTarget < 0 || volumeTarget > 1)
//		        			return true ;// TODO : or close this touch action.
//		        		slideVolume.setText((int)(volumeTarget*100)+"%");
//		        		autoHide(slideVolumeHint, 1000);
		        		
		        		int curVolumeIndex = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		        		volumeDelta += value * 0.005; // volume grade index.
		        		volumeTarget = Math.min(Math.max(curVolumeIndex + volumeDelta, 0), maxVolumeIndex);
		        		Log.w(LOG_TAG, "Volume: " + volumeDelta + "," + curVolumeIndex + "," + volumeTarget + "," + maxVolumeIndex);
//		        		if(volumeTarget < 0 || volumeTarget > maxVolumeIndex)
//		        			return true ;
		        		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int)volumeTarget, AudioManager.FLAG_PLAY_SOUND);
		        		slideVolume.setText((int)(volumeTarget/maxVolumeIndex*100)+"%");
		        		autoHide(slideVolumeHint, 1000);
		        	}
		        	
		        	lastX = curX;
		        	lastY = curY;
		            return true;
		        case (MotionEvent.ACTION_UP) :
//		            Log.w(LOG_TAG,"ACTION_UP");
		        	if(direction == LEFT || direction == RIGHT)
		        		player.seekTo((int)(player.getCurrentPosition() + timeDelta));
//		        	else if(area == 1)
		        	else if(area == 2)
		        	{
		        		volumeButton.setVolume((int)(volumeTarget/maxVolumeIndex*100));
		        	}
		        	direction = 0;
		        	timeDelta = 0;
		        	volumeDelta = 0;
		        	brightnessDelta = 0;
		            return true;
		        case (MotionEvent.ACTION_OUTSIDE) :
//		            Log.w(LOG_TAG,"ACTION_OUTSIDE");
		        	direction = 0;
		        	timeDelta = 0;
		        	volumeDelta = 0;
		        	brightnessDelta = 0;
		        	return true;      
		    }      
			
			return false;
		}
	};
	
	final OnClickListener onPlayerViewContainerClickListener = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			autoHide(controlBar, AUTO_HIDE_DELAY_MILLIS);
		}
	};
	private void autoHide(View v, int duration)
	{
		final View view = v;
		Handler mHideHandler = new Handler();
		Runnable mHideRunnable = new Runnable() {
			@Override
			public void run() {
				view.setVisibility(View.INVISIBLE);
			}
		};
		view.setVisibility(View.VISIBLE);
		if (AUTO_HIDE) 
		{
			mHideHandler.removeCallbacks(mHideRunnable);
			mHideHandler.postDelayed(mHideRunnable, duration);
		}
	}
	
	
	
	
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
