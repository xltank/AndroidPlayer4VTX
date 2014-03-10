package com.jasonxuli.test.comps;

import com.jasonxuli.test.constants.APIConstant;
import com.jasonxuli.test.utils.GlobalData;

import android.os.Handler;

public class Facade {

	private static Facade _facade = new Facade();
	
	private Facade()
	{
	}
	
	public static Facade ins()
	{
		return _facade;
	}
	
	
	
	public String login(Handler handler, String email, String passwd)
	{
		String params = new HttpParamsVTX("email", email, "passwd", passwd).toString();
    	
		try {
			APILoader apiLoader = new APILoader(handler, 
												GlobalData.apiDomain + APIConstant.LOGIN, 
												APILoader.POST, 
												params);
			return apiLoader.execute().get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
	public String getVideoInfo(Handler handler, String videoId, String publisherId, String format, String types)
	{
		String params = new HttpParamsVTX("videoId", videoId, 
							  			  "publisherId", publisherId, 
							  			  "format", format, 
							  			  "types", types).toString();
		APILoader apiLoader = new APILoader(handler, 
											GlobalData.apiDomain + APIConstant.VIDEO_INFO, 
											APILoader.GET, 
											params);
		try {
			return apiLoader.execute().get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
	public String getRecentVideos(Handler handler, String maxResults, String firstResult)
	{
		String params = new HttpParamsVTX("maxResults", maxResults, 
							  			  "firstResult", firstResult,
							  			  "token", GlobalData.token).toString();
		APILoader apiLoader = new APILoader(handler, 
											GlobalData.apiDomain + APIConstant.GET_RECENT_VIDEOS, 
											APILoader.GET, 
											params);
		try {
			return apiLoader.execute().get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
}
