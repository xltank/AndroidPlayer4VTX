package com.jasonxuli.test.comps;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.AsyncTask;

import com.jasonxuli.test.MainActivity;

public class APILoader extends AsyncTask<String, Integer, String> {

	
	public static String videoXML = "http://api.staging.video-tx.com/public/video?videoId=117636516590649345&publisherId=94986174405279744&format=xml&types=flv";
	public static String videoJSON = "http://api.staging.video-tx.com/public/video?videoId=117636516590649345&publisherId=94986174405279744&format=json&types=flv";
	
	
	private MainActivity _activity ;
	
	public APILoader(MainActivity activity)
	{
		_activity = activity;
	}
	
	protected String doInBackground(String... urls)
	{
		String apiUrl = urls[0];
		HttpURLConnection conn = null;
		InputStream in = null;
		String response = "";
		
		try {
			URL url = new URL(apiUrl);
		    conn = (HttpURLConnection) url.openConnection();
		    conn.setRequestMethod("GET");
		    conn.connect();
		    in = conn.getInputStream();
		    BufferedReader bReader = new BufferedReader(new InputStreamReader(in));
		    String temp = "";
		    while ((temp = bReader.readLine()) != null) {
		        response += temp;
		    }
		    return response;

		    
		} catch (Exception e) {
			e.printStackTrace();
	    } finally {
	    	if(in != null) {
	    		try {
	    			in.close();
	    		}catch(Exception e){
	    		}
	    	}
	    	if(conn != null){
	    		conn.disconnect();
	    	}
	    }
		
		return "";
	}
	
	
	protected void onPostExecute(String result)
	{
		_activity.showAPIResult(result);
	}
	
}
