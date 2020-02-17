package mud.arca.io.mud;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class ChartTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_test);

        // in this example, a LineChart is initialized from xml
        //LineChart chart = (LineChart) findViewById(R.id.chart);
        BarChart barChart = findViewById(R.id.chart);

        List<BarEntry> entries = new ArrayList<BarEntry>();
        entries.add(new BarEntry(0, 4));
        entries.add(new BarEntry(1, 8));
        entries.add(new BarEntry(2, 7));

        // turn your data into Entry objects
        //entries.add(new Entry(1, 2));
        //entries.add(new Entry(2, 4));
        //entries.add(new Entry(3, 9));

        BarDataSet dataSet = new BarDataSet(entries, "Label"); // add entries to dataset
        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        // dataSet.setValueTextColor(...); // styling, ...

        BarData lineData = new BarData(dataSet);
        //barChart.setData(lineData);
        //chart.invalidate();
        barChart.setData(lineData);
        barChart.invalidate();

        Legend legend = barChart.getLegend();
        legend.setEnabled(false);
        Description description = barChart.getDescription();
        description.setEnabled(false);
        // Disable the text above each bar for each data pt
        barChart.setMaxVisibleValueCount(0);

    }
}
