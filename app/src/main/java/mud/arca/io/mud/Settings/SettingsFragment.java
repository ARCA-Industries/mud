package mud.arca.io.mud.Settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import mud.arca.io.mud.DataStructures.MockUser;
import mud.arca.io.mud.DataStructures.User;
import mud.arca.io.mud.Database.DatabaseExporter;
import mud.arca.io.mud.LoginScreenActivity;
import mud.arca.io.mud.Notifications.AlarmReceiver;
import mud.arca.io.mud.Notifications.MyAlarmManager;
import mud.arca.io.mud.Notifications.TimePreference;
import mud.arca.io.mud.Notifications.TimePreferenceDialogFragmentCompat;
import mud.arca.io.mud.R;
import mud.arca.io.mud.Util.ThemeUtil;
import mud.arca.io.mud.Util.Util;

public class SettingsFragment extends PreferenceFragmentCompat {
    SharedPreferences sharedPrefs;
    MyAlarmManager myAlarmManager;

    // Set to true to show the Debug PreferenceCategory.
    public boolean showDebugPrefs = true;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        setupToolbar(view);
        return view;
    }

    private void setupToolbar(View rootView) {
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_profile));
    }


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);

        initPreferences();

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPrefs.registerOnSharedPreferenceChangeListener(spChanged);

        myAlarmManager = new MyAlarmManager(getContext());

        if (showDebugPrefs) {
            createDebugPrefs();
        }
    }

    public void createDebugPrefs() {
        PreferenceScreen screen = this.getPreferenceScreen();
        PreferenceCategory debugPrefsCategory = new PreferenceCategory(getContext());
        debugPrefsCategory.setTitle("Debug");
        screen.addPreference(debugPrefsCategory);

        Preference addSampleData = new Preference(getContext());
        addSampleData.setTitle("Add sample data");
        addSampleData.setSummary("Don't press this a lot, it uses up Firestore quota");
        addSampleData.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                MockUser.addSampleData(User.getCurrentUser());
                Util.debug("Added sample data");
                return true;
            }
        });
        debugPrefsCategory.addPreference(addSampleData);

        Preference fillSampleData = new Preference(getContext());
        fillSampleData.setTitle("Replace data with sample");
        fillSampleData.setSummary("Replaces data in every existing day with sample data.\nDon't press this a lot, it uses up Firestore quota");
        fillSampleData.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                MockUser.fillSampleData();
                return true;
            }
        });
        debugPrefsCategory.addPreference(fillSampleData);

        Preference outputSharedPrefs = new Preference(getContext());
        outputSharedPrefs.setTitle("Output shared preferences");
        outputSharedPrefs.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Util.debug("##### sharedPrefs: " + sharedPrefs.getAll());
                return true;
            }
        });
        debugPrefsCategory.addPreference(outputSharedPrefs);

        Preference clearSharedPrefs = new Preference(getContext());
        clearSharedPrefs.setTitle("Clear shared preferences");
        clearSharedPrefs.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                sharedPrefs.edit().clear().commit();
                return true;
            }
        });
        debugPrefsCategory.addPreference(clearSharedPrefs);

        // This is a quick ugly fix to make clearSharedPrefs show up.
        Preference testpref = new Preference(getContext());
        testpref.setTitle("test");
        debugPrefsCategory.addPreference(testpref);
    }

    public void removeNotifTime() {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.remove("notification_time");
        editor.apply();
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

        findPreference("notifications_test").setOnPreferenceClickListener(preference -> {
            Intent broadcastIntent = new Intent(getContext(), AlarmReceiver.class);
            getContext().sendBroadcast(broadcastIntent);
            return true;
        });

        findPreference("export_data").setOnPreferenceClickListener(preference -> {
            try {
                startActivity(DatabaseExporter.getShareIntent(getContext()));
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error: Could not create report.", Toast.LENGTH_LONG).show();
            }
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