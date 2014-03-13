package com.jasonxuli.test;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.jasonxuli.test.comps.Facade;
import com.jasonxuli.test.comps.HttpParamsVTX;
import com.jasonxuli.test.comps.HttpRequester;
import com.jasonxuli.test.utils.GlobalData;
import com.jasonxuli.test.vo.Manager;
import com.jasonxuli.test.vo.Publisher;

public class LoginActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setupActionBar();
		
		test();
	}
	
	private void test()
	{
		String params = new HttpParamsVTX("format", "mrss", "videoId", "137687869375381505", "publisherId", "94986174405279744").toString();
    	HttpRequester requester = new HttpRequester(testHandler, 
    			"http://api.staging.video-tx.com/public/video",
    			//http://my.staging.video-tx.com/static/images/logo/logo.png,
    			HttpRequester.GET, 
    			params);
		requester.execute();
	}
	final Handler testHandler = new Handler()
	{
		@Override
    	public void handleMessage(Message msg) 
		{
			super.handleMessage(msg);
			try {
				String str = new String(msg.getData().getByteArray("result"), "UTF-8");
				System.out.println(str);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	
	
	

    public void onLoginClick(View v)
	{
    	String email = ((EditText) findViewById(R.id.userName)).getText().toString();
    	String pwd = ((EditText) findViewById(R.id.password)).getText().toString();
    	
    	Facade.ins().login(loginHandler, email, pwd);
	}
    
    
    final Handler loginHandler = new Handler()
    {
    	@Override
    	public void handleMessage(Message msg) {
    		super.handleMessage(msg);
    		String result = msg.getData().getString("result");
    		System.out.println(result);

    		JSONObject json = null;
			try {
				json = (JSONObject) new JSONTokener(result).nextValue();
				
				String status = json.getString("status");
				
				if(!status.equals("SUCCESS")){
					// TODO: pop up message .
					return ;
				}else
				{
					GlobalData.token = json.getString("token");
					GlobalData.curPublisher = new Publisher(json.getJSONObject("publisher"));
					GlobalData.curManager = new Manager(json.getJSONObject("manager"));
					
					toMainPage();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
    	}
    };
    
    private void toMainPage(){
    	Intent intent = new Intent(this, MainActivity.class);
    	startActivity(intent);
    }
	
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_video, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
