package com.jasonxuli.test.comps;

import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import android.graphics.Bitmap;



/**
 * @author VTX
 *  TODO: write bitmap cache to disk. 
 */
public class ImageManager {

	private ConcurrentHashMap<String, SoftReference<Bitmap>> cache ;
	
	
	public Bitmap get(String url)
	{
		Bitmap bm;
		
		SoftReference<Bitmap> ref = cache.get(url);
		if(ref != null)
		{
			bm = ref.get();
			if(bm != null)
				return bm;			
		}
		
		bm = getFromDisk(url);
		if(bm != null)
			return bm;
		
		bm = getFromServer(url);
		if(bm != null)
			return bm;
		
		return null;
	}
	
	private Bitmap getFromDisk(String url)
	{
		// get bitmap from disk
		
		// put bitmap to cache
		
		
		return null;
	}
	
	private Bitmap getFromServer(String url)
	{
		// get bitmap from server
		
		// put bitmap to cache
		
		
		return null;
	}
	
}
