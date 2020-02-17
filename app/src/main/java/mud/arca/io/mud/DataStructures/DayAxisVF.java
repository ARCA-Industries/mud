package mud.arca.io.mud.DataStructures;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.Date;

import mud.arca.io.mud.Analysis.charts.VariableVsTimeView;

// Custom Value formatter.
// It is used to convert the x-axis date values (stored as floats) to strings.
public class DayAxisVF extends ValueFormatter {
    private final BarLineChartBase<?> chart;

    public DayAxisVF(BarLineChartBase<?> chart) {
        this.chart = chart;
    }

    @Override
    public String getFormattedValue(float value) {
        Date d = Util.floatToDate(VariableVsTimeView.getBaseDate(), value);
        return Util.formatDate(d);
    }
}