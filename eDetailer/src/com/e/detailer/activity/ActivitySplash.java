package com.e.detailer.activity;

import java.util.Timer;
import java.util.TimerTask;

import com.e.detailer.DetailerConstants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;

public class ActivitySplash extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
	    Timer timer = new Timer();
	    scheduleTimer(timer);
	    String calls = "{Calls:[]}";
	    DetailerApplication.getmAppPrefEditor().putString(DetailerConstants.ANALYTICS, calls).commit();
	}

	private void scheduleTimer(Timer timer)
	{
		timer.schedule( new TimerTask()
		{
			@Override
			public void run()
			{
				if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(DetailerConstants.TEAM_ID_KEY, "").equalsIgnoreCase(""))
					startActivity(new Intent(ActivitySplash.this, ActivityLogIn.class));
				else
					startActivity(new Intent(ActivitySplash.this, ActivityDetailerMainTabs.class));
					
				finish();
			}
		}, 3000);	
	}
}