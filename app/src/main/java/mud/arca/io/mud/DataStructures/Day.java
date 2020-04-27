package mud.arca.io.mud.DataStructures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.Exclude;

@IgnoreExtraProperties
public class Day implements Serializable {
    private Date date;
    private MoodRecording moodRecording;
    private List<Measurement> measurements;

    public Day() {
        this.measurements = new ArrayList<>();
    }

    public Day(Date date) {
        this.date = date;
        this.measurements = new ArrayList<>();
    }

    public Day(Date date, MoodRecording moodRecording, List<Measurement> measurements) {
        this.date = date;
        this.moodRecording = moodRecording;
        this.measurements = measurements;
    }

    @Exclude
    public float getAverageMood() {
        if (moodRecording == null) {
            throw new NoSuchElementException("No mood recordings for that day");
        }
        return moodRecording.getValue();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public MoodRecording getMoodRecording() {
        return moodRecording;
    }

    public void setMoodRecording(MoodRecording moodRecording) {
        this.moodRecording = moodRecording;
    }

    public Collection<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }

    /**
     * Return the average mood formatted as a String, or "-" if there are no mood recordings.
     * @return
     */
    @Exclude
    public String getMoodString() {
        String moodStr = "-";
        try {
            double avgMood = getAverageMood();
            moodStr = String.format("%.1f", avgMood);
        } catch (NoSuchElementException e) {
            // do nothing
        }
        return moodStr;
    }

    /**
     * Return the variable's measurement formatted as a String, or "-" if there
     * are no measurements.
     * @return
     */
    @Exclude
    public String getVarString(String varName) {
        String varStr = "-";
        try {
            Measurement m = Measurement.searchList(getMeasurements(), varName);
            //varStr = String.format("%.1f", m.getValue());
            varStr = m.getFormattedValue();
        } catch (NoSuchElementException e) {
            // do nothing
        }
        return varStr;
    }

    public void removeMeasurement(Measurement m) {
        measurements.remove(m);
    }
}
