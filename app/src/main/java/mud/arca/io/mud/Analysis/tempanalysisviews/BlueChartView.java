package mud.arca.io.mud.Analysis.tempanalysisviews;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

public class BlueChartView extends DummyChartView {

    public static final int COLOR = Color.BLUE;

    public BlueChartView(Context context) {
        super(context, COLOR);
    }

    public BlueChartView(Context context, AttributeSet attrs) {
        super(context, attrs, COLOR);
    }

    public BlueChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle, COLOR);
    }
}
