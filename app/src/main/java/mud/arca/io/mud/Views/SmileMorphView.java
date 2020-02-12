package mud.arca.io.mud.Views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import mud.arca.io.mud.Analysis.AnalysisChart;
import mud.arca.io.mud.DataStructures.Day;
import mud.arca.io.mud.R;

public class SmileMorphView extends ImageView {
    private String mExampleString = "null"; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;

//    public static ImageViewCompat newInstance(Context context) {
//        return new SmileMorphView(context);
//    }

    public SmileMorphView(Context context) {
        super(context);
        init(null, 0);
    }

    public SmileMorphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SmileMorphView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

    }
//        // Load attributes
//        final TypedArray a = getContext().obtainStyledAttributes(
//                attrs, R.styleable.YearSummaryView, defStyle, 0);
//
////        mExampleString = a.getString(
////                R.styleable.YearSummaryView_exampleString);
//        mExampleColor = a.getColor(
//                R.styleable.YearSummaryView_exampleColor,
//                mExampleColor);
//        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
//        // values that should fall on pixel boundaries.
//        mExampleDimension = a.getDimension(
//                R.styleable.YearSummaryView_exampleDimension,
//                mExampleDimension);
//
//        if (a.hasValue(R.styleable.YearSummaryView_exampleDrawable)) {
//            mExampleDrawable = a.getDrawable(
//                    R.styleable.YearSummaryView_exampleDrawable);
//            mExampleDrawable.setCallback(this);
//        }
//
//        a.recycle();
//
//        // Set up a default TextPaint object
//        mTextPaint = new TextPaint();
//        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
//        mTextPaint.setTextAlign(Paint.Align.LEFT);
//
//
//
//
//
//
//
//
//
//        // Update TextPaint and text measurements from attributes
//        invalidateTextPaintAndMeasurements();
//    }



    public void setProgress(int i) {
        // i is [0,100]
        Drawable d = getDrawable();

        AnimatedVectorDrawable anim = (AnimatedVectorDrawable) d;

        anim.start();

    }

}
