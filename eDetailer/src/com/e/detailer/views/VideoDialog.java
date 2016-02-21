package com.e.detailer.views;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.e.detailer.activity.ActivityMOAPreview;
import com.e.detailer.activity.R;

public class VideoDialog implements OnDismissListener, OnCancelListener 
{
	private String mVideoUrl;
	private Activity mActivity;
	private ProgressDialog mDialog;
	private MediaController mMediaController;
	private VideoView mVideoView;

	public VideoDialog(Activity mActivity, String mVideoUrl) 
	{
		this.mActivity = mActivity;
		this.mVideoUrl = mVideoUrl;
	}

	public Dialog show() 
	{
		Dialog dialog = new Dialog(mActivity, android.R.style.Theme_Translucent_NoTitleBar);
		dialog.setContentView(R.layout.activity_moa_preview);
		dialog.setOnDismissListener(this);
		dialog.setOnCancelListener(this);
		InitUI(dialog);
		dialog.show();
		return dialog;
	}

	private void InitUI(Dialog dialog)
	{
		mVideoView = (VideoView) dialog.findViewById(R.id.video_play_layout_video_view);
		
		DisplayMetrics displaymetrics = new DisplayMetrics();
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

		mDialog = ProgressDialog.show(mActivity, "", "Loading...!"/*ActivityMOAPreview.this.getResources().getString(R.string.progress_msg)*/);
		mDialog.setCancelable(true);
		
		try
		{
			mMediaController = new MediaController(mActivity);
			mMediaController.setAnchorView(mVideoView);
			
			mVideoView.setMediaController(mMediaController);
			mVideoView.setVideoPath(mVideoUrl);
			
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
					intent.setDataAndType(Uri.parse(mVideoUrl), "video/mp4");
					mActivity.startActivity(intent);
					mActivity.finish();
					return false;
				}
			});

		}
		catch (Exception e)
		{
			System.out.println("Video Play Error :" + e.getMessage());
		}
		
	}

	@Override
	public void onDismiss(DialogInterface dialog)
	{
		mVideoView.suspend();
	}

	@Override
	public void onCancel(DialogInterface dialog) 
	{
		mVideoView.suspend();
	}
}