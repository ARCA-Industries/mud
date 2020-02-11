package mud.arca.io.mud.Analysis.charts;

import android.content.Context;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.BarChart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import mud.arca.io.mud.Analysis.AnalysisChart;
import mud.arca.io.mud.DataStructures.Day;
import mud.arca.io.mud.DataStructures.Measurement;
import mud.arca.io.mud.DataStructures.MockUser;
import mud.arca.io.mud.DataStructures.Util;

public class MoodVsTimeView extends BarChart implements AnalysisChart {
    public MoodVsTimeView(Context context) {
        super(context);
        init(null, 0);
    }

    public MoodVsTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MoodVsTimeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

    }

    @Override
    public void setDays(Collection<Day> days) {
        plotMockUser(); // TODO: Use days
    }


    // Input: a list of days
    // Plots the mood over those days.
    void plotListOfDays(ArrayList<Day> dayData, String varName) {
        ArrayList<Date> xs = new ArrayList<>();
        ArrayList<Float> ys = new ArrayList<>();

        for (Day day : dayData) {
            float avgMood = (float) day.getAverageMood();
            ys.add(avgMood);
            Date d = day.getDate();
            xs.add(d);
        }

        VariableVsTimeView.plotDates(xs, ys, this);
    }

    void plotMockUser() {
        MockUser mockUser = new MockUser();
        plotListOfDays(mockUser.getDayData(), "Sleep");
    }
}
