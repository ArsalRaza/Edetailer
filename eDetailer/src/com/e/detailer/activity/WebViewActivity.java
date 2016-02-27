package com.e.detailer.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
//import android.webkit.WebChromeClient;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.e.detailer.DetailerConstants;
import com.e.detailer.DetailerUtils;
import com.e.detailer.JSInterface;

public class WebViewActivity extends Activity implements OnClickListener {
	
	private String urlForWebView = "";
	public static List<String> mUriList;
	private WebView mWebView;
	private FrameLayout frameLayout;
	private MediaPlayer mPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_web_view);
	        
		if (!getIntent().hasExtra(DetailerConstants.WEB_VIEW_URL_KEY)){
	    	finish();
	    }
		urlForWebView  = getIntent().getExtras().getString(DetailerConstants.WEB_VIEW_URL_KEY);
		InitUI();

	}
	
	@Override
	public void onDestroy()
	{
		if (mPlayer != null)
		{
			mPlayer.stop();
		}
		mWebView.loadUrl("javascript:requestForJson();");
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed()
	{
		new AlertDialog
		.Builder(WebViewActivity.this)
		.setTitle(getString(R.string.app_name))
		.setMessage("Are you sure, you want to end this call?")
		.setPositiveButton("Yes", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				WebViewActivity.super.onBackPressed();
			}
		})
		.setNegativeButton("No", 
				new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).create().show();
	}
	
	private void InitUI()
	{
		ImageView screenShotImageView = (ImageView) findViewById(R.id.snape_taker);
		ImageView appIcon = (ImageView) findViewById(R.id.app_icon);
		ImageView backArrowImageView = (ImageView) findViewById(R.id.back_arrow);
		frameLayout = (FrameLayout) findViewById(R.id.pnlFlash);
		
		appIcon.setOnClickListener(this);
		backArrowImageView.setOnClickListener(this);
		screenShotImageView.setOnClickListener(this);
	
		if (isSlideFirstLaunch())
		{
			LoadWebView();
		}
		else
		{
			LoadWebView();
		}
	
	}
	 
	private boolean isSlideFirstLaunch() {
		
		String html = DetailerUtils.htmlReader(urlForWebView);
		return !html.contains(DetailerConstants.REPLACE_WITH);
	
	}
	private Activity getActivity() {
		return this;
	}

	@SuppressLint("JavascriptInterface")
	@SuppressWarnings("deprecation")
	public void LoadWebView() {
		
		if (mWebView == null){
			mWebView = (WebView) findViewById(R.id.web_view);
			WebViewClient mWebViewClient = new WebViewClient(){
				
				private ProgressDialog mDialog;
				
				@Override
				public void onPageStarted(WebView view, String url, Bitmap favicon) {
					mDialog = ProgressDialog.show(getActivity(), getString(R.string.app_name), "Loading slide");
				}
				public void onPageFinished(WebView view, String url) {
					if(mDialog.isShowing()){
						mDialog.dismiss();
					}
				}
			};
			WebChromeClient webChromeClient = new WebChromeClient()
			{
	            @Override
	            public void onProgressChanged(WebView view, int newProgress) {
	           
	            }
	        };
	
		        mWebView.setWebViewClient(mWebViewClient);
		        mWebView.setWebChromeClient(webChromeClient);
		        
		        String str = DetailerUtils.convertPathToUriString(urlForWebView);
		        str = str.substring(0, str.lastIndexOf('/'));

		        mWebView.addJavascriptInterface(new JSInterface(this, str),"Android"/* "JSInterface"*/);
		        mWebView.getSettings().setAllowFileAccessFromFileURLs(true);	
	        	mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
	        	mWebView.getSettings().setAllowContentAccess(true);
	        	mWebView.getSettings().setAllowFileAccess(true);
	        	mWebView.getSettings().setUseWideViewPort(true);
	        	mWebView.getSettings().setDisplayZoomControls (false);
	        	mWebView.getSettings().setLoadWithOverviewMode(true);
	        	
	        	mWebView.getSettings().setRenderPriority(RenderPriority.HIGH);
	        	mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
	        	
	        	//Chromium hardware acceleration false
//	        	mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
	        	
	        	
	        	mWebView.setVerticalScrollBarEnabled(false);
	        	mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
	        	mWebView.getSettings().setJavaScriptEnabled(true);
	        	mWebView.setScrollbarFadingEnabled(false); 	
		}
		mWebView.loadUrl(DetailerUtils.convertPathToUriString(urlForWebView));
		
        return;
	}
	
	@Override
	public void onClick(View v) {
		
		if (v.getId() == R.id.snape_taker){
			takeSlideShot();
			
		}else if (v.getId() == R.id.app_icon) {
			((LinearLayout) findViewById(R.id.web_view_layout)).removeView(mWebView);
			finish();
		
		}else if (v.getId() == R.id.back_arrow) {
			((LinearLayout) findViewById(R.id.web_view_layout)).removeView(mWebView);
			finish();
		
		}
		
	}
   
	private void takeSlideShot() {
		playSound();
		
		frameLayout.setVisibility(View.VISIBLE);
		
		AlphaAnimation fade = new AlphaAnimation(1, 0);
		fade.setDuration(700);
		fade.setAnimationListener(new AnimationListener() {
		    @Override
		    public void onAnimationEnd(Animation anim) {
		    	frameLayout.setVisibility(View.GONE);
		    }

			@Override
			public void onAnimationStart(Animation animation) {
				
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
		});
		
		frameLayout.startAnimation(fade);
		new SaveBitmapToFileThread(DetailerUtils.getBitmapScreenshot(mWebView)).start();
	}
	
	private void playSound() {
		mPlayer = MediaPlayer.create(this, R.raw.camera_screen_shot);
		mPlayer.start();
	}

	public void addImageToArray(String path) {
		if (mUriList == null){
			mUriList = new ArrayList<String>();
		}
		mUriList.add(path);
	}
	
	private class SaveBitmapToFileThread extends Thread{
		
		private Bitmap mBitmap;
		
		SaveBitmapToFileThread(Bitmap bitmap){
			this.mBitmap = bitmap;
		}
		@Override
		public void run(){
			String pathString = DetailerUtils.saveImageToSD(mBitmap);
			addImageToArray(pathString);
		}
	}
	
}
