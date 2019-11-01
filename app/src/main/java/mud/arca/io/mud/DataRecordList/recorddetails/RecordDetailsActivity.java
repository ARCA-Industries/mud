package mud.arca.io.mud.DataRecordList.recorddetails;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

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

        setTitle("October 24");
    }
}
