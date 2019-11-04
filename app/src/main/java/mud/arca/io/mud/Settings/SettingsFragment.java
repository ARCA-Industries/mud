package mud.arca.io.mud.Settings;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;
import mud.arca.io.mud.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);
    }
}