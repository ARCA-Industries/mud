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
import mud.arca.io.mud.DataRecordList.DayListFragment;
import mud.arca.io.mud.DataStructures.AlarmReceiver;
import mud.arca.io.mud.DataStructures.NotificationPublisher;
import mud.arca.io.mud.Settings.SettingsFragment;
import mud.arca.io.mud.database.DatabaseHelper;

import static android.app.AlarmManager.INTERVAL_DAY;

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

        // scheduleNotification(); // doesn't work
        //setNotificationScheduler(getApplicationContext());
        //notiftest(getApplicationContext());
        notiftest2();
    }


    public void scheduleNotification() {
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");

        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 15);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
    }

    public static void setNotificationScheduler(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationPublisher.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 999999, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        // Set the alarm to start at 00:00
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 35);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), INTERVAL_DAY, alarmIntent);
    }


    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;


    public void notiftest(Context context) {
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        //Intent intent = new Intent(context, AlarmReceiver.class);
        Intent intent = new Intent(context, MainActivity.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        // Set the alarm to start at 8:30 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 58);

// setRepeating() lets you specify a precise custom interval--in this case,
// 20 minutes.
        //alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 1, alarmIntent);
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), INTERVAL_DAY, alarmIntent);
    }

    public void notiftest2() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 20);
        calendar.set(Calendar.SECOND, 0);
        Intent intent1 = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) MainActivity.this.getSystemService(MainActivity.this.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }


}
