package com.jasonxuli.test.fragments;

import com.jasonxuli.test.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class VideoListFragment extends Fragment {

	public VideoListFragment() {
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_video_list, container, false);
	}
}
