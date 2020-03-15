package mud.arca.io.mud.Analysis;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
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

import com.google.android.material.textfield.TextInputLayout;

import mud.arca.io.mud.Analysis.charts.MoodVsTimeView;
import mud.arca.io.mud.Analysis.charts.MoodVsVariableView;
import mud.arca.io.mud.Analysis.charts.VariableVsTimeView;
import mud.arca.io.mud.Analysis.charts.YearSummaryView;
import mud.arca.io.mud.Util.MyAnimationHandler;
import mud.arca.io.mud.DataStructures.User;
import mud.arca.io.mud.Util.Util;
import mud.arca.io.mud.R;

public class AnalysisFragment extends Fragment {

    private enum ChartType {
        VARIABLE_VS_TIME_CHART(VariableVsTimeView.class, "Variable vs Time"),
        MOOD_VS_TIME_CHART(MoodVsTimeView.class, "Mood vs Time"),
        MOOD_VS_VARIABLE_CHART(MoodVsVariableView.class, "Mood vs Variable"),
        YEAR_SUMMARY_CHART(YearSummaryView.class, "Year Summary"),
        ;

        Class<? extends AnalysisChart> view;
        String label;

        ChartType(Class<? extends AnalysisChart> view, String label) {
            this.view = view;
            this.label = label;
        }
    }

    public DateSelector endDS;
    public DateSelector startDS;
    private int varSelected = 0;
    private ChartType chartTypeSelected = ChartType.VARIABLE_VS_TIME_CHART;

    /**
     * Earliest Date in the current User.
     */
    Date earliestDate;

    /**
     * Latest Date in the current User.
     */
    Date latestDate;

    /**
     * Start Date for the graph.
     */
    Date startDate;

    /**
     * End Date for the graph.
     */
    Date endDate;

    AppCompatSpinner varSpinner;
    AppCompatSpinner plotTypeSpinner;
    TextInputLayout inputStartLayout;
    TextInputLayout inputEndLayout;

    public List<String> getChartTypeLabels() {
        List<String> ret = new ArrayList<>();
        for (ChartType ct : ChartType.values()) {
            ret.add(ct.label);
        }
        return ret;
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.analysis_fragment, container, false);

        User.getCurrentUser().updateUserData(user -> {
            refreshView();
        });

