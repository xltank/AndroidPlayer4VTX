package com.jasonxuli.test;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
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
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.jasonxuli.test.comps.CustomRelativeLayout;
import com.jasonxuli.test.constants.MessageConstant;
import com.jasonxuli.test.utils.CommonUtil;
import com.jasonxuli.test.utils.VideoUtil;
import com.jasonxuli.test.vo.VideoInfo;

public class ViewVideoActivity extends Activity 
	implements Callback, OnPreparedListener, OnBufferingUpdateListener, OnVideoSizeChangedListener,
	OnTimedTextListener, OnCompletionListener, OnSeekCompleteListener, OnErrorListener, OnInfoListener{

	private CustomRelativeLayout playerViewContainer;
	private SurfaceView playerView;
	private MediaPlayer player;
	
	private VideoInfo videoInfo;
	private String videoUrl;
	
	private Timer timer;
//	private ViewTreeObserver viewTreeObserver;

	private TextView timeLabel;
	private SeekBar seekBar;
	private ImageButton playButton;
	private ImageButton pauseButton;
	private TextView videoTitle;
	private TextView videoDesc;
	
	private int bufferPercent;
	private int duration;
	private int videoWidth;
	private int videoHeight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		setContentView(R.layout.activity_view_video);
		setupActionBar();
		
		Intent intent = getIntent();
		String videoJSON = intent.getStringExtra(MessageConstant.VIDEO_INFO_JSON);
		videoInfo = VideoUtil.parseVideoInfoJSON(videoJSON);
		videoUrl = videoInfo.renditions.get(0).getUrl();
		
		initListeners();
		initPlayer();
	}
	
	private void initListeners()
	{
		playerViewContainer = (CustomRelativeLayout) findViewById(R.id.playerContainer);
		playerViewContainer.setBackgroundColor(0x33FF33);
		playerViewContainer.setOnSizeChangedListener(new CustomRelativeLayout.OnSizeChangedListener() 
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
		});
		
		timeLabel = (TextView) findViewById(R.id.time);
		
		seekBar = (SeekBar) findViewById(R.id.seekBar);
		seekBar.setOnSeekBarChangeListener(onSeekBarChange);
		
		playButton = (ImageButton) findViewById(R.id.play_button);
		playButton.setOnClickListener(onPlayButtonClick);
		
		pauseButton = (ImageButton) findViewById(R.id.pause_button);
		pauseButton.setOnClickListener(onPauseButtonClick);
		
		videoTitle = (TextView) findViewById(R.id.video_title);
		videoTitle.setText(videoInfo.title);
		
		videoDesc = (TextView) findViewById(R.id.video_desc);
		videoDesc.setText(videoInfo.description);
	}
	
	private void initPlayer()
	{
		playerView = (SurfaceView) findViewById(R.id.playerView);
		playerView.getHolder().addCallback(this);
		
		player = new MediaPlayer();
		player.setOnPreparedListener(this);
		player.setOnBufferingUpdateListener(this);
		player.setOnVideoSizeChangedListener(this);
		player.setOnTimedTextListener(this);
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
		
//		viewTreeObserver = ((View) playerView.getParent()).getViewTreeObserver();
//		viewTreeObserver.addOnGlobalLayoutListener(onVideoContainerLayoutListener);
	}
	
	
	////////////// player event listeners
	
	public void onPrepared(MediaPlayer mp) {
		System.out.println("onPrepared");
		duration = player.getDuration();
		videoWidth = player.getVideoWidth();
		videoHeight = player.getVideoHeight();
		layoutVideo();
	}
	
	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
	}
	
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		System.out.println("onBufferingUpdate : " + percent);
		bufferPercent = percent;
	}

	public boolean onInfo(MediaPlayer mp, int what, int extra) {
//		System.out.println("onInfo : " + what + " , "  + extra);
		switch (what) {
		case MediaPlayer.MEDIA_INFO_UNKNOWN :
			break;
		case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING :
			break;
		case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START :
			System.out.println("onInfo : MEDIA_INFO_VIDEO_RENDERING_START");
			break;
		case MediaPlayer.MEDIA_INFO_BUFFERING_START :
			System.out.println("onInfo : MEDIA_INFO_BUFFERING_START");
			break;
		case MediaPlayer.MEDIA_INFO_BUFFERING_END :
			System.out.println("onInfo : MEDIA_INFO_BUFFERING_END");
			break;
		case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING :
			break;
		case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE :
			break;
		case MediaPlayer.MEDIA_INFO_METADATA_UPDATE :
			System.out.println("onInfo : MEDIA_INFO_METADATA_UPDATE");
			break;
		case MediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE :
			break;
		case MediaPlayer.MEDIA_INFO_SUBTITLE_TIMED_OUT :
			break;
		}
		return false;
	}

	public boolean onError(MediaPlayer mp, int what, int extra) {
		System.out.println("onError : " + what + " , "  + extra);
		return false;
	}
	
	public void onTimedText(MediaPlayer mp, TimedText text) {
//		System.out.println("onTimedText : " + text);
	}

	public void onSeekComplete(MediaPlayer mp) {
		System.out.println("onSeekComplete : " + mp.getCurrentPosition());
	}

	public void onCompletion(MediaPlayer mp) {
		System.out.println("onCompletion");
	}


	////////// SurfaceView implements
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		playStart();
	}

	private void playStart()
	{
		player.setDisplay(playerView.getHolder());
		player.start();
		
		playButton.setVisibility(View.INVISIBLE);
		pauseButton.setVisibility(View.VISIBLE);
		
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
	
	
	/////////// layout event listener
//	ViewTreeObserver.OnGlobalLayoutListener onVideoContainerLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() 
//	{
//		@Override
//		public void onGlobalLayout() 
//		{
//			layoutVideo();
//		}
//	};
	
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
		
		System.out.println("Container Size : " + containerW + " , " + containerH);
		
		Point newSize = CommonUtil.getSuitableSize(videoWidth, videoHeight, containerW, containerH);
		System.out.println("Calculated Size : " + newSize.x + " , " + newSize.y);
		
//		playerView.getHolder().setFixedSize(newSize.x, newSize.y);
		
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
			playButton.setVisibility(View.INVISIBLE);
			pauseButton.setVisibility(View.VISIBLE);
		}
	};
	
	private OnClickListener onPauseButtonClick = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			player.pause();
			playButton.setVisibility(View.VISIBLE);
			pauseButton.setVisibility(View.INVISIBLE);
		}
	};
	
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
			
//			viewTreeObserver.removeOnGlobalLayoutListener(onVideoContainerLayoutListener);
//			viewTreeObserver = null;
			
			player.release();
			player = null;
		}
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
