package com.vtx.player.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CommonUtil {

	
	public static Document parseXML(String xml)
	{
		Document dom ;
    	DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
    	try {
			DocumentBuilder domBuilder = domFactory.newDocumentBuilder();
			dom = domBuilder.parse(new ByteArrayInputStream(xml.getBytes()));
//			System.out.println(dom.getTextContent());
			return dom;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null ;
	}
	
	
	public static Boolean isWIFI(Context context)
	{
		ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = conn.getActiveNetworkInfo();
		System.out.println(info.getType());
		if(info != null && info.getType() == ConnectivityManager.TYPE_WIFI)
			return true ;
		
		return false;
	}
	
	
	/**
	 * 
	 * @param duration in milliseconds
	 * @return "HH:MM:SS"
	 */
	public static String formatDuration(long duration)
	{
//		int mil = (int) duration%1000 ;
		
		int seconds = (int) duration/1000;
		
		int hour = seconds/3600;
		String hourStr = fillWith(hour+"", "0", 2);
		
		int minute = (seconds%3600)/60;
		String minuteStr = fillWith(minute+"", "0", 2);
		
		int second = seconds%60;
		String secondStr = fillWith(second+"", "0", 2);
		
		if(hour == 0)
			return minuteStr+":"+secondStr;
		else
			return hourStr+":"+minuteStr+":"+secondStr;
	}
	
	
	public static String fillWith(String source, String filler, int num)
	{
		while(source.length() < num)
			source = filler + source;
		
		return source ;
	}
	
	public static String getHTTPFileName(String url)
	{
		return url.substring(url.lastIndexOf("/"));
	}
	
	
	public static Point getSuitableSize(int w, int h, int containerW, int containerH)
	{
		int resultW = 0, resultH = 0;
		if((float)w/h >= (float)containerW/containerH)
		{
			resultW = containerW;
			resultH = h * resultW/w ;
		}else {
			resultH = containerH;
			resultW = w * resultH/h ;
		}
		return new Point(resultW, resultH);
	}
	
}
