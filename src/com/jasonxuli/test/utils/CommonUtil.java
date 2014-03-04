package com.jasonxuli.test.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class CommonUtil {

	
	public static Document parseXML(String xml)
	{
		Document dom ;
    	DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
    	try {
			DocumentBuilder domBuilder = domFactory.newDocumentBuilder();
			dom = domBuilder.parse(new ByteArrayInputStream(xml.getBytes()));
			System.out.println(dom.getTextContent());
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
}