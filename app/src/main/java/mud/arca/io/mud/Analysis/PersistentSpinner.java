package mud.arca.io.mud.Analysis;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.preference.PreferenceManager;
import mud.arca.io.mud.Util.Util;

/**
 * A PersistentSpinner is used to manage a spinner that saves the item selected to shared preferences.
 */
public class PersistentSpinner {
    /**
     * The key used to store the spinner position in shared preferences.
     */
    public String key;

    /**
     * A reference to the shared preferences
     */
    public SharedPreferences sharedPrefs;

    /**
     * A reference to the spinner that this PersistentSpinner manages.
     */
    public AppCompatSpinner spinner;

    /**
     * Initialize a PersistentSpinner with the given context, spinner, and key.
     * @param context
     * @param spinner
     * @param key
     */
    public PersistentSpinner(Context context, AppCompatSpinner spinner, String key) {
        this.key = key;
        this.spinner = spinner;
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        setSelectionToSharedPref();
    }

    /**
     * Save the position pos to shared preferences.
     * @param pos
     */
    public void savePosition(int pos) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(key, pos);
        editor.commit();
    }

    /**
     * Initialize the selection of the spinner to the position stored in sharedPrefs
     */
    public void setSelectionToSharedPref() {
        int selection = sharedPrefs.getInt(key, 0);
        int count = spinner.getAdapter().getCount();
        // The selection starts counting from 0, so it must be between 0 and (count-1).
        if (selection <= count-1) {
            spinner.setSelection(selection, false);
        }
    }
}
