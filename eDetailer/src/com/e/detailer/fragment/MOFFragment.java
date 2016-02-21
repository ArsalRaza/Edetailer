package com.e.detailer.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.e.detailer.DetailerConstants;
import com.e.detailer.PresentationBean;
import com.e.detailer.activity.ActivityDetailerMainTabs;
import com.e.detailer.activity.R;
import com.e.detailer.adaper.MainScreenAdapter;
import com.e.detailer.asynctask.PresentationCollectionAsynctask;

public class MOFFragment extends Fragment implements OnClickListener
{
	public static MOFFragment mInstanceFragment;
	private ArrayList<PresentationBean> mPreBeanList;
	private GridView mainGridView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle saveInstanceState){
		View view = inflater.inflate(R.layout.fragment_moa, viewGroup, false);
		InitUI(view);
		if (ActivityDetailerMainTabs.getInstance().getmVideoBeans() != null)
		{
			if (mainGridView.getAdapter() != null)
			{
				((BaseAdapter) mainGridView.getAdapter()).notifyDataSetChanged();
			}
			else
			{
				MainScreenAdapter adapter = new MainScreenAdapter(getActivity(), ActivityDetailerMainTabs.getInstance().getmVideoBeans(), DetailerConstants.MOA_FRAGMENT_ID);
				mainGridView.setAdapter(adapter);
			}
		}
		else
		{
			loadPresentations();
		}

		mInstanceFragment = this;
		return view;
	}

	public void loadPresentations()
	{
		new PresentationCollectionAsynctask(getActivity(), DetailerConstants.MOA_FRAGMENT_ID).execute();
	}

	private void InitUI(View view) 
	{
		mainGridView = (GridView) view.findViewById(R.id.gridView1);
//		Button downloadButton = (Button) view.findViewById(R.id.download_home_frag);
//		downloadButton.setOnClickListener(this);
	}

	public void noDataFound()
	{
		mainGridView.setAdapter(null);
		Toast.makeText(getActivity(), "No data is available", Toast.LENGTH_LONG).show();
	}
	
	public void setGridViewAdapter(ArrayList<PresentationBean> preBeanList)
	{
		this.setmPreBeansList(preBeanList);
		ActivityDetailerMainTabs.getInstance().setmVideoBeans(preBeanList);
		if (preBeanList != null)
		{
			if (mainGridView.getAdapter() != null)
			{
				 ((BaseAdapter) mainGridView.getAdapter()).notifyDataSetChanged();
			}
			else
			{
				MainScreenAdapter adapter = new MainScreenAdapter(getActivity(), mPreBeanList, DetailerConstants.MOA_FRAGMENT_ID);
				mainGridView.setAdapter(adapter);
			}
		}
		else
		{
			mainGridView.setAdapter(null);
		}
	}
	private void setmPreBeansList(ArrayList<PresentationBean> preBeanList) 
	{
		this.mPreBeanList = preBeanList;
	}
	
	@Override
	public void onClick(View v) 
	{
		
	}

	public void refresh() {
		
	}
}
