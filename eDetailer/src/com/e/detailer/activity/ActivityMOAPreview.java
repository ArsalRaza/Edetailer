package com.e.detailer.activity;

import com.e.detailer.DetailerConstants;
import com.e.detailer.DetailerUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.widget.MediaController;
import android.widget.VideoView;

public class ActivityMOAPreview extends Activity
{
	private ProgressDialog mDialog;
	private MediaController mMediaController;
	private VideoView mVideoView;
	private String videoUri;
	
	@Override
	public void onPause()
	{
		super.onPause();
		mVideoView.suspend();
	}
	
	@Override
	public void onBackPressed()
	{
		mVideoView.pause();
		
		new AlertDialog
		.Builder(ActivityMOAPreview.this)
		.setTitle(getString(R.string.app_name))
		.setMessage("Are you sure, you want to end this MOA?")
		.setPositiveButton("Yes", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				ActivityMOAPreview.super.onBackPressed();
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_moa_preview);

		mVideoView = (VideoView) findViewById(R.id.video_play_layout_video_view);
		if (getIntent() != null && getIntent().getExtras() != null && getIntent().hasExtra(DetailerConstants.VIDEO_VIEW_URL_KEY))
		{
			String indexToSample = getIntent().getExtras().getString(DetailerConstants.VIDEO_VIEW_URL_KEY).replace("index", "sample");
			videoUri = DetailerUtils.convertPathToUriString(indexToSample);
		}
		else
		{
			finish();
		}
		
		DisplayMetrics displaymetrics = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

		new ProgressDialog(ActivityMOAPreview.this);
		mDialog = ProgressDialog.show(ActivityMOAPreview.this, "", "Loading...!");
		mDialog.setCancelable(true);
		try
		{
			mMediaController = new MediaController(ActivityMOAPreview.this);

			mVideoView.setMediaController(mMediaController);
			mVideoView.setVideoPath(videoUri);
			
			mVideoView.setOnPreparedListener(new OnPreparedListener()
			{

				public void onPrepared(MediaPlayer mp)
				{
					Log.e("Message", "Ready to go");
					if (mDialog != null)
					{
						mDialog.dismiss();
					}
					
					mVideoView.start();
				}
			});
			
			mVideoView.setOnErrorListener(new OnErrorListener()
			{

				@Override
				public boolean onError(MediaPlayer mp, int what, int extra)
				{
					mDialog.dismiss();

					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.parse(videoUri), "video/mp4");
					startActivity(intent);
					finish();
					return false;
				}
			});

		}
		catch (Exception e)
		{
			System.out.println("Video Play Error :" + e.getMessage());
		}
	}
}
