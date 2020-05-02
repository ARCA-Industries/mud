package mud.arca.io.mud.Analysis;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.Chart;
import com.google.android.material.textfield.TextInputLayout;

import androidx.preference.PreferenceManager;
import mud.arca.io.mud.Analysis.charts.MoodVsTimeView;
import mud.arca.io.mud.Analysis.charts.MoodVsVariableView;
import mud.arca.io.mud.Analysis.charts.VariableImportancesChart;
import mud.arca.io.mud.Analysis.charts.VariableStatisticsView;
import mud.arca.io.mud.Analysis.charts.VariableVsTimeView;
import mud.arca.io.mud.Analysis.charts.YearSummaryView;
import mud.arca.io.mud.BuildConfig;
import mud.arca.io.mud.DataStructures.Variable;
import mud.arca.io.mud.Util.MyAnimationHandler;
import mud.arca.io.mud.DataStructures.User;
import mud.arca.io.mud.Util.Util;
import mud.arca.io.mud.R;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class AnalysisFragment extends Fragment {

    private enum ChartType {
        VARIABLE_VS_TIME_CHART(VariableVsTimeView.class, "Variable vs Time"),
        // MOOD_VS_TIME_CHART(MoodVsTimeView.class, "Mood vs Time"),
        MOOD_VS_VARIABLE_CHART(MoodVsVariableView.class, "Mood vs Variable"),
        YEAR_SUMMARY_CHART(YearSummaryView.class, "Year Summary"),
        VARIABLE_STATISTICS(VariableStatisticsView.class, "Variable Statistics"),
        VARIABLE_IMPORTANCES(VariableImportancesChart.class, "Insights"),
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
     * when clicked.
     */
    public class DateSelector {
        public EditText et;
        public boolean isStartDate;

        public Date getDate() {
            if (isStartDate) {
                return startDate;
            } else {
                return endDate;
            }
        }

        /**
         * Get the key used to save and load the date string from sharedPrefs.
         * @return
         */
        public String getKey() {
            if (isStartDate) {
                return "AnalysisStartDate";
            } else {
                return "AnalysisEndDate";
            }
        }

        public void setDate(Date dateSelected) {
            // Set text of EditText
            String dateString = Util.formatDateWithYear(dateSelected);
            et.setText(dateString);

            // Save to field in AnalysisFragment and sharedPrefs
            if (isStartDate) {
                startDate = dateSelected;
            } else {
                endDate = dateSelected;
            }
            saveString(getKey(), dateString);
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
                    cal.setTime(getDate());
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
            Date newDate;
            String dateString = sharedPrefs.getString(getKey(), "");
            if (dateString.equals("")) {
                newDate = getDate();
            } else {
                newDate = Util.parseDateWithYear(dateString);
            }
            setDate(newDate);
        }
    }

    /**
     * Save the String s to sharedPrefs.
     * @param key
     * @param s
     */
    public void saveString(String key, String s) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(key, s);
        editor.commit();
    }

    public interface ToolbarItem {
        public String getText();
        public void onClick();
    }

    /**
     * This class is used to generate dropdown items where the user can select last 7 days, last 30 days, etc.
     */
    public class ItemToSelectDays implements ToolbarItem {
        private int numDays;

        public ItemToSelectDays(int numDays) {
            this.numDays = numDays;
        }

        public String getText() {
            return String.format("Select last %d days", numDays);
        }

        public void onClick() {
            // Add (-numDays+1) Because if you set it to 6 days ago, then the range is 7 days.
            Date newStartDate = Util.intToDate(latestDate, -numDays+1);

            // Make sure new start date is not before the user's earliest day.
            if (newStartDate.before(earliestDate)) {
                newStartDate = earliestDate;
            }

            startDS.setDate(newStartDate);
            endDS.setDate(latestDate);
            updatePlot();
        }
    }


    /**
     * Saves the image as PNG to the app's cache directory.
     * @param image Bitmap to save.
     * @return Uri of the saved file or null
     */
    public Uri saveImage(Bitmap image) {
        // TODO - Should be processed in another thread
        File imagesFolder = new File(getContext().getCacheDir(), "images");
        Uri uri = null;
        try {
            imagesFolder.mkdirs();
            File file = new File(imagesFolder, "chart.png");

            FileOutputStream stream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.flush();
            stream.close();
            uri = FileProvider.getUriForFile(Objects.requireNonNull(getContext()),
                    BuildConfig.APPLICATION_ID + ".provider", file);

        } catch (IOException e) {
            Log.d("TAG12", "IOException while trying to write file for sharing: " + e.getMessage());
        }
        return uri;
    }

    public class ShareChartItem implements ToolbarItem {
        public String getText() {
            return "Share chart";
        }

        public void onClick() {
//            String text = null;
//            if (analysisChart instanceof ShareableChart) {
//                ShareableChart sc = (ShareableChart) analysisChart;
//                text = sc.getShareChartString();
//            }
//
//            Intent sendIntent = new Intent();
//            sendIntent.setAction(Intent.ACTION_SEND);
//            sendIntent.putExtra(Intent.EXTRA_TEXT, text);
//            sendIntent.setType("text/plain");
//
//            Intent shareIntent = Intent.createChooser(sendIntent, null);
//            startActivity(shareIntent);
//
//            Util.debug("Sharing text: " + text);


            if (analysisChart instanceof Chart) {
                Chart chart = (Chart) analysisChart;
                Uri uri = saveImage(chart.getChartBitmap());

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                shareIntent.setType("image/png");
                startActivity(Intent.createChooser(shareIntent, "Send to"));
            }
        }
    }

    public List<ToolbarItem> getMenuDropdownItems() {
        List<ToolbarItem> items = new ArrayList<>();
        if (ShareableChart.class.isAssignableFrom(getChartTypeSelected().view)) {
            items.add(new ShareChartItem());
        }

        int[] numDaysList = new int[]{7, 30, 100};
        for (int i : numDaysList) {
            items.add(new ItemToSelectDays(i));
        }
        return items;
    }

    /**
     * This function must be called every time the chart type is updated.
     */
    public void updateToolbarItems(View rootView) {
        List<ToolbarItem> menuDropdownItems = getMenuDropdownItems();
        toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_analysis));
        toolbar.setOnMenuItemClickListener(item -> {
            // Note: This assumes that all menu items in the toolbar are from menuDropdownItems.
            //       If this is no longer the case, we'll need to check IDs first.
            menuDropdownItems.get(item.getItemId()).onClick();
            return true;
        });

        // Inflate menu
        toolbar.getMenu().clear();
        for (int id = 0; id < menuDropdownItems.size(); id++) {
            ToolbarItem item = menuDropdownItems.get(id);
            toolbar.getMenu().add(Menu.NONE, id, id, item.getText());
        }

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
    Toolbar toolbar;
    View view;
    AnalysisChart analysisChart;

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
        //Util.debug("onCreateView called");
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        view = inflater.inflate(R.layout.analysis_fragment, container, false);
        updateToolbarItems(view);

        User.getCurrentUser().updateUserData(user -> {
            initializeView(view);
            updateSpinners(getChartTypeSelectedInt());
            updatePlot();
        });

        return view;
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
        setSpinnerLabels(context, spinner, labels);
        return new PersistentSpinner(context, spinner, key);
    }

    /**
     * Set the items in the dropdown of the spinner.
     * @param context
     * @param spinner
     * @param labels
     */
    public static void setSpinnerLabels(Context context, AppCompatSpinner spinner, List<String> labels) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, labels);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    public List<String> getVariableLabelsWithMood() {
        List<String> ret = Util.getVariableLabels();
        ret.add("Mood");
        return ret;
    }

    public List<ChartType> chartsWithMoodPseudoVariable = Arrays.asList(
            ChartType.VARIABLE_VS_TIME_CHART,
            ChartType.VARIABLE_STATISTICS
    );

    /**
     * Return the name of the variable that is selected in the variable dropdown.
     * Handles a special case for "Mood" pseudo-variable.
     * @return
     */
    public String getSelectedVarName() {
        ArrayList<Variable> varData = User.getCurrentUser().getVarData();
        if (varData.size() == getVarSelectedInt()) {
            return Util.MOOD_STRING;
        } else {
            return varData.get(getVarSelectedInt()).getName();
        }
    }

    public int getChartTypeSelectedInt() {
        return sharedPrefs.getInt("chartTypeSelectedInt", 0);
    }

    public int getVarSelectedInt() {
        return sharedPrefs.getInt("varSelectedInt", 0);
    }

    public ChartType getChartTypeSelected() {
        return ChartType.values()[getChartTypeSelectedInt()];
    }

    /**
     * Get the correct variable labels.
     * The labels contain "Mood" or not depending on the chartType.
     * @return
     */
    public List<String> getVariableLabels() {
        if (chartsWithMoodPseudoVariable.contains(getChartTypeSelected())) {
            return getVariableLabelsWithMood();
        } else {
            return Util.getVariableLabels();
        }
    }

    private void initializeView(View rootView) {
        Util.debug("^^^^initializeView called");

        // Set up variable spinner
        varSpinner = getView().findViewById(R.id.inputVariableDropdown);
        PersistentSpinner varPS = setupSpinner(getContext(), varSpinner, getVariableLabels(), "varSelectedInt");

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

                ChartType chartTypeSelected = getChartTypeSelected();
                // If the user just switched to a chart that has a variable dropdown, we must update
                // the dropdown items.
                if (ChartWithVariable.class.isAssignableFrom(chartTypeSelected.view)) {
                    // Update the dropdown items.
                    setSpinnerLabels(getContext(), varSpinner, getVariableLabels());
                    // After updating the dropdown items, we must set the selection of varSpinner again.
                    // If we are switching to a chart type without the Mood pseudo-variable
                    // and Mood is selected, then reset the index to 0.
                    int varSelectedIndex = getVarSelectedInt();
                    if (!chartsWithMoodPseudoVariable.contains(chartTypeSelected) &&
                            getVarSelectedInt() == Util.getVariableLabels().size()) {
                        varSelectedIndex = 0;
                    }
                    varSpinner.setSelection(varSelectedIndex);
                }

                updateToolbarItems(rootView);

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
        endDate = getDefaultEndDate();
        startDate = getDefaultStartDate();

        // Initialize DateSelectors
        EditText startET = getView().findViewById(R.id.inputStartEditText);
        startDS = new DateSelector(getView(), startET, true);
        EditText endET = getView().findViewById(R.id.inputEndEditText);
        endDS = new DateSelector(getView(), endET, false);
    }

    public Date getDefaultEndDate() {
        return latestDate;
    }

    public Date getDefaultStartDate() {
        // Initialize start date to 29 days before end date (So that the range is 30 days)
        return Util.intToDate(latestDate, -30+1);
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

        String varName = getSelectedVarName();
        ChartType chartTypeSelected = getChartTypeSelected();

        // There's definitely a nicer and safer way to do this.
        //AnalysisChart analysisChart = null;
        analysisChart = null;
        try {
            analysisChart = chartTypeSelected.view.getDeclaredConstructor(Context.class).newInstance(getContext());
        } catch (IllegalAccessException | java.lang.InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

        // If the chart type selected extends ChartWithDates, set start and end dates.
        if (ChartWithDates.class.isAssignableFrom(chartTypeSelected.view)) {
            ChartWithDates cwd = (ChartWithDates) analysisChart;
            cwd.setStartDate(startDate);
            cwd.setEndDate(endDate);
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