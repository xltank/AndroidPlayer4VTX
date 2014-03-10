package com.jasonxuli.test;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;

import com.jasonxuli.test.comps.APILoader;
import com.jasonxuli.test.comps.Facade;
import com.jasonxuli.test.comps.HttpParamsVTX;
import com.jasonxuli.test.constants.APIConstant;
import com.jasonxuli.test.constants.MessageConstant;
import com.jasonxuli.test.utils.GlobalData;
import com.jasonxuli.test.utils.VideoUtil;
import com.jasonxuli.test.vo.VideoInfo;

public class MainActivity extends Activity {

	protected APILoader apiLoader;
	
	protected VideoInfo curVideoInfo = null; 
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
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
    		JSONArray videos = (JSONArray) new JSONTokener().nextValue();
    	}
    };
    
    
    public void onVideoItemClick(View v)
	{
    	Facade.ins().getVideoInfo(  onVideoInfoHandler, 
					    			APIConstant.DEFAULT_VIDEO_ID, 
					    			APIConstant.DEFAULT_PUBLISHER_ID, 
					    			APIConstant.DEFAULT_RESULT_FORMAT, 
					    			APIConstant.DEFAULT_VIDEO_TYPES);
	}
    
    final Handler onVideoInfoHandler = new Handler(){
    	
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
