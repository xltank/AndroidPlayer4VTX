package com.jasonxuli.test.control;

import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;
import android.widget.ImageView;

import com.jasonxuli.test.utils.CommonUtil;
import com.jasonxuli.test.utils.GlobalData;


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
	
	
	public void loadImage(String url, ImageView imageView, int inSampleSize)
	{
		Bitmap bm = cache.get(url);
		if(bm != null)
		{
			System.out.println("from cache"+url);
			imageView.setImageBitmap(bm);
			return ;
		}
		
		bm = getFromDisk(url, imageView);
		if(bm != null)
			imageView.setImageBitmap(bm);
		
		bm = getFromServer(url, imageView, inSampleSize);
	}
	
	public void loadImage(String url, ImageView imageView)
	{
		Bitmap bm = cache.get(url);
		if(bm != null)
		{
			imageView.setImageBitmap(bm);
			return ;
		}
		
		bm = getFromDisk(url, imageView);
		if(bm != null)
			imageView.setImageBitmap(bm);
		
		bm = getFromServer(url, imageView, 1);
	}
	
	
	private Bitmap getFromDisk(String url, ImageView imageView)
	{
		// get bitmap from disk
		
		// put bitmap to cache
		
		
		return null;
	}
	
	
	private Bitmap getFromServer(String url, ImageView imageView, int inSampleSize)
	{
		System.out.println("from server : " + CommonUtil.getHTTPFileName(url));
		// get bitmap from server
		ImageLoader loader = new ImageLoader(new BitmapHandler(url, imageView, inSampleSize), url);
    	loader.execute();
		
		// put bitmap to cache
		
		
		return null;
	}

	private class BitmapHandler extends Handler
	{
		private String _url;
		private WeakReference<ImageView> _imageViewRef;
		private int _inSampleSize;
		
		public BitmapHandler(String url, ImageView imageView, int inSampleSize)
		{
			_url = url;
			_imageViewRef = new WeakReference<ImageView>(imageView);
			_inSampleSize = inSampleSize;
		}
		
		@Override
    	public void handleMessage(Message msg) 
		{
			super.handleMessage(msg);
			
			byte[] bytes = msg.getData().getByteArray("result");
			if(bytes == null || bytes.length == 0)
				return ;
			
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = _inSampleSize;
			Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
			_imageViewRef.get().setImageBitmap(bm);
			putBitmapToCache(_url, bm);
		}
	};
	
	
	private void putBitmapToCache(String url, Bitmap bm)
	{
		if(cache.get(url) == null)
			cache.put(url, bm);
	}
	
	
//	private void putBitmapToDisk(Bitmap bm)
//	{
//		
//	}
	
}
