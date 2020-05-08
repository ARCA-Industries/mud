package mud.arca.io.mud.Analysis;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.Arrays;
import java.util.Date;
import java.time.DayOfWeek;
import java.util.List;

import mud.arca.io.mud.Util.Util;

/**
 * X-axis Value formatter.
 * It is used to convert the x-axis day of week values (stored as floats) to strings.
 */
public class DayOfWeekVF extends ValueFormatter {
    private final BarLineChartBase<?> chart;

    public DayOfWeekVF(BarLineChartBase<?> chart) {
        this.chart = chart;
    }

    List<String> dayShortNames = Arrays.asList(
            "0", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat");

    @Override
    public String getFormattedValue(float value) {
        return dayShortNames.get(Math.round(value));
    }
}