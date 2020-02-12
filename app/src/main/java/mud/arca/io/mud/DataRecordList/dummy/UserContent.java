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
            String dateStr = Util.formatDate(d.getDate());

            String moodStr = "-";
            try {
                double avgMood = d.getAverageMood();
                moodStr = String.format("%.1f", avgMood);
            } catch (NoSuchElementException e) {
                // do nothing
            }

            String sleepStr = "-";
            try {
                Measurement m = Measurement.searchList(d.getMeasurements(), "Sleep");
                sleepStr = String.format("%.1f", m.getValue());
            } catch (NoSuchElementException e) {
                // do nothing
            }

            DummyContent.DummyItem item = new DummyContent.DummyItem(dateStr, moodStr, sleepStr);
            ITEMS.add(item);
        }
    }

}
