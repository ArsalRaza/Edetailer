package com.e.detailer.activity;


import android.app.Activity;
import android.os.Bundle;

import com.e.detailer.DetailerConstants;
//import com.epapyrus.plugpdf.SimpleDocumentReader;
//import com.epapyrus.plugpdf.SimpleDocumentReaderListener;
//import com.epapyrus.plugpdf.SimpleReaderFactory;
//import com.epapyrus.plugpdf.core.PropertyManager;
//import com.epapyrus.plugpdf.core.annotation.AnnotEventListener;
//import com.epapyrus.plugpdf.core.annotation.BaseAnnot;
//import com.epapyrus.plugpdf.core.annotation.acroform.BaseField;
//import com.epapyrus.plugpdf.core.annotation.acroform.ButtonField;
//import com.epapyrus.plugpdf.core.annotation.acroform.CheckBoxField;
//import com.epapyrus.plugpdf.core.annotation.acroform.CustomCheckBoxPainter;
//import com.epapyrus.plugpdf.core.annotation.acroform.FieldEventListener;
//import com.epapyrus.plugpdf.core.annotation.acroform.BaseField.FieldState;
//import com.epapyrus.plugpdf.core.viewer.DocumentState;
//import com.epapyrus.plugpdf.core.viewer.PageViewListener;
//import com.epapyrus.plugpdf.core.viewer.ReaderView;
//import com.epapyrus.plugpdf.core.viewer.DocumentState.OPEN;
//import com.google.code.microlog4android.Level;
//import com.logdog.LogDog;

public class PdfActivity extends Activity{

//	SimpleDocumentReader mViewer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pdf);

//		DetailerUtils.createDialogWithTheme(this,"This application is only for PO purpose...");
//		PropertyManager.setScrollFrictionCoef(1);
//		PropertyManager.setScrollVelocityCoef(1);
//		PropertyManager.setPreviewQualityCoef(1.5);
//		
//		CheckBoxField.setGlobalCustomPainter(new CustomCheckBoxPainter() {
//			
//			@Override
//			public void draw(CheckBoxField arg0, Canvas arg1) {
//				
//			}
//		});

		if (!getIntent().hasExtra(DetailerConstants.PDF_URL_KEY)){
	    	finish();
	    }else{
//			open(getIntent().getStringExtra(DetailerConstants.PDF_URL_KEY));
	    }
		printHeapState();
	}

	
