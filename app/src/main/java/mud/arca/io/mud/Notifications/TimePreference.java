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

public class TimePreference extends DialogPreference {
    /**
     * The time is stored in H:M format, where 0 <= H <= 23.
     * There are no leading zeros, so 4:07 PM is represented as "16:7".
     */
    private String time = "12:0";

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
        setPositiveButtonText("Set value");
        setNegativeButtonText("Cancel");
        updateSummaryText();
    }

    public String getTime() {
        return time;
    }

    public void updateSummaryText() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, getHour(getTime()));
        cal.set(Calendar.MINUTE, getMinute(getTime()));
        Date date = cal.getTime();

        final SimpleDateFormat sdf = new SimpleDateFormat("h:mm aa");
        setSummary(sdf.format(date));
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
        Util.debug("Set time to: " + time);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    public int getDialogLayoutResource() {
        return R.layout.pref_dialog_time;
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        setTime(restorePersistedValue ?
                getPersistedString(time) : (String) defaultValue);
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