package mud.arca.io.mud.DataStructures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.Exclude;

@IgnoreExtraProperties
public class Day {

    private Date date;
    private List<MoodRecording> moodRecordings;
    private List<Measurement> measurements;

    public Day() {
        this.moodRecordings = new ArrayList<>();
        this.measurements = new ArrayList<>();
    }

    public Day(Date date) {
        this.date = date;
        this.moodRecordings = new ArrayList<>();
        this.measurements = new ArrayList<>();
    }

    public Day(Date date, List<MoodRecording> moodRecordings, List<Measurement> measurements) {
        this.date = date;
        this.moodRecordings = moodRecordings;
        this.measurements = measurements;
    }

    @Exclude
    public double getAverageMood() {
        if (moodRecordings.isEmpty()) {
            //return -1;
            throw new NoSuchElementException("No mood recordings for that day");
        }

        double average = 0;
        for (MoodRecording recording : moodRecordings) {
            average += recording.getValue();
        }
        average /= moodRecordings.size();

        return average;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Collection<MoodRecording> getMoodRecordings() {
        return moodRecordings;
    }

    public void setMoodRecordings(List<MoodRecording> moodRecordings) {
        this.moodRecordings = moodRecordings;
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
            varStr = String.format("%.1f", m.getValue());
        } catch (NoSuchElementException e) {
            // do nothing
        }
        return varStr;
    }
}
