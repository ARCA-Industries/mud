package mud.arca.io.mud.DataRecordList.recorddetails;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import mud.arca.io.mud.DataRecordList.DataRecordListFragment;
import mud.arca.io.mud.DataStructures.Day;
import mud.arca.io.mud.DataStructures.Util;
import mud.arca.io.mud.R;

public class RecordDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_details_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, RecordDetailsFragment.newInstance())
                    .commitNow();
        }

//        Bundle extras = getIntent().getExtras();
//        String value = extras.getString("key1");
//        setTitle(value);

        Day d = DataRecordListFragment.daySelected;
        String title = Util.formatDateWithYear(d.getDate());
        setTitle(title);
    }
}
