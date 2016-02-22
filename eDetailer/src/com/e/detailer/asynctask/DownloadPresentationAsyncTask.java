package com.e.detailer.asynctask;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;

import com.e.detailer.ArchiveOperations;
import com.e.detailer.DetailerConstants;
import com.e.detailer.DetailerUtils;
import com.e.detailer.activity.ActivityDetailerMainTabs;
import com.e.detailer.activity.R;

public class DownloadPresentationAsyncTask extends
		AsyncTask<String, Integer, String> 
{
	private static final CharSequence DOWNLOADING_TEXT = "Downloading...";
	private Activity mFragment;
	private ProgressDialog mDialog;
	private String mDownloadedFileLocation;
	private String mPath;
	private WakeLock wakeLock;
	private int mFrom;
	private int mFragmentId;
	private String mDownloadingDirectory;
	private boolean mIsDownloadingRunning;
	
	
	public DownloadPresentationAsyncTask(Activity mFragment,String filePath, int i)
	{
		this.mFragment = mFragment;
		this.mFragmentId = i;
		mPath = filePath;
		mDownloadingDirectory = (i == DetailerConstants.HOME_FRAGMENT_ID)? DetailerConstants.HTML_FOLDER : DetailerConstants.MOA_FOLDER;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onPreExecute()
	{
		mIsDownloadingRunning = true;
		mDialog = new ProgressDialog(mFragment);
		mDialog.setIndeterminate(false);
		mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mDialog.setMessage(DOWNLOADING_TEXT);
		mDialog.setCancelable(false);
		mDialog.setOnKeyListener(new OnKeyListener() {
		    @Override
		    public boolean onKey(final DialogInterface dialog1, int keyCode, KeyEvent event) {
		        if(keyCode==KeyEvent.KEYCODE_BACK && !event.isCanceled()) {
		            if(mDialog.isShowing()) 
		            {
		            	if (mIsDownloadingRunning)
		            	{
			            	new AlertDialog
			            	.Builder(mFragment)
			            	.setTitle(mFragment.getString(R.string.app_name))
			            	.setMessage("Are you sure, you want to cancel the downloading?")
			            	.setPositiveButton("Yes", new DialogInterface.OnClickListener()
			            	{
			            		@Override
			            		public void onClick(DialogInterface dialog, int which)
			            		{
			            			dialog.dismiss();
			            			dialog1.dismiss();
			            			cancelDownloading();
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
			            else
			            {
			            	dialog1.dismiss();
			            }
		            } 
		        }
		        return false;
		    }
		});
		mDialog.show();

		PowerManager powerManager = (PowerManager) mFragment
				.getSystemService(Context.POWER_SERVICE);
		
		wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "wakeLock");
		wakeLock.acquire();
	}
	@Override
	protected String doInBackground(String... params)
	{
//		setmInstance(this);
		String status = downloadFileFromUrl(mPath);
		
		if (isCancelled() == true)
		{
			return "Cancelled...!";
		}
		
		if (status.equalsIgnoreCase(""))
			unZipData();
		else
		{
			return status;
		}
		return "";
	}

//	if (status.equalsIgnoreCase("false") == false)
//	return "Error in downloading file, please try again or contact to your admin...!";
	private void unZipData()
	{
		try
		{
			String unzipLocation = DetailerConstants.MAIN_DIR_PATH + mDownloadingDirectory;
			ArchiveOperations archiveOperations = new ArchiveOperations();
			archiveOperations.unzip(mDownloadedFileLocation,unzipLocation);
		}
		catch(Exception exception)
		{
			Log.e(exception.getMessage(), "Exception");
		}
	}
	
	private String downloadFileFromUrl(String fileUrl)
	{
		int count;
		String fileName = new File(fileUrl).getName();
		try
		{
			URL url = new URL(fileUrl);
			URLConnection conection = url.openConnection();
            conection.connect();
            int lenghtOfFile = conection.getContentLength();
            InputStream input = new BufferedInputStream(url.openStream(), 8192);
            // File to be saved on disk
            mDownloadedFileLocation = DetailerConstants.MAIN_DIR_PATH + DetailerConstants.ZIP_FOLDER + fileName;
            @SuppressWarnings("resource")
			OutputStream output = new FileOutputStream(mDownloadedFileLocation);
 
            byte data[] = new byte[1024];
 
            long total = 0;
            while ((count = input.read(data)) != -1) 
            {
                // allow canceling with back button
                if (isCancelled()) 
                {
                    input.close();
                    return null;
                }
                Log.e("total:" + total, "count: " + count);
                total += count;
                // publishing the progress....
                if (lenghtOfFile > 0) // only if total length is known
                    publishProgress((int) (total * 100 / lenghtOfFile));
                output.write(data, 0, count);
            
            }
            output.flush();
            output.close();
            input.close();
		}
		catch (MalformedURLException e)
		{
			return e.getLocalizedMessage();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return e.getLocalizedMessage();
		}
		return "";
	}
	@Override
	protected void onProgressUpdate(Integer... values)
	{
		if (mDialog.isShowing())
			mDialog.setProgress(values[0]);
	}
	
	@Override
	protected void onPostExecute(String result)
	{

		mIsDownloadingRunning = false;
		if (mDialog.isShowing()){
			mDialog.dismiss();
		}
		wakeLock.release();

//		setmInstance(null);
		if (result.equalsIgnoreCase(""))
		{
			if (mFragmentId == DetailerConstants.HOME_FRAGMENT_ID)
			{
				ActivityDetailerMainTabs.getInstance().setmBrowseBeansList(null);
			}
			
			((ActivityDetailerMainTabs) mFragment).refresh();
		}
		else
		{
			DetailerUtils.showMsgDialog(mFragment, "", result, null);
		}
	}

//	public static DownloadPresentationAsyncTask getmInstance() {
//		return mInstance;
//	}
//
//	public static void setmInstance(DownloadPresentationAsyncTask mInstance) {
//		DownloadPresentationAsyncTask.mInstance = mInstance;
//	}

	@Override
	protected void onCancelled() 
	{
		super.onCancelled();
		
		mIsDownloadingRunning = false;

		if (mDialog.isShowing())
		{
			mDialog.dismiss();
		}
		wakeLock.release();
//		setmInstance(null);
	}
	
	public void cancelDownloading() 
	{
		cancel(true);
	}
}