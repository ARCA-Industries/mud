package mud.arca.io.mud.DataStructures;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import mud.arca.io.mud.VariableVsTimeActivity;

/**
 * Use this as you'd use any User. For example,
 * User a = new MockUser()
 * will behave just as if it's a regular user, but it'll be filled with random data.
 * If you'd like, specify a seed with new MockUser(seed)
 */
public class MockUser extends User {

    private final static int NUM_DAYS = 30;
    private final static int NUM_MOODRECORDINGS = 10;
    private final static double PROB_DAY = .9;  // Probability that for any given day exists

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
            day.getMoodRecordings().clear();
            day.getMoodRecordings().addAll(getMockMoodRecordings(day));

            day.getMeasurements().clear();
            day.getMeasurements().addAll(getMockMeasurements(day));

            days.add(day);
        }
        return days;
    }


    private ArrayList<MoodRecording> getMockMoodRecordings(Day day) {
        ArrayList<MoodRecording> recordings = new ArrayList<>();
        // TODO: Generating recordings this way will mean that the average recording for each day is around 0.5. Maybe I should pick a random value and generate mood recordings around that so that each day has a nice distribution from 0 to 10.
        for (int i = 0; i < NUM_MOODRECORDINGS; i++) {
            Timestamp timestamp = new Timestamp(day.getDate().getTime() + r.nextInt(86400000));

            MoodRecording recording = new MoodRecording(timestamp, r.nextInt(10));

            recordings.add(recording);
        }
        return recordings;
    }

    private ArrayList<Measurement> getMockMeasurements(Day day) {
        ArrayList<Measurement> measurements = new ArrayList<>();

        Measurement measurement = new Measurement((float) (r.nextGaussian() + 8), "hr", new Variable("Sleep", VarType.FLOAT));
        measurements.add(measurement);

        return measurements;
    }


}
