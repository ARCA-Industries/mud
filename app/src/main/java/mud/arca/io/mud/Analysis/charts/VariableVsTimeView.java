package mud.arca.io.mud.Analysis.charts;

import android.content.Context;
import android.util.AttributeSet;

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

import mud.arca.io.mud.Analysis.AnalysisChart;
import mud.arca.io.mud.Analysis.ChartWithDates;
import mud.arca.io.mud.Analysis.ChartWithVariable;
import mud.arca.io.mud.DataStructures.Day;
import mud.arca.io.mud.Analysis.DayAxisVF;
import mud.arca.io.mud.DataStructures.Measurement;
import mud.arca.io.mud.DataStructures.User;
import mud.arca.io.mud.Util.Util;

public class VariableVsTimeView extends BarChart
        implements AnalysisChart, ChartWithVariable, ChartWithDates {
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

    public static Date getBaseDate() {
        return Util.parseDate("01-January-1970");
    }

    private String varName;

    public void setVarName(String varName) {
        this.varName = varName;
    }

    private Date startDate;
    private Date endDate;

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    static void plotFloats(ArrayList<Float> xs, ArrayList<Float> ys, BarChart barChart) {
//        setContentView(R.layout.activity_chart_test);

        // This view is a BarChart
        //BarChart barChart = this;

        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < xs.size(); i++) {
            entries.add(new BarEntry(xs.get(i), ys.get(i)));
        }

        // add entries to dataset
        BarDataSet dataSet = new BarDataSet(entries, "Label");
        dataSet.setColors(Util.MUD_GRAPH_COLORS);
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.invalidate();

        // Disable legend, disable description
        Legend legend = barChart.getLegend();
        legend.setEnabled(false);
        Description description = barChart.getDescription();
        description.setEnabled(false);
        // Disable the text labeling data points
        barChart.setMaxVisibleValueCount(0);
    }

    public static void plotDates(ArrayList<Date> xs, ArrayList<Float> ys, BarChart barChart) {
        ArrayList<Float> xsFloat = new ArrayList<>();
        for (Date d : xs) {
            xsFloat.add(Util.dateToFloat(d, getBaseDate()));
        }
        plotFloats(xsFloat, ys, barChart);

        // Apply the value formatter DayAxisVF
        barChart.getXAxis().setValueFormatter(new DayAxisVF(barChart));
    }

    // Input: a list of days, variable name
    // Plots the variable over those days.
    void plotListOfDays(Collection<Day> dayData, String varName) {
        ArrayList<Date> xs = new ArrayList<>();
        ArrayList<Float> ys = new ArrayList<>();

        if (varName.equals(Util.MOOD_STRING)) {
            // Handle special case for Mood pseudo-variable
            for (Day day : dayData) {
                try {
                    float avgMood = day.getAverageMood();
                    // If the above line does not throw an exception, add the date and value.
                    xs.add(day.getDate());
                    ys.add(avgMood);
                } catch (NoSuchElementException e) {
                    // Do nothing
                }
            }
        } else {
            for (Day day : dayData) {
                Collection<Measurement> measurements = day.getMeasurements();
                try {
                    Measurement m = Measurement.searchList(measurements, varName);
                    // If the above line does not throw an exception, add the date and value.
                    ys.add(m.getValue());
                    xs.add(day.getDate());
                } catch (NoSuchElementException e) {
                    // do nothing
                }
            }
        }

        plotDates(xs, ys, this);
    }

    /**
     * Update the plot based on startDate, endDate, varName.
     */
    public void updateChart() {
        ArrayList<Day> dayData = User.getCurrentUser().fetchDays(startDate, endDate);
        //Util.debug(String.valueOf(dayData.size()));
        plotListOfDays(dayData, varName);
    }

    @Override
    public void setDaysAndVariable(Collection<Day> days, String varName) {
        plotListOfDays(days, varName);
    }
}
