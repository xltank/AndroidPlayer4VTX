package com.jasonxuli.test.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.jasonxuli.test.vo.CuePoint;
import com.jasonxuli.test.vo.PlaylistInfo;
import com.jasonxuli.test.vo.Rendition;
import com.jasonxuli.test.vo.ThumbnailsVTX;
import com.jasonxuli.test.vo.VideoInfo;

public class VideoUtil {
	
	
	public static VideoInfo parseVideoInfoJSON(String json)
	{
		JSONObject obj;
    	try {
    		obj = (JSONObject) new JSONTokener(json).nextValue();
    		
//			System.out.println(obj);
			JSONArray items = obj.getJSONArray("items");
			JSONObject item = items.getJSONObject(0);
			VideoInfo video = new VideoInfo();
			video.id = item.getString("id");
			video.title = item.getString("title");
			video.link = item.getString("link");
			video.description = item.getString("description");
			video.link = item.getString("link");
			
			JSONArray elements = item.getJSONArray("vtxElements");
			for(int i=0; i< elements.length(); i++){
				String k = elements.getJSONObject(i).getString("name");
				String v = elements.getJSONObject(i).getString("value");
				if(k.equals("vtx:publisherId")){
					video.publisherId = v;
				}else if(k.equals("vtx:managerId")){
					video.managerId = v;
				}else if(k.equals("vtx:transferEncoding")){
					video.transferEncoding = v;
				}else if(k.equals("vtx:tags")){
					video.tags = v;
				}else if(k.equals("vtx:autoBps")){
					video.autoBps = v;
				}else if(k.equals("vtx:metadata")){
					video.metadata = v;
				}
			}
			
			JSONArray thumbnails = item.getJSONArray("thumbnails");
			for(int j=0; j<thumbnails.length(); j++){
				JSONObject o = thumbnails.getJSONObject(j);
				ThumbnailsVTX tn = new ThumbnailsVTX(o.getString("url"), o.getInt("width"), o.getInt("height"));
				video.thumbnails.add(tn);
			}
			
			JSONArray cuepoints = item.getJSONArray("cuepoints");
			for(int k=0; k<cuepoints.length(); k++){
				JSONObject ob = cuepoints.getJSONObject(k);
				CuePoint cp = new CuePoint(ob);
				video.cuepoints.add(cp);
			}
			
			JSONArray rends = item.getJSONArray("contents");
			for(int m=0; m<rends.length(); m++){
				JSONObject ob = rends.getJSONObject(m);
				Rendition tn = new Rendition(ob);
				video.renditions.add(tn);
			}
			
			return video ;
    	} catch (JSONException e) {
			e.printStackTrace();
		} finally
		{
		}
    	
    	return null;
	}
	
	
	public static PlaylistInfo parsePlaylistInfoJSON(String json)
	{
		JSONObject obj;
		PlaylistInfo playlist = new PlaylistInfo();
    	try {
    		obj = (JSONObject) new JSONTokener(json).nextValue();
    		
    		playlist.id = obj.getString("id");
    		playlist.title = obj.getString("title");
    		playlist.link = obj.getString("link");
    		playlist.description = obj.getString("description");
    		playlist.link = obj.getString("link");
    		
			JSONArray items = obj.getJSONArray("items");
//			JSONArray elements = item.getJSONArray("vtxElements");
			for(int i=0; i< items.length(); i++)
			{
				VideoInfo video = parseVideoInfoJSON((items.getJSONObject(i)).toString());
				playlist.videos.add(video);
			}
			
			return playlist ;
			
    	} catch (JSONException e) {
			e.printStackTrace();
		} finally
		{
		}
    	
    	return null;
	}
	
}
