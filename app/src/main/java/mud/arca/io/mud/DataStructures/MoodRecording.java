package mud.arca.io.mud.DataStructures;

import java.sql.Timestamp;

public class MoodRecording {
    private Timestamp timestamp;
    private float value;

    public MoodRecording(Timestamp timestamp, float value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp t) {
        timestamp = t;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float v) {
        value = v;
    }
}
