package mud.arca.io.mud.DataStructures;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * Use this as you'd use any User. For example,
 * User a = new MockUser()
 * will behave just as if it's a regular user, but it'll be filled with random data.
 * If you'd like, specify a seed with new MockUser(seed)
 */
public class MockUser extends User {

    private final static int NUM_DAYS = 500;
    private final static int NUM_MOODRECORDINGS = 10;
    private final static int NUM_MEASUREMENTS = 5;

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


    private ArrayList<Day> getMockDays() {
        ArrayList<Day> days = new ArrayList<>();
        for (int i = 0; i < NUM_DAYS; i++) {
            Day day = new Day(new Date(2020, 1, i + 1));
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

        for (int i = 0; i < NUM_MEASUREMENTS; i++) {
            // TODO: Implement mock Measurements
//            Measurement measurement = new Measurement();
//
//            measurements.add(measurement);
        }

        return measurements;
    }


}
