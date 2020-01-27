package mud.arca.io.mud.DataStructures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class Day {

    private Date date;
    private Collection<MoodRecording> moodRecordings;
    private Collection<Variable> variables;

    public Day(Date date) {
        this.date = date;
        this.moodRecordings = new ArrayList<>();
        this.variables = new ArrayList<>();
    }

    public Day(Date date, Collection<MoodRecording> moodRecordings, Collection<Variable> variables) {
        this.date = date;
        this.moodRecordings = moodRecordings;
        this.variables = variables;
    }

    public double getAverageMood() {
        // TODO: Implement method
        return 0;
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

    public void setMoodRecordings(Collection<MoodRecording> moodRecordings) {
        this.moodRecordings = moodRecordings;
    }

    public Collection<Variable> getVariables() {
        return variables;
    }

    public void setVariables(Collection<Variable> variables) {
        this.variables = variables;
    }
}
