package com.vtx.player.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

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
import com.vtx.player.comps.PlaylistListArrayAdapter;
import com.vtx.player.comps.PopupConfirm;
import com.vtx.player.constants.MessageConstant;
import com.vtx.player.control.Facade;
import com.vtx.player.utils.CommonUtil;
import com.vtx.player.utils.GlobalData;
import com.vtx.player.vo.Playlist;
import com.vtx.player.ViewVideoActivity;

public class PlaylistListFragment extends Fragment {

	private ListView playlistList;
	private Playlist curPlaylist;
	
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
    		curPlaylist = (Playlist) parent.getItemAtPosition(position);
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
						viewPlaylist();
						popup.dismiss();
					}
				});
    			popup.show();
    		}
    		else {
    			viewPlaylist();
    		}
    	}
	};
	
    
//	 TODO: view playlist.
    public void viewPlaylist()
    {
    	Intent intent = new Intent(getActivity(), ViewVideoActivity.class);
    	intent.putExtra(MessageConstant.PLAYLIST_ID, curPlaylist.getId());
    	intent.putExtra(MessageConstant.PUBLISHER_ID, curPlaylist.getPublisherId());
//    	startActivity(intent);
    }

	
}
