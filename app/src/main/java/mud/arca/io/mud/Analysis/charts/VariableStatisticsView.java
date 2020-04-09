package mud.arca.io.mud.Analysis.charts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import mud.arca.io.mud.Analysis.AnalysisChart;
import mud.arca.io.mud.DataStructures.Day;
import mud.arca.io.mud.R;

public class VariableStatisticsView extends RecyclerView implements AnalysisChart {
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

        List<String> data = Arrays.asList("foo", "bar");
        MyAdapter myAdapter = new MyAdapter(getContext(), data);
        setAdapter(myAdapter);

    }

    public void setDaysAndVariable(Collection<Day> days, String varName) {

    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private List<String> mDataset;

        private Context context;

        public MyAdapter(Context context, List<String> data) {
            this.context = context;
            this.mDataset = data;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public String text;

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
            tv1.setText(mDataset.get(position));

            TextView tv2 = holder.itemView.findViewById(R.id.textview2);
            tv2.setText(mDataset.get(position));
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }
}
