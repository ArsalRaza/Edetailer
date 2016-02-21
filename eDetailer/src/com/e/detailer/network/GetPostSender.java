package com.e.detailer.network;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.CookieManager;
import java.net.HttpURLConnection;

import android.text.TextUtils;
import android.util.Log;

import com.e.detailer.DetailerConstants;

public class GetPostSender extends NetworkUtils
{
	public String sendGet(String url)
	{
		HttpURLConnection httpURLConnection;
		try
		{
			httpURLConnection = getUrlConnection(url, HTTP_GET, PLAIN_TEXT, "");
			httpURLConnection.connect();
			String response = getResponse(httpURLConnection, false);
	        
	        return response;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	public String sendPostJSON(String url, String params)
	{
		HttpURLConnection httpURLConnection;
		try 
		{
			if(DetailerConstants.isLogEnabled){
				Log.e("URL", DetailerConstants.BASE_URL + url);
				Log.e("Request Params", params.toString());
			}
			
			httpURLConnection = getUrlConnection(DetailerConstants.BASE_URL + url, HTTP_POST, APPLICATION_JSON, "");
			CookieManager msCookieManager = new CookieManager();
			if(msCookieManager.getCookieStore().getCookies().size() > 0)
			{
			    //While joining the Cookies, use ',' or ';' as needed. Most of the server are using ';'
				httpURLConnection.setRequestProperty("Cookie",
			    TextUtils.join(";",  msCookieManager.getCookieStore().getCookies()));    
			}
			
			httpURLConnection.connect();
			
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8"),
	                true);
			writer.write(params.toString());
			writer.flush();
	        String response = getResponse(httpURLConnection, false);
	        if(DetailerConstants.isLogEnabled)
	        {
				Log.e("Response", response);
			}
	        return response;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "false";
		}
	}

	public String sendPostJSONSaveResponseCookie(String url, String params)
	{
		HttpURLConnection httpURLConnection;
		try
		{
			if(DetailerConstants.isLogEnabled){
				Log.e("URL", DetailerConstants.BASE_URL + url);
				Log.e("Request Params", params.toString());
			}
			
			httpURLConnection = getUrlConnection(DetailerConstants.BASE_URL + url, HTTP_POST, APPLICATION_JSON, "");
			httpURLConnection.connect();
			
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8"),
	                true);
			writer.write(params.toString());
			writer.flush();
			
	        String response = getResponse(httpURLConnection, true);
	        if(DetailerConstants.isLogEnabled)
	        {
				Log.e("Response", response);
			}
	        return response;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "false";
		}
	}

}