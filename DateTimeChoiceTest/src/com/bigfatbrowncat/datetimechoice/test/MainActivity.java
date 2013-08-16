package com.bigfatbrowncat.datetimechoice.test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.bigfatbrowncat.datetimechoice.DateTimeChoice;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	private Button demoButton;
	private TextView demoTextView;
	private DateTimeChoice dateTimeChoice;
	
	private static final long MILLISECONDS_BASE = 75600000L;	// 21 hour between 1 Jan 1970 3:00 and 2 Jan 1970 0:00
	private static final long MAX_MEETING_DURATION = 8L * 60 * 60 * 1000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		dateTimeChoice = (DateTimeChoice)this.findViewById(R.id.dateTimeChoice);
		/*dateTimeChoice.setMinValue(new GregorianCalendar(1985, Calendar.APRIL, 13, 8, 40));
		dateTimeChoice.setMaxValue(new GregorianCalendar(2013, Calendar.AUGUST, 15, 2, 44));*/
		
		Calendar base = new GregorianCalendar();
		base.setTimeInMillis(MILLISECONDS_BASE);
		
		Calendar max = new GregorianCalendar();
		max.setTimeInMillis(MILLISECONDS_BASE + MAX_MEETING_DURATION);
		
		dateTimeChoice.setMinValue(base);
		dateTimeChoice.setMaxValue(max);
		dateTimeChoice.setValue(base);
		
		demoButton = (Button)findViewById(R.id.demo_button);
		demoTextView = (TextView)findViewById(R.id.demo_textView);
		
		demoButton.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
		if (arg0 == demoButton) {
			long msec = dateTimeChoice.getValue().getTimeInMillis();
			long allMinutes = (dateTimeChoice.getValue().getTimeInMillis() - MILLISECONDS_BASE) / 1000 / 60;
			long hours = allMinutes / 60;
			long minutes = allMinutes % 60;
			
			demoTextView.setText(msec + "; " + hours + ":" + minutes);
		}
		
	}

}
