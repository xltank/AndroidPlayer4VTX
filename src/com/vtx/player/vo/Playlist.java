package com.vtx.player.vo;

import org.json.JSONException;
import org.json.JSONObject;

public class Playlist {
	
	
	public Playlist(JSONObject json)
	{
		try {
			String ids = json.getJSONArray("videoIds").toString().replace("\"", "");
			videoIds = ids.substring(1, ids.length()-1).split(",");
//			System.out.println();
//			videoIds = json.getJSONArray("videoIds").toString();//.replaceAll(regularExpression, replacement).split(",");
			tags = json.getString("tags");
			rule = json.getString("rule");
			publisherId = json.getString("publisherId");
			type = json.getString("type");
			title = json.getString("title");
			description = json.getString("description");
			coverUrl = json.getString("coverUrl");
			data = json.getString("data");
			contentType = json.getString("contentType");
			deleted = json.getBoolean("deleted");
			status = json.getString("status");
			id = json.getString("id");
			version = json.getInt("version");
			creationTime = json.getInt("creationTime");
			modifiedTime = json.getInt("modifiedTime");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	private String[] videoIds;
	private String tags = "";
	private String rule = "";
	private String publisherId = "";
	private String type = "";
	private String title = "";
	private String description = "";
	private String coverUrl = "";
	private String data = "";
	private String contentType = "";
	private boolean deleted = false;
	private String status = "";
	private String id = "";
	private int version = 0;
	private int creationTime = 0;
	private int modifiedTime = 0;
	
	
	public String[] getVideoIds() {
		return videoIds;
	}
	public String getTags() {
		return tags;
	}
	public String getRule() {
		return rule;
	}
	public String getPublisherId() {
		return publisherId;
	}
	public String getType() {
		return type;
	}
	public String getTitle() {
		return title;
	}
	public String getDescription() {
		return description;
	}
	public String getCoverUrl() {
		return coverUrl;
	}
	public String getData() {
		return data;
	}
	public String getContentType() {
		return contentType;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public String getStatus() {
		return status;
	}
	public String getId() {
		return id;
	}
	public int getVersion() {
		return version;
	}
	public int getCreationTime() {
		return creationTime;
	}
	public int getModifiedTime() {
		return modifiedTime;
	}
	
	
	
	
	
	
}
