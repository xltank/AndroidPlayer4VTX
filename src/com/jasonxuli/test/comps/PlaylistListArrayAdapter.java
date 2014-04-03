package com.jasonxuli.test.comps;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jasonxuli.test.R;
import com.jasonxuli.test.vo.Playlist;


public class PlaylistListArrayAdapter extends ArrayAdapter<Playlist> {

	public PlaylistListArrayAdapter(Context context, int resourceId, List<Playlist> objects)
	{
		super(context, resourceId, objects);
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View view = convertView;
		if(convertView == null)
		{
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.item_video_list, null, false);
		}
		
//		ImageView imageView = (ImageView) view.findViewById(R.id.image_item_video_list);
		TextView titleText = (TextView) view.findViewById(R.id.title_item_video_list);
		TextView durationText = (TextView) view.findViewById(R.id.duration_item_video_list);
		
		Playlist playlist = getItem(position);
		
//		ImageManager.ins().loadImage(list.getThumbnailUrl(), imageView, 2);
		
		titleText.setText(playlist.getTitle());
		int playlistLen = playlist.getVideoIds().length;
		durationText.setText(playlistLen + playlistLen > 1 ? " videos" : " video");
		
		return view;
    }
}
