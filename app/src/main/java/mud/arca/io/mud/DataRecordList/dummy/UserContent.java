package mud.arca.io.mud.DataRecordList.dummy;

import java.util.ArrayList;
import java.util.List;

import mud.arca.io.mud.DataStructures.Day;
import mud.arca.io.mud.DataStructures.User;

public class UserContent {
    /**
     * An array of sample (dummy) items.
     */
    public static final List<DayListContent.DayListItem> ITEMS = new ArrayList<DayListContent.DayListItem>();

    private User user;

    private static final int COUNT = 30;

    public UserContent(User user) {
        this.user = user;
        ArrayList<Day> dayData = user.getDayData();

        // add the DayListItems to ITEMS
        int bound = Math.min(COUNT - 1, dayData.size() - 1);
        for (int j = 0; j <= bound; j++) {
            Day d = dayData.get(dayData.size() - 1 - j);
            DayListContent.DayListItem item = new DayListContent.DayListItem(d);
            ITEMS.add(item);
        }
    }

}
