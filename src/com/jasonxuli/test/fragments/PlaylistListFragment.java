package com.jasonxuli.test.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

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

import com.jasonxuli.test.R;
import com.jasonxuli.test.comps.PlaylistListArrayAdapter;
import com.jasonxuli.test.comps.PopupConfirm;
import com.jasonxuli.test.constants.APIConstant;
import com.jasonxuli.test.control.Facade;
import com.jasonxuli.test.utils.CommonUtil;
import com.jasonxuli.test.utils.GlobalData;
import com.jasonxuli.test.utils.VideoUtil;
import com.jasonxuli.test.vo.Playlist;
import com.jasonxuli.test.vo.PlaylistInfo;

public class PlaylistListFragment extends Fragment {

	private ListView playlistList;
	
	private String curPlaylistInfoJSON;
	private PlaylistInfo curPlaylistInfo;
	
	public PlaylistListFragment() {
	}

	
	@Override
	public void onStart()
	{
		super.onStart();

		if(playlistList == null)
		{
			playlistList = (ListView) getActivity().findViewById(R.id.playlistList);
			playlistList.setOnItemClickListener(onPlaylistItemClickHandler);
		}
	}
	
	
	@Override
	public void onResume()
	{
		super.onResume();
		Facade.ins().getPlaylists(onGetPlaylistsHandler, "20", "0");
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_playlist_list, container, false);
	}
	

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
    		
    		PlaylistListArrayAdapter adapter = new PlaylistListArrayAdapter(getActivity(), R.layout.item_playlist_list, GlobalData.playlists);
    		playlistList.setAdapter(adapter);
    	}
    };
    
    final OnItemClickListener onPlaylistItemClickHandler = new OnItemClickListener() 
    {
    	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    	{
    		final Playlist playlist = (Playlist) parent.getItemAtPosition(position);
    		if(!CommonUtil.isWIFI(getActivity()))
    		{
    			final PopupConfirm popup = new PopupConfirm(getActivity(), 
    					getString(R.string.warning), 
    					getString(R.string.no_wifi_message));
    			popup.setOKButton(new OnClickListener() 
    			{
					@Override
					public void onClick(View v) 
					{
						getPlaylistInfo(playlist);
						popup.dismiss();
					}
				});
    			popup.show();
    		}
    		else {
    			getPlaylistInfo(playlist);
    		}
    	}
	};
	
    /////////// playlist
    private void getPlaylistInfo(Playlist playlist)
	{
		Facade.ins().getPlaylistInfo(
				onPlaylistInfoHandler, 
				playlist.getId(), 
				playlist.getPublisherId(), 
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
				System.err.println("ERROR: video info error or no playable rendition");
				return ;
			}
    		viewPlaylist();
    	}
    	
    };
    
//	 TODO: view playlist.
    public void viewPlaylist()
    {
//    	Intent intent = new Intent(this, ViewVideoActivity.class);
//    	intent.putExtra(MessageConstant.VIDEO_INFO_JSON, curVideoInfoJSON);
//    	startActivity(intent);
    }

	
}
