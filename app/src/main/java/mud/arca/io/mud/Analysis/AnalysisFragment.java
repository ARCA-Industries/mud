package mud.arca.io.mud.Analysis;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import mud.arca.io.mud.Analysis.charts.MoodVsTimeView;
import mud.arca.io.mud.Analysis.charts.MoodVsVariableView;
import mud.arca.io.mud.Analysis.charts.VariableVsTimeView;
import mud.arca.io.mud.DataStructures.MockUser;
import mud.arca.io.mud.R;
import mud.arca.io.mud.Analysis.tempanalysisviews.BlueChartView;
import mud.arca.io.mud.Analysis.tempanalysisviews.GreenChartView;
import mud.arca.io.mud.Analysis.tempanalysisviews.RedChartView;
import mud.arca.io.mud.Analysis.tempanalysisviews.YellowChartView;
import mud.arca.io.mud.Views.YearSummaryView;

public class AnalysisFragment extends Fragment {

    private enum ChartType {
        YEAR_SUMMARY_CHART(YearSummaryView.class, "Year Summary"),
        VARIABLE_VS_TIME_CHART(VariableVsTimeView.class, "Variable vs Time"),
        MOOD_VS_TIME_CHART(MoodVsTimeView.class, "Mood vs Time"),
        MOOD_VS_VARIABLE_CHART(MoodVsVariableView.class, "Mood vs Variable"),
        ;

        Class<? extends AnalysisChart> view;
        String label;

        ChartType(Class<? extends AnalysisChart> view, String label) {
            this.view = view;
            this.label = label;
        }
    }

    // TODO: This will crash on versions below N. Convert Stream to plain old loops.
    @SuppressLint("NewApi")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.analysis_fragment, container, false);


        // Set up spinner
        AppCompatSpinner spinner = view.findViewById(R.id.inputPlotTypeDropdown);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_spinner_item,
                Arrays.stream(ChartType.values()).map(chartType -> chartType.label).collect(Collectors.toList()));
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setChartType(ChartType.values()[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO: Implement
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    private void setChartType(ChartType chartType) {
        // There's definitely a nicer and safer way to do this.
        try {
            AnalysisChart analysisChart = chartType.view.getDeclaredConstructor(Context.class).newInstance(getContext());

            analysisChart.setDays(new MockUser().getDayData());

            ((FrameLayout) getView().findViewById(R.id.imageView)).removeAllViews();
            ((FrameLayout) getView().findViewById(R.id.imageView)).addView((View) analysisChart);
        } catch (IllegalAccessException | java.lang.InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
