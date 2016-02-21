package com.e.detailer.activity;


import com.e.detailer.DetailerConstants;
import com.e.detailer.PresentationBean;
import com.e.detailer.adaper.AdapterViewPagerImagesFromSd;
import com.e.detailer.asynctask.SavePresAsyncTask;
import com.e.detailer.asynctask.ThreadForFindingDivsInCreate;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CreateNewPresentActivity extends Activity implements OnClickListener {

	public static final int IMAGES_ARE_NOT_AVAILABLE_FOR_PREVIEW_VIEW_PAGER = -1;
	public static PresentationBean mSelectPresentation;
    private ArrayList<String> tempArrayList = new ArrayList<String>();

    private ViewPager viewPager;
	private LinearLayout mLowerBarLayout;
	private TextView noImgsPreviewTextView;
	private ImageView savePresentationButton;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_create_new_present);
        
        InitUI();

    }
    
    @Override
    protected void onStop(){
    	super.onStop();
    	mSelectPresentation = null;
    }
    private void InitUI() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        noImgsPreviewTextView = (TextView) findViewById(R.id.textView1);
        mLowerBarLayout = (LinearLayout) findViewById(R.id.selected_img_layout);
        savePresentationButton = (ImageView) findViewById(R.id.save_btn);
        savePresentationButton.setOnClickListener(this);
        setMainAdapter(0);
        
        if (viewPager.getAdapter() != null)
        	viewPager.setCurrentItem(1, false);
    }

    public void setMainAdapter(int pos) {
    	
	    	if (viewPager.getAdapter() != null){
	    		
	    		if (pos != IMAGES_ARE_NOT_AVAILABLE_FOR_PREVIEW_VIEW_PAGER){
	    			viewPager.setAdapter(viewPager.getAdapter());
		    		if (pos != 0)
		    			viewPager.setCurrentItem(pos-1);
		    		
		    		noImgsPreviewTextView.setVisibility(View.INVISIBLE);
	    		}else{
	    			noImgsPreviewTextView.setVisibility(View.VISIBLE);
	    		}
	    		
	    	}else{
	    		List<File> listForAdapterFiles = new LinkedList<File>(
		            				Arrays.asList(
		    	                    new File(mSelectPresentation.getPathOnSdCard()+"/"+ DetailerConstants.FOLDER_INNER_THUMB)
		    	                    .listFiles()));
	    		if (listForAdapterFiles.size() != 0){
	    			AdapterViewPagerImagesFromSd createPagerAdapter = new AdapterViewPagerImagesFromSd(this,listForAdapterFiles, null, DetailerConstants.CREATE_FRAGMENT_ID);
	    			viewPager.setAdapter(createPagerAdapter);
	            }else{
	            	noImgsPreviewTextView.setVisibility(View.VISIBLE);
	            }
	        }
        
    }
    
    public void updateDataInLowerBar(String dataToBeAdded){
    		if (dataToBeAdded != null){
	    		tempArrayList.add(dataToBeAdded);
	    	}
    		mLowerBarLayout.removeAllViews();
    		updateLowerView();
    }
    private Picasso mPicasso = Picasso.with(this);
    private void updateLowerView() {
    	for (int i = 0; i < tempArrayList.size(); i++){
    		
    		LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	    View v = layoutInflater.inflate(R.layout.create_pres_lower_bar_adap, null);
    	    
    	    ImageView imgViewTempData = (ImageView) v.findViewById(R.id.image_view_lower_adapter);
//    	    ImageUrlLoaderUtils imgLoaderUtils = new ImageUrlLoaderUtils(this);
    	    mPicasso.load(new File(tempArrayList.get(i))).into(imgViewTempData);
//    	    imgLoaderUtils.setImageFromSdCard(imgViewTempData, tempArrayList.get(i), null, false);
    	    v.setTag(i);
    	    v.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int pos = (Integer) v.getTag();
					String dataString = tempArrayList.get(pos);
					((AdapterViewPagerImagesFromSd) viewPager.getAdapter()).addDataToAdapter(dataString);
					tempArrayList.remove(pos);
					updateDataInLowerBar(null);
				}
			});
    	    mLowerBarLayout.addView(v);
    	}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.save_btn){
			savePreTask();
			
		}
	}
	private Activity getActivity() {
		return this;
	}
	private void savePreTask() {
		
		if (DetailerConstants.isLogEnabled)
			System.out.println("CreateNewPresentActivity.savePreTask()"+tempArrayList.size());
		
		final ThreadForFindingDivsInCreate threadForFindingDivsInCreate = new ThreadForFindingDivsInCreate(tempArrayList);
		threadForFindingDivsInCreate.start();
		final Dialog dialog = new Dialog(getActivity());
		dialog.setTitle(getResources().getString(R.string.app_name));
		dialog.setContentView(R.layout.dialog_create_new_present);
		final EditText editText = (EditText) dialog.findViewById(R.id.pres_name_edit);
		final ImageView imageView = (ImageView) dialog.findViewById(R.id.imageView1);
		final Button positiveButton= (Button) dialog.findViewById(R.id.positive_btn)
				, negativeButton = (Button) dialog.findViewById(R.id.negative_btn);
		
		Picasso.with(getActivity())
		.load(new File(mSelectPresentation.getImageUrl()))
		.into(imageView);

		positiveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				String name = editText.getText().toString();
				new SavePresAsyncTask(getActivity(), name, mSelectPresentation, threadForFindingDivsInCreate).execute();
			}
		});
		
		negativeButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}
}
