package com.e.detailer.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.e.detailer.DetailerConstants;
import com.e.detailer.PresentationBean;
import com.e.detailer.activity.ActivityDetailerMainTabs;
import com.e.detailer.activity.R;
import com.e.detailer.adaper.MainScreenAdapter;
import com.e.detailer.asynctask.PresentationCollectionAsynctask;


public class HomeFragment extends Fragment implements OnClickListener{
	
	private GridView mainGridView;
	public static HomeFragment mFragmentHome;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle saveInstanceState){
		
		View view = inflater.inflate(R.layout.fragment_home_and_create, viewGroup, false);
		InitUI(view);
		if (ActivityDetailerMainTabs.getInstance().getmBrowseBeansList() != null){
			if (mainGridView.getAdapter() != null){
				((BaseAdapter) mainGridView.getAdapter()).notifyDataSetChanged();
			}else{
				MainScreenAdapter adapter = new MainScreenAdapter(getActivity(), ActivityDetailerMainTabs.getInstance().getmBrowseBeansList(), DetailerConstants.HOME_FRAGMENT_ID);
				mainGridView.setAdapter(adapter);
			}
		}else{
			loadPresentations();
		}
		mFragmentHome = this;
		return view;
	}
	
	public void refresh(){
		
		if (DetailerConstants.isLogEnabled)
			System.out.println("HomeFragment.refresh()");
		
		ActivityDetailerMainTabs.getInstance().setmBrowseBeansList(null);
		loadPresentations();
//		if (ActivityDetailerMainTabs.getInstance().getmBrowseBeansList() != null){
//			if (mainGridView.getAdapter() != null){
//				((BaseAdapter) mainGridView.getAdapter()).notifyDataSetChanged();
//				
//				if(DetailerConstants.isLogEnabled)
//					System.out.println("HomeFragment.refresh().notifyDataSetChanged()");
//
//			}else{
//				MainScreenAdapter adapter = new MainScreenAdapter(getActivity(), ActivityDetailerMainTabs.getInstance().getmBrowseBeansList(), DetailerConstants.HOME_FRAGMENT_ID);
//				mainGridView.setAdapter(adapter);
//			}
//		}else{
//			loadPresentations();
//		}
	}
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		System.out.println("HomeFragment.onActivityResult()");
//		if (requestCode == DetailerConstants.INSTALL_SLIDE_INTENT_CODE){
//			Uri uri = data.getData();
//			if (uri != null){
//				new DownloadPresentationAsyncTask(this
//						, DetailerUtils.getPath(uri, getActivity())
//						, DetailerConstants.HOME_FRAGMENT_ID)
//				.execute();
//				
//			}else{
//				Toast.makeText(getActivity(), "No data is selected.", Toast.LENGTH_LONG).show();
//			}
//		}
//	}
	
	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.download_home_frag){
//			new DownloadPresentationAsyncTask(mFragmentHome, null, DetailerConstants.HOME_FRAGMENT_ID).execute();

//			AlertDialog.Builder builder = DetailerUtils.createDialogWithTheme(getActivity(), getString(R.string.install_slide_from_sd));
//			builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					dialog.dismiss();
//					DetailerUtils.requestToPickFileFromSd(mFragmentHome);
//				}
//			});
//			builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					dialog.dismiss();
//					new DownloadPresentationAsyncTask(mFragmentHome, null, DetailerConstants.HOME_FRAGMENT_ID).execute();
//				}
//			});
//			builder.create().show();
			
		}
		
//		switch (v.getId()) {
//		case R.id.download_home_frag:
//			
//			AlertDialog.Builder builder = DetailerUtils.createDialogWithTheme(getActivity(), getString(R.string.install_slide_from_sd));
//			builder.setPositiveButton(getString(android.R.string.yes), new DialogInterface.OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					dialog.dismiss();
//					DetailerUtils.requestToPickFileFromSd(getActivity());
//				}
//			});
//			builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					dialog.dismiss();
//				}
//			});
//			builder.create().show();
//			
//			break;
//
//		default:
//			break;
//		}
	}

	private void InitUI(View view) {
		mainGridView = (GridView) view.findViewById(R.id.gridView1);
		Button downloadButton = (Button) view.findViewById(R.id.download_home_frag);
		downloadButton.setOnClickListener(this);
	}
	
	public void setGridViewAdapter(ArrayList<PresentationBean> preBeanList) {
	
		ActivityDetailerMainTabs.getInstance().setmBrowseBeansList(preBeanList);
		if (preBeanList != null){
			mainGridView.setAdapter(null);
			if (mainGridView.getAdapter() != null){
				((BaseAdapter) mainGridView.getAdapter()).notifyDataSetChanged();
			}else{
				MainScreenAdapter adapter = new MainScreenAdapter(getActivity(), ActivityDetailerMainTabs.getInstance().getmBrowseBeansList(), DetailerConstants.HOME_FRAGMENT_ID);
				mainGridView.setAdapter(adapter);
			}
		}else{
			mainGridView.setAdapter(null);
		}
		
	}
	
	public void noDataFound() {
		mainGridView.setAdapter(null);
		Toast.makeText(getActivity(), "No data is available", Toast.LENGTH_LONG).show();
	}
	
	public void loadPresentations(){
		new PresentationCollectionAsynctask(getActivity(), DetailerConstants.HOME_FRAGMENT_ID).execute();
	}


}
