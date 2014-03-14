package com.jasonxuli.test;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.jasonxuli.test.comps.Facade;
import com.jasonxuli.test.comps.ImageLoader;
import com.jasonxuli.test.comps.ImageManager;
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
    	ImageManager.ins().loadImage(
    			"http://www.video-tx.com/wp-content/uploads/2012/01/logo.png"
    			, (ImageView) findViewById(R.id.welcome_image));
	}
	

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
//    		System.out.println(result);

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
