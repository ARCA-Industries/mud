package mud.arca.io.mud.DayDetails;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import mud.arca.io.mud.DataStructures.Day;
import mud.arca.io.mud.R;

public class DayDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_DAY = "day";

    public static Intent getLaunchIntentForDay(Context context, Day day) {
        Intent intent = new Intent(context, DayDetailsActivity.class);
        intent.putExtra(EXTRA_DAY, day);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_details_activity);

        Day day = (Day) getIntent().getSerializableExtra(EXTRA_DAY);

        if (day == null) {
            throw new IllegalArgumentException("Must start this activity with a Day! Use getLaunchIntentForDay.");
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, DayDetailsFragment.newInstance(day))
                    .commitNow();
        }
    }
}
