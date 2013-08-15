package com.bigfatbrowncat.datetimechoice;

import java.util.Calendar;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class DateTimeChoice extends LinearLayout implements NumberSpinEdit.OnValueChangeListener {

	private static class SavedState extends BaseSavedState {
		private static final String FIELD_YEAR = "year";
		private static final String FIELD_MONTH = "month";
		private static final String FIELD_DAY_OF_MONTH = "day";
		private static final String FIELD_HOUR_OF_DAY = "hour";
		private static final String FIELD_MINUTE = "minute";
		
		private Calendar value;

		public Calendar getValue() {
			return value;
		}

		public void setValue(Calendar value) {
			this.value = value;
		}

		SavedState(Parcelable superState) {
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			Bundle calendarBundle = in.readBundle();
			
			Calendar cal = Calendar.getInstance();
			cal.clear();
			cal.set(Calendar.YEAR, calendarBundle.getInt(FIELD_YEAR));
			cal.set(Calendar.MONTH, calendarBundle.getInt(FIELD_MONTH));
			cal.set(Calendar.DAY_OF_MONTH, calendarBundle.getInt(FIELD_DAY_OF_MONTH));
			cal.set(Calendar.HOUR_OF_DAY, calendarBundle.getInt(FIELD_HOUR_OF_DAY));
			cal.set(Calendar.MINUTE, calendarBundle.getInt(FIELD_MINUTE));
			value = cal;
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			Bundle cal = new Bundle();
			
			cal.putInt(FIELD_YEAR, value.get(Calendar.YEAR));
			cal.putInt(FIELD_MONTH, value.get(Calendar.MONTH));
			cal.putInt(FIELD_DAY_OF_MONTH, value.get(Calendar.DAY_OF_MONTH));
			cal.putInt(FIELD_HOUR_OF_DAY, value.get(Calendar.HOUR_OF_DAY));
			cal.putInt(FIELD_MINUTE, value.get(Calendar.MINUTE));
			
			out.writeBundle(cal);
		}

		// required field that makes Parcelables from a Parcel
		@SuppressWarnings("unused")
		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}
	
	private NumberSpinEdit day_numberSpinEdit;
	private NumberSpinEdit month_numberSpinEdit;
	private NumberSpinEdit year_numberSpinEdit;

	private NumberSpinEdit hour_numberSpinEdit;
	private NumberSpinEdit minute_numberSpinEdit;
	
	private Calendar minValue;
	private Calendar maxValue;
	
	private void initLayout(Context context) {
		// if (!isInEditMode()) {
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.view_date_time_choice, this, true);

		day_numberSpinEdit = (NumberSpinEdit)findViewById(R.id.day_numberSpinEdit);
		month_numberSpinEdit = (NumberSpinEdit)findViewById(R.id.month_numberSpinEdit);
		year_numberSpinEdit = (NumberSpinEdit)findViewById(R.id.year_numberSpinEdit);

		hour_numberSpinEdit = (NumberSpinEdit)findViewById(R.id.hour_numberSpinEdit);
		minute_numberSpinEdit = (NumberSpinEdit)findViewById(R.id.minute_numberSpinEdit);
		
		month_numberSpinEdit.setValueConverter(new NumberSpinEdit.MonthValueConverter(getContext()));
		
		if (!isInEditMode()) {
			day_numberSpinEdit.setOnValueChangeListener(this);
			month_numberSpinEdit.setOnValueChangeListener(this);
			year_numberSpinEdit.setOnValueChangeListener(this);
			
			hour_numberSpinEdit.setOnValueChangeListener(this);
			minute_numberSpinEdit.setOnValueChangeListener(this);
		}
	}

	public DateTimeChoice(Context context) {
		super(context);
		initLayout(context);
	}

	public DateTimeChoice(Context context, AttributeSet attrs) {
		super(context, attrs);
		initLayout(context);
	}

	public Calendar getValue() {
		Calendar value = Calendar.getInstance();

		value.set(Calendar.YEAR, year_numberSpinEdit.getValue());
		value.set(Calendar.MONTH, month_numberSpinEdit.getValue() - 1);
		value.set(Calendar.DAY_OF_MONTH, day_numberSpinEdit.getValue());
		value.set(Calendar.HOUR_OF_DAY, hour_numberSpinEdit.getValue());
		value.set(Calendar.MINUTE, minute_numberSpinEdit.getValue());
		
		return value;
	}
	
	public void setValue(Calendar value) {
		year_numberSpinEdit.setValue(value.get(Calendar.YEAR));
		month_numberSpinEdit.setValue(value.get(Calendar.MONTH) + 1);
		day_numberSpinEdit.setValue(value.get(Calendar.DAY_OF_MONTH));
		hour_numberSpinEdit.setValue(value.get(Calendar.HOUR_OF_DAY));
		minute_numberSpinEdit.setValue(value.get(Calendar.MINUTE));
		
		checkTimeBounds();
	}
	
	@Override
	public void onRaising(NumberSpinEdit view) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLowering(NumberSpinEdit view) {
	
	}
	
	@Override
	public void onChanged(NumberSpinEdit view) {
		checkTimeBounds();
	}

	@Override
	public boolean onChanging(NumberSpinEdit view, int newValue) {
		return true;
	}
	
	@Override
	protected Parcelable onSaveInstanceState() {
	    Parcelable superState = super.onSaveInstanceState();
	    SavedState ss = new SavedState(superState);

	    ss.setValue(getValue());

	    return ss;
	}
	
	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (!(state instanceof SavedState)) {
			super.onRestoreInstanceState(state);
			return;
		}

		SavedState ss = (SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());

		setValue(ss.getValue());
	}

	public Calendar getMinValue() {
		return minValue;
	}

	/**
	 * 
	 * @param month the month (January = 1, February = 2, ...)
	 * @param year
	 * @return
	 */
	private int getHighestDayInMonth(int month, int year) {
		Calendar curCal = Calendar.getInstance();
		curCal.clear();
		curCal.set(Calendar.DAY_OF_MONTH, 1);
		curCal.set(Calendar.MONTH, month - 1);
		curCal.set(Calendar.YEAR, year);
		
		return curCal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	private void checkTimeBounds() {
		Calendar curvalue = getValue();
		
		int lowerYear = 1970;
		int lowerMonth = 1;
		int lowerDay = 1;
		int lowerHour = 0;
		int lowerMinute = 0;
		
		int higherYear = 2100;
		int higherMonth = 12;
		int higherDay = getHighestDayInMonth(month_numberSpinEdit.getValue(), year_numberSpinEdit.getValue());
		int higherHour = 23;
		int higherMinute = 59;
		
		if (minValue != null) {
			lowerYear = minValue.get(Calendar.YEAR);
			if (curvalue.get(Calendar.YEAR) <= lowerYear) {
				lowerMonth = minValue.get(Calendar.MONTH) + 1;
				
				if (curvalue.get(Calendar.MONTH) + 1 <= lowerMonth) {
					lowerDay = minValue.get(Calendar.DAY_OF_MONTH);
					
					if (curvalue.get(Calendar.DAY_OF_MONTH) <= lowerDay) {
						lowerHour = minValue.get(Calendar.HOUR_OF_DAY);
						
						if (curvalue.get(Calendar.HOUR_OF_DAY) <= lowerHour) {
							lowerMinute = minValue.get(Calendar.MINUTE);
						}
					}
				}
			}
		}
		
		if (maxValue != null) {
			higherYear = maxValue.get(Calendar.YEAR);
			if (curvalue.get(Calendar.YEAR) >= higherYear) {
				higherMonth = maxValue.get(Calendar.MONTH) + 1;
				
				if (curvalue.get(Calendar.MONTH) + 1 >= higherMonth) {
					higherDay = maxValue.get(Calendar.DAY_OF_MONTH);
					
					if (curvalue.get(Calendar.DAY_OF_MONTH) >= higherDay) {
						higherHour = maxValue.get(Calendar.HOUR_OF_DAY);
						
						if (curvalue.get(Calendar.HOUR_OF_DAY) >= higherHour) {
							higherMinute = maxValue.get(Calendar.MINUTE);
						}
					}
				}
			}		
		}
		
		year_numberSpinEdit.setMinValue(lowerYear);
		month_numberSpinEdit.setMinValue(lowerMonth);
		day_numberSpinEdit.setMinValue(lowerDay);
		hour_numberSpinEdit.setMinValue(lowerHour);
		minute_numberSpinEdit.setMinValue(lowerMinute);

		year_numberSpinEdit.setMaxValue(higherYear);
		month_numberSpinEdit.setMaxValue(higherMonth);
		day_numberSpinEdit.setMaxValue(higherDay);
		hour_numberSpinEdit.setMaxValue(higherHour);
		minute_numberSpinEdit.setMaxValue(higherMinute);

	}
	
	public void setMinValue(Calendar minValue) {
		this.minValue = minValue;
		checkTimeBounds();
	};

	public Calendar getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Calendar maxValue) {
		this.maxValue = maxValue;
		checkTimeBounds();
	}
	
	
}
