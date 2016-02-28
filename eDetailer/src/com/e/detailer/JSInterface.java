package com.e.detailer;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.e.detailer.activity.ActivityMOAPreview;
import com.e.detailer.activity.WebViewActivity;

public class JSInterface 
{
	private String mHtmlUrl;
	private Activity mActivity;

	public JSInterface(Activity activity, String htmlUrl)
	{
		this.mHtmlUrl = htmlUrl;
		this.mActivity = activity;
	}

	@JavascriptInterface
	public void playVideo(String mVideoUrl) 
	{
		Log.e("video-url:", mVideoUrl);
		mVideoUrl = mHtmlUrl + "/" + mVideoUrl;
		Intent intent = new Intent(mActivity, ActivityMOAPreview.class);
		intent.putExtra(DetailerConstants.VIDEO_VIEW_URL_KEY, mVideoUrl);
		mActivity.startActivity(intent);	
	}
	
	@JavascriptInterface
	public void onEDALogObtained(String logs)
	{
		Log.e("eda-logs-json:", logs);
		//Sample: [{"PageId":"1","StartTime":"Sun Feb 28 2016 00:57:37 GMT+0500 (PKT)","EndTime":"Sun Feb 28 2016 00:57:40 GMT+0500 (PKT)","BreakPoints":[]},{"PageId":"2","StartTime":"Sun Feb 28 2016 00:57:40 GMT+0500 (PKT)","EndTime":null,"BreakPoints":[]}]
//		EDALogs = logs;
		if (mActivity instanceof WebViewActivity)
		{
			((WebViewActivity) mActivity).onLogsObtained(logs);
		}
	}
}