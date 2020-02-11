package mud.arca.io.mud.Analysis.charts;

import android.content.Context;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.NoSuchElementException;

import mud.arca.io.mud.Analysis.AnalysisChart;
import mud.arca.io.mud.DataStructures.Day;
import mud.arca.io.mud.DataStructures.Measurement;
import mud.arca.io.mud.DataStructures.MockUser;
import mud.arca.io.mud.DataStructures.Util;

public class MoodVsVariableView extends com.github.mikephil.charting.charts.BarChart implements AnalysisChart {
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

    // Input: a list of days, variable name
    // Makes a plot with variable on X-axis, mood on Y-axis.
    void plotListOfDays(ArrayList<Day> dayData, String varName) {
        ArrayList<Float> xs = new ArrayList<>();
        ArrayList<Float> ys = new ArrayList<>();

        for (Day day : dayData) {
            Collection<Measurement> measurements = day.getMeasurements();
            try {
                Measurement m = Measurement.searchList(measurements, varName);
                xs.add(m.getValue());

                float avgMood = (float) day.getAverageMood();
                ys.add(avgMood);
            } catch (NoSuchElementException e) {
                // do nothing
            }

        }

        VariableVsTimeView.plotFloats(xs, ys, this);
    }

    void plotMockUser() {
        MockUser mockUser = new MockUser();
        plotListOfDays(mockUser.getDayData(), "Sleep");
    }

    @Override
    public void setDays(Collection<Day> days) {
        plotMockUser(); // TODO: Use days
    }
}