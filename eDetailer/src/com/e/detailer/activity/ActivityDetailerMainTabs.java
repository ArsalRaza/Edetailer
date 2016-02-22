package com.e.detailer.activity;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.e.detailer.DetailerConstants;
import com.e.detailer.PresentationBean;
import com.e.detailer.asynctask.AsynctaskGetContent;
import com.e.detailer.asynctask.DownloadPresentationAsyncTask;
import com.e.detailer.beans.DownloadPresentationBeans;
import com.e.detailer.fragment.HomeFragment;
import com.e.detailer.fragment.MOFFragment;
import com.e.detailer.fragment.ProgramFragment;
import com.squareup.picasso.Callback;

public class ActivityDetailerMainTabs extends FragmentActivity implements OnClickListener
{
	private static final int COLLECT_DOCTOR_REQUEST_CODE = 999;
	private FragmentManager mFragmentManagerObject;
	private Fragment mCurrentFragment;
	private HomeFragment mHomeFragment;
	private int mPreviousId;
	private ImageView mFragHomeImageView;
	private TextView mFragHomeTextView;
	private RelativeLayout mHomeLayout;
	private RelativeLayout mMOFLayout;
	private RelativeLayout mProgramLayout;
	private ImageView mFragMOFImageView;
	private TextView mFragMOFTextView;
	private ImageView mFragProgramImageView;
	private TextView mFragProgramTextView;
	private MOFFragment mMOAFragment;
	private ImageView mTitleImageView;
	private TextView mTitleTextView;
	private ProgramFragment mProgramFragment;
	private LinearLayout mLayoutForInflation;
	private ArrayList<PresentationBean> mVideoBeans;
	private ArrayList<DownloadPresentationBeans> mServerBeans;
	private ArrayList<DownloadPresentationBeans> mServerVideoContent;
	private ImageView mMakeCall;
	private ImageView mSyncDoctor;
	private TextView mSyncDoctorTxt;
	private ImageView mDoctocSyncTick;
	private TextView mContentSyncTextView;
	private RelativeLayout mSyncBottomLayout;
	private ImageView mSyncCloseImageView;
	private static ArrayList<PresentationBean> mBrowseBeansList;
	private static ArrayList<PresentationBean> mCreateBeansList;
	public static ActivityDetailerMainTabs mTabActivity;
	
	public static ActivityDetailerMainTabs getInstance() 
	{
		return mTabActivity;
	}
	
	public ArrayList<PresentationBean> getmCreateBeansList()
	{
		return mCreateBeansList;
	}

	public void setmCreateBeansList(ArrayList<PresentationBean> mCreateBeansList1)
	{
		mCreateBeansList = mCreateBeansList1;
	}
	
	public ArrayList<PresentationBean> getmBrowseBeansList() 
	{
		return mBrowseBeansList;
	}

	public void setmBrowseBeansList(ArrayList<PresentationBean> mPreBeansList1) 
	{
		mBrowseBeansList = mPreBeansList1;
	}
//	@Override
//    public boolean onKeyUp(int keyCode, KeyEvent objEvent) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//        	Log.e("asd", "asd");
//        	return true;
//        }
//        return super.onKeyUp(keyCode, objEvent);
//    }
//	 @Override
//	   public boolean onKeyDown(int keyCode, KeyEvent event) {
//     	Log.e("asd", "asd");
//		 
//		 if (keyCode == KeyEvent.KEYCODE_BACK) {
////			 onBackPressed();
//		 }
//		return false;
//		 
//	}
	@Override
	public void onBackPressed()
	{
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mFragmentManagerObject = getSupportFragmentManager();
		setContentView(R.layout.activity_detailer_main_tabs);
		mTabActivity = this;
		
		mHomeFragment = new HomeFragment();
		mMOAFragment = new MOFFragment();
		mProgramFragment = new ProgramFragment();
		
		initIU();
		
		String doctor = DetailerApplication.getmAppPreferences().getString(DetailerConstants.DOCTORS_JSON, "");
		
		// Check: Doctor not found
		if (!doctor.equalsIgnoreCase(""))
		{
			mDoctocSyncTick.setVisibility(View.VISIBLE);
			mSyncDoctorTxt.setText("CHANGE DOCTOR");
		}
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		if (mCurrentFragment == null)
		{
			onClick(mHomeLayout);
		}
	}
	
	private void initIU() 
	{
		InitBotttomLayout();
		
		mTitleImageView = (ImageView) findViewById(R.id.title_img_imgview);
		mTitleTextView = (TextView) findViewById(R.id.dash_title);
		
		mDoctocSyncTick = (ImageView) findViewById(R.id.main_detailer_header_doctor_sync_tick_img);
		mSyncCloseImageView = (ImageView) findViewById(R.id.cross_sync_bottom_layout);
		mSyncDoctor = (ImageView) findViewById(R.id.main_detailer_header_doctor_sync_btn);
		mSyncDoctorTxt = (TextView) findViewById(R.id.main_detailer_header_doctor_sync_txt);
		
		mContentSyncTextView = (TextView) findViewById(R.id.main_detailer_header_sync_txt);
		mLayoutForInflation = (LinearLayout) findViewById(R.id.layout_sync_edas);
		mSyncBottomLayout = (RelativeLayout) findViewById(R.id.sync_bottom_layout);
		
		mMakeCall = (ImageView) findViewById(R.id.make_call);
		
		mMakeCall.setOnClickListener(this);
		mSyncDoctor.setOnClickListener(this);
		mSyncDoctorTxt.setOnClickListener(this);
		mContentSyncTextView.setOnClickListener(this);
		mSyncCloseImageView.setOnClickListener(this);
	}
	
