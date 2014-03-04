package com.jasonxuli.test.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.jasonxuli.test.vo.VideoInfo;

public class VideoUtil {
	
	
	public static VideoInfo parseVideoInfoJSON(String json)
	{
		JSONObject obj;
    	try {
    		obj = (JSONObject) new JSONTokener(json).nextValue();
    		
			System.out.println(obj);
			JSONArray items = (JSONArray) obj.get("items");
			JSONObject item = (JSONObject) items.get(0);
			VideoInfo video = new VideoInfo();
			video.id = item.getString("id");
			video.title = item.getString("title");
			video.link = item.getString("link");
			video.description = item.getString("description");
			video.link = item.getString("link");
			
			JSONArray elements = (JSONArray) item.get("vtxElements");
			for(int i=0; i< elements.length(); i++){
				String k = elements.getJSONObject(i).getString("name");
				String v = elements.getJSONObject(i).getString("value");
				if(k == "vtx:publisherId"){
					video.publisherId = v;
				}else if(k == "vtx:managerId"){
					video.publisherId = v;
				}else if(k == "vtx:transferEncoding"){
					video.publisherId = v;
				}else if(k == "vtx:tags"){
					video.publisherId = v;
				}else if(k == "vtx:autoBps"){
					video.publisherId = v;
				}else if(k == "vtx:metadata"){
					video.publisherId = v;
				}
			}
			
			JSONArray thumbnails = (JSONArray) item.get("vtxElements");
			
			return null ;
    	} catch (JSONException e) {
			e.printStackTrace();
		} finally
		{
			
		}
    	
    	return 
	}
}
