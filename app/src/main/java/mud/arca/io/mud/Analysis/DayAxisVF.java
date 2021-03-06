package mud.arca.io.mud.Analysis;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.Date;

import mud.arca.io.mud.Analysis.charts.VariableVsTimeView;
import mud.arca.io.mud.Util.Util;

/**
 * X-axis Value formatter.
 * It is used to convert the x-axis date values (stored as floats) to strings.
 */
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
        Date d = Util.floatToDate(value);
        return Util.formatDate(d);
    }
}