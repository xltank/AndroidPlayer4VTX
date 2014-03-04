package com.jasonxuli.test.vo;

public class Rendition {

	public String id = "";
	public String type = "";
	public String url = "";
	public String raw = "";
	public String name = "";
	public String segments = "";
	
	public long fileSize = 0L;
	public int bitRate = 0;
	public long duration = 0L;
	public int width = 0;
	public int height = 0;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}


	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}


	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
	public String getRaw() {
		return raw;
	}
	public void setRaw(String raw) {
		this.raw = raw;
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	public String getSegments() {
		return segments;
	}
	public void setSegments(String segments) {
		this.segments = segments;
	}
	
	
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}


	public int getBitRate() {
		return bitRate;
	}
	public void setBitRate(int bitRate) {
		this.bitRate = bitRate;
	}


	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}


	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}


	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	
	
	public Rendition(String id,
					 String type,
					 String url,
					 String raw,
					 String name,
					 String segments,
					 long fileSize,
					 int bitRate,
					 long duration,
					 int width,
					 int height) {
		
		setId(id);
		setType(type);
		setUrl(url);
		setRaw(raw);
		setName(name);
		setSegments(segments);
		setFileSize(fileSize);
		setBitRate(bitRate);
		setDuration(duration);
		setWidth(width);
		setHeight(height);
	}
	
	 
}
