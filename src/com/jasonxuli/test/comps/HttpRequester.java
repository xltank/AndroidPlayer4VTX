package com.jasonxuli.test.comps;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.util.ByteArrayBuffer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.jasonxuli.test.utils.GlobalData;

@Deprecated
/**
 * want to make this class a base class of all http request( api loader and image loader),
 * but it seems not so reasonable.
 * @author VTX
 */
public class HttpRequester extends AsyncTask<String, Integer, byte[]> {

	public static final String GET = "GET";
	public static final String POST = "POST";
	
	private Handler _handler ;
	private String _url ;
	private String _method ;
	private String _params ;
	
	
	/**
	 * 
	 * @param handler  callback
	 * @param url  http url
	 * @param method  GET/POST
	 * @param params  "key1=value1&key2=value2"
	 */
	public HttpRequester(Handler handler, String url, String method, String params)
	{
		_handler = handler;
		_url = url;
		_method = method;
		_params = params;
	}
	
	
	protected byte[] doInBackground(String... urls)
	{
		if(_method.equals(GET)){
			return doGet();
		}else if(_method.equals(POST)){
			return doPost();
		} 
		
		return null;
	}
	
	
	private byte[] doGet()
	{
		HttpURLConnection conn = null;
		InputStream in = null;
		byte[] response ;
		
		try {
			_url += "?" + _params;
			URL url = new URL(_url);
		    conn = (HttpURLConnection) url.openConnection();
		    conn.setRequestMethod(_method);
		    
	    	conn.setDoInput(true);
	    	conn.setDoOutput(true);
	    	conn.setUseCaches(false);
	    	
	    	conn.connect();
		    in = new BufferedInputStream(conn.getInputStream());
		    BufferedReader bReader = new BufferedReader(new InputStreamReader(in));
		    ByteArrayBuffer bab = new ByteArrayBuffer(0);
		    int pos = 0;
		    while ((pos = bReader.read()) != -1) {
		    	bab.append((byte) pos);
		    }
		    response = bab.buffer();
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
		
		return null;
	}
	
	
	private byte[] doPost()
	{
		HttpURLConnection conn = null;
		InputStream in = null;
		byte[] response = null ;
		
		try {
			URL url = new URL(_url);
		    conn = (HttpURLConnection) url.openConnection();
		    conn.setRequestMethod(_method);
		    conn.setRequestProperty("Authorization", GlobalData.token);
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
		    in.read(response);
//		    BufferedReader bReader = new BufferedReader(new InputStreamReader(in));
//		    String temp = "";
//		    while ((temp = bReader.readLine()) != null) {
//		        response += temp;
//		    }
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
		
		return null;
	}
	
	
	@Override
	protected void onPostExecute(byte[] result)
	{
		Bundle bundle = new Bundle();
		bundle.putByteArray("result", result);
		Message msg = new Message();
		msg.setData(bundle);
		_handler.handleMessage(msg);
	}
	
}
