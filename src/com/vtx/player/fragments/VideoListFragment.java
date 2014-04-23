package com.vtx.player.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.vtx.player.R;
import com.vtx.player.comps.PopupConfirm;
import com.vtx.player.comps.VideoListArrayAdapter;
import com.vtx.player.constants.MessageConstant;
import com.vtx.player.control.Facade;
import com.vtx.player.utils.CommonUtil;
import com.vtx.player.utils.GlobalData;
import com.vtx.player.vo.Video;
import com.vtx.player.ViewVideoActivity;

public class VideoListFragment extends Fragment {

	private ListView videoList;
	private Video curVideo;
	
	public VideoListFragment() {
	}

	
	@Override
	public void onAttach (Activity activity)
	{
		super.onAttach(activity);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_video_list, container, false);
	}
	
	
	@Override
	public void onStart()
	{
		super.onStart();

		if(videoList == null)
		{
			videoList = (ListView) getActivity().findViewById(R.id.videoList);
			videoList.setOnItemClickListener(onVideoListItemClickHandler);
		}
	}
	
	
	@Override
	public void onResume()
	{
		super.onResume();
		Facade.ins().getRecentVideos(onGetRecentVideosHandler, "20", "0");
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
    		
    		VideoListArrayAdapter adapter = new VideoListArrayAdapter(getActivity(), R.layout.item_video_list, GlobalData.videos);
    		videoList.setAdapter(adapter);
    	}
    };
    
    
    final OnItemClickListener onVideoListItemClickHandler = new OnItemClickListener() 
    {
    	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    	{
    		curVideo = (Video) parent.getItemAtPosition(position);
    		if(!GlobalData.mobile_data_allowed && !CommonUtil.isWIFI(getActivity()))
    		{
    			final PopupConfirm popup = new PopupConfirm(getActivity(), 
    					getString(R.string.warning), 
    					getString(R.string.no_wifi_message));
    			popup.setOKButton(new OnClickListener() 
    			{
					@Override
					public void onClick(View v) 
					{
						popup.dismiss();
						GlobalData.mobile_data_allowed = true;
						viewVideo();
					}
				});
    			popup.show();
    		}
    		else {
    			viewVideo();
    		}
    	}
	};
	
	
	/////////// video
	
	public void viewVideo()
    {
    	Intent intent = new Intent(getActivity(), ViewVideoActivity.class);
    	intent.putExtra(MessageConstant.VIDEO_ID, curVideo.getId());
    	intent.putExtra(MessageConstant.PUBLISHER_ID, curVideo.getPublisherId());
    	startActivity(intent);
    }
	
}
