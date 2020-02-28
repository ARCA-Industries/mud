package mud.arca.io.mud.Analysis;

import java.util.Collection;

import mud.arca.io.mud.DataStructures.Day;

public interface AnalysisChart {

    void setDaysAndVariable(Collection<Day> days, String varName);
    void updateChart();

}
