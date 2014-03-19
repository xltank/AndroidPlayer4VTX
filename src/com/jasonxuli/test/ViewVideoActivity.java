package com.jasonxuli.test;

import java.io.IOException;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.TimedText;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnTimedTextListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.jasonxuli.test.constants.MessageConstant;

public class ViewVideoActivity extends Activity {

	private MediaPlayer player;
	private SurfaceView playerView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_video);
		setupActionBar();
		
		Intent intent = getIntent();
		String videoUrl = intent.getStringExtra(MessageConstant.VIDEO_URL);
		
//		VideoView player = (VideoView) findViewById(R.id.player);
//		player.setMediaController(new MediaController(this));
//		player.setVideoURI(Uri.parse(videoUrl));
//		player.start();
//		player.requestFocus();
		
		playerView = (SurfaceView) findViewById(R.id.playerView);
		player = new MediaPlayer();
		player.setOnPreparedListener(onPrepared);
		player.setOnBufferingUpdateListener(onBufferingUpdate);
		player.setOnVideoSizeChangedListener(onVideoSizeChanged);
		player.setOnTimedTextListener(onTimedText);
		player.setOnCompletionListener(onCompletion);
		player.setOnSeekCompleteListener(onSeekComplete);
		player.setOnErrorListener(onError);
		player.setOnErrorListener(onError);
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
		player.setDisplay(playerView.getHolder());
		player.start();
		
	}
	
	final OnBufferingUpdateListener onBufferingUpdate = new OnBufferingUpdateListener() {
		
		@Override
		public void onBufferingUpdate(MediaPlayer mp, int percent) {
			System.out.println("onBufferingUpdate : " + percent);
		}
	};
	
	final OnInfoListener onInfo = new OnInfoListener() {
		
		@Override
		public boolean onInfo(MediaPlayer mp, int what, int extra) {
			System.out.println("OnInfoListener : " + what + " , "  + extra);
			return false;
		}
	};
	
	final OnPreparedListener onPrepared = new OnPreparedListener() {
		
		@Override
		public void onPrepared(MediaPlayer mp) {
			System.out.println("onPrepared");
		}
	};
	
	final OnErrorListener onError = new OnErrorListener() {
		
		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			System.out.println("onError : " + what + " , "  + extra);
			return false;
		}
	};
	
	final OnTimedTextListener onTimedText = new OnTimedTextListener() {
		
		@Override
		public void onTimedText(MediaPlayer mp, TimedText text) {
			System.out.println("onError : " + text);
		}
	};
	
	final OnSeekCompleteListener onSeekComplete = new OnSeekCompleteListener() {
		
		@Override
		public void onSeekComplete(MediaPlayer mp) {
			System.out.println("onSeekComplete : " + mp.getCurrentPosition());
		}
	};
	
	final OnVideoSizeChangedListener onVideoSizeChanged = new OnVideoSizeChangedListener() {
		
		@Override
		public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
			System.out.println("onVideoSizeChanged : " + width + " , " + height);
		}
	};
	
	final OnCompletionListener onCompletion = new OnCompletionListener() {
		
		@Override
		public void onCompletion(MediaPlayer mp) {
			System.out.println("onCompletion");
		}
	};
	
	
	
	@Override
	protected void onPause()
	{
		if(player != null && player.isPlaying())
		{
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
