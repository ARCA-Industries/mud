package mud.arca.io.mud.Analysis;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import mud.arca.io.mud.Analysis.charts.MoodVsTimeView;
import mud.arca.io.mud.Analysis.charts.MoodVsVariableView;
import mud.arca.io.mud.Analysis.charts.VariableVsTimeView;
import mud.arca.io.mud.DataRecordList.DayListFragment;
import mud.arca.io.mud.DataStructures.User;
import mud.arca.io.mud.DataStructures.Util;
import mud.arca.io.mud.R;
import mud.arca.io.mud.Analysis.charts.YearSummaryView;

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

    public List<String> getChartTypeLabels() {
        List<String> ret = new ArrayList<>();
        for (ChartType ct : ChartType.values()) {
            ret.add(ct.label);
        }
        return ret;
    }

    private int plotTypeSelected = 0;
    private int varSelected = 0;

    /**
     * Looks at the plotTypeSelected and varSelected to update the plot.
     */
    private void updatePlot() {
        setChartType(
                ChartType.values()[plotTypeSelected],
                User.getCurrentUser().getVarData().get(varSelected).getName()
        );
    }

    /**
     * Hides a spinner.
     * @param spinner
     */
    private void hideSpinner(AppCompatSpinner spinner) {
        ViewGroup.LayoutParams params = spinner.getLayoutParams();
        params.height = 0;
        spinner.setLayoutParams(params);
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.analysis_fragment, container, false);

        // Set up plot type spinner
        AppCompatSpinner spinner = view.findViewById(R.id.inputPlotTypeDropdown);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_spinner_item,
                getChartTypeLabels());
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //setChartType(ChartType.values()[i], "Sleep");
                plotTypeSelected = i;
                updatePlot();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Not implemented
            }
        });

        // Set up variable spinner
        AppCompatSpinner varSpinner = view.findViewById(R.id.inputVariableDropdown);
        ArrayAdapter<String> varSpinnerArrayAdapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_spinner_item,
                DayListFragment.getVariableLabels());
        varSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        varSpinner.setAdapter(varSpinnerArrayAdapter);
        varSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                varSelected = i;
                updatePlot();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Not implemented
            }
        });

        //hideSpinner(varSpinner);

        EditText startET = (EditText) view.findViewById(R.id.inputStartEditText);
        DatePickerDialog.OnDateSetListener startListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                // Set text
                Date date = new GregorianCalendar(year, month, day).getTime();
                startET.setText(Util.formatDateWithYear(date));
                // Save to field
                startDate = date;
            }
        };
        setupDatePicker(view, startET, startListener);

        EditText endET = (EditText) view.findViewById(R.id.inputEndEditText);
        DatePickerDialog.OnDateSetListener endListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                // Set text
                Date date = new GregorianCalendar(year, month, day).getTime();
                endET.setText(Util.formatDateWithYear(date));
                // Save to field
                endDate = date;
            }
        };
        setupDatePicker(view, endET, endListener);

        return view;
    }

    void setupDatePicker(View view, EditText et, DatePickerDialog.OnDateSetListener listener) {
        // Stop the keyboard from popping up
        et.setShowSoftInputOnFocus(false);
        et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int curDay = cldr.get(Calendar.DAY_OF_MONTH);
                int curMonth = cldr.get(Calendar.MONTH);
                int curYear = cldr.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog picker = new DatePickerDialog(view.getContext(), listener,
                        curYear, curMonth, curDay);
                picker.show();
            }
        });
    }

    private Date endDate;
    private Date startDate;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    private void setChartType(ChartType chartType, String varName) {
        // There's definitely a nicer and safer way to do this.
        try {
            AnalysisChart analysisChart = chartType.view.getDeclaredConstructor(Context.class).newInstance(getContext());

            analysisChart.setDaysAndVariable(User.getCurrentUser().getDayData(), varName);

            ((FrameLayout) getView().findViewById(R.id.imageView)).removeAllViews();
            ((FrameLayout) getView().findViewById(R.id.imageView)).addView((View) analysisChart);
        } catch (IllegalAccessException | java.lang.InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
