package com.e.detailer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.Toast;

import com.e.detailer.activity.R;

public class DetailerUtils{

	public static String convertPathToUriString(String path) {
		if (!path.contains("file://"))
			return "file://"+path;
		else
			return path;
	}
	//Android versions
	public static boolean isVersionGreaterThanKitkat() {
		
		int currentVersion = android.os.Build.VERSION.SDK_INT;
		if (currentVersion >= android.os.Build.VERSION_CODES.KITKAT)
		{
			return true;
		}else{
			return false;
		}
	}
	
	public static String getCurrentDate(String format)
	{
		return new SimpleDateFormat(format).format(Calendar.getInstance().getTime());
	}

	public static String getMacAddress(Context context) 
	{
		WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = manager.getConnectionInfo();
		return info.getMacAddress();
	}
	
	//For reading and writing files
	public static String htmlReader(String Path) {
		File html = new File(Path);

		if (html.exists()){
			String htmlCode = "";
			try {
				BufferedReader in;
				in = new BufferedReader(new InputStreamReader(new FileInputStream(html.getAbsolutePath())));
				String input = null; 
		        while ((input=in.readLine()) != null) {
		        	htmlCode = htmlCode+"\n" + input;
				}
		        in.close(); 
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return htmlCode;
		}else {
			return null;
		}
	}
	public static void saveAndWriteFile(File file , String toWrite){
		try {
			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(toWrite);
			bw.close();
			if (DetailerConstants.isLogEnabled){
				System.out.println(toWrite);
				System.out.println("Done");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// method for rounding corner of bitmap.........
	/**
	 * Takes bitmap and make its corner to rounded.
	 * @param bitmap to make corner rounded.
	 * @return Bitmap of rounded corners.
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap)
	{
		Bitmap output = null;
		try
		{
			output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);

			final int color = 0xff424242;
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			final RectF rectF = new RectF(rect);
			// final float roundPx = 50;

			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			paint.setStrokeWidth(3);
			canvas.drawRoundRect(rectF, 50, 50, paint);

			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return output;
	}
	/**
	 * Take snapshot of a view.
	 * @exception returns <u>Black</u> bitmap if the view is secured by FLAG_SECURED. 
	 * @param view view of which image has been taken.
	 * @return Bitmap of the image.
	 */
	
	public static Bitmap getBitmapScreenshot(View view) {
		view.measure(MeasureSpec.makeMeasureSpec(view.getWidth(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(view.getHeight(), MeasureSpec.EXACTLY));
		view.layout((int)view.getX(), (int)view.getY(), (int)view.getX() + view.getMeasuredWidth(), (int)view.getY() + view.getMeasuredHeight());

		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache(true);
		Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
		view.setDrawingCacheEnabled(false);

		return bitmap;
	}
	/**
	 * Save bitmap on sd-card into <u>JPEG</u> format.
	 * 
	 * @param img bitmap image.
	 * @return Path of the image on sd card.
	 */
	public static String saveImageToSD(Bitmap img)
	{
		//Bitmap bit = img;
		String root = DetailerConstants.SNAPSHOT_SAVE_FILE_PATH;
		File myDir = new File(root);
		
		if (!myDir.exists())
			myDir.mkdirs();

		String fname = System.currentTimeMillis() + ".jpg";
		File file = new File(myDir, fname);
		if (file.exists())
			file.delete();
		try
		{
			FileOutputStream out = new FileOutputStream(file);
			img.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return file.getPath();
	}
	/**
	 * Get the path of an image and convert it into byte[] array.
	 * @param path of the image to convert into byte[] array.
	 * @return byte[] array of image if exist else return null.
	 */
	@SuppressWarnings("resource")
	public static byte[] getBytesFromImagePath(String path) {
		
		//Open img 
		FileInputStream inputStream;
		try {
			inputStream = new FileInputStream(path);
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] bytes = new byte[1024];
		
		for (int readNum; (readNum = inputStream.read(bytes)) != -1;){
			
			outputStream.write(bytes, 0, readNum);
		
			if (DetailerConstants.isLogEnabled)
				System.out.println(readNum);
		}
			return outputStream.toByteArray();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Request for make intent for sending mails.
	 * 
	 * @param mActivity context from which activity it is called.
	 * @param path path of attachment if exist else pass null.
	 */
	
	public static void requestForMailIntent(Activity mActivity,String path) {
		 String subject = mActivity.getString(R.string.app_name);
		 String message = "PDF slide.";
		

		  Intent email = new Intent(Intent.ACTION_SEND);
//		  email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
		
		  email.putExtra(Intent.EXTRA_SUBJECT, subject);
		  email.putExtra(Intent.EXTRA_TEXT, message);
		  File root = new File(path);
		  File file = root;//asd
		  if (!file.exists() || !file.canRead()) {
		      Toast.makeText(mActivity, "Attachment Error", Toast.LENGTH_SHORT).show();
		      return;
		  }

		  Uri U = Uri.fromFile(file);
		  email.setType("application/octet-stream");
		  email.putExtra(Intent.EXTRA_STREAM, U);
		  
		  
		  mActivity.startActivity(Intent.createChooser(email, "Choose an Email client :"));
		  
	}

//	public static AlertDialog.Builder createDialogWithTheme(Activity activity, String msg) {
//		
//		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//		
//		builder.setMessage(msg);
//		builder.setTitle(R.string.app_name);
//		return builder;
//	}

	public static void requestToPickFileFromSd(Fragment fragment) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("file/*");
		fragment.startActivityForResult(intent, DetailerConstants.INSTALL_SLIDE_INTENT_CODE);
	}
	
	//Image utility
	public static String getPath(Uri uri, Activity activity)
	{
		// just some safety built in
		if (uri == null)
		{
			return null;
		}
		// try to retrieve the image from the media store first
		// this will only work for images selected from gallery
		String[] projection = { MediaStore.Images.Media.DATA };
		@SuppressWarnings("deprecation")
		Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
		if (cursor != null)
		{
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}
		// this is our fallback here
		return uri.getPath();
	}
	
	public static void showMsgDialog(Activity activityLogIn,
			String ti, String msg, OnClickListener positiveButton) 
	{
		try
		{
			if (positiveButton == null)
			{
				positiveButton = new DialogInterface.OnClickListener() 
				{
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				};
			}
			
			new AlertDialog.Builder(activityLogIn).setTitle(ti).setMessage(msg).setIcon(R.drawable.ic_launcher).setCancelable(false)
					.setPositiveButton(activityLogIn.getString(android.R.string.ok), positiveButton).create().show();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public static String getDeviceMacAddress(Activity activity)
	{
		WifiManager manager = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = manager.getConnectionInfo();
		String address = info.getMacAddress();
		return address;
	}
}