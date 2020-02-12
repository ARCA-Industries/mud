package mud.arca.io.mud.DataRecordList.dummy;

import java.util.ArrayList;
import java.util.List;

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
        // TODO: handle missing values for mood or sleep. Currently a day without Sleep recording won't show up in the list.
        ArrayList<Day> dayData = user.getDayData();

        for (int i = dayData.size() - 1; i >= 0; i--) {
            Day d = dayData.get(i);
            String dateStr = Util.formatDate(d.getDate());
            // String moodStr = String.valueOf(d.getAverageMood());
            String moodStr = String.format("%.2f", d.getAverageMood());
            Measurement m = Measurement.searchList(d.getMeasurements(), "Sleep");
            // String sleepStr = String.valueOf(m.getValue());
            String sleepStr = String.format("%.2f", m.getValue());

            DummyContent.DummyItem item = new DummyContent.DummyItem(dateStr, moodStr, sleepStr);
            ITEMS.add(item);
        }
    }

}
