package com.e.detailer.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.e.detailer.DetailerUtils;
import com.e.detailer.asynctask.AsynctaskLogin;

public class ActivityLogIn extends Activity /*implements OnClickListener*/ implements OnClickListener 
{
	private EditText mEmailEditText;
	private EditText mPasswordEditText;
	private Button mLoginButton;
	private String mEmailString;
	private String mPasswordString;

	protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		 setContentView(R.layout.activity_log_in);
		 Init();
	 }

	private void Init()
	{
		mEmailEditText = (EditText) findViewById(R.id.activity_login_email_address);
		mPasswordEditText = (EditText) findViewById(R.id.activity_login_password);
		mLoginButton = (Button) findViewById(R.id.activity_login_button);
		mPasswordEditText.setOnKeyListener(new OnKeyListener() 
		{
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) 
			{
				switch (keyCode)
	            {
	                case KeyEvent.KEYCODE_DPAD_CENTER:
	                case KeyEvent.KEYCODE_ENTER:
	                	if (validateInfo())
	            			new AsynctaskLogin(ActivityLogIn.this, mEmailString, mPasswordString).execute();
	                return true;
	                default:
	                    break;
	            }
				return false;
			}
		});
		mLoginButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		if (validateInfo())
			new AsynctaskLogin(this, mEmailString, mPasswordString).execute();
	}

	private boolean validateInfo() 
	{
		mEmailString = mEmailEditText.getText().toString();
		mPasswordString = mPasswordEditText.getText().toString();
		
		if (mEmailString.isEmpty())
		{
			DetailerUtils.showMsgDialog(this, "", "Please enter password", null);
			return false;
		}
		
		if (mPasswordString.isEmpty())
		{
			DetailerUtils.showMsgDialog(this, "", "Please enter Email address", null);
			return false;
		}
		
		return true;
	}
}
