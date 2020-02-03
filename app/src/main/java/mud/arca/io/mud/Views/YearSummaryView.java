package mud.arca.io.mud.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import mud.arca.io.mud.DataStructures.Day;
import mud.arca.io.mud.R;

/**
 * TODO: document your custom view class.
 */
public class YearSummaryView extends RecyclerView {
    private String mExampleString = "null"; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;

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
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.YearSummaryView, defStyle, 0);

//        mExampleString = a.getString(
//                R.styleable.YearSummaryView_exampleString);
        mExampleColor = a.getColor(
                R.styleable.YearSummaryView_exampleColor,
                mExampleColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(
                R.styleable.YearSummaryView_exampleDimension,
                mExampleDimension);

        if (a.hasValue(R.styleable.YearSummaryView_exampleDrawable)) {
            mExampleDrawable = a.getDrawable(
                    R.styleable.YearSummaryView_exampleDrawable);
            mExampleDrawable.setCallback(this);
        }

        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mExampleDimension);
        mTextPaint.setColor(mExampleColor);
        mTextWidth = mTextPaint.measureText(mExampleString);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }


    public void setData(Collection<Day> days) {
        System.out.println("days = " + days);

//        Collections.sort(days, Comparator);

        mExampleString = "BEGIN:";

        for (Day day : days) {
            mExampleString += day.getAverageMood() + ", ";
        }


//        setLayoutManager(new LinearLayoutManager(getContext()));

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        layoutManager.setReverseLayout(true);

        setLayoutManager(layoutManager);

        MyAdapter myAdapter = new MyAdapter(getContext(), new ArrayList<>(days));
        setAdapter(myAdapter);


        System.out.println("getAdapter().getItemCount() = " + getAdapter().getItemCount());

        invalidateTextPaintAndMeasurements();
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

            customView.setPadding(12, 12, 12, 12);

            ViewHolder viewHolder = new ViewHolder(customView);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            // ANYTHING HERE?

            double mood = userData.get(position).getAverageMood();

            ((TextView) holder.itemView.findViewById(R.id.textview)).setText(String.valueOf(mood));

            int color = Color.rgb((int) (255 * (mood / 10)), 0, 0);

            ((TextView) holder.itemView.findViewById(R.id.textview)).setBackgroundColor(color);


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
