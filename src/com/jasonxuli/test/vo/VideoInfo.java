package com.jasonxuli.test.vo;

import java.util.ArrayList;
import java.util.List;

public class VideoInfo {
	
	
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
	
	
}
