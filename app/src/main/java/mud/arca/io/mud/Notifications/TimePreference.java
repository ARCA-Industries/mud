package mud.arca.io.mud.Notifications;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.preference.DialogPreference;
import androidx.preference.PreferenceManager;
import mud.arca.io.mud.R;
import mud.arca.io.mud.Util.Util;

import static mud.arca.io.mud.Util.Util.formatTime;

public class TimePreference extends DialogPreference {
    public static String DEFAULT_TIME_STRING = "12:0";

    /**
     * The time is stored in H:M format, where 0 <= H <= 23.
     * There are no leading zeros, so 4:07 PM is represented as "16:7".
     */
    private String time = DEFAULT_TIME_STRING;

    public TimePreference(Context context) {
        // Delegate to other constructor
        this(context, null);
    }

    public TimePreference(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.preferenceStyle);
    }

    public TimePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        // Delegate to other constructor
        this(context, attrs, defStyleAttr, defStyleAttr);
    }

    public TimePreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setPositiveButtonText("Set time");
        setNegativeButtonText("Cancel");
        updateSummaryText();
    }

    public String getTime() {
        return time;
    }

    public void updateSummaryText() {
        String timeStr = Util.formatTime(getHour(), getMinute());
        setSummary(timeStr);
    }

//    public int hour;
//    public int minute;
//
//    public void setTime(int hour, int minute) {
//        this.hour = hour;
//        this.minute = minute;
//        String time = hour + ":" + minute;
//        setTime(time);
//    }

    public void setTime(String time) {
        this.time = time;
        // save to SharedPreference
        persistString(time);

        updateSummaryText();
//        Util.debug("Set time to: " + time);
//        AlarmReceiver.setRepeatingNotification(getContext(), getHour(), getMinute());
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    public int getDialogLayoutResource() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean useSpinner = sharedPrefs.getBoolean("use_spinner_timepicker", false);
        if (useSpinner) {
            return R.layout.pref_dialog_time;
        } else {
            return R.layout.pref_dialog_time_clock;
        }
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        setTime(restorePersistedValue ?
                getPersistedString(time) : (String) defaultValue);
    }

    public int getHour() {
        return getHour(time);
    }

    public int getMinute() {
        return getMinute(time);
    }

    public static int getHour(String time) {
        try {
            String[] pieces = time.split(":");
            return Integer.parseInt(pieces[0]);
        } catch (NumberFormatException e) {
            return 12;
        }
    }

    public static int getMinute(String time) {
        try {
            String[] pieces = time.split(":");
            return Integer.parseInt(pieces[1]);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}