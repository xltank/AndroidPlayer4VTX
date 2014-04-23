package com.vtx.player;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.vtx.player.R;
import com.vtx.player.comps.PopupConfirm;
import com.vtx.player.comps.VideoListArrayAdapter;
import com.vtx.player.constants.APIConstant;
import com.vtx.player.constants.MessageConstant;
import com.vtx.player.control.APILoader;
import com.vtx.player.control.Facade;
import com.vtx.player.utils.CommonUtil;
import com.vtx.player.utils.GlobalData;
import com.vtx.player.utils.VideoUtil;
import com.vtx.player.vo.Video;
import com.vtx.player.vo.VideoInfo;

public class VideoListActivity extends FragmentActivity {

	protected APILoader apiLoader;
	
	protected VideoInfo curVideoInfo = null; 
	
	private ListView videoList;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_video_list);
        
        // when add onClickHandler in ListView in layout xml, 
        // a Exception: error inflating class android.widget.listview
        videoList = (ListView) findViewById(R.id.videoList);
        videoList.setOnItemClickListener(onVideoListItemClickHandler);
        
        if(GlobalData.token == null)
        {
        	Intent loginIntent = new Intent(this, LoginActivity.class);
        	startActivity(loginIntent);
        }else
        {
        	Facade.ins().getRecentVideos(onGetRecentVideosHandler, "20", "1");
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
    		
    		VideoListArrayAdapter adapter = new VideoListArrayAdapter(VideoListActivity.this, R.layout.item_video_list, GlobalData.videos);
    		videoList.setAdapter(adapter);
    	}
    };
    
    
    final OnItemClickListener onVideoListItemClickHandler = new OnItemClickListener() 
    {
    	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    	{
    		final Video video = (Video) parent.getItemAtPosition(position);
    		if(!CommonUtil.isWIFI(VideoListActivity.this))
    		{
    			final PopupConfirm popup = new PopupConfirm(VideoListActivity.this, 
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
	
	private void getVideoInfo(Video video)
	{
		Facade.ins().getVideoInfo(  onVideoInfoHandler, 
				video.getId(), 
				video.getPublisherId(), 
    			APIConstant.DEFAULT_RESULT_FORMAT, 
    			APIConstant.VIDEO_TYPE_MP4);
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
