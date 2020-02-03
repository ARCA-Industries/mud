package mud.arca.io.mud;

import androidx.appcompat.app.AppCompatActivity;
import mud.arca.io.mud.DataStructures.MockUser;
import mud.arca.io.mud.Views.YearSummaryView;

import android.os.Bundle;

public class ChartTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_test);

        // in this example, a LineChart is initialized from xml
        YearSummaryView chart = findViewById(R.id.chart);
        chart.setData(new MockUser().getDayData());

//        List<Entry> entries = new ArrayList<Entry>();
//
//        // turn your data into Entry objects
//        entries.add(new Entry(1, 2));
//        entries.add(new Entry(2, 4));
//        entries.add(new Entry(3, 9));
//
//        LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
//        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
//        // dataSet.setValueTextColor(...); // styling, ...
//
//        LineData lineData = new LineData(dataSet);
//        chart.setData(lineData);
//        chart.invalidate(); // refresh

    }
}
