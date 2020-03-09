package mud.arca.io.mud.Settings;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.preference.DialogPreference;
import mud.arca.io.mud.R;

public class TimePreference extends DialogPreference {

    private String time;

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

//        setPositiveButtonText(R.string.set_value);
//        setNegativeButtonText(R.string.cancel);
        setPositiveButtonText("Set value");
        setNegativeButtonText("Cancel");
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;

        // save to SharedPreference
        persistString(time);
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
//        String[] pieces = time.split(":");
//        return Integer.parseInt(pieces[0]);
        return 4;
    }

    public static int getMinute(String time) {
//        String[] pieces = time.split(":");
//        return Integer.parseInt(pieces[1]);
        return 44;
    }
}