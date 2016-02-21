package com.e.detailer.activity;


//import com.epapyrus.plugpdf.core.PlugPDF;
//import com.epapyrus.plugpdf.core.PlugPDFException.InvalidLicense;
//import com.epapyrus.plugpdf.core.PlugPDFException.LicenseMismatchAppID;
//import com.epapyrus.plugpdf.core.PlugPDFException.LicenseTrialTimeOut;
//import com.epapyrus.plugpdf.core.PlugPDFException.LicenseUnusableOS;
//import com.epapyrus.plugpdf.core.PlugPDFException.LicenseWrongProductVersion;

import com.squareup.picasso.Picasso;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class DetailerApplication extends Application 
{
	private static SharedPreferences mPreferences;
	private static Picasso mCacheManager;
	
	
	public static SharedPreferences getmAppPreferences() {
		return mPreferences;
	}

	public static void setmAppPreferences(SharedPreferences mPreferences) {
		DetailerApplication.mPreferences = mPreferences;
	}

	public static Editor getmAppPrefEditor() {
		return mPreferences.edit();
	}
	@Override
	public void onCreate(){
		super.onCreate();
		setmAppPreferences(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
		setmCacheManager(Picasso.with(getApplicationContext()));
		
		//HTML5 and CSS3 initializing...
//		ChromeInitializer.initialize(this);

		//PDF library initializing...

//		try {
//			PlugPDF.init(getApplicationContext(),
//					"E5F5BE8F5GG4AD6FED3498D6D9G87D7D343GGG4DH887GEDCEF4DFBF5");
//			
//			PlugPDF.deployAssetFontResource(getApplicationContext());
//			PlugPDF.enableUncaughtExceptionHandler();
//			PlugPDF.setUpdateCheckEnabled(true);
//			//PlugPDF.setBitmapConfig(Bitmap.Config.RGB_565);
//		
//		} catch (LicenseWrongProductVersion ex) {
//			Log.d("LicenseEx", "LicenseWrongProductVersion");
//		} catch (LicenseTrialTimeOut ex) {
//			Log.d("LicenseEx", "LicenseTrialTimeOut");
//		} catch (LicenseUnusableOS ex) {
//			Log.d("LicenseEx", "LicenseUnusableOS");
//		} catch (LicenseMismatchAppID ex) {
//			Log.d("LicenseEx", "LicenseMismatchAppID");
//		} catch (InvalidLicense ex) {
//			Log.d("LicenseEx", "Invalid License");
//		} catch (Exception ex) {
//			Log.d("Exception", (ex.getMessage() == null) ? "Unknown Error!" : ex.getMessage());
//		}

	}

	public static Picasso getmCacheManager() {
		return mCacheManager;
	}

	public static void setmCacheManager(Picasso mCacheManager) {
		DetailerApplication.mCacheManager = mCacheManager;
	}
}
