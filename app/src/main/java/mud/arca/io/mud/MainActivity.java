package mud.arca.io.mud;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.view.MenuItem;
import android.widget.FrameLayout;

import java.util.Calendar;

import mud.arca.io.mud.Analysis.AnalysisFragment;
import mud.arca.io.mud.DataStructures.MockUser;
import mud.arca.io.mud.DayList.DayListFragment;
import mud.arca.io.mud.Notifications.AlarmReceiver;
import mud.arca.io.mud.Settings.SettingsFragment;
import mud.arca.io.mud.Database.DatabaseHelper;
import mud.arca.io.mud.Util.ThemeUtil;
import mud.arca.io.mud.Util.Util;

public class MainActivity extends AppCompatActivity {

    private enum MudFragment {
        ANALYSIS(new AnalysisFragment(), R.string.title_analysis),
        DASHBOARD(new DayListFragment(), R.string.title_dashboard),
        SETTINGS(new SettingsFragment(), R.string.title_profile),
        HOME(new HomeFragment(), R.string.title_home),
        ;

        Fragment fragment;
        int titleRes;

        MudFragment(Fragment fragment, int titleRes) {
            this.fragment = fragment;
            this.titleRes = titleRes;
        }
    }

    private FrameLayout mTextMessage;


    private Fragment currentFragment;

    private void switchToFragment(MudFragment mudFragment) {
        // Each fragment sets the title now.
        // setTitle(getString(mudFragment.titleRes));
        currentFragment = mudFragment.fragment;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, currentFragment)
                .commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_analysis:
                    switchToFragment(MudFragment.ANALYSIS);
                    return true;
                case R.id.navigation_dashboard:
                    switchToFragment(MudFragment.DASHBOARD);
                    return true;
                case R.id.navigation_notifications:
                    switchToFragment(MudFragment.SETTINGS);
                    return true;
                case R.id.navigation_home:
                    switchToFragment(MudFragment.HOME);
                    return true;
            }

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM); // TODO: Load from preferences

        mTextMessage = findViewById(R.id.main_container);
//        getSupportFragmentManager().beginTransaction().add(R.id.main_container, fragmentHome).commit();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);

        DatabaseHelper.ensureDefaultVariables();

        ThemeUtil.loadAndSetThemeFromPreferences(getApplicationContext());

    }
}
