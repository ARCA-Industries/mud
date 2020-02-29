package mud.arca.io.mud.Analysis;

import java.util.Date;

/**
 * A chart that has a startDate and endDate as parameters.
 */
public interface ChartWithDates {
    void setStartDate(Date startDate);
    void setEndDate(Date endDate);
}
