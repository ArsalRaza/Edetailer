package com.e.detailer.asynctask;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.e.detailer.DetailerConstants;
import com.e.detailer.DetailerUtils;
import com.e.detailer.PresentationBean;
import com.e.detailer.activity.ActivityDetailerMainTabs;
import com.e.detailer.activity.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class SavePresAsyncTask extends AsyncTask<String, Void, String> {

	private PresentationBean mBean;
	private String PresentationName;
	private Activity mActivity;
	private ProgressDialog mDialog;
	private ThreadForFindingDivsInCreate divThread;

	public SavePresAsyncTask(Activity activity, String name, PresentationBean mSelectPresentation, ThreadForFindingDivsInCreate threadForFindingDivsInCreate) {
		this.mActivity = activity;
		this.mBean = mSelectPresentation;
		this.PresentationName = name;
		this.divThread = threadForFindingDivsInCreate;
	}
	
	@Override
	protected void onPreExecute() {
		mDialog = ProgressDialog.show(mActivity, mActivity.getResources().getString(R.string.app_name), mActivity.getResources().getString(R.string.saving));
	}
	@Override
	protected String doInBackground(String... params) {
		
		File newPresDir = new File(DetailerConstants.MAIN_DIR_PATH+DetailerConstants.HTML_FOLDER
				+PresentationName+DetailerConstants.CREATED_PRES_CONCAT_TEXT);
		
		try {
			copyDirectoryOneLocationToAnotherLocation(new File(mBean.getPathOnSdCard()), newPresDir);
			for(;;){
				if (!divThread.isAlive()){
					break;
				}
			}
			
		File indexFile = new File (newPresDir.getAbsolutePath()+"/"+DetailerConstants.HTML_INDEX_FILE_NAME+".html");
		indexFile.delete();
		
		Log.e("replaced:",divThread.getResult());
		
		String obtainResultFromThread = divThread.getResult();
		
		DetailerUtils.saveAndWriteFile(indexFile, obtainResultFromThread);
		
		} catch (IOException e) {
			Toast.makeText(mActivity, "Service is not available", Toast.LENGTH_LONG).show();
			this.cancel(true);
		}
		return null;
	}
	
		
	@Override
	protected void onPostExecute(String result) {
		if (mDialog.isShowing()){
			mDialog.dismiss();
		}
		if (!isCancelled()){
			ActivityDetailerMainTabs.getInstance().setmBrowseBeansList(null);
			mActivity.finish();
		}
	}
	private void copyDirectoryOneLocationToAnotherLocation(File sourceLocation, File targetLocation)
		    throws IOException {

		if (sourceLocation.isDirectory()) {
		    if (!targetLocation.exists()) {
		        targetLocation.mkdir();
		    }

		    String[] children = sourceLocation.list();
		    for (int i = 0; i < sourceLocation.listFiles().length; i++) {

		        copyDirectoryOneLocationToAnotherLocation(new File(sourceLocation, children[i]),
		                new File(targetLocation, children[i]));
		    }
		} else {

		    InputStream in = new FileInputStream(sourceLocation);

		    OutputStream out = new FileOutputStream(targetLocation);

		    byte[] buf = new byte[1024];
		    int len;
		    while ((len = in.read(buf)) > 0) {
		        out.write(buf, 0, len);
		    }
		    in.close();
		    out.close();
		}

	}

}