	public void updateDataAfterSync(final ArrayList<DownloadPresentationBeans> beans)
	{
		mLayoutForInflation.removeAllViews();
		if (beans.size() == 0)
		{
			mSyncBottomLayout.setVisibility(View.GONE);
			return;
		}
		else
		{
			mSyncBottomLayout.setVisibility(View.VISIBLE);
			
		}
		
		final boolean isHomeBean = mCurrentFragment instanceof HomeFragment;

		if (isHomeBean) 
		{
			mServerBeans = beans;
		}
		else
		{
			mServerVideoContent = beans;
		}
		
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for (int i = 0; i < beans.size(); i++) 
		{
			View view = layoutInflater.inflate(R.layout.adapter_server_bean, null);
			
			ImageView imageView = (ImageView) view.findViewById(R.id.adapter_server_bean_imgview);
			ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.adapter_server_bean_progress_bar);
			TextView mTextView = (TextView) view.findViewById(R.id.adapter_server_bean_txtview);
			mTextView.setText(beans.get(i).getmNameString());
			setContentView(beans.get(i).getMmImageString(), imageView, progressBar);
			view.setTag(i);
			view.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) 
				{
					int pos = (Integer) v.getTag();
					String zipurlString = beans.get(pos).getMmZipUrlString();
					new DownloadPresentationAsyncTask(ActivityDetailerMainTabs.this, zipurlString, (isHomeBean)? DetailerConstants.HOME_FRAGMENT_ID : DetailerConstants.VIDEO_FRAGMENT_ID).execute();
				}
			});
			mLayoutForInflation.addView(view);
		}
	}
	
	private void setContentView(String url, ImageView mFirstImageView, final ProgressBar progressBar) 
	{
		DetailerApplication.getmCacheManager().load(Uri.parse(url)).fit().into(mFirstImageView, new Callback(){

			@Override
			public void onError() 
			{
				progressBar.setVisibility(View.GONE);
			}

			@Override
			public void onSuccess() 
			{
				progressBar.setVisibility(View.GONE);
			}});
	}
	
	private void InitBotttomLayout()
	{
		ImageView mSyncImageView = (ImageView) findViewById(R.id.main_detailer_header_sync_btn);
		
		mHomeLayout = (RelativeLayout) findViewById(R.id.home_frag);
		mMOFLayout = (RelativeLayout) findViewById(R.id.video_frag);
		mProgramLayout = (RelativeLayout) findViewById(R.id.program_frag);
		
		RelativeLayout mLogoutLayout = (RelativeLayout) findViewById(R.id.logout_frag);
		
		mFragHomeImageView = (ImageView) findViewById(R.id.home_frag_imageview);
		mFragHomeTextView = (TextView) findViewById(R.id.home_frag_textview);
		
		mFragMOFImageView = (ImageView) findViewById(R.id.video_frag_imageview);
		mFragMOFTextView = (TextView) findViewById(R.id.video_frag_textview);
		
		mFragProgramImageView = (ImageView) findViewById(R.id.program_frag_imageview);
		mFragProgramTextView = (TextView) findViewById(R.id.program_frag_textview);
		
		mSyncImageView.setOnClickListener(this);
		mHomeLayout.setOnClickListener(this);
		mMOFLayout.setOnClickListener(this);
		mProgramLayout.setOnClickListener(this);
		mLogoutLayout.setOnClickListener(this);
	}

	private void changeFragment()
	{
		if (mCurrentFragment != null)
		{
			mFragmentManagerObject.beginTransaction().replace(R.id.content_frame, mCurrentFragment).commitAllowingStateLoss();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		
		if (requestCode == COLLECT_DOCTOR_REQUEST_CODE)
		{
			String doctor = DetailerApplication.getmAppPreferences().getString(DetailerConstants.DOCTORS_JSON, "");
			
			// Check: Doctor not found
			if (doctor.equalsIgnoreCase(""))
			{
				return;
			}
			mDoctocSyncTick.setVisibility(View.VISIBLE);
			mSyncDoctorTxt.setText("CHANGE DOCTOR");
		}
	}
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.cross_sync_bottom_layout:
			mSyncBottomLayout.setVisibility(View.GONE);
			break;
			
		case R.id.main_detailer_header_doctor_sync_btn:
		case R.id.main_detailer_header_doctor_sync_txt:
			startActivityForResult(new Intent(this, ActivityDoctorList.class), COLLECT_DOCTOR_REQUEST_CODE);
			break;
			
		case R.id.make_call:
			break;
	
		case R.id.home_frag:
			mCurrentFragment = mHomeFragment;
			tabPressed(R.id.home_frag);
			changeFragment();
			break;
			
		case R.id.video_frag:
			mCurrentFragment = mMOAFragment;
			tabPressed(R.id.video_frag);
			changeFragment();
			break;
			
		case R.id.program_frag:
			mCurrentFragment = mProgramFragment;
			tabPressed(R.id.program_frag);
			changeFragment();
			break;
			
		case R.id.logout_frag:
			
			new AlertDialog
				.Builder(ActivityDetailerMainTabs.this)
				.setTitle(getString(R.string.app_name))
				.setMessage("Are you sure, you want to Log out?")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener()
			{
				
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					dialog.dismiss();
			
					Editor editor = DetailerApplication.getmAppPrefEditor();
					editor.putString(DetailerConstants.COOKIE_HANDLER, "[]");
					editor.putString(DetailerConstants.TEAM_ID_KEY, "");
					editor.putString(DetailerConstants.DOCTORS_JSON, "");
					editor.commit();
					
					startActivity(new Intent (ActivityDetailerMainTabs.this, ActivityLogIn.class));
					finish();
				}
			})
			.setNegativeButton("No", 
					new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			}).create().show();
			break;
			
		case R.id.main_detailer_header_sync_btn:
		case R.id.main_detailer_header_sync_txt:
			if (mCurrentFragment instanceof HomeFragment)
			{
				if (mServerBeans == null)
				{
					new AsynctaskGetContent(this, DetailerConstants.HOME_FRAGMENT_ID).execute();
				}
				else
				{
					updateDataAfterSync(mServerBeans);
				}
			}
			else
			{
				if (mServerVideoContent == null)
				{
					new AsynctaskGetContent(this, DetailerConstants.VIDEO_FRAGMENT_ID).execute();
				}
				else
				{
					updateDataAfterSync(mServerVideoContent);
				}
			}
			break;
		}
	}

	private void tabPressed(int id) 
	{
		mSyncBottomLayout.setVisibility(View.GONE);
		
		switch (id)
		{
		
		case R.id.home_frag:
			if (mPreviousId != R.id.home_frag)
			{
				mFragHomeImageView.setImageResource(R.drawable.home_frag_book_selected/*home_frag_book_selected*/);
				mFragHomeTextView.setTextColor(getResources().getColor(R.color.selected_tab_color));
				
				mFragMOFTextView.setTextColor(getResources().getColor(android.R.color.white));
				mFragProgramTextView.setTextColor(getResources().getColor(android.R.color.white));
				
				mFragMOFImageView.setImageResource(R.drawable.video/*home_frag_book_selected*/);
				mFragProgramImageView.setImageResource(R.drawable.program/*home_frag_book_selected*/);
				
				mTitleImageView.setImageResource(R.drawable.dashboard_header);
				mTitleTextView.setText("DASHBOARD");
			}
			mPreviousId = R.id.home_frag;
			break;
			
		case R.id.video_frag:
			if (mPreviousId != R.id.video_frag)
			{
				mFragMOFImageView.setImageResource(R.drawable.video_selected/*home_frag_book_selected*/);
				mFragMOFTextView.setTextColor(getResources().getColor(R.color.selected_tab_color));
				
				mFragHomeTextView.setTextColor(getResources().getColor(android.R.color.white));
				mFragProgramTextView.setTextColor(getResources().getColor(android.R.color.white));
				
				mFragHomeImageView.setImageResource(R.drawable.dashboard_header/*home_frag_book_selected*/);
				mFragProgramImageView.setImageResource(R.drawable.program/*home_frag_book_selected*/);
				
				mTitleImageView.setImageResource(R.drawable.video);
				mTitleTextView.setText("MOA/Video");
			}
			mPreviousId = R.id.video_frag;
			break;
			
		case R.id.program_frag:
			if (mPreviousId != R.id.program_frag)
			{
				mFragProgramImageView.setImageResource(R.drawable.program_selected/*home_frag_book_selected*/);
				mFragProgramTextView.setTextColor(getResources().getColor(R.color.selected_tab_color));
				
				mFragMOFTextView.setTextColor(getResources().getColor(android.R.color.white));
				mFragHomeTextView.setTextColor(getResources().getColor(android.R.color.white));
				
				mFragHomeImageView.setImageResource(R.drawable.dashboard_header/*home_frag_book_selected*/);
				mFragMOFImageView.setImageResource(R.drawable.video/*home_frag_book_selected*/);
				
				mTitleImageView.setImageResource(R.drawable.program);
				mTitleTextView.setText("PROGRAM");
			}
			mPreviousId = R.id.program_frag;
			break;
		}
	}

	public void refresh() 
	{
		if (mCurrentFragment instanceof HomeFragment)
		{
			((HomeFragment) mCurrentFragment).refresh();
		}
		else
		{
			((MOFFragment) mCurrentFragment).refresh();
		}
	}

	public ArrayList<PresentationBean> getmVideoBeans() {
		return mVideoBeans;
	}

	public void setmVideoBeans(ArrayList<PresentationBean> mVideoBeans) {
		this.mVideoBeans = mVideoBeans;
	}
}