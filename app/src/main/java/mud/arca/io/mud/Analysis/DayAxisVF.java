package mud.arca.io.mud.Analysis;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.Date;

import mud.arca.io.mud.Analysis.charts.VariableVsTimeView;
import mud.arca.io.mud.Util.Util;

// Custom Value formatter.
// It is used to convert the x-axis date values (stored as floats) to strings.
public class DayAxisVF extends ValueFormatter {
    private final BarLineChartBase<?> chart;

    public DayAxisVF(BarLineChartBase<?> chart) {
        this.chart = chart;
    }

    /**
     * Value represents the number of days since the base date.
     * @return
     */
    @Override
    public String getFormattedValue(float value) {
        // For some reason you have to add 1 to value to get the correct axis label.
        // Maybe because of Daylight Saving Time?
        Date d = Util.floatToDate(VariableVsTimeView.getBaseDate(), value + 1);
        return Util.formatDate(d);
    }
}