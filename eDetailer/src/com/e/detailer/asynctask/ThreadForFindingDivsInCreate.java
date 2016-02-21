package com.e.detailer.asynctask;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.util.Log;

import com.e.detailer.DetailerConstants;
import com.e.detailer.DetailerUtils;
import com.e.detailer.PresentationBean;
import com.e.detailer.activity.CreateNewPresentActivity;

public class ThreadForFindingDivsInCreate extends Thread {
	
	private ArrayList<String> list;
	private final PresentationBean mBean = CreateNewPresentActivity.mSelectPresentation;
	private String mSelectedDivs = "";
	private String replacedString = "";
	
	public ThreadForFindingDivsInCreate(ArrayList<String> tempArrayList) {
		this.list = tempArrayList;
	}
	
	@Override
	public void run() {
		
		String sampleString = DetailerUtils.htmlReader(mBean.getmSampleFilePath());
		String indexString = DetailerUtils.htmlReader(mBean.getmIndexFilePath());
		
		int numberOfDivsChanged = 0;
		
		for(numberOfDivsChanged = 0 ; numberOfDivsChanged < list.size(); numberOfDivsChanged++){
			findingImage(indexString, list.get(numberOfDivsChanged));
		}
		
		replacedString  = sampleString.replace(DetailerConstants.REPLACEMENT_FOR_DIV_TEXT, mSelectedDivs)
				.replace(DetailerConstants.SLIDES_TEXT_REPLACEMENT, (numberOfDivsChanged+1) +"px");
		
//		File newPresDir = new File(DetailerConstants.MAIN_DIR_PATH +
//				""
//				+DetailerConstants.CREATED_PRES_CONCAT_TEXT+"/");
		
//		if (!newPresDir.exists()){
//			newPresDir.mkdirs();
//		}
//		fileCreaterAndWriter(new File(newPresDir.getAbsolutePath()+"/"
//				+DetailerConstants.HTML_INDEX_FILE_NAME), replacedString);
	
	}
	
	public String getResult() {
		return replacedString;
	}

	private void findingImage(String html, String url) {

			
		Document doc;
		doc = Jsoup.parse(html);
		
		String numericString = removeZeroFromStart(extractDigits(url));
		Log.e("Numeric", numericString);
		
		String div = doc.getElementById("Page"+numericString/*+extractDigits(url)*/).outerHtml();
		mSelectedDivs = mSelectedDivs +"\n"+div;
		
	}
	
	private String removeZeroFromStart(String extractDigits) {
		if (extractDigits.charAt(0) == '0'){
			extractDigits = extractDigits.substring(1);
			return extractDigits;
		}else{
			return extractDigits;
		}
	}

	public String extractDigits(String src) {
	    StringBuilder builder = new StringBuilder();
	    for (int i = 0; i < src.length(); i++) {
	        char c = src.charAt(i);
	        if (Character.isDigit(c)) {
	            builder.append(c);
	        }
	    }
	    return builder.toString();
	}
}
