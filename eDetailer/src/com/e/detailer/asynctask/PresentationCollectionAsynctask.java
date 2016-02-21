package com.e.detailer.asynctask;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.e.detailer.DetailerConstants;
import com.e.detailer.PresentationBean;
import com.e.detailer.fragment.CreateFragment;
import com.e.detailer.fragment.DataSheetFragment;
import com.e.detailer.fragment.HomeFragment;
import com.e.detailer.fragment.MOFFragment;

public class PresentationCollectionAsynctask extends
		AsyncTask<String, Void, String> {
	
	private ArrayList<PresentationBean> preBeanList = new ArrayList<PresentationBean>();
	private Activity mActivity;
	private int mFrom;
	private ProgressDialog mDialog;
//	private ArrayList<VideoBean> mVideoBeans;
	
	public PresentationCollectionAsynctask(Activity mActivity, int mFrom)
	{
		this.mActivity = mActivity;
		this.mFrom = mFrom;
	}
	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
		mDialog = new ProgressDialog(mActivity);
		mDialog.setIndeterminate(false);
		
		mDialog.setMessage("Working hard....");
		mDialog.setCancelable(false);
		mDialog.show();

	}
	@Override
	protected String doInBackground(String... params) {
		String statusString = "";
		onFirstLaunch();
		File htmlDirectory;
		if(mFrom == DetailerConstants.HOME_FRAGMENT_ID || 
				mFrom == DetailerConstants.CREATE_FRAGMENT_ID){
			
			htmlDirectory = new File(DetailerConstants.MAIN_DIR_PATH + DetailerConstants.HTML_FOLDER);
		}
		else /*if (mFrom == DetailerConstants.MOA_FRAGMENT_ID)*/
		{
			htmlDirectory = new File(DetailerConstants.MAIN_DIR_PATH + DetailerConstants.MOA_FOLDER);
		}
		
		for (File singleBean: htmlDirectory.listFiles())
		{
			if(singleBean.isDirectory())
			{
				if (mFrom == DetailerConstants.CREATE_FRAGMENT_ID)
				{
					if (!singleBean.getName().contains(DetailerConstants.CREATED_PRES_CONCAT_TEXT)){
						PresentationBean bean = new PresentationBean();
						beanCollecter(singleBean, bean);
					}
				}
				else
				{
					PresentationBean bean = new PresentationBean();
					beanCollecter(singleBean, bean);
				}
			}
		}
		return statusString;
	}

	private void beanCollecter(File singleBean, PresentationBean bean) {
		
		bean.setName(singleBean.getName());
		bean.setPathOnSdCard(singleBean.getAbsolutePath());
		
		bean.setmIndexFilePath(singleBean.getAbsolutePath()+"/"
						+ DetailerConstants.HTML_INDEX_FILE_NAME);
		switch (mFrom)
		{
		case DetailerConstants.HOME_FRAGMENT_ID:
			bean.setmIndexFilePath(bean.getmIndexFilePath()+".html");
			break;
		case DetailerConstants.DATA_SHEET_FRAGMENT_ID:
			bean.setmIndexFilePath(bean.getmIndexFilePath()+".pdf");
			break;
		case DetailerConstants.CREATE_FRAGMENT_ID:
			bean.setmIndexFilePath(bean.getmIndexFilePath()+".html");
			break;
		case DetailerConstants.MOA_FRAGMENT_ID:
			bean.setmIndexFilePath(bean.getmIndexFilePath()+".mp4");
			break;
		}
		
		bean.setImageUrl(singleBean.getAbsolutePath() + "/"
						+ DetailerConstants.HTML_THUMBNAIL_DIR + "/" 
						+ DetailerConstants.HTML_MAIN_IMG);
		
//		if (mFrom != DetailerConstants.MOA_FRAGMENT_ID)
//		{
//			bean.setmSampleFilePath(singleBean.getAbsolutePath()+"/"
//					+ DetailerConstants.HTML_SAMPLE_FILE_NAME);
//		}
		
		preBeanList.add(bean);
	}

	private void onFirstLaunch() {
		File mainDirectoryPath = new File(DetailerConstants.MAIN_DIR_PATH);
		if (!mainDirectoryPath.exists()){
			mainDirectoryPath.mkdirs();
			new File(DetailerConstants.MAIN_DIR_PATH + DetailerConstants.ZIP_FOLDER).mkdirs();
			new File(DetailerConstants.MAIN_DIR_PATH + DetailerConstants.HTML_FOLDER).mkdirs();
			new File(DetailerConstants.MAIN_DIR_PATH + DetailerConstants.MOA_FOLDER).mkdirs();
			new File(DetailerConstants.MAIN_DIR_PATH + DetailerConstants.PDF_FOLDER).mkdirs();
			new File(DetailerConstants.MAIN_DIR_PATH + DetailerConstants.MOA_FOLDER).mkdirs();
		}
	}
	@Override
	protected void onPostExecute(String result)
	{
		super.onPostExecute(result);
		
		if (mDialog.isShowing()){
			mDialog.dismiss();
		}

		if (mFrom == DetailerConstants.HOME_FRAGMENT_ID){
			if (preBeanList.size() != 0){
				if (DetailerConstants.isLogEnabled)
					System.out
							.println("PresentationCollectionAsynctask.onPostExecute()");
				HomeFragment.mFragmentHome.setGridViewAdapter(preBeanList);
			}else{
				HomeFragment.mFragmentHome.noDataFound();
			}			
		}else 
			if (mFrom == DetailerConstants.CREATE_FRAGMENT_ID) {
				if (preBeanList.size() != 0){
					CreateFragment.mFragmentCreate.setGridViewAdapter(preBeanList);
				}else{
					CreateFragment.mFragmentCreate.noDataFound();
				}
		}else 
			if(mFrom == DetailerConstants.DATA_SHEET_FRAGMENT_ID)
			{
				if (preBeanList.size() != 0){
					DataSheetFragment.mFragmentDataSheet.setGridViewAdapter(preBeanList);
				}else{
					DataSheetFragment.mFragmentDataSheet.noDataFound();
				}
			}
			else
				if (mFrom == DetailerConstants.MOA_FRAGMENT_ID)
				{
					if (preBeanList.size() != 0)
					{
						MOFFragment.mInstanceFragment.setGridViewAdapter(preBeanList);
					}
					else
					{
						MOFFragment.mInstanceFragment.noDataFound();
					}
				}
	}
}
