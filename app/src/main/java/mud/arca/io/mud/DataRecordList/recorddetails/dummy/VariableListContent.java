package mud.arca.io.mud.DataRecordList.recorddetails.dummy;

import java.util.ArrayList;
import java.util.List;

import mud.arca.io.mud.DataStructures.Measurement;

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



    /**
     * A dummy item representing a piece of type.
     */
    public static class VariableListItem {
        public final String type;
        public final String value;
        public Measurement measurement;

        public VariableListItem(String type, String value, Measurement measurement) {
            this.type = type;
            this.value = value;
            this.measurement = measurement;
        }

        @Override
        public String toString() {
            return type;
        }
    }
}
