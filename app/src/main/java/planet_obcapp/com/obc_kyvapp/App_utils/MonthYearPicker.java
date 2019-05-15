
package planet_obcapp.com.obc_kyvapp.App_utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.NumberPicker;
import java.util.Calendar;
import planet_obcapp.com.obc_kyvapp.R;

@SuppressLint("InflateParams")
public class MonthYearPicker {

	private static final int MIN_YEAR = 1960;

	private static final int MAX_YEAR = 2089;

	private static final String[] PICKER_DISPLAY_MONTHS_NAMES = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct",
			"Nov", "Dec" };

	private static final String[] MONTHS = new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September",
			"October", "November", "December" };

	private View view;
	private Activity activity;
	private AlertDialog pickerDialog;
	private boolean build = false;
	private NumberPicker monthNumberPicker;
	private NumberPicker yearNumberPicker;


	public MonthYearPicker(Activity activity) {
		this.activity = activity;
		this.view = activity.getLayoutInflater().inflate(R.layout.month_year_picker_view, null);
	}


	public void build(DialogInterface.OnClickListener positiveButtonListener, DialogInterface.OnClickListener negativeButtonListener) {
		this.build(-1, -1, positiveButtonListener, negativeButtonListener);
	}


	private void build(int selectedMonth, int selectedYear, DialogInterface.OnClickListener positiveButtonListener,
			DialogInterface.OnClickListener negativeButtonListener) {

		final Calendar instance = Calendar.getInstance();
		int currentMonth = instance.get(Calendar.MONTH);
		int currentYear = instance.get(Calendar.YEAR);

		if (selectedMonth > 11 || selectedMonth < -1) {
			selectedMonth = currentMonth;
		}

		if (selectedYear < MIN_YEAR || selectedYear > MAX_YEAR) {
			selectedYear = currentYear;
		}

		if (selectedMonth == -1) {
			selectedMonth = currentMonth;
		}

		if (selectedYear == -1) {
			selectedYear = currentYear;
		}

		AlertDialog.Builder builder= new AlertDialog.Builder(activity);
		builder.setView(view);

		monthNumberPicker = (NumberPicker) view.findViewById(R.id.monthNumberPicker);
		monthNumberPicker.setDisplayedValues(PICKER_DISPLAY_MONTHS_NAMES);

		monthNumberPicker.setMinValue(0);
		monthNumberPicker.setMaxValue(MONTHS.length - 1);

		yearNumberPicker = (NumberPicker) view.findViewById(R.id.yearNumberPicker);
		yearNumberPicker.setMinValue(MIN_YEAR);
		yearNumberPicker.setMaxValue(currentYear);

		monthNumberPicker.setValue(selectedMonth);
		yearNumberPicker.setValue(currentYear);

		monthNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		yearNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

		builder.setTitle(activity.getString(R.string.alert_dialog_title));
		builder.setPositiveButton(activity.getString(R.string.positive_button_text), positiveButtonListener);
		builder.setNegativeButton(activity.getString(R.string.negative_button_text), negativeButtonListener);
		build = true;
		pickerDialog = builder.create();

	}
	public void show() {
		if (build) {
			pickerDialog.show();
		} else {
			throw new IllegalStateException("Build picker before use");
		}
	}


	public String getSelectedMonthName() {
		return MONTHS[monthNumberPicker.getValue()];
	}



	public int getSelectedYear() {
		return yearNumberPicker.getValue();
	}



}
