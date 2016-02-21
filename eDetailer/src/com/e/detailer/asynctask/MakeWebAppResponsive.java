package com.e.detailer.asynctask;

import java.io.File;

import com.e.detailer.DetailerConstants;
import com.e.detailer.DetailerUtils;
import com.e.detailer.activity.R;
import com.e.detailer.activity.WebViewActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public class MakeWebAppResponsive extends AsyncTask<String, Void, String> {

	private WebViewActivity mWebViewActivity;
	private String mUrl;
	private ProgressDialog mDialog;

	public MakeWebAppResponsive(WebViewActivity webViewActivity,
			String urlForWebView) {
		this.mWebViewActivity = webViewActivity;
		this.mUrl = urlForWebView;
	}
	@Override
	protected void onPreExecute(){
		mDialog = ProgressDialog.show(mWebViewActivity, mWebViewActivity.getResources().getString(R.string.app_name), "Setting up slide for first time...");
//		mDialog.show();
	}
	@Override
	protected String doInBackground(String... params) {
		
		String htmlString = DetailerUtils.htmlReader(mUrl);
		String newHtmlCode = "";
		
		if (htmlString.contains(DetailerConstants.CONTAIN_WITH)){
			newHtmlCode = htmlString.replace(DetailerConstants.CONTAIN_WITH, DetailerConstants.REPLACE_WITH);
		}
		
		if (htmlString.contains(DetailerConstants.HTML_SCALABLE_NO_CODE)){
			newHtmlCode = htmlString.replace(DetailerConstants.HTML_SCALABLE_NO_CODE, DetailerConstants.HTML_SCALABLE_YES_CODE);
		}
		
		File indexFile = new File(mUrl);
		indexFile.delete();
		DetailerUtils.saveAndWriteFile(indexFile, newHtmlCode);
		return null;
	}
	
	@Override
	protected void onPostExecute(String result){
		
		if (mDialog.isShowing()){
			mDialog.dismiss();
		}
		
		Log.e("path", mUrl);
		mWebViewActivity.LoadWebView();
	}

}
