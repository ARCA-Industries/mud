package mud.arca.io.mud.Settings;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import mud.arca.io.mud.DataStructures.MockUser;
import mud.arca.io.mud.DataStructures.User;
import mud.arca.io.mud.LoginScreenActivity;
import mud.arca.io.mud.MainActivity;
import mud.arca.io.mud.R;
import mud.arca.io.mud.Util.Util;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);

        initPreferences();

        Preference addSampleData = findPreference("add_sample_data");
        addSampleData.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                MockUser.addSampleData(User.getCurrentUser());

                Util.debug("Added sample data");

                return true;
            }
        });
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
    }
}