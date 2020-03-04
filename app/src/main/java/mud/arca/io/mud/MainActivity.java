package mud.arca.io.mud;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.view.MenuItem;
import android.widget.FrameLayout;

import java.util.Calendar;

import mud.arca.io.mud.Analysis.AnalysisFragment;
import mud.arca.io.mud.DayList.DayListFragment;
import mud.arca.io.mud.Notifications.AlarmReceiver;
import mud.arca.io.mud.Settings.SettingsFragment;
import mud.arca.io.mud.Database.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private FrameLayout mTextMessage;

    final private Fragment fragmentHome = new AnalysisFragment();
    final private Fragment fragmentDashboard = new DayListFragment();
    final private Fragment fragmentNotifications = new SettingsFragment();

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
                    return true;
                case R.id.navigation_dashboard:
                    setTitle(R.string.title_dashboard);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_container, fragmentDashboard)
                            .commit();
                    return true;
                case R.id.navigation_notifications:
                    setTitle(R.string.title_profile);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_container, fragmentNotifications)
                            .commit();
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

        testScheduleNotification(getApplicationContext());
    }

    public void testScheduleNotification(Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 14);
        calendar.set(Calendar.MINUTE, 3);
        calendar.set(Calendar.SECOND, 0);

        Intent intent1 = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
    }


}
