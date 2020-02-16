package mud.arca.io.mud.DataRecordList.dummy;

import java.util.ArrayList;
import java.util.List;

import mud.arca.io.mud.DataStructures.Day;
import mud.arca.io.mud.DataStructures.User;

public class UserContent {
    /**
     * An array of sample (dummy) items.
     */
    public static final List<DayListContent.DummyItem> ITEMS = new ArrayList<DayListContent.DummyItem>();

    private User user;

    private static final int COUNT = 25;

    public UserContent(User user) {
        this.user = user;

        // add the DummyItems to ITEMS
        ArrayList<Day> dayData = user.getDayData();

        for (int i = dayData.size() - 1; i >= 0; i--) {
            Day d = dayData.get(i);
            DayListContent.DummyItem item = new DayListContent.DummyItem(d);
            ITEMS.add(item);
        }
    }

}
