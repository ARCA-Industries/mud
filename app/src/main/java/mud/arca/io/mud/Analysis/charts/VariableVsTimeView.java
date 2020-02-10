package mud.arca.io.mud.Analysis.charts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

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
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import mud.arca.io.mud.Analysis.AnalysisChart;
import mud.arca.io.mud.DataStructures.Day;
import mud.arca.io.mud.DataStructures.DayAxisVF;
import mud.arca.io.mud.DataStructures.Measurement;
import mud.arca.io.mud.DataStructures.MockUser;
import mud.arca.io.mud.DataStructures.Util;
import mud.arca.io.mud.R;

public class VariableVsTimeView extends com.github.mikephil.charting.charts.BarChart implements AnalysisChart {

    public VariableVsTimeView(Context context) {
        super(context);
        init(null, 0);
    }

    public VariableVsTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public VariableVsTimeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

    }

    @Override
    public void setDays(Collection<Day> days) {
        plotMockUser(); // TODO: Use days
    }

    public static Date getBaseDate() {
        return Util.parseDate("01-January-1970");
    }

    // Convert a date to float.
    // Returns the number of days passed since base date.
    static float dateToFloat(Date d) {
        long diff = d.getTime() - getBaseDate().getTime();
        return (float) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    static void plotFloats(ArrayList<Float> xs, ArrayList<Float> ys, BarChart barChart) {
//        setContentView(R.layout.activity_chart_test);

        // This view is a BarChart
        //BarChart barChart = this;

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

        // Apply the value formatter DayAxisVF
        barChart.getXAxis().setValueFormatter(new DayAxisVF(barChart));
    }

    public static void plotDates(ArrayList<Date> xs, ArrayList<Float> ys, BarChart barchart) {
        ArrayList<Float> xsFloat = new ArrayList<>();
        for (Date d : xs) {
            xsFloat.add(dateToFloat(d));
        }
        plotFloats(xsFloat, ys, barchart);
    }

    // Given a list of days and a name of variable, plot the variable over those days.
    void plotListOfDays(ArrayList<Day> dayData, String varName) {
        ArrayList<Date> xs = new ArrayList<>();
        ArrayList<Float> ys = new ArrayList<>();

        for (Day day : dayData) {
            Collection<Measurement> measurements = day.getMeasurements();
            try {
                Measurement m = Measurement.searchList(measurements, varName);
                ys.add(m.getValue());
                Date d = day.getDate();
                xs.add(d);
                Util.debug("" + d);
            } catch (NoSuchElementException e) {
                // do nothing
            }
        }

        plotDates(xs, ys, this);
    }

    void plotMockUser() {
        MockUser mockUser = new MockUser();
        plotListOfDays(mockUser.getDayData(), "Sleep");
    }
}
