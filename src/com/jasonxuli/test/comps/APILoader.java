package com.jasonxuli.test.comps;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.jasonxuli.test.MainActivity;

import android.os.AsyncTask;
import android.view.View;

public class APILoader extends AsyncTask<String, Integer, String> {

	
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

//		    JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
//		    ((EditText) findViewById(R.id.result)).setText(response);
		    
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
