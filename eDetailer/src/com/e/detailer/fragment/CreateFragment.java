package com.e.detailer.fragment;

import java.util.ArrayList;

import com.e.detailer.DetailerConstants;
import com.e.detailer.PresentationBean;
import com.e.detailer.activity.ActivityDetailerMainTabs;
import com.e.detailer.activity.R;
import com.e.detailer.adaper.MainScreenAdapter;
import com.e.detailer.asynctask.PresentationCollectionAsynctask;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

public class CreateFragment extends Fragment {
	
	public static CreateFragment mFragmentCreate;
	private GridView mainGridView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle saveInstanceState){
		
		View view = inflater.inflate(R.layout.fragment_home_and_create, viewGroup, false);
		InitUI(view);
		if (ActivityDetailerMainTabs.getInstance().getmCreateBeansList() != null){
			if (mainGridView.getAdapter() != null){
				((BaseAdapter) mainGridView.getAdapter()).notifyDataSetChanged();
			}else{
				MainScreenAdapter adapter = new MainScreenAdapter(getActivity(), ActivityDetailerMainTabs.getInstance().getmCreateBeansList(), DetailerConstants.CREATE_FRAGMENT_ID);
				mainGridView.setAdapter(adapter);
			}
		}else{
			loadPresentations();
		}
		mFragmentCreate = this;
		return view;
	}
	
	private void InitUI(View view) {
		
		mainGridView = (GridView) view.findViewById(R.id.gridView1);
		Button downloadButton = (Button) view.findViewById(R.id.download_home_frag);
		downloadButton.setVisibility(View.GONE);
	}
	
	private void loadPresentations(){
		new PresentationCollectionAsynctask(getActivity(), DetailerConstants.CREATE_FRAGMENT_ID).execute();
	}

	public void noDataFound() {
		mainGridView.setAdapter(null);
		Toast.makeText(getActivity(), "No data is available", Toast.LENGTH_LONG).show();
	}
	
	public void setGridViewAdapter(ArrayList<PresentationBean> preBeanList) {
		ActivityDetailerMainTabs.getInstance().setmCreateBeansList(preBeanList);
		if (preBeanList != null){
			if (mainGridView.getAdapter() != null){
				 ((BaseAdapter) mainGridView.getAdapter()).notifyDataSetChanged();
			}else{
				MainScreenAdapter adapter = new MainScreenAdapter(getActivity(), ActivityDetailerMainTabs.getInstance().getmCreateBeansList(), DetailerConstants.CREATE_FRAGMENT_ID);
				mainGridView.setAdapter(adapter);
			}
		}else{
			mainGridView.setAdapter(null);
		}
	}
}