        return view;
    }

    /**
     * Create spinner with resource id and objects to populate dropdown.
     * @param id
     * @param objects
     * @return
     */
    private AppCompatSpinner setupSpinner(int id, List objects) {
        AppCompatSpinner spinner = getView().findViewById(id);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getView().getContext(),
                android.R.layout.simple_spinner_item, objects);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        return spinner;
    }

    private void refreshView() {
        // Set up variable spinner
        varSpinner = setupSpinner(R.id.inputVariableDropdown, Util.getVariableLabels());
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

        inputStartLayout = getView().findViewById(R.id.inputStartLayout);
        inputEndLayout = getView().findViewById(R.id.inputEndLayout);
        MyAnimationHandler startAH = new MyAnimationHandler(inputStartLayout);
        MyAnimationHandler endAH = new MyAnimationHandler(inputEndLayout);
        MyAnimationHandler varSpinnerAH = new MyAnimationHandler(varSpinner);

        // Set up plot type spinner
        plotTypeSpinner = setupSpinner(R.id.inputPlotTypeDropdown, getChartTypeLabels());
        plotTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                chartTypeSelected = ChartType.values()[i];

                if (ChartWithVariable.class.isAssignableFrom(chartTypeSelected.view)) {
                    varSpinnerAH.expand();
                } else {
                    varSpinnerAH.collapse();
                }

                if (ChartWithDates.class.isAssignableFrom(chartTypeSelected.view)) {
                    startAH.expand();
                    endAH.expand();
                } else {
                    startAH.collapse();
                    endAH.collapse();
                }

                updatePlot();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Not implemented
            }
        });

        // Initialize dates
        earliestDate = User.getCurrentUser().getEarliestDate();
        latestDate = User.getCurrentUser().getLatestDate();
        // Initialize end date to latest date in current user.
        // Initialize start date to 30 days before end date.
        endDate = latestDate;
        startDate = Util.intToDate(latestDate, -30);

        // Initialize DateSelectors
        EditText startET = getView().findViewById(R.id.inputStartEditText);
        startDS = new DateSelector(getView(), startET, true);
        EditText endET = getView().findViewById(R.id.inputEndEditText);
        endDS = new DateSelector(getView(), endET, false);
    }

    /**
     * DateSelector is used to initialize an EditText, so that it pops up a date picker dialog
     * when clicked. The date field keeps track of the date the user selects in the dialog.
     */
    public class DateSelector {
        public Date date;
        public EditText et;
        public boolean isStartDate;

        public void setDate(Date dateSelected) {
            // Set text of EditText
            et.setText(Util.formatDateWithYear(dateSelected));
            // Save to field
            date = dateSelected;
            // Save to field in AnalysisFragment
            if (isStartDate) {
                startDate = dateSelected;
            } else {
                endDate = dateSelected;
            }
        }

        public DateSelector(View view, EditText et, boolean isStartDate) {
            this.et = et;
            this.isStartDate = isStartDate;

            // Stop the keyboard from popping up when the EditText is clicked.
            et.setShowSoftInputOnFocus(false);
            // Disable blinking cursor
            et.setCursorVisible(false);

            et.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int day) {
                            Date dateSelected = new GregorianCalendar(year, month, day).getTime();

                            setDate(dateSelected);
                            updatePlot();
                        }
                    };

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    int oldDay = cal.get(Calendar.DAY_OF_MONTH);
                    int oldMonth = cal.get(Calendar.MONTH);
                    int oldYear = cal.get(Calendar.YEAR);

                    // Initialize the DatePickerDialog with the old day selected.
                    DatePickerDialog picker = new DatePickerDialog(view.getContext(), listener,
                            oldYear, oldMonth, oldDay);

                    // Restrict the dates in DatePickerDialog.
                    Date minDate;
                    Date maxDate;
                    if (isStartDate) {
                        minDate = earliestDate;
                        maxDate = endDate;
                    } else {
                        minDate = startDate;
                        maxDate = latestDate;
                    }
                    picker.getDatePicker().setMinDate(minDate.getTime());
                    picker.getDatePicker().setMaxDate(maxDate.getTime());

                    picker.show();
                }
            });

            if (isStartDate) {
                setDate(startDate);
            } else {
                setDate(latestDate);
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Looks at the chartTypeSelected and varSelected to update the plot.
     */
    private void updatePlot() {
        Util.debug("updatePlot called");

        if (User.getCurrentUser().getVarData().isEmpty()) {
            Util.debug("User var data is empty, skipping updatePlot");
            return;
        }

        String varName = User.getCurrentUser().getVarData().get(varSelected).getName();

        // There's definitely a nicer and safer way to do this.
        try {
            AnalysisChart analysisChart = chartTypeSelected.view.getDeclaredConstructor(Context.class).newInstance(getContext());

            // If the chart type selected extends ChartWithDates, set start and end dates.
            if (ChartWithDates.class.isAssignableFrom(chartTypeSelected.view)) {
                ChartWithDates cwd = (ChartWithDates) analysisChart;
                cwd.setStartDate(startDS.date);
                cwd.setEndDate(endDS.date);
            }

            // If the chart type selected extends ChartWithVariable, set variable name.
            if (ChartWithVariable.class.isAssignableFrom(chartTypeSelected.view)) {
                ChartWithVariable cwv = (ChartWithVariable) analysisChart;
                cwv.setVarName(varName);
            }

            //analysisChart.setDaysAndVariable(User.getCurrentUser().getDayData(), varName);
            analysisChart.updateChart();

            FrameLayout imageView = getView().findViewById(R.id.imageView);
            imageView.removeAllViews();
            imageView.addView((View) analysisChart);
        } catch (IllegalAccessException | java.lang.InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }




}
