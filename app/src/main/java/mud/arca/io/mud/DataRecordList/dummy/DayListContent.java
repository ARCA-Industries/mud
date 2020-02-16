package mud.arca.io.mud.DataRecordList.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mud.arca.io.mud.DataStructures.Day;
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
    public static final List<DayListItem> ITEMS = new ArrayList<DayListItem>();

//    /**
//     * A map of sample (dummy) items, by ID.
//     */
//    // Is the HashMap necessary?
//    public static final Map<String, DayListItem> ITEM_MAP = new HashMap<String, DayListItem>();
//
//    private static void addItem(DayListItem item) {
//        ITEMS.add(item);
//        ITEM_MAP.put(item.id, item);
//    }

    private static final int COUNT = 25;



    /**
     * A dummy item representing a piece of type.
     */
    public static class DayListItem {
        public final String dateStr;
        public final String moodStr;
        public final String varStr;
        public final Day day;

//        public DayListItem(String id, String content, String details) {
//            this.id = id;
//            this.content = content;
//            this.details = details;
//        }

        public DayListItem(Day d) {
            this.day = d;
            String dateStr = Util.formatDate(d.getDate());
            this.dateStr = dateStr;
            this.moodStr = d.getMoodString();
            this.varStr = d.getVarString("Sleep");
        }

        @Override
        public String toString() {
            return moodStr;
        }
    }
}
