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
    public static List<VariableListContent.VariableListItem> ITEMS = new ArrayList<>();

    private static final int COUNT = 25;

    public static void loadItems() {
        ITEMS.clear();
        Day d = DayListFragment.daySelected;
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

            VariableListContent.VariableListItem item =
                    new VariableListContent.VariableListItem(varStr, valueStr, m, v, d);
            ITEMS.add(item);
        }
    }

    public static List<VariableListContent.VariableListItem> getItems() {
        loadItems();
        return ITEMS;
    }
}
