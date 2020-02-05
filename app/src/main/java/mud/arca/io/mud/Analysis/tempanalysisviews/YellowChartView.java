package mud.arca.io.mud.Analysis.tempanalysisviews;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

public class YellowChartView extends DummyChartView {

    public static final int COLOR = Color.YELLOW;

    public YellowChartView(Context context) {
        super(context, COLOR);
    }

    public YellowChartView(Context context, AttributeSet attrs) {
        super(context, attrs, COLOR);
    }

    public YellowChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle, COLOR);
    }
}
