package mud.arca.io.mud.Settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import androidx.fragment.app.DialogFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import mud.arca.io.mud.DataStructures.MockUser;
import mud.arca.io.mud.DataStructures.User;
import mud.arca.io.mud.LoginScreenActivity;
import mud.arca.io.mud.Notifications.MyAlarmManager;
import mud.arca.io.mud.Notifications.TimePreference;
import mud.arca.io.mud.Notifications.TimePreferenceDialogFragmentCompat;
import mud.arca.io.mud.R;
import mud.arca.io.mud.Util.ThemeUtil;
import mud.arca.io.mud.Util.Util;

public class SettingsFragment extends PreferenceFragmentCompat {
    SharedPreferences sharedPrefs;
    MyAlarmManager myAlarmManager;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);

        initPreferences();

        Preference notifTime = findPreference("notification_time");
        notifTime.setDependency("notifications_enabled");

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPrefs.registerOnSharedPreferenceChangeListener(spChanged);

        myAlarmManager = new MyAlarmManager(getContext());

        // Set up the debug preference onclicks
        Preference addSampleData = findPreference("add_sample_data");
        addSampleData.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                MockUser.addSampleData(User.getCurrentUser());
                Util.debug("Added sample data");
                return true;
            }
        });

        Preference outputSharedPrefs = findPreference("output_shared_preferences");
        outputSharedPrefs.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Util.debug("##### sharedPrefs: " + sharedPrefs.getAll());
                return true;
            }
        });

        Preference clearSharedPrefs = findPreference("clear_shared_preferences");
        clearSharedPrefs.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                removeSharedPrefs();
                return true;
            }
        });
    }

    public void removeNotifTime() {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.remove("notification_time");
        editor.apply();
    }

    public void removeSharedPrefs() {
        sharedPrefs.edit().clear().commit();
    }

    private void initPreferences() {
        // Init auth logout preference
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            findPreference("auth_logout").setSummary(
                    getString(R.string.preference_auth_logout_summary,
                            FirebaseAuth.getInstance().getCurrentUser().getUid())
            );
        } else {
            findPreference("auth_logout").setSummary(getString(R.string.preference_auth_logout_summary_error));
        }
        findPreference("auth_logout").setOnPreferenceClickListener(preference -> {
            AuthUI.getInstance().signOut(getContext()).addOnCompleteListener(runnable -> {
                startActivity(new Intent(getContext(), LoginScreenActivity.class));
                getActivity().finish();
            });
            return true;
        });

        findPreference("theme").setOnPreferenceChangeListener((preference, newValue) -> {
            ThemeUtil.setTheme(newValue.toString());
            return true;
        });
    }


    // We need to override onDisplayPreferenceDialog to implement TimePreference.
    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        DialogFragment dialogFragment = null;
        if (preference instanceof TimePreference) {
            dialogFragment = TimePreferenceDialogFragmentCompat.newInstance(preference.getKey());
        }

        if (dialogFragment != null) {
            dialogFragment.setTargetFragment(this, 0);
            dialogFragment.show(this.getParentFragmentManager(),
                    "androidx.preference.PreferenceFragment.DIALOG");
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }

    public void updateNotificationTime() {
        String timeString = sharedPrefs.getString("notification_time", TimePreference.DEFAULT_TIME_STRING);
        myAlarmManager.setRepeatingNotification(TimePreference.getHour(timeString), TimePreference.getMinute(timeString));
    }

    OnSharedPreferenceChangeListener spChanged = new OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            //Util.debug("Preference changed, key="+key);
            if (key.equals("notifications_enabled")) {
                boolean enabled = sharedPrefs.getBoolean("notifications_enabled", true);
                if (enabled) {
                    updateNotificationTime();
                } else {
                    myAlarmManager.cancelRepeatingNotification();
                }
            } else if (key.equals("notification_time")) {
                updateNotificationTime();
            }
        }
    };
}