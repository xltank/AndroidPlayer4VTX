package com.jasonxuli.test.comps;

import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;

import com.jasonxuli.test.utils.CommonUtil;
import com.jasonxuli.test.utils.GlobalData;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;
import android.widget.ImageView;


/**
 * @author VTX
 *  TODO: write bitmap cache to disk. 
 */
public class ImageManager {

	private LruCache<String, Bitmap> cache ;
	
	private static ImageManager _instance;
	
	private ImageManager()
	{
		cache = new LruCache<String, Bitmap>(GlobalData.IMAGE_CACHE_SIZE)
			{
				@Override
				protected int sizeOf(String key, Bitmap value)
				{
					return value.getByteCount()/1024 ;
				}
			};
	}
	
	public static ImageManager ins()
	{
		if(_instance == null)
			_instance = new ImageManager();
		
		return _instance;
	}
	
	
	public void loadImage(String url, ImageView imageView)
	{
		Bitmap bm = cache.get(url);
		if(bm != null)
		{
			imageView.setImageBitmap(bm);
			return ;
		}
		
//		bm = getFromDisk(url, imageView);
//		if(bm != null)
//			imageView.setImageBitmap(bm);
		
		bm = getFromServer(url, imageView);
	}
	
//	private Bitmap getFromDisk(String url, ImageView imageView)
//	{
//		// get bitmap from disk
//		
//		// put bitmap to cache
//		
//		
//		return null;
//	}
	
	private Bitmap getFromServer(String url, ImageView imageView)
	{
		// get bitmap from server
		ImageLoader loader = new ImageLoader(new BitmapHandler(url, imageView), url);
    	loader.execute();
		
		// put bitmap to cache
		
		
		return null;
	}

	private class BitmapHandler extends Handler
	{
		private String _url;
		private ImageView _imageView;
		public BitmapHandler(String url, ImageView imageView)
		{
			_url = url;
			_imageView = imageView;
		}
		
		@Override
    	public void handleMessage(Message msg) 
		{
			super.handleMessage(msg);
			
			byte[] bytes = msg.getData().getByteArray("result");
//			BitmapFactory.Options options = new 
			Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			_imageView.setImageBitmap(bm);
			cache.put(_url, bm);
		}
	};
	
	
	private void putBitmapToCache(Bitmap bm)
	{
		
	}
	
	
	private void putBitmapToDisk(Bitmap bm)
	{
		
	}
	
}
