package com.jasonxuli.test.comps;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class HttpParamsVTX {

	private String[] _params;
	
	public HttpParamsVTX(String... args){
		_params = args;
	}
	
	@Override
	public String toString(){
		String result = "";
		for(int i=0; i<_params.length; i++){
			if(i%2 == 0){ // no "?"
				result += _params[i] + "=";
			}else{
				try {
					result += URLEncoder.encode(_params[i], "UTF-8") + "&";
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
}
