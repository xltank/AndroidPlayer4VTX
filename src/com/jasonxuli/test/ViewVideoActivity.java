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
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jasonxuli.test.constants.MessageConstant;
import com.jasonxuli.test.utils.CommonUtil;

public class ViewVideoActivity extends Activity 
	implements Callback, OnPreparedListener, OnBufferingUpdateListener, OnVideoSizeChangedListener,
	OnTimedTextListener, OnCompletionListener, OnSeekCompleteListener, OnErrorListener, OnInfoListener{

	private MediaPlayer player;
	private SurfaceView playerView;
	
	private Timer timer;

	private TextView timeLabel;
	private SeekBar seekBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		setContentView(R.layout.activity_view_video);
		setupActionBar();
		
		Intent intent = getIntent();
		String videoUrl = intent.getStringExtra(MessageConstant.VIDEO_URL);
		
		timeLabel = (TextView) findViewById(R.id.time);
		seekBar = (SeekBar) findViewById(R.id.seekBar);
		
		playerView = (SurfaceView) findViewById(R.id.playerView);
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
		playerView.getHolder().addCallback(this);
		
		ViewTreeObserver viewTreeObserver = ((View) playerView.getParent()).getViewTreeObserver();
		viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() 
		{
			@Override
			public void onGlobalLayout() 
			{
				layoutVideo();
			}
		});
	}
	
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
//		System.out.println("onBufferingUpdate : " + percent);
	}

	public boolean onInfo(MediaPlayer mp, int what, int extra) {
//		System.out.println("onInfo : " + what + " , "  + extra);
		return false;
	}

	public void onPrepared(MediaPlayer mp) {
		System.out.println("onPrepared");
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

	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
		System.out.println("onVideoSizeChanged : " + width + " , " + height);
		layoutVideo();
	}

	public void onCompletion(MediaPlayer mp) {
		System.out.println("onCompletion");
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
			timer.cancel();
			timer.purge();
			player.release();
			player = null;
		}
	}
	
	
	// SurfaceView implements
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		playStart();
		((View) playerView.getParent()).setBackgroundColor(0xFFFF33);
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		
	}
	
	
	private void layoutVideo()
	{
		if(player == null || playerView == null)
			return ;
		
		int videoW = player.getVideoWidth();
		int videoH = player.getVideoHeight();
		if(videoW == 0 || videoH == 0)
			return ;
		
		System.out.println("Video Size : " + videoW + " , " + videoH);
		
		View playerViewParent = (View) playerView.getParent();
		int containerW = playerViewParent.getWidth();
		int containerH = playerViewParent.getHeight();
		if(containerW == 0 || containerH == 0)
			return ;
		
		System.out.println("Container Size : " + containerW + " , " + containerH);
		
		Point newSize = CommonUtil.getSuitableSize(videoW, videoH, containerW, containerH);
		System.out.println("Calculated Size : " + newSize.x + " , " + newSize.y);
		playerView.getHolder().setFixedSize(newSize.x, newSize.y);
//		playerView.setX((containerW - newSize.x)/2);
//		playerView.setY((containerH - newSize.y)/2);
	}
	
	
	private void playStart()
	{
		player.setDisplay(playerView.getHolder());
		player.start();
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
//			seekBar.setSecondaryProgress(player.)
			
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