//	protected void open(String path) {
//		
//		final AnnotEventListener mAnnotEventListener = new AnnotEventListener() {
//
//			@Override
//			public boolean onTapUp(BaseAnnot annot) {
//				return false;
//			}
//
//			@Override
//			public boolean onLongPress(BaseAnnot annot) {
//				return false;
//			}
//		};
//		
//		SimpleDocumentReaderListener mViewerListener = new SimpleDocumentReaderListener() {
//
//			@Override
//			public void onLoadFinish(DocumentState.OPEN state) {
//				LogDog.Print(Level.INFO, "state::"+state);
//				if (state == OPEN.SUCCESS) {
//					mViewer.setAnnotEventLisener(mAnnotEventListener);
//					
//					testSetFieldState();
//					testSetFieldValue();
//				}	
//				
//			}
//
//			private void testSetFieldValue() {
//				ReaderView readerView = mViewer.getReaderView();
//				readerView.setFieldValue(0, "TextField1[0]", "TEST3");
//
//				LogDog.Print(
//						Level.DEBUG,
//						"test1 fieldValue: "
//								+ readerView.getFieldValue(0, "TextField1[0]"));
//			}
//
//			private void testSetFieldState() {
//				ReaderView readerView = mViewer.getReaderView();
//				readerView.setFieldState(0, "test1", FieldState.READONLY);
//				readerView.setFieldState(0, "test2", FieldState.DISABLE);
//
//				LogDog.Print(Level.DEBUG,
//						"test1 fieldState: " + readerView.getFieldState(0, "test1"));
//				LogDog.Print(Level.DEBUG,
//						"test2 fieldState: " + readerView.getFieldState(0, "test2"));
//
//				readerView.setFieldState(0, "test1", FieldState.ENABLE);
//				LogDog.Print(Level.DEBUG,
//						"test1 fieldState: " + readerView.getFieldState(0, "test1"));
//			}
//		};
//
//
//
//		PageViewListener mPageViewListener = new PageViewListener() {
//
//			@Override
//			public void cachePageBitmap(int pageIdx, Bitmap bitmap) {
//			}
//
//			@Override
//			public void onAnnotationList(int pageIdx, List<BaseAnnot> annotList) {
//
//				LogDog.Print(Level.DEBUG, "onAnnotationList pageIdx : " + pageIdx);
//		
//				for (BaseAnnot annot : annotList) {
//					StringBuilder builder = new StringBuilder();
//					builder.append("Annot Info - ");
//					builder.append("pageIdx: " + annot.getPageIdx());
//					builder.append(", type: " + annot.getType());
//
//					LogDog.Print(Level.DEBUG, builder.toString());
//				}
//			
//			}
//
//			@Override
//			public void onFieldList(int pageIdx, List<BaseField> fieldList) {
//				LogDog.Print(Level.DEBUG, "PageIdx: " + pageIdx);
//
//				for (BaseField field : fieldList) {
//					LogDog.Print(Level.DEBUG, "Acroform type: " + field.getType());
//
//					if (field.getType().equals("TEXT_FIELD")) {
//						LogDog.Print(Level.DEBUG, "TEXT_FIELD uid: " + field.getUID());
//						field.setListener(new FieldEventListener() {
//
//							@Override
//							public boolean onTapUp(BaseField field) {
//								LogDog.Print(Level.DEBUG, "Acroform onTapUp type: " + field.getType());
//								return false;
//							}
//
//							@Override
//							public boolean onFocusChange(BaseField field,
//									boolean hasFocus) {
//								return false;
//							}
//
//							@Override
//							public boolean onChangedValue(BaseField field) {
//								return false;
//							}
//						});
//
//					} else if (field.getType().equals("CHECK_BOX")) {
//						LogDog.Print(Level.DEBUG, "Acroform CHECK_BOX uid: " + field.getUID());
//						field.setListener(new FieldEventListener() {
//
//							@Override
//							public boolean onTapUp(BaseField field) {
//								LogDog.Print(Level.DEBUG, "Acroform onTapUp type: " + field.getType());
//								return false;
//							}
//
//							@Override
//							public boolean onFocusChange(BaseField field, boolean hasFocus) {
//								return false;
//							}
//							
//							@Override
//							public boolean onChangedValue(BaseField field) {
//								return false;
//							}
//						});
//
//					} else if (field.getType().equals("BUTTON")) {
//						ButtonField btnField = (ButtonField) field;
//						btnField.setAlphaBitmap(BitmapFactory.decodeResource(
//								getResources(), R.drawable.btn_brightness_on), true);
//						LogDog.Print(Level.DEBUG, "Acroform BUTTON uid: " + field.getUID());
//						field.setListener(new FieldEventListener() {
//
//							@Override
//							public boolean onTapUp(BaseField field) {
//								LogDog.Print(Level.DEBUG, "Acroform onTapUp type: " + field.getType());
//								return false;
//							}
//
//							@Override
//							public boolean onFocusChange(BaseField field,
//									boolean hasFocus) {
//								return false;
//							}
//							
//							@Override
//							public boolean onChangedValue(BaseField field) {
//								return false;
//							}
//						});
//
//					} else if (field.getType().equals("RADIO_BUTTON")) {
//						LogDog.Print(Level.DEBUG, "Acroform RADIO_BUTTON uid: " + field.getUID() + " title: " + field.getTitle());
//						field.setListener(new FieldEventListener() {
//
//							@Override
//							public boolean onTapUp(BaseField field) {
//								LogDog.Print(Level.DEBUG, "Acroform onTapUp type: " + field.getType());
//								return false;
//							}
//
//							@Override
//							public boolean onFocusChange(BaseField field, boolean hasFocus) {
//								return false;
//							}
//
//							@Override
//							public boolean onChangedValue(BaseField field) {
//								return false;
//							}
//						});
//
//					}
//				}
//			}
//
//			@Override
//			public void onPageLoadFinish() {
//				
//			}
//			
//			@Override
//			public void onAnnotationEdited(List<BaseAnnot> annotList) {
//			}
//		};
//
//		mViewer = SimpleReaderFactory.createSimpleViewer(this, mViewerListener);
//		mViewer.openFile(path, "");
//		mViewer.setPageViewListener(mPageViewListener);
//		mViewer.setTitle("Sample");
//	}

	private void printHeapState() {
		if (Runtime.getRuntime() == null) return ;
		long maxMemory = Runtime.getRuntime().maxMemory();
		long totalMemory = Runtime.getRuntime().totalMemory();
		long freeMemory = Runtime.getRuntime().freeMemory();
		long allocMemory = totalMemory - freeMemory;

//		LogDog.Print(Level.DEBUG, "Heap - max: " + maxMemory + ", total: "
//				+ totalMemory + ", free: " + freeMemory + ", alloc: "
//				+ allocMemory);
	}
//	@Override
//	protected void onDestroy() {
//		mViewer.save(); // if you opened this Activity by openData(...), it will save root directory of externel storage on your device. 
//		mViewer.clear();
//		super.onDestroy();
//	}
}
