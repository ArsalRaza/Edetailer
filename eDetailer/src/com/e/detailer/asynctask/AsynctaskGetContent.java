package com.e.detailer.asynctask;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.e.detailer.DetailerConstants;
import com.e.detailer.DetailerUtils;
import com.e.detailer.activity.ActivityDetailerMainTabs;
import com.e.detailer.beans.DownloadPresentationBeans;
import com.e.detailer.network.GetPostSender;

public class AsynctaskGetContent extends
		AsyncTask<String, Void, String> 
{
	private Activity mActivity;
	private ProgressDialog mDialog;
	private ArrayList<DownloadPresentationBeans> mList;
	private int mPurpose;
	
	public AsynctaskGetContent(Activity activityLogIn, int purposeString) 
	{
		this.mActivity = activityLogIn;
		this.mPurpose = purposeString;
	}
	
	@Override
	protected String doInBackground(String... params) 
	{
		String response = obtainResponseFromService();
		if (response != null)
		{
			try 
			{
				mList = new ArrayList<DownloadPresentationBeans>();
				JSONArray jsonObject = new JSONArray(response);
				for (int i = 0; i < jsonObject.length(); i++) 
				{
					JSONObject downloadJsonObject = jsonObject.getJSONObject(i);
					DownloadPresentationBeans downloadPresentationBeans = new DownloadPresentationBeans();
					
					downloadPresentationBeans.setMmImageString("http://cmsmd.ipadedetailer.com" + downloadJsonObject.getString("image"));
					if (mPurpose == DetailerConstants.HOME_FRAGMENT_ID)
					{
						downloadPresentationBeans.setMmZipUrlString((downloadJsonObject.getString("presentation_file_url").contains("www")?downloadJsonObject.getString("presentation_file_url").replace("www", "cmsmd"):downloadJsonObject.getString("presentation_file_url")));
					}
					else
					{
						downloadPresentationBeans.setMmZipUrlString((downloadJsonObject.getString("video_file_url").contains("www")?downloadJsonObject.getString("video_file_url").replace("www", "cmsmd") : downloadJsonObject.getString("video_file_url")));
					}
					downloadPresentationBeans.setmNameString(downloadJsonObject.getString("product_name"));
					
					mList.add(downloadPresentationBeans);
				}
				return "";
			}
			catch (Exception e) 
			{
				e.printStackTrace();
				return "Oops...! Irregular response is coming from server.";
			}
		}
		else
		{
			return "Oops...! Unable to get the response";
		}
	}
	
	private String obtainResponseFromService()
	{
		String response = null;
		try
		{
			String url = "";
			if (mPurpose == DetailerConstants.HOME_FRAGMENT_ID)
			{
				url = DetailerConstants.GET_CONTENT_URL;
			}
			else
			{
				url = DetailerConstants.GET_VIDEO_URL;
			}

			response = new GetPostSender().sendGet(DetailerConstants.BASE_URL + url);
			Log.e("response:", response);
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		return response;
	}

	@Override
	protected void onPostExecute(String result)
	{
		super.onPostExecute(result);
		
		if (mDialog != null && mDialog.isShowing())
		{
			mDialog.dismiss();
		}
		
		if (result.equalsIgnoreCase(""))
		{
			if (mActivity instanceof ActivityDetailerMainTabs)
				((ActivityDetailerMainTabs) mActivity).updateDataAfterSync(mList);
		}
		else
		{
			DetailerUtils.showMsgDialog(mActivity, "", result, null);
		}
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mDialog = ProgressDialog.show(mActivity, "", "Please wait...!");
		mDialog.show();
	}
}
