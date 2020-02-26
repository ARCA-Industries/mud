package mud.arca.io.mud.DataStructures;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class MoodRecording implements Serializable {
    private Timestamp timestamp;
    private float value;

    public MoodRecording() {
    }

    public MoodRecording(Timestamp timestamp, float value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date t) {
        timestamp = new Timestamp(t.getTime());
    }

    public float getValue() {
        return value;
    }

    public void setValue(float v) {
        value = v;
    }
}
