package com.jasonxuli.test;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.TabHost;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.jasonxuli.test.comps.APILoader;
import com.jasonxuli.test.comps.Facade;
import com.jasonxuli.test.comps.PlaylistListArrayAdapter;
import com.jasonxuli.test.comps.PopupConfirm;
import com.jasonxuli.test.comps.VideoListArrayAdapter;
import com.jasonxuli.test.constants.APIConstant;
import com.jasonxuli.test.constants.MessageConstant;
import com.jasonxuli.test.utils.CommonUtil;
import com.jasonxuli.test.utils.GlobalData;
import com.jasonxuli.test.utils.VideoUtil;
import com.jasonxuli.test.vo.Playlist;
import com.jasonxuli.test.vo.Video;
import com.jasonxuli.test.vo.VideoInfo;

public class VideoAndPlayListActivity extends FragmentActivity {

	protected APILoader apiLoader;
	
	protected VideoInfo curVideoInfo = null; 
	
	private ListView videoList;
	private ListView playlistList;
	
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_playlist);
        
        TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator(getString(R.string.video_list_tab_title)).setContent(R.id.videoList));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator(getString(R.string.playlist_tab_title)).setContent(R.id.playList));
        
        // when add onClickHandler in ListView in layout xml, 
        // a Exception: error inflating class android.widget.listview
        videoList = (ListView) findViewById(R.id.videoList);
        videoList.setOnItemClickListener(onVideoListItemClickHandler);
        
        playlistList = (ListView) findViewById(R.id.playList);
        playlistList.setOnItemClickListener(onPlaylistItemClickHandler);
        
        if(GlobalData.token == null)
        {
        	Intent loginIntent = new Intent(this, LoginActivity.class);
        	startActivity(loginIntent);
        }else
        {
        	Facade.ins().getRecentVideos(onGetRecentVideosHandler, "20", "0");
        	Facade.ins().getPlaylists(onGetPlaylistsHandler, "20", "0");
        }
    }
    
    
    final Handler onGetRecentVideosHandler = new Handler()
    {
    	@Override
    	public void handleMessage(Message msg)
    	{
    		super.handleMessage(msg);
    		
    		String result = msg.getData().getString("result");
    		GlobalData.videos = new ArrayList<Video>();
    		try {
				JSONArray videos = (JSONArray) new JSONTokener(result).nextValue();
				for(int i=0; i<videos.length(); i++)
				{
					GlobalData.videos.add(new Video(videos.getJSONObject(i)));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
    		
    		VideoListArrayAdapter adapter = new VideoListArrayAdapter(VideoAndPlayListActivity.this, R.layout.item_video_list, GlobalData.videos);
    		videoList.setAdapter(adapter);
    	}
    };
    
    
    final OnItemClickListener onVideoListItemClickHandler = new OnItemClickListener() 
    {
    	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    	{
    		final Video video = (Video) parent.getItemAtPosition(position);
    		if(!CommonUtil.isWIFI(VideoAndPlayListActivity.this))
    		{
    			final PopupConfirm popup = new PopupConfirm(VideoAndPlayListActivity.this, 
    					getString(R.string.warning), 
    					getString(R.string.no_wifi_message));
    			popup.setOKButton(new OnClickListener() 
    			{
					@Override
					public void onClick(View v) 
					{
						getVideoInfo(video);
					}
				});
    			popup.show();
    		}
    		else {
    			getVideoInfo(video);
    		}
    	}
	};
	
	
	final Handler onGetPlaylistsHandler = new Handler()
    {
    	@Override
    	public void handleMessage(Message msg)
    	{
    		super.handleMessage(msg);
    		
    		String result = msg.getData().getString("result");
    		GlobalData.playlists = new ArrayList<Playlist>();
    		try {
				JSONArray playlists = (JSONArray) new JSONTokener(result).nextValue();
				for(int i=0; i<playlists.length(); i++)
				{
					GlobalData.playlists.add(new Playlist(playlists.getJSONObject(i)));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
    		
    		PlaylistListArrayAdapter adapter = new PlaylistListArrayAdapter(VideoAndPlayListActivity.this, R.layout.item_playlist_list, GlobalData.playlists);
    		playlistList.setAdapter(adapter);
    	}
    };
    
    final OnItemClickListener onPlaylistItemClickHandler = new OnItemClickListener() 
    {
    	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    	{
    		final Playlist video = (Playlist) parent.getItemAtPosition(position);
    		if(!CommonUtil.isWIFI(VideoAndPlayListActivity.this))
    		{
    			final PopupConfirm popup = new PopupConfirm(VideoAndPlayListActivity.this, 
    					getString(R.string.warning), 
    					getString(R.string.no_wifi_message));
    			popup.setOKButton(new OnClickListener() 
    			{
					@Override
					public void onClick(View v) 
					{
						getPlaylistInfo(video);
					}
				});
    			popup.show();
    		}
    		else {
    			getPlaylistInfo(video);
    		}
    	}
	};
	
	
	private void getVideoInfo(Video video)
	{
		Facade.ins().getVideoInfo(
				onVideoInfoHandler, 
				video.getId(), 
				video.getPublisherId(), 
    			APIConstant.DEFAULT_RESULT_FORMAT, 
    			APIConstant.DEFAULT_VIDEO_TYPES);
	}
    final Handler onVideoInfoHandler = new Handler()
    {
    	@Override
    	public void handleMessage(Message msg){
    		super.handleMessage(msg);
    		
    		String result = msg.getData().getString("result");
    		curVideoInfo = VideoUtil.parseVideoInfoJSON(result);
    		
    		viewVideo();
    	}
    };
    
    
    private void getPlaylistInfo(Playlist playlist)
	{
		Facade.ins().getVideoInfo(
				onPlaylistInfoHandler, 
				playlist.getId(), 
				playlist.getPublisherId(), 
    			APIConstant.DEFAULT_RESULT_FORMAT, 
    			APIConstant.DEFAULT_VIDEO_TYPES);
	}
    final Handler onPlaylistInfoHandler = new Handler()
    {
    	@Override
    	public void handleMessage(Message msg){
    		super.handleMessage(msg);
    		// TODO: view playlist.
//    		String result = msg.getData().getString("result");
//    		curVideoInfo = VideoUtil.parseVideoInfoJSON(result);
    		
//    		viewVideo();
    	}
    	
    };
    
    
    public void viewVideo()
    {
    	Intent intent = new Intent(this, ViewVideoActivity.class);
    	String videoUrl = curVideoInfo.renditions.get(0).getUrl();
    	intent.putExtra(MessageConstant.VIDEO_URL, videoUrl);
    	startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}