package com.jasonxuli.test;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.jasonxuli.test.comps.APILoader;
import com.jasonxuli.test.comps.Facade;
import com.jasonxuli.test.comps.HttpParamsVTX;
import com.jasonxuli.test.comps.HttpRequester;
import com.jasonxuli.test.comps.PopupConfirm;
import com.jasonxuli.test.comps.VideoListArrayAdapter;
import com.jasonxuli.test.constants.APIConstant;
import com.jasonxuli.test.constants.MessageConstant;
import com.jasonxuli.test.utils.CommonUtil;
import com.jasonxuli.test.utils.GlobalData;
import com.jasonxuli.test.utils.VideoUtil;
import com.jasonxuli.test.vo.Video;
import com.jasonxuli.test.vo.VideoInfo;

public class MainActivity extends Activity {

	protected APILoader apiLoader;
	
	protected VideoInfo curVideoInfo = null; 
	
	private ListView videoList;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        System.out.println("activity w " + getWindow().getDecorView().getWidth()+ " -- h " + getWindow().getDecorView().getHeight());
        
        
        // when add onClickHandler in ListView in layout xml, 
        // a Exception: error inflating class android.widget.listview
        videoList = (ListView) findViewById(R.id.videoList);
        videoList.setOnItemClickListener(onVideoListItemClickHandler);
        
        System.out.println("Main Activity");
        if(GlobalData.token == null)
        {
        	Intent loginIntent = new Intent(this, LoginActivity.class);
        	startActivity(loginIntent);
        }else
        {
        	Facade.ins().getRecentVideos(onGetRecentVideosHandler, "10", "1");
        }
    }
    
    
    final Handler onGetRecentVideosHandler = new Handler()
    {
    	@Override
    	public void handleMessage(Message msg)
    	{
    		super.handleMessage(msg);
    		
    		String result = msg.getData().getString("result");
    		System.out.println(result);
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
    		
    		VideoListArrayAdapter adapter = new VideoListArrayAdapter(MainActivity.this, R.layout.item_videolist, GlobalData.videos);
    		videoList.setAdapter(adapter);
    	}
    };
    
    
    final OnItemClickListener onVideoListItemClickHandler = new OnItemClickListener() 
    {
    	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    	{
    		final Video video = (Video) parent.getItemAtPosition(position);
    		if(!CommonUtil.isWIFI(MainActivity.this))
    		{
    			final PopupConfirm popup = new PopupConfirm(MainActivity.this, 
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
    
    
    public void viewVideo()
    {
    	Intent intent = new Intent(this, ViewVideoActivity.class);
    	String videoUrl = curVideoInfo.renditions.get(0).getUrl();
    	intent.putExtra(MessageConstant.VIDEO_URL, videoUrl);
    	startActivity(intent);
    }

    
//    public void showPopup()
//    {
//    	PopupMessage popup = new PopupMessage(this, "Information", "This is a information from TestApp.");
//		popup.show();
//		System.out.println("show popup");
//    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
