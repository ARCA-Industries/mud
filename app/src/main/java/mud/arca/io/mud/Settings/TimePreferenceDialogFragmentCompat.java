package mud.arca.io.mud.Settings;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TimePicker;

import androidx.preference.DialogPreference;
import androidx.preference.PreferenceDialogFragmentCompat;
import mud.arca.io.mud.R;
import mud.arca.io.mud.Util.Util;

public class TimePreferenceDialogFragmentCompat extends PreferenceDialogFragmentCompat {

    private TimePicker mTimePicker;

    public static TimePreferenceDialogFragmentCompat newInstance(String key) {
        final TimePreferenceDialogFragmentCompat fragment = new TimePreferenceDialogFragmentCompat();
        final Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        fragment.setArguments(b);

        return fragment;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        mTimePicker = view.findViewById(R.id.time_picker);

        if (mTimePicker == null) {
            throw new IllegalStateException("Dialog view must contain a TimePicker with id 'time_picker'");
        }

        DialogPreference preference = getPreference();
        if (preference instanceof TimePreference) {
            TimePreference timePref = (TimePreference) preference;
            String time = timePref.getTime();

            // Set the time to the TimePicker
            if (time != null) {
                mTimePicker.setIs24HourView(DateFormat.is24HourFormat(getContext()));
                mTimePicker.setHour(TimePreference.getHour(time));
                mTimePicker.setMinute(TimePreference.getMinute(time));
            }
        }
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            // Get the current values from the TimePicker
            int hour = mTimePicker.getHour();
            int minute = mTimePicker.getMinute();

            // Generate value to save
            String time = hour + ":" + minute;

            DialogPreference preference = getPreference();
            if (preference instanceof TimePreference) {
                TimePreference timePreference = ((TimePreference) preference);
                if (timePreference.callChangeListener(time)) {
                    timePreference.setTime(time);
                }
            }
        }
    }
}