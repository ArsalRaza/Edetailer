package com.e.detailer.adaper;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.e.detailer.DetailerConstants;
import com.e.detailer.activity.DetailerApplication;
import com.e.detailer.activity.R;
import com.e.detailer.beans.DoctorBean;

public class AdapterDoctorList extends BaseAdapter implements OnClickListener, Filterable
{

	private ArrayList<DoctorBean> mList;
	private Activity mActivity;
	private LayoutInflater mLayoutInflater;
	private ArrayList<DoctorBean> mFilterList;
	private DoctorFilter mFilter;

	public AdapterDoctorList(Activity activityDoctorList, ArrayList<DoctorBean> mAllDoctorsBean2)
	{
		this.mActivity = activityDoctorList;
		this.mList = mAllDoctorsBean2;
		this.mFilterList = mAllDoctorsBean2;
		this.mLayoutInflater = (LayoutInflater) activityDoctorList.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() 
	{
		return mList.size();
	}

	@Override
	public Object getItem(int position)
	{
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = mLayoutInflater.inflate(android.R.layout.simple_list_item_1, null);
		}
		TextView mTextView = (TextView) convertView.findViewById(android.R.id.text1);
		mTextView.setTag(position);
		mTextView.setText("Dr. " + mList.get(position).mDoctorName);
		convertView.setOnClickListener(this);
		return convertView;
	}

	public void updateContent(ArrayList<DoctorBean> mAllDoctorsBean2)
	{
		this.mList = mAllDoctorsBean2;
		this.mFilterList = mAllDoctorsBean2;
		notifyDataSetChanged();
	}

	@Override
	public void onClick(View v)
	{
		final View view = v;
		new AlertDialog
		.Builder(mActivity)
		.setTitle(mActivity.getString(R.string.app_name))
		.setMessage("Are you sure, you want to select this doctor?")
		.setPositiveButton("Yes", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				Integer pos = (Integer) view.getTag();
				DetailerApplication.getmAppPrefEditor().putString(DetailerConstants.DOCTORS_JSON, mList.get(pos).toString()).commit();
				mActivity.finish();
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
	public Filter getFilter() 
	{
	    if(mFilter == null)
	    {

	    	mFilter = new DoctorFilter();
	    }

	    return mFilter;
	}
	
	private class DoctorFilter extends Filter
	{

		@Override
		protected FilterResults performFiltering(CharSequence constraint) 
		{
	        FilterResults results=new FilterResults();

	        if(constraint!=null && constraint.length()>0)
	        {
	            ArrayList<DoctorBean> filterList=new ArrayList<DoctorBean>();
	            for(int i=0;i<mFilterList.size();i++)
	            {
	            	Log.e("conrt:" + constraint.toString(), "doctorBean:" + mFilterList.get(i).mDoctorName);
	                if(mFilterList.get(i).mDoctorName.toLowerCase().contains(constraint.toString().toLowerCase())) 
	                {
	                    filterList.add(mFilterList.get(i));
	                }
	            }
	            results.count=filterList.size();
	            results.values=filterList;
	        }
	        else
	        {
	            results.count=mFilterList.size();
	            results.values=mFilterList;
	        }
	        return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) 
		{
			mList = (ArrayList<DoctorBean>) results.values;
			notifyDataSetChanged();
		}
	}
}