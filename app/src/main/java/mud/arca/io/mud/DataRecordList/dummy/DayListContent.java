package mud.arca.io.mud.DataRecordList.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mud.arca.io.mud.DataStructures.Day;
import mud.arca.io.mud.DataStructures.User;
import mud.arca.io.mud.DataStructures.Util;

/**
 * Helper class for providing sample type for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DayListContent {

    /**
     * An array of sample (dummy) items.
     */
    public List<DayListItem> ITEMS = new ArrayList<DayListItem>();

    private static final int COUNT = 30;

    private User user;

    public DayListContent(User user) {
        this.user = user;
        ArrayList<Day> dayData = user.getDayData();

        // add the DayListItems to ITEMS
        int bound = Math.min(COUNT - 1, dayData.size() - 1);
        for (int j = 0; j <= bound; j++) {
            Day d = dayData.get(dayData.size() - 1 - j);
            DayListItem item = new DayListItem(d);
            ITEMS.add(item);
        }
    }

    /**
     * A dummy item representing a piece of type.
     */
    public static class DayListItem {
        public final String dateStr;
        public final String moodStr;
        public final String varStr;
        public final Day day;


        public DayListItem(Day d) {
            this.day = d;
            this.dateStr = Util.formatDate(d.getDate());
            this.moodStr = d.getMoodString();
            this.varStr = d.getVarString("Sleep");
        }

        @Override
        public String toString() {
            return moodStr;
        }
    }
}
