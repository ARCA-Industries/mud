package mud.arca.io.mud;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import mud.arca.io.mud.Analysis.charts.VariableStatisticsView;
import mud.arca.io.mud.DataStructures.Day;
import mud.arca.io.mud.DataStructures.User;
import mud.arca.io.mud.Util.Util;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class HomeFragment extends Fragment {

    public ArrayList<Float> variableValues;
    public ArrayList<Day> daysSelected;
    public float sleepMean;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setupToolbar(view);
        getAverages(view);
        getTimeFromAndroid(view);
        return view;
    }


    private void setupToolbar(View rootView) {
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_home));
    }

    /*
     * Hold on to your hats folks, it's about to get real ugly real quick.
     *
     * - Robert
     */

    private void getTimeFromAndroid(View rootView) {
        TextView timeOfDay = rootView.findViewById(R.id.time_of_day);
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        int hours = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);

        if (hours >= 1 && hours <= 12) {
            // Good morning!
            timeOfDay.setText(getString(R.string.greeting_morning));
        } else if (hours > 12 && hours <= 16) {
            // Good afternoon!
            timeOfDay.setText(getString(R.string.greeting_afternoon));
        } else if (hours > 16 && hours <= 24) {
            // Good evening!
            timeOfDay.setText(getString(R.string.greeting_evening));

        }
    }


    private void getWeekDateRange(Date current, Date last) {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int i = c.get(Calendar.DAY_OF_WEEK);
        c.add(Calendar.DATE, - 7);
        last = c.getTime();
        c.add(Calendar.DATE, 7);
        current = c.getTime();
        daysSelected = User.getCurrentUser().fetchDays(last, current);
    }

    private void initVariableValues(String varName) {
        variableValues = Util.getVariableValues(daysSelected, varName);
        Util.debug("varName: " + varName);
        Util.debug("variableValues: " + variableValues);
    }

    private float calculateAverage(String varName){
        initVariableValues(varName);
        int size = variableValues.size();
        float mean;

        if (size == 0) {
            // Set value to NaN
            mean = 0f / 0f;
        } else {
            float sum = 0;
            for (float f : variableValues) {
                sum += f;
            }
            mean = sum / size;
        }

        return mean;
    }


    private void getAverages(View rootView) {
        Date current = new Date();
        Date last = new Date();
        float mood_mean;
        float sleep_mean;
        TextView averageMoodValue = rootView.findViewById(R.id.avg_mood_value);
        TextView averageMoodSuggestion = rootView.findViewById(R.id.avg_mood_suggestion);
        TextView averageSleepValue = rootView.findViewById(R.id.avg_sleep_value);
        TextView averageSleepSuggestion = rootView.findViewById(R.id.avg_sleep_suggestion);
        DecoView avgMoodArc = rootView.findViewById(R.id.avg_mood_arc);
        DecoView avgSleepArc = rootView.findViewById(R.id.avg_sleep_arc);

        avgMoodArc.addSeries(new SeriesItem.Builder(Color.argb(255, 50, 50, 50))
                .setRange(0, 100, 100)
                .setInitialVisibility(true)
                .setLineWidth(32f)
                .build());

        avgSleepArc.addSeries(new SeriesItem.Builder(Color.argb(255, 50, 50, 50))
                .setRange(0, 100, 100)
                .setInitialVisibility(true)
                .setLineWidth(32f)
                .build());

        SeriesItem seriesItem1 = new SeriesItem.Builder(Color.CYAN)
                .setRange(0, 100, 0)
                .setLineWidth(32f)
                .setSpinClockwise(false)
                .build();

        SeriesItem seriesItem2 = new SeriesItem.Builder(Color.CYAN)
                .setRange(0, 100, 0)
                .setLineWidth(32f)
                .setSpinClockwise(false)
                .build();

        int series1Index = avgMoodArc.addSeries(seriesItem1);
        int series2Index = avgSleepArc.addSeries(seriesItem2);




        getWeekDateRange(current, last);
        mood_mean = calculateAverage(Util.MOOD_STRING);
        averageMoodValue.setText(String.format("%.2f", mood_mean));
        if (mood_mean >= 0 && mood_mean <= 3.3) {
            // Bad mood :(
            averageMoodSuggestion.setText(getString(R.string.suggestion_low_mood));
        } else if (mood_mean > 3.3 && mood_mean <= 6.7) {
            // Meh :|
            averageMoodSuggestion.setText(getString(R.string.suggestion_med_mood));
        } else if (mood_mean > 6.7 && mood_mean <= 10) {
            // Good mood! :D
            averageMoodSuggestion.setText(getString(R.string.suggestion_hi_mood));
        }
        avgMoodArc.addEvent(new DecoEvent.Builder((mood_mean) * 10).setIndex(series1Index).build());

        sleep_mean = calculateAverage("Sleep");
        averageSleepValue.setText(String.format("%.2f", sleep_mean));
        if (sleep_mean >= 0 && sleep_mean <= 2.6) {
            // No sleep :(
            averageSleepSuggestion.setText(getString(R.string.suggestion_low_sleep));
        } else if (sleep_mean > 2.6 && sleep_mean <= 5.2) {
            // Meh :|
            averageSleepSuggestion.setText(getString(R.string.suggestion_med_sleep));
        } else if (sleep_mean > 5.2 && sleep_mean <= 8) {
            // Good sleep! :D
            averageSleepSuggestion.setText(getString(R.string.suggestion_hi_sleep));
        }
        avgSleepArc.addEvent(new DecoEvent.Builder(((sleep_mean) / 8) * 100).setIndex(series2Index).build());


    }
}
