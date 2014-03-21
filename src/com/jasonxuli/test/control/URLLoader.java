package com.jasonxuli.test.control;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class URLLoader {

	
	public static String TEXT = "text";
	public static String BINARY = "binary";
	
	private ExecutorService pool ;
//	private ArrayList<Future<String>> results ;
	
	public URLLoader(){
		
		pool = Executors.newCachedThreadPool();
//		results = new ArrayList<Future<String>>();
	}
	
	
	public String load(String url) throws InterruptedException, ExecutionException{
		
		MyCallable myCall = new MyCallable(url);
		Future<String> result = pool.submit(myCall);
//		results.add(result);
		
		if(result.isDone() ){
			String str = result.get();
			return str ;
		}
		return "";
//		return t.response;
	}
	
}

class MyCallable implements Callable<String>{
	
	private String _url = "";
	public MyCallable(String url){
		this._url = url;
	}
	
	@Override
	public String call() throws Exception
	{
		HttpURLConnection conn = null;
		InputStream in = null;
		String response = "";
		
		try {
			URL url = new URL(this._url);
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
	
}