package mud.arca.io.mud.DataRecordList.recorddetails.dummy;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing sample type for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class VariableListContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<VariableListItem> ITEMS = new ArrayList<VariableListItem>();


    private static final int COUNT = 25;

    static {
        // Add some sample items.
//        for (int i = 1; i <= COUNT; i++) {
//            addItem(createDummyItem(makeType(i), makevalue(i)));
//        }

        addItem(createDummyItem(VariableType.SLEEP, makevalue(0)));
        addItem(createDummyItem(VariableType.MEALS, makevalue(1)));
        addItem(createDummyItem(VariableType.EXERCISED, makevalue(1)));
    }

    private static void addItem(VariableListItem item) {
        ITEMS.add(item);
    }

    public enum VariableType {
        SLEEP("Sleep (hr)"),
        MEALS("Number of Meals"),
        EXERCISED("Exercised");

        String title;
        VariableType(String title) {
            this.title = title;
        }
    }

    private static VariableListItem createDummyItem(VariableType variableType, String value) {
        return new VariableListItem(variableType.title, value);
    }

    private static VariableType makeType(int position) {
        if (position == 0) {
            return VariableType.SLEEP;
        }
        return null;
    }

    private static String makevalue(int position) {
        return String.valueOf((position % 3 + 6));
    }

    /**
     * A dummy item representing a piece of type.
     */
    public static class VariableListItem {
        public final String type;
        public final String value;

        public VariableListItem(String type, String value) {
            this.type = type;
            this.value = value;
        }

        @Override
        public String toString() {
            return type;
        }
    }
}
