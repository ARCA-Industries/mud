package mud.arca.io.mud.DataStructures;

import java.sql.Timestamp;

public class MoodRecording {
    private Timestamp timestamp;
    private int value;

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp t) {
        timestamp = t;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int v) {
        value = v;
    }
}
