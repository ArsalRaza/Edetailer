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
import android.os.AsyncTask;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

import com.e.detailer.ArchiveOperations;
import com.e.detailer.DetailerConstants;
import com.e.detailer.DetailerUtils;
import com.e.detailer.activity.ActivityDetailerMainTabs;

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
	
	
	public DownloadPresentationAsyncTask(Activity mFragment,String filePath, int i)
	{
		this.mFragment = mFragment;
		this.mFragmentId = i;
		mPath = filePath;
		mDownloadingDirectory = (i == DetailerConstants.HOME_FRAGMENT_ID)? DetailerConstants.HTML_FOLDER : DetailerConstants.MOA_FOLDER;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onPreExecute(){
	  
		mDialog = new ProgressDialog(mFragment);
		mDialog.setIndeterminate(false);
		mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mDialog.setMessage(DOWNLOADING_TEXT);
		mDialog.setCancelable(false);
		mDialog.show();

		PowerManager powerManager = (PowerManager) mFragment
				.getSystemService(Context.POWER_SERVICE);
		
		wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "wakeLock");
		wakeLock.acquire();
	}

	@Override
	protected String doInBackground(String... params)
	{
		String status = downloadFileFromUrl(mPath);
		
//		if (status.equalsIgnoreCase("false") == false)
//			return "Error in downloading file, please try again or contact to your admin...!";
		if (status.equalsIgnoreCase(""))
			unZipData();
		else
		{
			return status;
		}
		return "";
	}

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
		mDialog.setProgress(values[0]);
	}
	
	@Override
	protected void onPostExecute(String result)
	{
		if (mDialog.isShowing()){
			mDialog.dismiss();
		}
		wakeLock.release();
		
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
}