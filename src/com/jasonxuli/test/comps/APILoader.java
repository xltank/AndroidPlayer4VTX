package com.jasonxuli.test.comps;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
	private String _params ;
	
	
	public APILoader(Handler handler, String url, String method, String params)
	{
		_handler = handler;
		_url = url;
		_method = method;
		_params = params;
	}
	
	
	protected String doInBackground(String... urls)
	{
		if(_method.equals(GET)){
			return doGet();
		}else if(_method.equals(POST)){
			return doPost();
		} 
		
		return "";
	}
	
	
	private String doGet()
	{
		HttpURLConnection conn = null;
		InputStream in = null;
		String response = "";
		
		try {
			_url += "?" + _params;
			URL url = new URL(_url);
		    conn = (HttpURLConnection) url.openConnection();
		    conn.setRequestMethod(_method);
		    
	    	conn.setDoInput(true);
	    	conn.setDoOutput(true);
	    	conn.setUseCaches(false);
	    	
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
	
	
	private String doPost()
	{
		HttpURLConnection conn = null;
		InputStream in = null;
		String response = "";
		
		try {
			URL url = new URL(_url);
		    conn = (HttpURLConnection) url.openConnection();
		    conn.setRequestMethod(_method);
		    
	    	conn.setDoInput(true);
	    	conn.setDoOutput(true);
	    	conn.setUseCaches(false);
	    	
	    	conn.connect();
		    
		    System.out.println(_params.toString());
		    DataOutputStream out = new DataOutputStream(conn.getOutputStream());
		    out.writeBytes(_params);
		    out.flush();
		    out.close();
		    
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
