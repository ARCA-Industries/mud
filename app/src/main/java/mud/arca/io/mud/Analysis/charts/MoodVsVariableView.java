package mud.arca.io.mud.Analysis.charts;

import android.content.Context;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import mud.arca.io.mud.Analysis.AnalysisChart;
import mud.arca.io.mud.Analysis.ChartWithDates;
import mud.arca.io.mud.Analysis.ChartWithVariable;
import mud.arca.io.mud.Analysis.ShareableChart;
import mud.arca.io.mud.DataStructures.Day;
import mud.arca.io.mud.DataStructures.Measurement;
import mud.arca.io.mud.DataStructures.User;
import mud.arca.io.mud.Util.Util;

public class MoodVsVariableView extends ScatterChart
        implements AnalysisChart, ChartWithVariable, ChartWithDates, ShareableChart {
    public MoodVsVariableView(Context context) {
        super(context);
        init(null, 0);
    }

    public MoodVsVariableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MoodVsVariableView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

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

    static void plotFloatsScatter(ArrayList<Float> xs, ArrayList<Float> ys, ScatterChart scatterChart) {
        List<Entry> scatterEntries = new ArrayList<>();
        for (int i = 0; i < xs.size(); i++) {
            scatterEntries.add(new BarEntry(xs.get(i), ys.get(i)));
        }

        ScatterDataSet scatterDataSet = new ScatterDataSet(scatterEntries, "");
        ScatterData scatterData = new ScatterData(scatterDataSet);
        scatterChart.setData(scatterData);
        scatterDataSet.setColors(Util.MUD_GRAPH_COLORS);

        scatterDataSet.setScatterShapeSize(30f);
        scatterDataSet.setScatterShape(ScatterShape.CIRCLE);

        // Disable legend, disable description
        Legend legend = scatterChart.getLegend();
        legend.setEnabled(false);
        Description description = scatterChart.getDescription();
        description.setEnabled(false);
        // Disable the text labeling data points
        scatterChart.setMaxVisibleValueCount(0);
    }

    // Input: a list of days, variable name
    // Makes a plot with variable on X-axis, mood on Y-axis.
    void plotListOfDays(Collection<Day> dayData, String varName) {
        ArrayList<Float> xs = new ArrayList<>();
        ArrayList<Float> ys = new ArrayList<>();

        for (Day day : dayData) {
            Collection<Measurement> measurements = day.getMeasurements();
            try {
                // this line might throw Exception
                Measurement m = Measurement.searchList(measurements, varName);
                // this line also might throw Exception
                float avgMood = (float) day.getAverageMood();

                xs.add(m.getValue());
                ys.add(avgMood);
            } catch (NoSuchElementException e) {
                // do nothing
            }

        }
        plotFloatsScatter(xs, ys, this);
    }

    /**
     * Update the plot based on startDate, endDate, varName.
     */
    public void updateChart() {
        ArrayList<Day> dayData = User.getCurrentUser().fetchDays(startDate, endDate);
        plotListOfDays(dayData, varName);
    }

    @Override
    public void setDaysAndVariable(Collection<Day> days, String varName) {
        plotListOfDays(days, varName);
    }
}
