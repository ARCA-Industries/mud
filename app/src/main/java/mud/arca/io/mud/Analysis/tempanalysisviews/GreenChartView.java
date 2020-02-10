package mud.arca.io.mud.Analysis.tempanalysisviews;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

public class GreenChartView extends DummyChartView {

    public static final int COLOR = Color.GREEN;

    public GreenChartView(Context context) {
        super(context, COLOR);
    }

    public GreenChartView(Context context, AttributeSet attrs) {
        super(context, attrs, COLOR);
    }

    public GreenChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle, COLOR);
    }
}
