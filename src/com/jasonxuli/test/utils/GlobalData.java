package com.jasonxuli.test.utils;

import java.util.List;

import com.jasonxuli.test.comps.ImageManager;
import com.jasonxuli.test.vo.Manager;
import com.jasonxuli.test.vo.Publisher;
import com.jasonxuli.test.vo.Video;

public class GlobalData {

	public static String apiDomain = "http://api.staging.video-tx.com/";
	
	public static String token = "";
	
	public static int IMAGE_CACHE_SIZE = 100;
	
	public static Publisher curPublisher = null;
	public static Manager curManager = null;
	
	
	public static List<Video> videos;
	
}
