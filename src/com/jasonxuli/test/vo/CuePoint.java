package com.jasonxuli.test.vo;

import org.json.JSONException;
import org.json.JSONObject;

public class CuePoint {
	
	public String type = "";
	public String name = "";
	public String data = "";
	public float offset = 0;
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}


	public float getOffset() {
		return offset;
	}
	public void setOffset(float offset) {
		this.offset = offset;
	}


	
	public CuePoint(JSONObject json){
		
		try {
			setType(json.getString("type"));
			setName(json.getString("name"));
			setData(json.getString("data"));
			setOffset((float)json.getDouble("offset"));
			
			
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
