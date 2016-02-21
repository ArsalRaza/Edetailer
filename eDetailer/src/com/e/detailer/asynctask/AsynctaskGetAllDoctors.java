package com.e.detailer.asynctask;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.e.detailer.DetailerConstants;
import com.e.detailer.activity.ActivityDoctorList;
import com.e.detailer.beans.DoctorBean;
import com.e.detailer.network.GetPostSender;

public class AsynctaskGetAllDoctors extends AsyncTask<String, Void, String> {

	private Activity mActivity;
	private ProgressDialog mDialog;
	private ArrayList<DoctorBean> mList;

	public AsynctaskGetAllDoctors(Activity activity)
	{
		this.mActivity = activity;
	}
	
	@Override
	protected void onPreExecute() 
	{
		super.onPreExecute();
		mDialog = ProgressDialog.show(mActivity, "", "Please wait...!");
	}
	
	@Override
	protected String doInBackground(String... params)
	{
		String response = new GetPostSender().sendGet(DetailerConstants.BASE_URL + DetailerConstants.GET_ALL_DOCTORS);//getResponseFromWebService();
		if (response != null)
		{
			try 
			{
				mList = new ArrayList<DoctorBean>();
				Log.e("response:", response);
				JSONArray jsonArray = new JSONArray(/*"[{\"name\":\"shan\",\"id\":1}]"*/response);
				for (int i = 0; i < jsonArray.length(); i++) 
				{
					JSONObject doctorJsonObject = jsonArray.getJSONObject(i);
					DoctorBean doctorBean = new DoctorBean();
					
					doctorBean.mDoctorName = doctorJsonObject.getString("name");
					doctorBean.mDoctorId = doctorJsonObject.getString("id");
					doctorBean.mJsonString = doctorJsonObject.toString();
					
					mList.add(doctorBean);
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

	@Override
	protected void onPostExecute(String result)
	{
		super.onPostExecute(result);
		if (mDialog != null && mDialog.isShowing())
		{
			mDialog.dismiss();
		}
		
		if (mActivity instanceof ActivityDoctorList)
		{
			((ActivityDoctorList) mActivity).updateDoctors(mList);
		}
	}
}