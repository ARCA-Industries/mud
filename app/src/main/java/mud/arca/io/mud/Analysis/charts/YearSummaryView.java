package mud.arca.io.mud.Analysis.charts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import mud.arca.io.mud.R;

/**
 * TODO: document your custom view class.
 */
public class YearSummaryView extends RecyclerView implements AnalysisChart {
    private String mExampleString = "null"; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;

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

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes


    }

    private void invalidateTextPaintAndMeasurements() {
//        mTextPaint.setTextSize(mExampleDimension);
//        mTextPaint.setColor(mExampleColor);
//        mTextWidth = mTextPaint.measureText(mExampleString);
//
//        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
//        mTextHeight = fontMetrics.bottom;
    }


    @SuppressLint("NewApi")
    public void setData(Collection<Day> days) {
        System.out.println("days = " + days);

//
//        mExampleString = "BEGIN:";
//
//        for (Day day : days) {
//            mExampleString += day.getAverageMood() + ", ";
//        }


//        setLayoutManager(new LinearLayoutManager(getContext()));

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
//        layoutManager.setReverseLayout(true);

        // TODO: Reverse layout is doing weird things. Horizontal order is good but vertical weeks are bad.

        setLayoutManager(layoutManager);


//        Collections.sort(days, Comparator.comparing(o -> ((Day) o).getDate().getTime()));

        List<Day> dayList = new ArrayList<>(days);

        dayList.sort(Comparator.comparing(Day::getDate));


        // Insert any missing spaces. Ensure index 0 is a Sunday
//
//        dayList.get(0).getDate().toInstant().atZone(ZoneId.of("UTC");
//
//        Instant.now().atZone(ZoneId.systemDefault()).getDayOfWeek();


        SimpleDateFormat dayOfWeek = new SimpleDateFormat("EEEE");
//        Date d = new Date();
//        String dayOfTheWeek = dayOfWeek.format(d);

//        while (dayList.get(0).getDate().toInstant().atZone(ZoneId.of("UTC")).getDayOfWeek() != DayOfWeek.SUNDAY) {
        System.out.println("dayOfWeek.format(dayList.get(0)) = " + dayOfWeek.format(dayList.get(0).getDate()));

        // Add in blank days to the beginning; pad with empty so that Sunday is first
        while (!dayOfWeek.format(dayList.get(0).getDate()).equals("Sunday")) {
            System.out.println("Adding: dayOfWeek.format(dayList.get(0)) = " + dayOfWeek.format(dayList.get(0).getDate()));

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
        setAdapter(myAdapter);

        // Scroll to end
        scrollToPosition(myAdapter.getItemCount() - 1);

        System.out.println("getAdapter().getItemCount() = " + getAdapter().getItemCount());

        invalidateTextPaintAndMeasurements();
    }

    @Override
    public void setDaysAndVariable(Collection<Day> days, String varName) {
        setData(days);
    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private Context context;

        private List<Day> userData;

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

//            customView.setPadding(12, 12, 12, 12);

            ViewHolder viewHolder = new ViewHolder(customView);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            // ANYTHING HERE?

            int color;
            try {
                double mood = userData.get(position).getAverageMood();
                color = Color.rgb((int) (255 * (mood / 10)), 0, 0);
                ((TextView) holder.itemView.findViewById(R.id.textview)).setText(String.valueOf(mood));
            } catch (NoSuchElementException e) {
                color = Color.BLUE;
            }

            holder.itemView.findViewById(R.id.textview).setBackgroundColor(color);


            // For debugging
            holder.itemView.setOnClickListener(view -> {
                System.out.println("date = " + userData.get(position).getDate());
            });


        }

        @Override
        public int getItemCount() {
            return userData.size();
        }

    }


    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getExampleString() {
        return mExampleString;
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param exampleString The example string attribute value to use.
     */
    public void setExampleString(String exampleString) {
//        mExampleString = exampleString;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getExampleColor() {
        return mExampleColor;
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param exampleColor The example color attribute value to use.
     */
    public void setExampleColor(int exampleColor) {
        mExampleColor = exampleColor;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    public float getExampleDimension() {
        return mExampleDimension;
    }

    /**
     * Sets the view's example dimension attribute value. In the example view, this dimension
     * is the font size.
     *
     * @param exampleDimension The example dimension attribute value to use.
     */
    public void setExampleDimension(float exampleDimension) {
        mExampleDimension = exampleDimension;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example drawable attribute value.
     *
     * @return The example drawable attribute value.
     */
    public Drawable getExampleDrawable() {
        return mExampleDrawable;
    }

    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param exampleDrawable The example drawable attribute value to use.
     */
    public void setExampleDrawable(Drawable exampleDrawable) {
        mExampleDrawable = exampleDrawable;
    }
}
