package com.e.detailer.asynctask;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.e.detailer.DetailerConstants;
import com.e.detailer.DetailerUtils;
import com.e.detailer.activity.ActivityDetailerMainTabs;
import com.e.detailer.network.GetPostSender;

public class AsynctaskLogin extends AsyncTask<String, Void, String> {

	
	private Activity mActivity;
	private String mEmailAddress;
	private String mPassword;
	private ProgressDialog mDialog;

	public AsynctaskLogin(Activity activityLogIn, String mEmailString,
			String mPasswordString) 
	{
		this.mActivity = activityLogIn;
		this.mEmailAddress = mEmailString;
		this.mPassword = mPasswordString;
	}

	@Override
	protected String doInBackground(String... params) 
	{
		String response = obtainResponseFromService();
		if (response != null)
		{
			try 
			{
				JSONObject jsonObject = new JSONObject(response);
				String status = jsonObject.getString("data");
				if (status.equalsIgnoreCase("success"))
				{
					PreferenceManager.getDefaultSharedPreferences(mActivity).edit().putString(DetailerConstants.TEAM_ID_KEY, mEmailAddress).commit();
					return "";
				}
				else
				{
					return "Oops, the system is unable to login you right now. Please try later!";
				}
			}
			catch (Exception e) 
			{
				e.printStackTrace();
				return "Oops, the system is unable to login you right now. Please try later!";
			}
		}
		else
		{
			return "Oops...! Unable to get the response";
		}
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mDialog = ProgressDialog.show(mActivity, "", "Please wait...!");
		mDialog.show();
	}
	
	@Override
	protected void onPostExecute(String result)
	{
		super.onPostExecute(result);
		try 
		{
			if (mDialog != null && mDialog.isShowing())
			{
				mDialog.dismiss();
			}			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		
		if (result.equalsIgnoreCase(""))
		{
			Intent intent = new Intent(mActivity, ActivityDetailerMainTabs.class);
			mActivity.startActivity(intent);
			mActivity.finish();
		}
		else
		{
			DetailerUtils.showMsgDialog(mActivity, "", result, null);
		}
	}
	
	private String obtainResponseFromService() 
	{
		String response = null;
		try
		{
			JSONObject jsonObject = new JSONObject();
			
			jsonObject.put("team_id", mEmailAddress);
			jsonObject.put("team_password", mPassword);
			jsonObject.put("RememberMe", "true");
			
			response = new GetPostSender().sendPostJSONSaveResponseCookie(DetailerConstants.LOGIN_URL, jsonObject.toString());
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		return response;
	}
}