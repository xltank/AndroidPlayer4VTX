package com.vtx.player.vo;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class PlaylistInfo {
	
	
	public String id = "";
	public String title = "";
	public String link = "";
	public String description = "";
	
	public List<VideoInfo> videos = new ArrayList<VideoInfo>();
	
	
}
