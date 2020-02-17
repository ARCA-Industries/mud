package mud.arca.io.mud.Analysis.tempanalysisviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import java.util.Collection;

import mud.arca.io.mud.Analysis.AnalysisChart;
import mud.arca.io.mud.DataStructures.Day;

public abstract class DummyChartView extends View implements AnalysisChart {

    public DummyChartView(Context context, int color) {
        super(context);
        setBackgroundColor(color);
    }

    public DummyChartView(Context context, AttributeSet attrs, int color) {
        super(context, attrs);
        setBackgroundColor(color);
    }

    public DummyChartView(Context context, AttributeSet attrs, int defStyle, int color) {
        super(context, attrs, defStyle);
        setBackgroundColor(color);
    }

    @Override
    public void setDays(Collection<Day> days) {

    }
}
