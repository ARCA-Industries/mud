package mud.arca.io.mud.Analysis;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.preference.PreferenceManager;
import mud.arca.io.mud.Util.Util;

public class PersistentSpinner {
    /**
     * The key used to store the spinner position in shared preferences.
     */
    public String key;

    public SharedPreferences sharedPrefs;
    public AppCompatSpinner spinner;

    /**
     * Save the position pos to shared preferences.
     * @param pos
     */
    public void savePosition(int pos) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(key, pos);
        editor.commit();
    }

    public PersistentSpinner(Context context, AppCompatSpinner spinner, String key) {
        this.key = key;
        this.spinner = spinner;
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        setSelectionToSharedPref();
    }

    // Initialize the selection of the spinner to the position stored in sharedPrefs
    public void setSelectionToSharedPref() {
        // Selection starts at 0. So it is at most (count-1).
        int selection = sharedPrefs.getInt(key, 0);
        int count = spinner.getAdapter().getCount();
        //Util.debug("selection: " + selection + ", count: " + count);
        if (selection <= count-1) {
            spinner.setSelection(selection, false);
        }
    }
}
