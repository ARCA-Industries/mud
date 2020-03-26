package mud.arca.io.mud.Analysis;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

import androidx.preference.PreferenceManager;
import mud.arca.io.mud.Analysis.charts.MoodVsTimeView;
import mud.arca.io.mud.Analysis.charts.MoodVsVariableView;
import mud.arca.io.mud.Analysis.charts.VariableVsTimeView;
import mud.arca.io.mud.Analysis.charts.YearSummaryView;
import mud.arca.io.mud.Util.FragmentWithMenu;
import mud.arca.io.mud.Util.MyAnimationHandler;
import mud.arca.io.mud.DataStructures.User;
import mud.arca.io.mud.Util.Util;
import mud.arca.io.mud.R;

public class AnalysisFragment extends Fragment implements FragmentWithMenu {

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
            // We have to set focusable to false, otherwise the user must click twice to set the date.
            et.setFocusable(false);

            et.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int day) {
                            Date dateSelected = new GregorianCalendar(year, month, day).getTime();
                            setDate(dateSelected);

                            Util.debug("onDateSet() called");
                            updatePlot();
                        }
                    };

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    int oldDay = cal.get(Calendar.DAY_OF_MONTH);
                    int oldMonth = cal.get(Calendar.MONTH);
                    int oldYear = cal.get(Calendar.YEAR);

                    boolean useSpinner = sharedPrefs.getBoolean("use_spinner_datepicker", false);
                    int themeResId = useSpinner ? R.style.MySpinnerDatePickerStyle : R.style.MyCalendarDatePickerStyle;

                    // Initialize the DatePickerDialog with the old day selected.
                    DatePickerDialog picker = new DatePickerDialog(view.getContext(),
                            themeResId, listener, oldYear, oldMonth, oldDay);

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
                    // TODO: There is no gap between buttons, fix it
                    //Button b1 = picker.getButton(DialogInterface.BUTTON_NEGATIVE);
                }
            });

            // Set the initial date of the DateSelector
            if (isStartDate) {
                setDate(startDate);
            } else {
                setDate(latestDate);
            }
        }
    }

    /**
     * This class is used to generate dropdown items where the user can select last 7 days, last 30 days, etc.
     */
    public class ItemToSelectDays {
        private int numDays;

        public ItemToSelectDays(int numDays) {
            this.numDays = numDays;
        }

        public String getText() {
            return String.format("Plot last %d days", numDays);
        }

        public void applyToDateSelectors() {
            endDS.setDate(latestDate);
            startDS.setDate(Util.intToDate(latestDate, -numDays));
            updatePlot();
        }
    }

    public List<ItemToSelectDays> menuDropdownItems = Arrays.asList(
            new ItemToSelectDays(7),
            new ItemToSelectDays(30)
    );

    @Override
    public void onThreeDotsClicked(View anchor) {
        PopupMenu menu = new PopupMenu(getActivity(), anchor);

        for (int id = 0; id < menuDropdownItems.size(); id++) {
            ItemToSelectDays item = menuDropdownItems.get(id);
            menu.getMenu().add(Menu.NONE, id, id, item.getText());
        }

        menu.show();

        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                int id = item.getItemId();
                if (id >= menuDropdownItems.size()) {
                    // This should never happen, it should always be that case that id < menuDropdownItems.size().
                    return false;
                } else {
                    menuDropdownItems.get(id).applyToDateSelectors();
                    return true;
                }
            }
        });
    }

    private void setupToolbar(View rootView) {
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_analysis));
        toolbar.setOnMenuItemClickListener(item -> {
            onThreeDotsClicked(toolbar);
            return true;
        });
        toolbar.inflateMenu(R.menu.three_dots_menu);
    }

    public DateSelector endDS;
    public DateSelector startDS;
    SharedPreferences sharedPrefs;
    AppCompatSpinner varSpinner;
    AppCompatSpinner plotTypeSpinner;
    TextInputLayout inputStartLayout;
    TextInputLayout inputEndLayout;
    MyAnimationHandler startAH;
    MyAnimationHandler endAH;
    MyAnimationHandler varSpinnerAH;
    View view;

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
        Util.debug("onCreateView called");
        view = inflater.inflate(R.layout.analysis_fragment, container, false);

        setupToolbar(view);

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        User.getCurrentUser().updateUserData(user -> {
            initializeView();
            updateSpinners(getChartTypeSelectedInt());
            updatePlot();
        });

        return view;
    }

    public int getChartTypeSelectedInt() {
        return sharedPrefs.getInt("chartTypeSelectedInt", 0);
    }

    public int getVarSelectedInt() {
        return sharedPrefs.getInt("varSelectedInt", 0);
    }

    /**
     * Expand/collapse spinners based on chartTypeSelectedInt.
     */
    private void updateSpinners(int chartTypeSelectedInt) {
        ChartType chartTypeSelected = ChartType.values()[chartTypeSelectedInt];

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
    }

    /**
     * Create spinner with resource id and objects to populate dropdown.
     * @param spinner
     * @param labels
     * @return
     */
    public static PersistentSpinner setupSpinner(Context context, AppCompatSpinner spinner, List<String> labels, String key) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, labels);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        return new PersistentSpinner(context, spinner, key);
    }

    private void initializeView() {
        // Set up variable spinner
        varSpinner = getView().findViewById(R.id.inputVariableDropdown);
        PersistentSpinner varPS = setupSpinner(getContext(), varSpinner, Util.getVariableLabels(), "varSelectedInt");

        varSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                varPS.savePosition(i);

                Util.debug("variableSpinner onItemSelected() called");
                updatePlot();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Not implemented
            }
        });

        // Need to initialize animation handlers before plot type spinner.
        inputStartLayout = getView().findViewById(R.id.inputStartLayout);
        inputEndLayout = getView().findViewById(R.id.inputEndLayout);
        startAH = new MyAnimationHandler(inputStartLayout);
        endAH = new MyAnimationHandler(inputEndLayout);
        varSpinnerAH = new MyAnimationHandler(varSpinner);

        // Set up plot type spinner
        plotTypeSpinner = getView().findViewById(R.id.inputPlotTypeDropdown);
        PersistentSpinner plotTypePS = setupSpinner(getContext(), plotTypeSpinner, getChartTypeLabels(), "chartTypeSelectedInt");

        plotTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                plotTypePS.savePosition(i);
                updateSpinners(i);

                Util.debug("plotTypeSpinner onItemSelected() called");
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

        int varSelectedInt = getVarSelectedInt();
        String varName = User.getCurrentUser().getVarData().get(varSelectedInt).getName();

        int chartTypeSelectedInt = getChartTypeSelectedInt();
        ChartType chartTypeSelected = ChartType.values()[chartTypeSelectedInt];

        // There's definitely a nicer and safer way to do this.
        AnalysisChart analysisChart = null;
        try {
            analysisChart = chartTypeSelected.view.getDeclaredConstructor(Context.class).newInstance(getContext());
        } catch (IllegalAccessException | java.lang.InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

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

        analysisChart.updateChart();

        FrameLayout imageView = getView().findViewById(R.id.imageView);
        imageView.removeAllViews();
        imageView.addView((View) analysisChart);
    }
}
