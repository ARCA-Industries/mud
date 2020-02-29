package mud.arca.io.mud.Analysis;

import java.util.Collection;

import mud.arca.io.mud.DataStructures.Day;

public interface AnalysisChart {
    // TODO: remove this function once YearSummaryView is updated
    void setDaysAndVariable(Collection<Day> days, String varName);

    /**
     * Update the chart based on internal fields like startDate, endDate, varName.
     */
    void updateChart();
}
