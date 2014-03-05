package com.jasonxuli.test.comps;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.http.client.params.HttpClientParams;
import org.apache.http.params.BasicHttpParams;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class APILoader extends AsyncTask<String, Integer, String> {

	public static final String GET = "GET";
	public static final String POST = "POST";
	
	private Handler _handler ;
	private String _url ;
	private String _method ;
	private List<BasicHttpParams> _params ;
	
	
	public APILoader(Handler handler, String url, String method, List<BasicHttpParams> params)
	{
		_handler = handler;
		_url = url;
		_method = method;
		_params = params;
	}
	
	
	protected String doInBackground(String... urls)
	{
		HttpURLConnection conn = null;
		InputStream in = null;
		String response = "";
		
		try {
			URL url = new URL(_url);
		    conn = (HttpURLConnection) url.openConnection();
		    conn.setRequestMethod(_method);
		    
		    if(_method.equals(GET)){
		    	
		    }else if(_method.equals(POST)){
		    	conn.setDoInput(true);
		    	conn.setDoOutput(true);
		    	conn.setUseCaches(false);
		    }
		    
		    conn.connect();
		    
		    _params.
		    DataOutputStream out = new DataOutputStream(conn.getOutputStream());
		    out.writeBytes(str)
		    
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
	
	@Override
	protected void onPostExecute(String result)
	{
		Bundle bundle = new Bundle();
		bundle.putString("result", result);
		Message msg = new Message();
		msg.setData(bundle);
		_handler.handleMessage(msg);
	}
	
}
