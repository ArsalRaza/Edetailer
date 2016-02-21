package com.e.detailer.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

public class MultiPartSender extends NetworkUtils
{
	public String sendPostStringUsingMultiPart(Activity mContext, String URL, String fieldName, String text) throws Exception
	{
		HttpURLConnection httpURLConnection = getUrlConnection(URL, HTTP_POST, APPLICATION_MULTIPART, mBoundary);
		mBoundary = END_REQUEST + mBoundary;

		httpURLConnection.connect();
		DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(dataOutputStream, UTF8),
                true);
		
		addJsonToPart(writer, text, fieldName);
		writer.append(CHANGE_LINE + mBoundary + END_REQUEST + CHANGE_LINE);
		writer.flush();
		
		return getResponse(httpURLConnection, false);
	}

	public String sendImagesWithJson(String url, String jsonString,ArrayList<String> mImageBeanList)
	{
		try {
			HttpURLConnection httpURLConnection = getUrlConnection(url, HTTP_POST, APPLICATION_MULTIPART, mBoundary);
			mBoundary = END_REQUEST + mBoundary;
			httpURLConnection.connect();

			DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(dataOutputStream, UTF8),true);

			addJsonToPart(writer, jsonString, "formstring");

			byte[] imageByteArray = {};
			for (int i = 0; i < mImageBeanList.size(); i++)
			{
				String path = mImageBeanList.get(i);
				Bitmap bitmap = BitmapFactory.decodeFile(path);
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				bitmap.compress(CompressFormat.JPEG, 100, outputStream);

				imageByteArray = outputStream.toByteArray();
				addFileAsByte(dataOutputStream, "image", imageByteArray, ("imageview" + (i + 1)) + ".jpeg", IMAGE_JPEG);
			}

			writer.append(mBoundary + END_REQUEST + CHANGE_LINE);
			writer.flush();

			return getResponse(httpURLConnection, false);

		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return "";
		}
	}

	public String uploadImage(String url, String imagePath) throws Exception
	{
		HttpURLConnection httpURLConnection = getUrlConnection(url, HTTP_POST, APPLICATION_MULTIPART, mBoundary);
		mBoundary = END_REQUEST + mBoundary;
		
		httpURLConnection.connect();
		DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(dataOutputStream, UTF8),
                true);
		byte[] imageByteArray = {};

		Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 100, outputStream);
		
		imageByteArray = outputStream.toByteArray();
		addFileAsByte(dataOutputStream, "image", imageByteArray, "image" + ".jpeg", IMAGE_JPEG);
		
		writer.append(mBoundary + END_REQUEST + CHANGE_LINE);
		writer.flush();
		
		return getResponse(httpURLConnection, false);
	}
	
//	public String uploadImagesAddPost(Activity mContext, String URL, String jsonString, ArrayList<ImageListBean> mImageBeanList) throws Exception
//	{
//		HttpURLConnection httpURLConnection = getUrlConnection(URL, HTTP_POST, APPLICATION_MULTIPART, mBoundary);
//		mBoundary = END_REQUEST + mBoundary;
//		
//		httpURLConnection.connect();
//		DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
//		PrintWriter writer = new PrintWriter(new OutputStreamWriter(dataOutputStream, UTF8),
//                true);
//		
//		addJsonToPart(writer, jsonString, "formstring");
//		
//		for (int i = 0; i < mImageBeanList.size(); i++)
//		{
//			try
//			{
//				byte[] imageByteArray = {};
//				Uri imageUri = mImageBeanList.get(i).getmUri();
//				String imagePath = ImageCaputureUtility.getPath(imageUri, mContext);
//				if (!imagePath.equals(""))
//				{
//					if (mImageBeanList.get(i).getmType().equalsIgnoreCase(MellTooConstants.IMG))
//					{
//						//For img
//						Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//						ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//						bitmap.compress(CompressFormat.JPEG, 100, outputStream);
//						
//						imageByteArray = outputStream.toByteArray();
//						addFileAsByte(dataOutputStream, "imageview" + (i + 1), imageByteArray, ("imageview" + (i + 1)) + ".jpeg", IMAGE_JPEG);
//					}
//					else
//					{
//						//For video
//						/* Uploading thumb*/
//						Bitmap bitmap = UtilsMellToo.createThumb(imageUri, mContext);
//						ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//						bitmap.compress(CompressFormat.JPEG, 100, outputStream);
//						imageByteArray = outputStream.toByteArray();
//						addFileAsByte(dataOutputStream, "imageview4", imageByteArray, "imageview4" + ".jpeg", IMAGE_JPEG);
//	
//						/* Uploading video*/
//						imageByteArray = MellTooUtil.readFileToByteArray(new File(imagePath));
//						addFileAsByte(dataOutputStream, "video", imageByteArray, "video" + (i + 1) + ".mp4", VIDEO_MP4);
//					}
//				}
//				else
//				{
//					//No need to upload data
//				}
//			}
//			catch (Exception e)
//			{
//				e.printStackTrace();
//			}
//		}
//		writer.append(mBoundary + END_REQUEST + CHANGE_LINE);
//		writer.flush();
//		
//		return getResponse(httpURLConnection);
//	}
	
	private void addJsonToPart(PrintWriter writer, String text, String fieldName)
	{
		writer.append(mBoundary).append(CHANGE_LINE);
		writer.append(CONTENT_DISPOSITION + FORM_DATA + NAME + "\"" + fieldName + "\"").append(CHANGE_LINE).append(CHANGE_LINE);
		writer.append(text).append(CHANGE_LINE);
		writer.flush();
	}

	public void addFileAsByte(DataOutputStream outputStream, String fieldName, byte[] imageByteArray, String fileName, String contentType) throws IOException
	{
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, UTF8),
                true);
		
		writer.append(CHANGE_LINE + mBoundary).append(CHANGE_LINE);
		writer.append(CONTENT_DISPOSITION + FORM_DATA + NAME + "\"" + fieldName + "\";" + FILE_NAME + "\"" + fileName + "\"").append(CHANGE_LINE);
		writer.append(CONTENT_TYPE + contentType).append(CHANGE_LINE).append(CHANGE_LINE);

		writer.flush();
		
		InputStream inputStream = new ByteArrayInputStream(imageByteArray);
		byte[] buffer = new byte[4096];
		int bytesRead = -1;
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, bytesRead);
		}
		outputStream.writeBytes(CHANGE_LINE);

		outputStream.flush();
		inputStream.close();
	}
}