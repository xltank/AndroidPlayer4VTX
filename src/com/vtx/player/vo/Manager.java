package com.vtx.player.vo;

import org.json.JSONException;
import org.json.JSONObject;

public class Manager {

	private String publisherId ;
	private String email ;
	private String passwd ;
	private String name ;
	private String role ;
	private String renditionProfileId ;
	private String audioRenditionProfileId ;
	private String id ;
	private int version ;
	private long creationTime ;
	private long modifiedTime ;
    
    
	public Manager(JSONObject json){
	
		try {
			setPublisherId(json.getString("publisherId"));
			setEmail(json.getString("email"));
			setPasswd(json.getString("passwd"));
			setName(json.getString("name"));
			setRole(json.getString("role"));
			setRenditionProfileId(json.getString("renditionProfileId"));
			setAudioRenditionProfileId(json.getString("audioRenditionProfileId"));
			setId(json.getString("id"));
			setVersion(json.getInt("version"));
			setCreationTime(json.getLong("creationTime"));
			setModifiedTime(json.getLong("modifiedTime"));

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
    
    public String getPublisherId() {
		return publisherId;
	}
	public void setPublisherId(String publisherId) {
		this.publisherId = publisherId;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

	public String getRenditionProfileId() {
		return renditionProfileId;
	}
	public void setRenditionProfileId(String renditionProfileId) {
		this.renditionProfileId = renditionProfileId;
	}

	public String getAudioRenditionProfileId() {
		return audioRenditionProfileId;
	}
	public void setAudioRenditionProfileId(String audioRenditionProfileId) {
		this.audioRenditionProfileId = audioRenditionProfileId;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	public long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}
	public long getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(long modifiedTime) {
		this.modifiedTime = modifiedTime;
	}



}
