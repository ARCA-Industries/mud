package mud.arca.io.mud.DayDetails;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Date;

import mud.arca.io.mud.DataStructures.Day;
import mud.arca.io.mud.Database.DatabaseHelper;
import mud.arca.io.mud.R;
import mud.arca.io.mud.Util.Util;

public class DayDetailsActivity extends AppCompatActivity implements DayDetailsFragment.SaveListener {
    public static final String EXTRA_DATE = "date";
    public static final String EXTRA_FLAG_USE_TODAY = "FLAG_USE_TODAY";

    public static Intent getLaunchIntentForDate(Context context, Date date) {
        Intent intent = new Intent(context, DayDetailsActivity.class);
        intent.putExtra(EXTRA_DATE, date);
        return intent;
    }

    /** Sets a flag that will use the day when this intent is fired **/
    public static Intent getLaunchIntentForCurrentDay(Context context) {
        Intent intent = new Intent(context, DayDetailsActivity.class);
        intent.putExtra(EXTRA_FLAG_USE_TODAY, "true");
        return intent;
    }


    DocumentSnapshot dayDocumentSnapshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_details_activity);

        Date date = (Date) getIntent().getSerializableExtra(EXTRA_DATE);

        if (getIntent().getExtras() != null && getIntent().getExtras().get("FLAG_USE_TODAY") != null) {
            System.out.println("DayDetailsActivity FLAG_USE_TODAY set. Using current date.");
            date = Util.getDateNoTime(new Date());
        }

        if (date == null) {
            throw new IllegalArgumentException("Must start this activity with a Date! Use getLaunchIntentForDate or getLaunchIntentForCurrentDay.");
        }

        if (savedInstanceState == null) {
            Date finalDate = date;
            DatabaseHelper.getDaysCollection().whereEqualTo("date", date).get().addOnCompleteListener(runnable -> {
                if (runnable.getResult().getDocuments().isEmpty()) {
                    // If the date isn't found in the database, create a new Day
                    DatabaseHelper.getDaysCollection().add(new Day(finalDate)).addOnSuccessListener(reference -> {
                        reference.get().addOnCompleteListener(documentSnapshotTask -> {
                            loadFragment(documentSnapshotTask.getResult());
                        });
                    });
                } else {
                    // Otherwise use the first (and hopefully only) document that matches that date
                    loadFragment(runnable.getResult().getDocuments().get(0));
                }
            });
        }
    }

    private void loadFragment(DocumentSnapshot documentSnapshot) {
        this.dayDocumentSnapshot = documentSnapshot;

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, DayDetailsFragment.newInstance(documentSnapshot.toObject(Day.class)))
                .commitNow();
    }

    @Override
    public void onSave(Day day) {
        dayDocumentSnapshot.getReference().set(day).addOnCompleteListener(runnable -> {
            finish();
        });
    }
}