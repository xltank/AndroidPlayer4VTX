package com.vtx.player.vo;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;


public class VideoInfo implements Parcelable {
	
	
	public VideoInfo()
	{
		
	}
	
	public String id = "";
	public String title = "";
	public String link = "";
	public String description = "";
	
	public String publisherId = "";
	public String managerId = "";
	public String transferEncoding = "";
	public String tags = "";
	public String autoBps = "";
	public String metadata = "";
	
	public List<ThumbnailsVTX> thumbnails = new ArrayList<ThumbnailsVTX>();
	public List<CuePoint> cuepoints = new ArrayList<CuePoint>();
	public List<Rendition> renditions = new ArrayList<Rendition>();
	
	
	////// !!! not tested and used.
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(title);
		dest.writeString(link);
		dest.writeString(description);
		dest.writeString(publisherId);
		dest.writeString(managerId);
		dest.writeString(transferEncoding);
		dest.writeString(tags);
		dest.writeString(autoBps);
		dest.writeString(metadata);
		
		dest.writeList(thumbnails);
		dest.writeList(cuepoints);
		dest.writeList(renditions);
	}
	
	public static final Parcelable.Creator<VideoInfo> CREATOR = new Parcelable.Creator<VideoInfo>() 
	{
		public VideoInfo createFromParcel(Parcel in) {
		    return new VideoInfo(in);
		}
		
		public VideoInfo[] newArray(int size) {
		    return new VideoInfo[size];
		}
	};

	private VideoInfo(Parcel in) {
		id = in.readString();
		title = in.readString();
		link = in.readString();
		description = in.readString();
		publisherId = in.readString();
		managerId = in.readString();
		transferEncoding = in.readString();
		tags = in.readString();
		autoBps = in.readString();
		metadata = in.readString();
		
		in.readList(thumbnails, null);
		in.readList(cuepoints, null);
		in.readList(renditions, null);
	}
	
	
}
