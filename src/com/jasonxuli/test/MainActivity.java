package com.jasonxuli.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jasonxuli.test.comps.URLLoader;

public class MainActivity extends Activity {

	protected URLLoader urlLoader ;
	
	protected String testURL = "http://api.staging.video-tx.com/public/video?videoId=117636516590649345&publisherId=94986174405279744&format=json&types=flv";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        urlLoader = new URLLoader();
        
//        Button submitBtn = (Button) findViewById(R.id.submitButton);
//        submitBtn.setOnClickListener( new View.OnClickListener() {
//			});
        
        
    }
    
    public void onSubmitClick(View v)
	{
		try {
			TextView result = (TextView) findViewById(R.id.result);
			String data = urlLoader.load(testURL);
			Thread.sleep(2000);
			result.setText(data);
			System.out.println("done");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    public final static String EXTRA_MESSAGE = "com.jasonxuli.test.MESSAGE";
    public void onSendClick(View v)
    {
    	Intent intent = new Intent(this, DisplayMessageActivity.class);
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
