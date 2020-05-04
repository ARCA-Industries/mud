package mud.arca.io.mud.Analysis.charts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import mud.arca.io.mud.Analysis.AnalysisChart;
import mud.arca.io.mud.DataStructures.Day;
import mud.arca.io.mud.DataStructures.User;
import mud.arca.io.mud.R;
import mud.arca.io.mud.Util.ColorGradientUtil;

/**
 * TODO: document your custom view class.
 */
public class YearSummaryView extends LinearLayout implements AnalysisChart {
    public static AnalysisChart newInstance(Context context) {
        return new YearSummaryView(context);
    }

    public YearSummaryView(Context context) {
        super(context);
        init(null, 0);
    }

    public YearSummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public YearSummaryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    ColorGradientUtil colorGradientUtil;

    RecyclerView recyclerView;

    private void init(AttributeSet attrs, int defStyle) {

        // Initialize a ColorGradientUtil
//        colorGradientUtil = new ColorGradientUtil(
//                Arrays.asList(0d, 10 / 3d, 10 * 2 / 3d, 10d),
//                Arrays.asList(
//                        R.color.gradient_moodindicator_0,
//                        R.color.gradient_moodindicator_33,
//                        R.color.gradient_moodindicator_66,
//                        R.color.gradient_moodindicator_100
//                ),
//                getResources()
//        );

        colorGradientUtil = new ColorGradientUtil(
            ColorGradientUtil.getFloatArray(getResources(), R.array.gradient_moodindicator_points),
                getResources().getIntArray(R.array.gradient_moodindicator_colors)
        );

        setOrientation(VERTICAL);

        LinearLayout weekdayHeader = new LinearLayout(getContext());

        String[] daysOfWeek = new String[]{"Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"};
        for (String s : daysOfWeek) {
            TextView textView = new TextView(getContext());
            textView.setText(s);
            textView.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            textView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            weekdayHeader.addView(textView);
        }

        addView(weekdayHeader);
        recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(recyclerView);

    }


    @SuppressLint("NewApi")
    public void setData(Collection<Day> days) {
        System.out.println("days = " + days);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));

        List<Day> dayList = new ArrayList<>(days);
        dayList.sort(Comparator.comparing(Day::getDate));


        // Insert any missing spaces. Ensure index 0 is a Sunday
        SimpleDateFormat dayOfWeek = new SimpleDateFormat("EEEE");
//        System.out.println("dayOfWeek.format(dayList.get(0)) = " + dayOfWeek.format(dayList.get(0).getDate()));

        // Add in blank days to the beginning; pad with empty so that Sunday is first
        while (!dayOfWeek.format(dayList.get(0).getDate()).equals("Sunday")) {
//            System.out.println("Adding: dayOfWeek.format(dayList.get(0)) = " + dayOfWeek.format(dayList.get(0).getDate()));
            dayList.add(0, new Day(Date.from(dayList.get(0).getDate().toInstant().minus(1, ChronoUnit.DAYS))));
        }

        // Pad any missing days with empty
        {
            int i = 0;
            while (i + 1 < dayList.size()) {
                while (Duration.between(dayList.get(i).getDate().toInstant(), dayList.get(i + 1).getDate().toInstant()).toDays() > 1) {
                    dayList.add(i + 1, new Day(Date.from(dayList.get(i).getDate().toInstant().plus(1, ChronoUnit.DAYS))));
                }
                i += 1;
            }
        }


        MyAdapter myAdapter = new MyAdapter(getContext(), dayList);
        recyclerView.setAdapter(myAdapter);

        // Scroll to end
        recyclerView.scrollToPosition(myAdapter.getItemCount() - 1);

//        System.out.println("getAdapter().getItemCount() = " + getAdapter().getItemCount());
    }

    @Override
    public void setDaysAndVariable(Collection<Day> days, String varName) {
        setData(days);
    }

    public void updateChart() {
        setData(User.getCurrentUser().getDayData());
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private Context context;

        private List<Day> userData;

        private Toast current_toast = null;

        public MyAdapter(Context context, List<Day> userData) {
            this.context = context;
            this.userData = userData;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public String text;

            public ViewHolder(View v) {
                super(v);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View customView = inflater.inflate(R.layout.yearsummary_day_layout, parent, false);

            ViewHolder viewHolder = new ViewHolder(customView);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            // ANYTHING HERE?

            int color;
            try {
                float mood = userData.get(position).getAverageMood();
                color = colorGradientUtil.getColorForPoint(mood);
//                ((TextView) holder.itemView.findViewById(R.id.textview)).setText(String.valueOf(mood));
            } catch (NoSuchElementException e) {
                color = getResources().getColor(R.color.gradient_moodindicator_blank, getResources().newTheme());
            }

            holder.itemView.findViewById(R.id.textview).setBackgroundColor(color);


            holder.itemView.setOnClickListener(view -> {
                if (current_toast != null) {
                    current_toast.cancel();
                }
                Toast t = Toast.makeText(context, SimpleDateFormat.getDateInstance().format(userData.get(position).getDate()), Toast.LENGTH_SHORT);
                t.setGravity(Gravity.TOP|Gravity.LEFT, holder.itemView.getLeft(), holder.itemView.getTop() + 3*holder.itemView.getHeight());
                // TODO: Need to center toast horizontally with view. Or just place it in the standard position at the bottom.
                t.show();
                current_toast = t;
            });

        }

        @Override
        public int getItemCount() {
            return userData.size();
        }

    }

}
