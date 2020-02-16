package mud.arca.io.mud.DataRecordList.dummy;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import mud.arca.io.mud.DataStructures.Day;
import mud.arca.io.mud.DataStructures.Measurement;
import mud.arca.io.mud.DataStructures.User;
import mud.arca.io.mud.DataStructures.Util;

public class UserContent {
    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyContent.DummyItem> ITEMS = new ArrayList<DummyContent.DummyItem>();

    private User user;

    private static final int COUNT = 25;

    public UserContent(User user) {
        this.user = user;

        // add the DummyItems to ITEMS
        ArrayList<Day> dayData = user.getDayData();

        for (int i = dayData.size() - 1; i >= 0; i--) {
            Day d = dayData.get(i);
            DummyContent.DummyItem item = new DummyContent.DummyItem(d);
            ITEMS.add(item);
        }
    }

}
