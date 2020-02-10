package mud.arca.io.mud.Analysis.tempanalysisviews;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

public class RedChartView extends DummyChartView {

    public static final int COLOR = Color.RED;

    public RedChartView(Context context) {
        super(context, COLOR);
    }

    public RedChartView(Context context, AttributeSet attrs) {
        super(context, attrs, COLOR);
    }

    public RedChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle, COLOR);
    }
}
