package mud.arca.io.mud.DataRecordList.recorddetails.dummy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import mud.arca.io.mud.DataRecordList.DayListFragment;
import mud.arca.io.mud.DataStructures.Day;
import mud.arca.io.mud.DataStructures.Measurement;
import mud.arca.io.mud.DataStructures.User;
import mud.arca.io.mud.DataStructures.Variable;

public class VariableListContent2 {
    /**
     * An array of sample (dummy) items.
     */
    public static List<VariableListItem> ITEMS = new ArrayList<>();

    public static void loadItems(Day d) {
        ITEMS.clear();
        Collection<Measurement> measurements = d.getMeasurements();
        for (Variable v : User.getCurrentUser().getVarData()) {
            String varStr = String.format("%s (%s)", v.getName(), v.getUnit());

            // If there is no measurement found, valueStr is empty string.
            // The RecyclerView will show hint text.
            String valueStr = "";
            Measurement m = null;

            try {
                m = Measurement.searchList(measurements, v.getName());
                valueStr = m.getFormattedValue();
            } catch (NoSuchElementException e) {
                // do nothing
            }

            VariableListItem item =
                    new VariableListItem(varStr, valueStr, m, v, d);
            ITEMS.add(item);
        }
    }

    public static List<VariableListItem> getItems(Day d) {
        loadItems(d);
        return ITEMS;
    }

    /**
     * A dummy item representing a piece of type.
     */
    public static class VariableListItem {
        public final String type;
        public final String value;
        public Measurement measurement;
        public Variable variable;
        public Day day;

        public VariableListItem(String type, String value, Measurement measurement,
                                Variable variable, Day day) {
            this.type = type;
            this.value = value;
            this.measurement = measurement;
            this.variable = variable;
            this.day = day;
        }

        @Override
        public String toString() {
            return type;
        }
    }
}
