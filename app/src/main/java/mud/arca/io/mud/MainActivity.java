package mud.arca.io.mud;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import java.util.Calendar;

import mud.arca.io.mud.Analysis.AnalysisFragment;
import mud.arca.io.mud.DataStructures.MockUser;
import mud.arca.io.mud.DayList.DayListFragment;
import mud.arca.io.mud.Notifications.AlarmReceiver;
import mud.arca.io.mud.Settings.SettingsFragment;
import mud.arca.io.mud.Database.DatabaseHelper;
import mud.arca.io.mud.Util.FragmentWithMenu;
import mud.arca.io.mud.Util.ThemeUtil;
import mud.arca.io.mud.Util.Util;

public class MainActivity extends AppCompatActivity {

    private enum MudFragment {
        ANALYSIS(new AnalysisFragment(), "Analysis"),
        DASHBOARD(new DayListFragment(), "Dashboard"),
        SETTINGS(new SettingsFragment(), "Settings"),
        ;

        Fragment fragment;
        String title;

        MudFragment(Fragment fragment, String title) {
            this.fragment = fragment;
            this.title = title;
        }
    }

    private FrameLayout mTextMessage;

//    final private Fragment fragmentHome = new AnalysisFragment();
//    final private Fragment fragmentDashboard = new DayListFragment();
//    final private Fragment fragmentNotifications = new SettingsFragment();

    private Fragment currentFragment;

    private void switchToFragment(MudFragment mudFragment) {
        setTitle(mudFragment.title);
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
            }

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM); // TODO: Load from preferences

        mTextMessage = findViewById(R.id.main_container);
//        getSupportFragmentManager().beginTransaction().add(R.id.main_container, fragmentHome).commit();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_dashboard);

        DatabaseHelper.ensureDefaultVariables();

        ThemeUtil.loadAndSetThemeFromPreferences(getApplicationContext());

        // Keep getting error here
        Toolbar toolbar = findViewById(R.id.mudToolbar);
        setSupportActionBar(toolbar);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.three_dots_menu, menu);
//
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.threeDots:
//                View anchor = findViewById(R.id.threeDots);
//                ((FragmentWithMenu) currentFragment).onThreeDotsClicked(anchor);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

}
