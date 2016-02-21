package com.e.detailer.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.e.detailer.DetailerConstants;
import com.e.detailer.DetailerUtils;
import com.e.detailer.PresentationBean;
import com.e.detailer.activity.R;
import com.e.detailer.adaper.MainScreenAdapter;
import com.e.detailer.asynctask.DownloadPresentationAsyncTask;
import com.e.detailer.asynctask.PresentationCollectionAsynctask;

public class DataSheetFragment extends Fragment implements View.OnClickListener{

	private GridView mainGridView;
	private ArrayList<PresentationBean> mPreBeanList;
	public static DataSheetFragment mFragmentDataSheet;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle saveInstanceState){
		View view = inflater.inflate(R.layout.fragment_home_and_create, viewGroup, false);
		InitUI(view);
		if (mPreBeanList != null){
			if (mainGridView.getAdapter() != null){
				((BaseAdapter) mainGridView.getAdapter()).notifyDataSetChanged();
			}else{
				MainScreenAdapter adapter = new MainScreenAdapter(getActivity(), mPreBeanList, DetailerConstants.DATA_SHEET_FRAGMENT_ID);
				mainGridView.setAdapter(adapter);
			}
		}else{
			loadPresentations();
		}
		mFragmentDataSheet = this;
		return view;
	
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == DetailerConstants.INSTALL_SLIDE_INTENT_CODE){
			Uri uri = data.getData();
			
			if (uri != null){
//				new DownloadPresentationAsyncTask(
//						this, 
//						DetailerUtils.getPath(uri, getActivity()), 
//						DetailerConstants.DATA_SHEET_FRAGMENT_ID)
//				.execute();
			}else{
				Toast.makeText(getActivity(), "No data is selected.", Toast.LENGTH_LONG).show();
			}
			
		}
	}

	private void InitUI(View view) {
		mainGridView = (GridView) view.findViewById(R.id.gridView1);
		Button downloadButton = (Button) view.findViewById(R.id.download_home_frag);
		downloadButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
//		switch (v.getId()) {
//		case R.id.download_home_frag:
//			new DownloadPresentationAsyncTask(getActivity()).execute();
//			break;
//
//		default:
//			break;
//		}
		if (v.getId() == R.id.download_home_frag){
			
//			DetailerUtils.createDialogWithTheme(getActivity(), "No link is available for this download...");
//			AlertDialog.Builder builder = DetailerUtils.createDialogWithTheme(getActivity(), getString(R.string.install_slide_from_sd));
//			builder.setPositiveButton(getString(android.R.string.yes), new DialogInterface.OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					dialog.dismiss();
//					DetailerUtils.requestToPickFileFromSd(mFragmentDataSheet);
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
			
		}
		
	}
	
	public void setGridViewAdapter(ArrayList<PresentationBean> preBeanList) {
		this.setmPreBeansList(preBeanList);
		if (preBeanList != null){
			if (mainGridView.getAdapter() != null){
				 ((BaseAdapter) mainGridView.getAdapter()).notifyDataSetChanged();
			}else{
				MainScreenAdapter adapter = new MainScreenAdapter(getActivity(), mPreBeanList, DetailerConstants.DATA_SHEET_FRAGMENT_ID);
				mainGridView.setAdapter(adapter);
			}
		}else{
			mainGridView.setAdapter(null);
		}
		
	}
	
	private void setmPreBeansList(ArrayList<PresentationBean> preBeanList) {
		this.mPreBeanList = preBeanList;
	}

	public void noDataFound() {
		mainGridView.setAdapter(null);
		Toast.makeText(getActivity(), "No data is available", Toast.LENGTH_LONG).show();
	}
	
	public void loadPresentations(){
		new PresentationCollectionAsynctask(getActivity(), DetailerConstants.DATA_SHEET_FRAGMENT_ID).execute();
	}


}
