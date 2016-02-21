package com.e.detailer.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import com.e.detailer.adaper.AdapterDoctorList;
import com.e.detailer.asynctask.AsynctaskGetAllDoctors;
import com.e.detailer.beans.DoctorBean;

public class ActivityDoctorList extends Activity 
{
	private ListView mDoctorListView;
	private ArrayList<DoctorBean> mAllDoctorsBean;
	private EditText mFilterEditText;
	private AdapterDoctorList mAdapterDoctorList;

	@Override
	public void onBackPressed() 
	{
//		super.onBackPressed();
		new AlertDialog
		.Builder(this)
		.setTitle(this.getString(R.string.app_name))
		.setMessage("Are you sure, you want to exit?")
		.setPositiveButton("Yes", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				ActivityDoctorList.super.onBackPressed();
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doctor_list);
		
		mDoctorListView = (ListView) findViewById(R.id.activity_doctor_listview);
		mFilterEditText = (EditText) findViewById(R.id.activity_doctor_sync_edittext);
		mFilterEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				if (mAdapterDoctorList != null)
				{
					mAdapterDoctorList.getFilter().filter(s);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		if (mAllDoctorsBean != null)
		{
			updateDoctors(mAllDoctorsBean);
		}
		else
		{
			new AsynctaskGetAllDoctors(this).execute();
		}
		
//		new Handler().postDelayed(new Runnable() {
//			
//			@Override
//			public void run()
//			{
//				
//			}
//		}, 500);
	}
	
	public void updateDoctors(ArrayList<DoctorBean> mAllDoctorsBean2)
	{
		this.mAllDoctorsBean = mAllDoctorsBean2;
		if (mAllDoctorsBean2 != null)
		{
			mAdapterDoctorList = new AdapterDoctorList(this, mAllDoctorsBean2); 
			mDoctorListView.setAdapter(mAdapterDoctorList);
			
		}
		else
		{
			((AdapterDoctorList) mDoctorListView.getAdapter()).updateContent(mAllDoctorsBean2);
		}
	}
}