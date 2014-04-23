package com.vtx.player.vo;

public class ThumbnailsVTX implements Comparable<ThumbnailsVTX>{

	public String url = "";
	public int width = 0;
	public int height = 0;
	
	
	public ThumbnailsVTX(String u, int w, int h) {
		url = u;
		width = w;
		height = h;
	}
	
	
	public int compareTo(ThumbnailsVTX item)
	{
		return this.width - item.width; 
	}
}
