package com.jasonxuli.test.comps;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.util.ByteArrayBuffer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


public class ImageLoader extends AsyncTask<String, Integer, byte[]> {

	private Handler _handler ;
	private String _url ;
	
	
	public ImageLoader(Handler handler, String url)
	{
		_handler = handler;
		_url = url;
	}
	
	
	protected byte[] doInBackground(String... urls)
	{
		HttpURLConnection conn = null;
		InputStream in = null;
		
		try {
			URL url = new URL(_url);
		    conn = (HttpURLConnection) url.openConnection();
		    conn.setRequestMethod("GET");
		    
	    	conn.setDoInput(true);
//	    	conn.setDoOutput(true); // this will return HTTP code 405, method not allowed.
	    	conn.setUseCaches(false);
	    	conn.connect();
		    
		    in = conn.getInputStream();
		    BufferedInputStream bfInputStream = new BufferedInputStream(in);
		    ByteArrayBuffer byteBuffer = new ByteArrayBuffer(0);
		    int i = 0;
		    while((i = bfInputStream.read()) != -1)
		    {
		    	byteBuffer.append((byte) i);
		    }
		    return byteBuffer.buffer();
		    
		} catch (Exception e) {
//			try {
//				System.out.println(conn.getResponseCode());
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
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
