package mud.arca.io.mud.Analysis.charts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import mud.arca.io.mud.Analysis.AnalysisChart;
import mud.arca.io.mud.Analysis.ChartWithDates;
import mud.arca.io.mud.DataStructures.Day;
import mud.arca.io.mud.DataStructures.User;
import mud.arca.io.mud.Util.Util;
import mud.arca.io.mud.Util.VariableImportanceUtil;

public class VariableImportancesChart extends LinearLayout implements AnalysisChart, ChartWithDates {

    private Date startDate;
    private Date endDate;

    public static AnalysisChart newInstance(Context context) {
        return new VariableImportancesChart(context);
    }

    public VariableImportancesChart(Context context) {
        super(context);
        init(null, 0);
    }

    public VariableImportancesChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public VariableImportancesChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }


    Collection<Day> dayData;

    private TextView outTextView;

    private void init(AttributeSet attrs, int defStyle) {
        setOrientation(VERTICAL);
        TextView outTextView = new TextView(getContext());
        outTextView.setSingleLine(false);
        this.outTextView = outTextView;
        addView(outTextView);
    }


    @SuppressLint("NewApi")
    private void onCalcComplete(Map<String, Double> covariances) {
        String text = covariances.entrySet().stream()
                .sorted(Comparator.comparing(entry -> Math.abs(((Map.Entry<String, Double>) entry).getValue())).reversed())
                .map(entry -> String.format("Var: %s\tCov: %f", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("\n"));

        outTextView.setText(text);
    }

    @Override
    public void updateChart() {
        new VariableImportanceUtil.CalculateTask(this::onCalcComplete).execute(
                User.getCurrentUser().fetchDays(startDate, endDate),
                Util.getVariableLabels()
        );
    }

    @SuppressLint("NewApi")
    public void setData(Collection<Day> days) {
        this.dayData = days;
    }

    @Override
    public void setDaysAndVariable(Collection<Day> days, String varName) {
        // Same as setData because this chart uses all variables
        setData(days);
    }


    @Override
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Override
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
