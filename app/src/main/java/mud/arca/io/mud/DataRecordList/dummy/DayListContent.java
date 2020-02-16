package mud.arca.io.mud.DataRecordList.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import mud.arca.io.mud.DataStructures.Day;
import mud.arca.io.mud.DataStructures.Measurement;
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
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    // Is the HashMap necessary?
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static final int COUNT = 25;

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of type.
     */
    public static class DummyItem {
        public final String id;
        public final String content;
        public final String details;
        public final Day day;

//        public DummyItem(String id, String content, String details) {
//            this.id = id;
//            this.content = content;
//            this.details = details;
//        }

        public DummyItem(Day d) {
            this.day = d;
            String dateStr = Util.formatDate(d.getDate());
            this.id = dateStr;
            this.content = d.getMoodString();
            this.details = d.getVarString("Sleep");
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
