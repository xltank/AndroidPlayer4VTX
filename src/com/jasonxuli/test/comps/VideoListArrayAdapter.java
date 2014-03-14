package com.jasonxuli.test.comps;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jasonxuli.test.R;
import com.jasonxuli.test.utils.CommonUtil;
import com.jasonxuli.test.utils.GlobalData;
import com.jasonxuli.test.vo.Video;


public class VideoListArrayAdapter extends ArrayAdapter<Video> {

	public VideoListArrayAdapter(Context context, int resourceId, List<Video> objects)
	{
		super(context, resourceId, objects);
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		System.out.println(position);
		View view = convertView;
		if(convertView == null)
		{
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.item_videolist, null, false);
		}
		
		ImageView imageView = (ImageView) view.findViewById(R.id.image_item_videolist);
		TextView titleText = (TextView) view.findViewById(R.id.title_item_videolist);
		TextView durationText = (TextView) view.findViewById(R.id.duration_item_videolist);
		
		Video video = getItem(position);
		
//		imageView.setImageURI(Uri.parse(video.getSnapshotUrl()));
		ImageManager.ins().loadImage(video.getThumbnailUrl(), imageView);
		
		titleText.setText(video.getTitle());
		durationText.setText(CommonUtil.formatDuration(video.getDuration()));
		
		return view;
    }
}
