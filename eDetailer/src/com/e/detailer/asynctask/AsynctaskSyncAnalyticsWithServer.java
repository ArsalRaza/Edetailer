package com.e.detailer.asynctask;

import org.json.JSONException;
import org.json.JSONObject;

import com.e.detailer.DetailerConstants;
import com.e.detailer.DetailerUtils;
import com.e.detailer.activity.DetailerApplication;
import com.e.detailer.activity.R;
import com.e.detailer.network.GetPostSender;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class AsynctaskSyncAnalyticsWithServer extends
		AsyncTask<String, Void, String>
{
	private JSONObject mAnalyticsObject;
	private Activity mActivity;
	private ProgressDialog mDialog;
	
	public AsynctaskSyncAnalyticsWithServer (Activity activity, JSONObject jsonObject)
	{
		this.mActivity = activity;
		this.mAnalyticsObject = jsonObject;
	}
	
	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
		mDialog = ProgressDialog.show(mActivity, "", "Please wait, while making you uptodate with the server...!");
	}
	
	@Override
	protected String doInBackground(String... params)
	{
		String response = new GetPostSender().sendPostJSON("http://cmsmd.ipadedetailer.com/api/" + DetailerConstants.SYNC_ANALYTICS, mAnalyticsObject.toString());
		if (response == null || response.equalsIgnoreCase("false"))
		{
			return "Server timed out...!";
		}

		try
		{
			JSONObject jsonObject = new JSONObject(response);
			return "Server is responding strangly...!";
		}
		catch (JSONException e) 
		{
			DetailerApplication.getmAppPrefEditor().putString(DetailerConstants.LAST_ANALYTICS_KEY, "[]").commit();
			e.printStackTrace();
		}
		return "";
	}
	
	@Override
	protected void onPostExecute(String result)
	{
		super.onPostExecute(result);
		if (mDialog != null && mDialog.isShowing())
		{
			mDialog.dismiss();
		}
		if (!result.equals(""))
		{
			DetailerUtils.showMsgDialog(mActivity, mActivity.getString(R.string.app_name), result, null);
		}
	}
}