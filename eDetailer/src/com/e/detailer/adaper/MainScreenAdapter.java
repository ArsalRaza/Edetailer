package com.e.detailer.adaper;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.e.detailer.DetailerConstants;
import com.e.detailer.PresentationBean;
import com.e.detailer.activity.ActivityMOAPreview;
import com.e.detailer.activity.CreateNewPresentActivity;
import com.e.detailer.activity.PdfActivity;
import com.e.detailer.activity.R;
import com.e.detailer.activity.WebViewActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class MainScreenAdapter extends BaseAdapter implements OnClickListener {
	
	private ArrayList<PresentationBean> preBeanList;
	private FragmentActivity mActivity;
	private LayoutInflater layoutInflater;
	private int mFrom = 0;
	private Picasso mPicasso;

	public MainScreenAdapter(FragmentActivity mActivity, ArrayList<PresentationBean> preBeanList, int mFrom){
		
		this.mActivity = mActivity;
		this.preBeanList = preBeanList;
		this.mFrom = mFrom;
		mPicasso = Picasso.with(mActivity);
		layoutInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	
	}
	
	@Override
	public int getCount() {
		return preBeanList.size();
	}

	@Override
	public Object getItem(int position) {
		return preBeanList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	public class ViewHolder
	{
		public RelativeLayout mRelativeLayout;
		public ImageView mPresentationImage;
		public TextView mNameTextView;
		public ProgressBar mProgressBar;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		final ViewHolder holder;
		if (convertView == null)
		{
			v = layoutInflater.inflate(R.layout.single_grid_home_frag, parent, false);
		}
		holder = new ViewHolder();
		holder.mPresentationImage = (ImageView) v.findViewById(R.id.image_button_single_grid);
		holder.mRelativeLayout = (RelativeLayout) v.findViewById(R.id.single_grid_layout);
		holder.mNameTextView = (TextView) v.findViewById(R.id.textview_name);
		holder.mProgressBar = (ProgressBar) v.findViewById(R.id.progress_bar_image);
		
		if (DetailerConstants.MOA_FRAGMENT_ID != mFrom)
		{
			mPicasso.load(new File(preBeanList.get(position).getImageUrl()))
					.into(holder.mPresentationImage, new Callback()
					{
	
						@Override
						public void onError() {
							holder.mProgressBar.setVisibility(View.INVISIBLE);				
						}
	
						@Override
						public void onSuccess() {
							holder.mProgressBar.setVisibility(View.INVISIBLE);
						}
						
					});
		}
		else
		{
			holder.mPresentationImage.setImageResource(R.drawable.moa_thumb);
			holder.mProgressBar.setVisibility(View.GONE);
		}
		if (preBeanList.get(position).getName().contains(DetailerConstants.CREATED_PRES_CONCAT_TEXT)){
		
			if (mFrom == DetailerConstants.HOME_FRAGMENT_ID){
				holder.mNameTextView.setText(preBeanList.
						get(position).
						getName().
						replace(DetailerConstants.CREATED_PRES_CONCAT_TEXT, ""));
				
			}else 
				if (mFrom == DetailerConstants.CREATE_FRAGMENT_ID) {
					v.setAlpha((float) 0.5);
					v.setClickable(false);
				}
			
		}else{
			holder.mNameTextView.setText(preBeanList.get(position).getName());
		}
		
		holder.mRelativeLayout.setTag(position);
		if (preBeanList.get(position).getName().contains(DetailerConstants.CREATED_PRES_CONCAT_TEXT)){
			if (mFrom == DetailerConstants.CREATE_FRAGMENT_ID) {
				v.setAlpha((float) 0.5);
				v.setClickable(false);
			}else{
				holder.mRelativeLayout.setOnClickListener(this);
			}
		}
		else
			holder.mRelativeLayout.setOnClickListener(this);
		
		return v;
	}
	
	@Override
	public void onClick(View v){
		int pos =(Integer) v.getTag();
		
		if (mFrom == DetailerConstants.HOME_FRAGMENT_ID){
			//For Home Click Listener
			Intent homeIntent = new Intent(mActivity, WebViewActivity.class);
			homeIntent.putExtra(DetailerConstants.WEB_VIEW_EDA_TITLE, preBeanList.get(pos).getName());
			homeIntent.putExtra(DetailerConstants.WEB_VIEW_URL_KEY, preBeanList.get(pos).getmIndexFilePath());
			mActivity.startActivity(homeIntent);
		}else
			if (mFrom == DetailerConstants.CREATE_FRAGMENT_ID) {
				//From Create
				CreateNewPresentActivity.mSelectPresentation = preBeanList.get(pos);
				Intent createIntent = new Intent(mActivity, CreateNewPresentActivity.class);
				mActivity.startActivity(createIntent);
		}else 
			if (mFrom == DetailerConstants.DATA_SHEET_FRAGMENT_ID) {
			//From PDF
//			PdfActivity.mPreBean = preBeanList.get(pos);
			Intent pdfIntent = new Intent(mActivity, PdfActivity.class);
			pdfIntent.putExtra(DetailerConstants.PDF_URL_KEY, preBeanList.get(pos).getmIndexFilePath());
			mActivity.startActivity(pdfIntent);
			}
			else 
				if (mFrom == DetailerConstants.MOA_FRAGMENT_ID)
				{
				//From PDF
//				PdfActivity.mPreBean = preBeanList.get(pos);
				Intent pdfIntent = new Intent(mActivity, ActivityMOAPreview.class);
				Log.e("video:", preBeanList.get(pos).getmIndexFilePath());
				pdfIntent.putExtra(DetailerConstants.VIDEO_VIEW_URL_KEY, preBeanList.get(pos).getmIndexFilePath());
				mActivity.startActivity(pdfIntent);
				}
	}
}
