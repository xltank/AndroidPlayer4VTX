package com.jasonxuli.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
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
        
        Button submitBtn = (Button) findViewById(R.id.submitButton);
        submitBtn.setOnClickListener( new View.OnClickListener() {
			public void onClick(View v)
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
		});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
