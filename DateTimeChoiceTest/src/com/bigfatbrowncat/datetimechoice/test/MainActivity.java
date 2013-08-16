package com.bigfatbrowncat.datetimechoice.test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.bigfatbrowncat.datetimechoice.DateTimeChoice;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		DateTimeChoice dateTimeChoice = (DateTimeChoice)this.findViewById(R.id.dateTimeChoice);
		dateTimeChoice.setMinValue(new GregorianCalendar(1985, Calendar.APRIL, 13, 8, 40));
		dateTimeChoice.setMaxValue(new GregorianCalendar(2013, Calendar.AUGUST, 15, 2, 44));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
