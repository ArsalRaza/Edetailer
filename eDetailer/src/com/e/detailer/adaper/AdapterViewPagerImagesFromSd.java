package com.e.detailer.adaper;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.e.detailer.DetailerConstants;
import com.e.detailer.activity.CreateNewPresentActivity;
import com.e.detailer.activity.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class AdapterViewPagerImagesFromSd extends PagerAdapter {

    private List<File> mFileList;
    private Activity mActivity ;
    private LayoutInflater layoutInflater;
    private Picasso mPicasso;
	private int mFrom;
	private List<String> mStringList;

    public AdapterViewPagerImagesFromSd(Activity act, List<File> files, List<String> bucketList, int From){
        mActivity = act;
        mFileList = files;
        mStringList = bucketList;
        mPicasso = Picasso.with(act);
        mFrom = From;
    }

	@Override
    public int getCount() {
		if (mFileList != null)
			return mFileList.size();
		else {
			return mStringList.size();
		}
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }
    
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.create_main_adap, null);
        
        ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
        final ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        
        if (mFileList != null)
        	mPicasso.load(new File(mFileList.get(position).getAbsolutePath()))
        	.into(imageView, new Callback(){

				@Override
				public void onError() {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					progressBar.setVisibility(View.GONE);
				}
        		
        	});
//        	imageLoader.setImageFromSdCard(imageView, mFileList.get(position).getAbsolutePath(), null, false);
        else
        	mPicasso.load(new File(mStringList.get(position)))   
        	.into(imageView, new Callback(){

				@Override
				public void onError() {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					progressBar.setVisibility(View.GONE);
				}
        		
        	});
//        	imageLoader.setImageFromSdCard(imageView, mStringList.get(position), null, false);
        
        v.setTag(position);
        if (mFrom == DetailerConstants.CREATE_FRAGMENT_ID){
	        v.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	
	            	int pos = (Integer) v.getTag();
	            	((CreateNewPresentActivity) mActivity).updateDataInLowerBar(mFileList.get(pos).getAbsolutePath());
	            	mFileList.remove(pos);
	          
	            	if (mFileList.size() != 0 )
	            		((CreateNewPresentActivity) mActivity).setMainAdapter(pos);
	            	else 
	            		((CreateNewPresentActivity) mActivity).setMainAdapter(CreateNewPresentActivity.IMAGES_ARE_NOT_AVAILABLE_FOR_PREVIEW_VIEW_PAGER);
	            }
	        });
        }else
        	if(mFrom == DetailerConstants.BUCKET_FRAGMENT_ID){
        		v.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//Bucket
						
					}
				});
        }
        ((ViewPager)container).addView(v);
        return v;
    
    }
    public void addDataToAdapter(String dataToBeAdded) {
    	File file = new File(dataToBeAdded);
		mFileList.add(file);
		((CreateNewPresentActivity) mActivity).setMainAdapter(mFileList.indexOf(file));
	}
    @Override
    public void destroyItem(View collection, int position, Object view) {
        ((ViewPager) collection).removeView((RelativeLayout) view);
    }
    /**
     * Called when the a change in the shown pages has been completed. At this
     * point you must ensure that all of the pages have actually been added or
     * removed from the container as appropriate.
     *
     * @param arg0
     *            The containing View which is displaying this adapter's page
     *            views.
     */
    @Override
    public void finishUpdate(View arg0)
    {
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1)
    {
    }

    @Override
    public Parcelable saveState()
    {
        return null;
    }

    @Override
    public void startUpdate(View arg0)
    {
    }


}
//    private final List<File> mFileList;
//    private final Activity mActivity ;
//    private final LayoutInflater layoutInflater;
//    private DisplayImageOptions options;
//    private ImageLoader imageLoader;
//    private ImageLoaderConfiguration config;
//
//    public CreateMainAdapter(Activity act, List<File> files){
//        mActivity = act;
//        mFileList = files;
//        layoutInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        initializeImageLoader();
//    }
//    private void initializeImageLoader()
//    {
//        options = new DisplayImageOptions.Builder().resetViewBeforeLoading(true).cacheInMemory(true).cacheOnDisk(true)
//                .bitmapConfig(Bitmap.Config.RGB_565).delayBeforeLoading(200).build();
//        imageLoader = ImageLoader.getInstance();
//
//        config = new ImageLoaderConfiguration.Builder(mActivity).threadPoolSize(1).threadPriority(1).denyCacheImageMultipleSizesInMemory()
//                .memoryCacheSize(1048576 * 30)
//
//                .diskCacheSize(1048576 * 50)
//                .build();
//
//        imageLoader.init(config);
//    }
//    @Override
//    public int getCount() {
//        return mFileList.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return mFileList.get(position).getAbsolutePath();
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View v = convertView;
//        if (v == null){
//            v = layoutInflater.inflate(R.layout.create_main_image_single, null);
//        }
//        ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
//        ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
//        setContentImage(imageView, progressBar, mFileList.get(position).getAbsolutePath());
//        return v;
//    }
//    private void setContentImage(final ImageView mProductImage, final ProgressBar mProgressBar, String path)
//    {
//        imageLoader.displayImage(DetailerUtils.convertPathToUriString(path), mProductImage, options, new ImageLoadingListener()
//        {
//
//            @Override
//            public void onLoadingCancelled(String arg0, View arg1)
//            {
//
//            }
//
//            @Override
//            public void onLoadingComplete(String arg0, View arg1, Bitmap arg2)
//            {
//                mProductImage.setImageBitmap(arg2);
//                mProgressBar.setVisibility(View.GONE);
//            }
//
//
//
//            @Override
//            public void onLoadingStarted(String arg0, View arg1)
//            {
//            }
//
//            @Override
//            public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
//
//            }
//        });
//    }
