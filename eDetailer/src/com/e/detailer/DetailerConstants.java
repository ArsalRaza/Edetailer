package com.e.detailer;

import android.os.Environment;

public class DetailerConstants
{
	//Basic Utils
	public static final boolean isLogEnabled = true;
	
	//Screen IDs
	public static final int HOME_FRAGMENT_ID = 0;
	public static final int CREATE_FRAGMENT_ID = 1;
	public static final int VIDEO_FRAGMENT_ID = 2;
	public static final int BUCKET_FRAGMENT_ID = 3;
	public static final int DATA_SHEET_FRAGMENT_ID = 4;

	//Paths of folders
	public static final String EXTERNAL_DIR_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
	public static final String MOA_FOLDER = "ALL_MOA/";
	public static final String MAIN_DIR_PATH = EXTERNAL_DIR_PATH + "/" + "E-DETAILER" + "/";
	public static final String ZIP_FOLDER = "ALL_ZIP_FOLDER" + "/";
	public static final String HTML_FOLDER = "ALL_HTML_FOLDER"+ "/";
//	public static final String VIDEO_FOLDER = "ALL_VIDEO_FOLDER"+ "/";
	public static final String PDF_FOLDER = "ALL_PDF_FOLDER" + "/";
	public static final String SNAPSHOT_SAVE_FILE_PATH = MAIN_DIR_PATH+ "ALL_SNAPSHOT_IMGS"+"/";
	
	//File names
	public static final String HTML_THUMBNAIL_DIR = "thumbnail";
	public static final String HTML_MAIN_IMG = "index.png";
	public static final String FOLDER_INNER_THUMB = "innerThumb";
	public static final String HTML_INDEX_FILE_NAME = "index";
	public static final String HTML_SAMPLE_FILE_NAME = "sample.html";
	
	//Text inside files
	public static final String REPLACEMENT_FOR_DIV_TEXT = "Aftabkhan";
	public static final CharSequence SLIDES_TEXT_REPLACEMENT = "no_of_slides";
	public static final String CREATED_PRES_CONCAT_TEXT = "_created";
	
	//Keys
	public static final String WEB_VIEW_URL_KEY = "webviewurlkeyfrommainscreen";
	public static final String PDF_URL_KEY = "pdffileurl";
	public static final int INSTALL_SLIDE_INTENT_CODE = 1;

	//Make website responsive
	public static final String CONTAIN_WITH = "maximum-scale=1.0";
	public static final String REPLACE_WITH = "maximum-scale=1.0\" /> \n <meta name=viewport content=target-densitydpi=medium-dpi, width=device-width, height=device-height/>";
	public static final String HTML_SCALABLE_NO_CODE = "user-scalable=no";
	public static final String HTML_SCALABLE_YES_CODE = "user-scalable=yes";

	public static final int MOA_FRAGMENT_ID = 123;

	public static final String BASE_URL = "http://cmsmd.ipadedetailer.com/Account/";

	public static final String LOGIN_URL = "Login";

	public static final String COOKIE_HANDLER = "COOKIE_HANDLER";

	public static final String GET_CONTENT_URL = "getcontent";

	public static final String VIDEO_VIEW_URL_KEY = "video_url";

	public static final String TEAM_ID_KEY = "TEAM_ID_KEY";

	public static final String GET_VIDEO_URL = "getvideos";

	public static final String ANALYTICS = "ANALYTICS";

	public static final String GET_ALL_DOCTORS = "doctor";

	public static final String DOCTORS_JSON = "DOCTORS_JSON";
}