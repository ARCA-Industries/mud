package mud.arca.io.mud.DataStructures;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * Use this as you'd use any User. For example,
 * User a = new MockUser()
 * will behave just as if it's a regular user, but it'll be filled with random data.
 * If you'd like, specify a seed with new MockUser(seed)
 */
public class MockUser extends User {

    private final static int NUM_DAYS = 30;
    private final static int NUM_MOODRECORDINGS = 1;
    private final static double PROB_DAY = .9;  // Probability that for any given day exists

    // number of milliseconds in 1 day
    private final static int MS_PER_DAY = 1000*60*60*24;

    private Random r;

    public MockUser() {
        this(0);
    }

    public MockUser(int seed) {
        super(Integer.toHexString(new Random(seed).nextInt()));

        r = new Random(seed);

        getDayData().clear();
        getDayData().addAll(getMockDays());
    }

    static Date getBaseDate() {
        return Util.parseDate("15-December-2019");
    }

    private ArrayList<Day> getMockDays() {
        ArrayList<Day> days = new ArrayList<>();
        for (int i = 0; i < NUM_DAYS; i++) {

            if (r.nextDouble() > PROB_DAY) {
                continue;
            }

            Day day = new Day(Util.intToDate(getBaseDate(), i));

            ArrayList<Measurement> mockMeasurements = getMockMeasurements(day);
            day.getMeasurements().clear();
            day.getMeasurements().addAll(mockMeasurements);

            day.getMoodRecordings().clear();
            day.getMoodRecordings().addAll(getMockMoodRecordings(day, mockMeasurements));

            days.add(day);
        }
        return days;
    }


    private ArrayList<MoodRecording> getMockMoodRecordings(Day day, ArrayList<Measurement> mockMeasurements) {
        ArrayList<MoodRecording> recordings = new ArrayList<>();

        for (int i = 0; i < NUM_MOODRECORDINGS; i++) {
            Timestamp timestamp = new Timestamp(day.getDate().getTime() + r.nextInt(MS_PER_DAY));

            try {
                Measurement m = Measurement.searchList(mockMeasurements, "Sleep");
                // Mood is calculated as a linear function of Sleep plus some random noise.
                //int moodValue = (int) (m.getValue() - 1);
                //int noise = r.nextInt(2) - 1;
                float linear = m.getValue() - 1;
                float noise = r.nextFloat()*2 - 1;
                MoodRecording recording = new MoodRecording(timestamp, linear+noise);
                recordings.add(recording);
            } catch (NoSuchElementException e) {

            }

        }
        return recordings;
    }

    private ArrayList<Measurement> getMockMeasurements(Day day) {
        ArrayList<Measurement> measurements = new ArrayList<>();

        float val = (float) (r.nextGaussian() + 8);
        Measurement measurement = new Measurement(val, "hr", new Variable("Sleep", VarType.FLOAT));
        measurements.add(measurement);

        return measurements;
    }


}
