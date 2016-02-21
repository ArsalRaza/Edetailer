package com.e.detailer.fragment;

import java.util.List;

import com.e.detailer.DetailerConstants;
import com.e.detailer.activity.R;
import com.e.detailer.activity.WebViewActivity;
import com.e.detailer.adaper.AdapterViewPagerImagesFromSd;
import com.e.detailer.asynctask.AsyncTaskCreatePdfFile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

public class BucketFragment extends Fragment implements OnClickListener{
	
	private ViewPager bucketPager;
	private List<String> mUriList;
	private ImageView leftSideImageView;
	private ImageView rightSideImageView;
	private ImageView saveImageView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle saveInstanceState){
		View view = inflater.inflate(R.layout.fragment_bucket, viewGroup, false);
		InitUI(view);
		setBucketList(WebViewActivity.mUriList);
		
		if (mUriList != null && mUriList.size() != 0){
			if (bucketPager.getAdapter() != null){
				bucketPager.setAdapter(bucketPager.getAdapter());
			}else{
				AdapterViewPagerImagesFromSd adapter = new AdapterViewPagerImagesFromSd(getActivity(), 
						null, getBucketList(), 
						DetailerConstants.BUCKET_FRAGMENT_ID);
				
				bucketPager.setAdapter(adapter);
			}
		}else{
			// if no data is available
			leftSideImageView.setVisibility(View.GONE);
			rightSideImageView.setVisibility(View.GONE);
			saveImageView.setVisibility(View.GONE);
			Toast.makeText(getActivity(), "No data is available", Toast.LENGTH_LONG).show();
		}
		
		return view;
	}
	private List<String> getBucketList() {
		return mUriList;
	}

	private void setBucketList(List<String> mUriList) {
		if (mUriList != null)
			this.mUriList = mUriList;
	}

	private void InitUI(View view) {
		
		leftSideImageView = (ImageView) view.findViewById(R.id.left_btn);
		saveImageView = (ImageView) view.findViewById(R.id.save_btn);
		rightSideImageView = (ImageView) view.findViewById(R.id.right_btn);
		bucketPager = (ViewPager) view.findViewById(R.id.bucket_pager);
		saveImageView.setOnClickListener(this);
		leftSideImageView.setOnClickListener(this);
		rightSideImageView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.left_btn){
			if (bucketPager.getCurrentItem() == 0){
				Toast.makeText(getActivity(), "No previous Item", Toast.LENGTH_LONG).show();
			}else{
				bucketPager.setCurrentItem(bucketPager.getCurrentItem()-1, true);
			}
		}else if (v.getId() == R.id.right_btn) {
			if (bucketPager.getCurrentItem() == getBucketList().size()- 1){
				Toast.makeText(getActivity(), "No more Items are available", Toast.LENGTH_LONG).show();
			}else{
				bucketPager.setCurrentItem(bucketPager.getCurrentItem() + 1, true);
			}
		}else if (v.getId() == R.id.save_btn) {
			saveToPDF();
		}

	}
//		switch (v.getId()) {
//		case R.id.left_btn:
//			if (bucketPager.getCurrentItem() == 0){
//				Toast.makeText(getActivity(), "No previous Item", Toast.LENGTH_LONG).show();
//			}else{
//				bucketPager.setCurrentItem(bucketPager.getCurrentItem()-1, true);
//			}
//			break;
//		case R.id.right_btn:
//			if (bucketPager.getCurrentItem() == getBucketList().size()- 1){
//				Toast.makeText(getActivity(), "No more Items are available", Toast.LENGTH_LONG).show();
//			}else{
//				bucketPager.setCurrentItem(bucketPager.getCurrentItem() + 1, true);
//			}
//			break;
//		case R.id.save_btn:
//			saveToPDF();
//			break;
//			
//		default:
//			break;
//		}
		
	private void saveToPDF() {
		new AsyncTaskCreatePdfFile(getActivity(), getBucketList()).execute();
	}
	
	
}
