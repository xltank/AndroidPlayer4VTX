package com.vtx.player;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.widget.TabHost;

import com.vtx.player.control.APILoader;
import com.vtx.player.fragments.PlaylistListFragment;
import com.vtx.player.fragments.VideoListFragment;
import com.vtx.player.utils.CommonUtil;

public class MainActivity extends FragmentActivity {

	protected APILoader apiLoader;
	
	private TabHost tabHost;
	private ViewPager viewPager;
	
	private VideoListFragment videoListFragment = new VideoListFragment();
	private PlaylistListFragment playlistListFragment = new PlaylistListFragment();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//    	Log.w(GlobalData.DEBUG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();
        // contents is hidden.
        tabHost.addTab(tabHost.newTabSpec("0").setIndicator(getString(R.string.video_list_tab_title)).setContent(R.id.fake_videoList));
        tabHost.addTab(tabHost.newTabSpec("1").setIndicator(getString(R.string.playlist_tab_title)).setContent(R.id.fake_playList));
        tabHost.setOnTabChangedListener(onTabChangeListener);
        
        viewPager = (ViewPager) findViewById(R.id.activity_main_viewPager);
        viewPager.setOnPageChangeListener(onPageChangeListener);
        viewPager.setAdapter(new ListPagerAdapter(getSupportFragmentManager()));
        
        if(!CommonUtil.checkLoginStatus(this))
        {
        	Intent loginIntent = new Intent(this, LoginActivity.class);
        	startActivity(loginIntent);
        	return ;
        }
    }
    
    
    private class ListPagerAdapter extends FragmentPagerAdapter
    {
    	public ListPagerAdapter(FragmentManager fm)
    	{
    		super(fm);
    	}

		@Override
		public Fragment getItem(int pos) {
			switch (pos) {
				case 0:
					return videoListFragment;
				case 1:
					return playlistListFragment;
			}
			return null;
		}

		@Override
		public int getCount() {
			return 2;
		}
    }
    
    
    final TabHost.OnTabChangeListener onTabChangeListener = new TabHost.OnTabChangeListener() 
    {
		@Override
		public void onTabChanged(String tabId) {
			Log.w("MainActivity", tabId);
			viewPager.setCurrentItem(Integer.parseInt(tabId));
		}
	};
	
	final ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() 
	{
		@Override
		public void onPageSelected(int position) {
			Log.w("MainActivity", "onPageSelected " + position);
			tabHost.setCurrentTab(position);
		}
		
		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){
//			Log.w("MainActivity", "onPageScrolled " + position + ", " + positionOffset + ", " + positionOffsetPixels);
		}
		
		@Override
		public void onPageScrollStateChanged(int state) {
//			Log.w("MainActivity", "onPageScrollStateChanged " + state);
		}
	};
    
    
    @Override
    protected void onStart()
    {
    	super.onStart();
//    	Log.w(GlobalData.DEBUG_TAG, "onStart");
    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    
    @Override
    protected void onResume()
    {
    	super.onResume();
    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
