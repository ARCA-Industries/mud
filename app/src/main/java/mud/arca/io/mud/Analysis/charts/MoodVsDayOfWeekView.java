package mud.arca.io.mud.Analysis.charts;

import android.content.Context;
import android.util.AttributeSet;

import mud.arca.io.mud.Analysis.AnalysisChart;
import mud.arca.io.mud.Analysis.ChartWithDates;
import mud.arca.io.mud.Analysis.DayAxisVF;
import mud.arca.io.mud.Analysis.DayOfWeekVF;
import mud.arca.io.mud.Analysis.ShareableChart;
import mud.arca.io.mud.DataStructures.Day;
import mud.arca.io.mud.DataStructures.User;
import mud.arca.io.mud.Util.Util;

import com.github.mikephil.charting.charts.BarChart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class MoodVsDayOfWeekView extends BarChart
        implements AnalysisChart, ChartWithDates, ShareableChart {

    public MoodVsDayOfWeekView(Context context) {
        super(context);
        init(null, 0);
    }

    public MoodVsDayOfWeekView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MoodVsDayOfWeekView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
    }

    private Date startDate;
    private Date endDate;
    ArrayList<Float> xValues;
    ArrayList<Float> yValues;

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public void setDaysAndVariable(Collection<Day> days, String varName) {
        // Not used
    }

    public void plotListOfDays(Collection<Day> dayData) {

    }

    /**
     * Update the plot based on startDate, endDate.
     */
    public void updateChart() {
        // map[dayOfWeek] = ArrayList of mood recordings
        HashMap<Integer, ArrayList<Float>> map = new HashMap<>();
        for (int i = 1; i <= 7; i++) {
            map.put(i, new ArrayList<>());
        }

        // Loop through the days and put mood recordings into 7 lists based on day of week.
        ArrayList<Day> dayData = User.getCurrentUser().fetchDays(startDate, endDate);
        for (Day day : dayData) {
            try {
                float avgMood = day.getAverageMood();
                int dayOfWeek = Util.getDayOfWeek(day.getDate());
                map.get(dayOfWeek).add(avgMood);
            } catch (NoSuchElementException e) {
                // Do nothing
            }
        }
        //Util.debug("map: " + map);

        // xValues is [1,2,3,4,5,6,7] as floats
        // yValues is the average mood for the corresponding day.
        xValues = new ArrayList<>();
        yValues = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            xValues.add((float) i);
            yValues.add(Util.getAverage(map.get(i)));
        }
        VariableVsTimeView.plotFloats(xValues, yValues, this);

        // Apply the value formatter DayOfWeekVF
        this.getXAxis().setValueFormatter(new DayOfWeekVF(this));
    }
}
