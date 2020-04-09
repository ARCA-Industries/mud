package mud.arca.io.mud.Analysis.charts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
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

    /**
     * Number of milliseconds in one day.
     */
    public static final int MS_PER_DAY = 1000 * 60 * 60 * 24;

    private String varName;
    private Date startDate;
    private Date endDate;

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

        List<Statistic> statistics = new ArrayList<>();
        statistics.add(new Statistic("Mean", 5f, false));
        statistics.add(getNumDays());
        statistics.add(getNumMissingValues());
        MyAdapter myAdapter = new MyAdapter(getContext(), statistics);
        setAdapter(myAdapter);

    }

    public void setDaysAndVariable(Collection<Day> days, String varName) {

    }

    /**
     * Calculate the number of days between startDate and endDate (inclusive).
     * @return
     */
    public Statistic getNumDays() {
        float value = (float) Util.daysBetween(startDate, endDate) + 1;
        return new Statistic("Number of days", value, true);
    }

    /**
     * Calculate the number of days between startDate and endDate (inclusive) that are
     * missing a Measurement for the selected variable.
     * @return
     */
    public Statistic getNumMissingValues() {
        int count = 0;
        for (Day day : User.getCurrentUser().fetchDays(startDate, endDate)) {
            Collection<Measurement> measurements = day.getMeasurements();
            try {
                Measurement m = Measurement.searchList(measurements, varName);
            } catch (NoSuchElementException e) {
                count++;
            }
        }

        return new Statistic("Days with missing values", (float) count, true);
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
            if (isInteger) {
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
