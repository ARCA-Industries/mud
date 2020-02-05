package mud.arca.io.mud;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import mud.arca.io.mud.DataStructures.Day;
import mud.arca.io.mud.DataStructures.DayAxisVF;
import mud.arca.io.mud.DataStructures.Measurement;
import mud.arca.io.mud.DataStructures.MockUser;
import mud.arca.io.mud.DataStructures.Util;

public class VariableVsTimeActivity extends AppCompatActivity {
    public static Date getBaseDate() {
        return Util.parseDate("01-January-1970");
    }

    // Convert a date to float.
    // Returns the number of days passed since base date.
    float dateToFloat(Date d) {
        long diff = d.getTime() - getBaseDate().getTime();
        return (float) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    void plotFloats(ArrayList<Float> xs, ArrayList<Float> ys) {
        setContentView(R.layout.activity_chart_test);

        // in this example, a LineChart is initialized from xml
        BarChart barChart = (BarChart) findViewById(R.id.chart);

        List<BarEntry> entries = new ArrayList<BarEntry>();
        int sz = xs.size();
        for (int i = 0; i < sz; i++) {
            entries.add(new BarEntry(xs.get(i), ys.get(i)));
        }

        // add entries to dataset
        BarDataSet dataSet = new BarDataSet(entries, "Label");
        //dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        dataSet.setColors(Util.MUD_GRAPH_COLORS);
        // dataSet.setValueTextColor(...); // styling, ...

        BarData lineData = new BarData(dataSet);
        barChart.setData(lineData);
        barChart.invalidate();

        Legend legend = barChart.getLegend();
        legend.setEnabled(false);
        Description description = barChart.getDescription();
        description.setEnabled(false);
        // Disable the text above each bar for each data pt
        barChart.setMaxVisibleValueCount(0);

        barChart.getXAxis().setValueFormatter(new DayAxisVF(barChart));
    }

    void plotDates(ArrayList<Date> xs, ArrayList<Float> ys) {
        ArrayList<Float> xsFloat = new ArrayList<>();
        for (Date d : xs) {
            xsFloat.add(dateToFloat(d));
        }
        plotFloats(xsFloat, ys);
    }

    // Given a list of days and a name of variable, plot the variable over those days.
    void plotListOfDays(ArrayList<Day> dayData, String varName) {
        ArrayList<Date> xs = new ArrayList<>();
        ArrayList<Float> ys = new ArrayList<>();

        for (Day day : dayData) {
            Date d = day.getDate();
            xs.add(d);
            Log.d("QQ66", "" + d);

            Collection<Measurement> measurements = day.getMeasurements();
            for (Measurement m : measurements) {
                if (m.getVariable().getName() == varName) {
                    ys.add(m.getValue());
                }
            }
        }

        plotDates(xs, ys);
    }

    void plotMockUser() {
        MockUser mockUser = new MockUser();
        plotListOfDays(mockUser.getDayData(), "Sleep");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        plotMockUser();
        //test10();
    }
}
