package mud.arca.io.mud;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.widget.PopupMenu;
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

    private FrameLayout mTextMessage;

    final private Fragment fragmentHome = new AnalysisFragment();
    final private Fragment fragmentDashboard = new DayListFragment();
    final private Fragment fragmentNotifications = new SettingsFragment();

    private Fragment currentFragment = fragmentDashboard;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_analysis:
                    setTitle(R.string.title_analysis);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_container, fragmentHome)
                            .commit();
                    currentFragment = fragmentHome;
                    return true;
                case R.id.navigation_dashboard:
                    setTitle(R.string.title_dashboard);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_container, fragmentDashboard)
                            .commit();
                    currentFragment = fragmentDashboard;
                    return true;
                case R.id.navigation_notifications:
                    setTitle(R.string.title_profile);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_container, fragmentNotifications)
                            .commit();
                    currentFragment = fragmentNotifications;
                    return true;
            }
//
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//
//            }

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

        navigation.setSelectedItemId(R.id.navigation_dashboard);

        DatabaseHelper.ensureDefaultVariables();

        ThemeUtil.loadAndSetThemeFromPreferences(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.three_dots_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.threeDots:
                View anchor = findViewById(R.id.threeDots);
                ((FragmentWithMenu) currentFragment).onThreeDotsClicked(anchor);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
