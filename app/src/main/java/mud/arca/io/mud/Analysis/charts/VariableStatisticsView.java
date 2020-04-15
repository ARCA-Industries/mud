package mud.arca.io.mud.Analysis.charts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import mud.arca.io.mud.Analysis.AnalysisChart;
import mud.arca.io.mud.Analysis.ChartWithDates;
import mud.arca.io.mud.Analysis.ChartWithVariable;
import mud.arca.io.mud.DataStructures.Day;
import mud.arca.io.mud.DataStructures.Measurement;
import mud.arca.io.mud.DataStructures.User;
import mud.arca.io.mud.R;
import mud.arca.io.mud.Util.Util;

public class VariableStatisticsView extends RecyclerView
        implements AnalysisChart, ChartWithVariable, ChartWithDates {

    private String varName;
    private Date startDate;
    private Date endDate;

    /**
     * List of days between startDate and endDate inclusive.
     */
    public ArrayList<Day> daysSelected;

    /**
     * Measurements for the selected variable.
     */
    public ArrayList<Float> variableValues;

    /**
     * Sorted measurements for the selected variable.
     */
    public ArrayList<Float> sortedVariableValues;

    /**
     * The mean of the variableValues. Re-used to calculate standard deviation.
     */
    public float mean;

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public VariableStatisticsView(Context context) {
        super(context);
        init(null, 0);
    }

    public VariableStatisticsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public VariableStatisticsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

    }

    public void updateChart() {
        LayoutManager layoutManager = new LinearLayoutManager(getContext());
        setLayoutManager(layoutManager);

        initDaysSelected();
        initVariableValues();
        initSortedVariableValues();
        initMean();

        List<Statistic> statistics = new ArrayList<>();
        statistics.add(getNumDays());
        statistics.add(getDaysWithMeasurements());
        statistics.add(getDaysWithoutMeasurements());
        statistics.add(getMinimum());
        statistics.add(getMaximum());
        statistics.add(getMedian());
        statistics.add(getMean());
        statistics.add(getStandardDeviation());

        MyAdapter myAdapter = new MyAdapter(getContext(), statistics);
        setAdapter(myAdapter);
    }

    public void setDaysAndVariable(Collection<Day> days, String varName) {

    }


    /**
     * This function should be called once in updateChart() so that User.fetchDays is only called once.
     */
    public void initDaysSelected() {
        daysSelected = User.getCurrentUser().fetchDays(startDate, endDate);
    }

    public void initVariableValues() {
        variableValues = Util.getVariableValues(daysSelected, varName);
        //Util.debug("variableValues: " + variableValues);
    }

    public void initSortedVariableValues() {
        sortedVariableValues = (ArrayList<Float>) variableValues.clone();
        Collections.sort(sortedVariableValues);
    }

    public Statistic getMinimum() {
        float value;
        if (variableValues.size() == 0) {
            // Set value to NaN
            value = 0f / 0f;
        } else {
            value = Collections.min(variableValues);
        }
        return new Statistic("Minimum", value, false);
    }

    public Statistic getMaximum() {
        float value;
        if (variableValues.size() == 0) {
            // Set value to NaN
            value = 0f / 0f;
        } else {
            value = Collections.max(variableValues);
        }
        return new Statistic("Maximum", value, false);
    }

    public Statistic getMedian() {
        float value;
        int size = sortedVariableValues.size();
        if (size == 0) {
            // Set value to NaN
            value = 0f / 0f;
        } else if (size % 2 == 0) {
            value = (sortedVariableValues.get(size/2) + sortedVariableValues.get(size/2 - 1)) / 2;
        } else {
            value = sortedVariableValues.get(size/2);
        }
        return new Statistic("Median", value, false);
    }

    public Statistic getMean() {
        return new Statistic("Mean", mean, false);
    }

    public Statistic getStandardDeviation() {
        int size = variableValues.size();
        float value;
        if (size == 0) {
            // Set value to NaN
            value = 0f / 0f;
        } else {
            float sum = 0;
            for (float f : variableValues) {
                float diff = f - mean;
                sum += diff * diff;
            }
            value = (float) Math.sqrt(sum / size);
        }
        return new Statistic("Standard deviation", value, false);
    }

    public void initMean() {
        int size = variableValues.size();
        if (size == 0) {
            // Set value to NaN
            mean = 0f / 0f;
        } else {
            float sum = 0;
            for (float f : variableValues) {
                sum += f;
            }
            mean = sum / size;
        }
    }

    /**
     * Calculate the number of days between startDate and endDate (inclusive).
     * @return
     */
    public Statistic getNumDays() {
        //float value = (float) Util.daysBetween(startDate, endDate) + 1;
        float value = (float) daysSelected.size();
        return new Statistic("Number of days", value, true);
    }

    public Statistic getDaysWithMeasurements() {
        float value = (float) variableValues.size();
        return new Statistic("Days with measurements", value, true);
    }

    /**
     * Calculate the number of days between startDate and endDate (inclusive) that are
     * missing a Measurement for the selected variable.
     * @return
     */
    public Statistic getDaysWithoutMeasurements() {
//        int count = 0;
//        for (Day day : daysSelected) {
//            Collection<Measurement> measurements = day.getMeasurements();
//            try {
//                Measurement m = Measurement.searchList(measurements, varName);
//            } catch (NoSuchElementException e) {
//                count++;
//            }
//        }
        int count = daysSelected.size() - variableValues.size();
        return new Statistic("Days without measurements", (float) count, true);
    }

    public boolean isNaN(float value) {
        return value != value;
    }

    /**
     * Stores name of statistic (mean, median, etc.) and float value.
     */
    public class Statistic {
        private String name;
        private float value;
        private boolean isInteger;

        public Statistic(String name, float value, boolean isInteger) {
            this.name = name;
            this.value = value;
            this.isInteger = isInteger;
        }

        public String getName() {
            return name;
        }

        public String getFormattedValue() {
            if (isNaN(value)) {
                return "(no value)";
            } else if (isInteger) {
                return String.format("%d", Math.round(value));
            } else {
                return String.format("%.2f", value);
            }
        }
    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private List<Statistic> mDataset;

        private Context context;

        public MyAdapter(Context context, List<Statistic> data) {
            this.context = context;
            this.mDataset = data;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            //public String text;

            public MyViewHolder(View v) {
                super(v);
            }
        }

        // Create new views (invoked by the layout manager)
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View customView = inflater.inflate(R.layout.variable_statistics_row, parent, false);

            MyAdapter.MyViewHolder viewHolder = new MyAdapter.MyViewHolder(customView);

            return viewHolder;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(MyAdapter.MyViewHolder holder, int position) {
            TextView tv1 = holder.itemView.findViewById(R.id.textview1);
            tv1.setText(mDataset.get(position).getName());

            TextView tv2 = holder.itemView.findViewById(R.id.textview2);
            tv2.setText(mDataset.get(position).getFormattedValue());
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }
}
