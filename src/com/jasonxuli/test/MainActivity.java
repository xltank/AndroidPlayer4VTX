package com.jasonxuli.test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.jasonxuli.test.comps.APILoader;
import com.jasonxuli.test.vo.VideoInfo;

public class MainActivity extends Activity {

//	protected URLLoader urlLoader ;
	protected APILoader apiLoader;
	
	
//	private TextView resultText;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
//        urlLoader = new URLLoader();
        
//        Button submitBtn = (Button) findViewById(R.id.submitButton);
//        submitBtn.setOnClickListener( new View.OnClickListener() {
//			});
        
    }
    
    public void onSubmitClick(View v)
	{
    	apiLoader = new APILoader(this);
		try {
//			resultText = (TextView) findViewById(R.id.result);
			apiLoader.execute(APILoader.videoJSON).get();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    public void showAPIResult(String result)
    {
    		
    	
    }
    
    
    public final static String EXTRA_MESSAGE = "com.jasonxuli.test.MESSAGE";
    public void onSendClick(View v)
    {
    	Intent intent = new Intent(this, ViewVideoActivity.class);
    	EditText text = (EditText) findViewById(R.id.userName);
    	String msg = text.getText().toString();
    	intent.putExtra(EXTRA_MESSAGE, msg);
    	startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
