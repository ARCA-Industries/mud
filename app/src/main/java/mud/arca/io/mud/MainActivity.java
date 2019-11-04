package mud.arca.io.mud;

import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.view.MenuItem;
import android.widget.FrameLayout;

import mud.arca.io.mud.Analysis.AnalysisFragment;
import mud.arca.io.mud.DataRecordList.DataRecordListFragment;
import mud.arca.io.mud.Settings.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    private FrameLayout mTextMessage;

    final private Fragment fragmentHome = new AnalysisFragment();
    final private Fragment fragmentDashboard = new DataRecordListFragment();
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

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        navigation.setSelectedItemId(R.id.navigation_dashboard);
    }

}
