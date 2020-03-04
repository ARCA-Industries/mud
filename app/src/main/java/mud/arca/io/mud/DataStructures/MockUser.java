package mud.arca.io.mud.DataStructures;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Random;

import mud.arca.io.mud.Util.Util;

/**
 * Use this as you'd use any User. For example,
 * User a = new MockUser()
 * will behave just as if it's a regular user, but it'll be filled with random data.
 * If you'd like, specify a seed with new MockUser(seed)
 */
public class MockUser extends User {
    private final static int NUM_DAYS = 300;
    private final static int NUM_MOODRECORDINGS = 1;

    // Probability that any given day exists
    private final static double PROB_DAY = .9;

    // Probability that there are mood recordings in a day.
    private final static double PROB_MOOD = 0.7;

    // Probability that there are measurement recordings in a day.
    private final static double PROB_MEASUREMENTS = 0.7;

    // number of milliseconds in 1 day
    private final static int MS_PER_DAY = 1000*60*60*24;

    private Random r;

    public MockUser() {
        this(0);
    }

    public MockUser(int seed) {
        super();

        r = new Random(seed);

        Variable sleep = new Variable("Sleep", "hours", Variable.VarType.FLOAT);
        Variable pizza = new Variable("Pizza", "slices", Variable.VarType.INT);
        Variable exercised = new Variable("Exercised", "bool", Variable.VarType.BOOL);
        getVarData().add(sleep);
        getVarData().add(pizza);
        getVarData().add(exercised);

        getDayData().clear();
        getDayData().addAll(getMockDays());
    }

    static Date getBaseDate() {
        return Util.parseDate("15-December-2019");
    }

    private ArrayList<Day> getMockDays() {
        ArrayList<Day> days = new ArrayList<>();
        for (int i = 0; i < NUM_DAYS; i++) {
            Day day = new Day(Util.intToDate(getBaseDate(), i));
            ArrayList<Measurement> mockMeasurements = getMockMeasurements(day);

            day.getMeasurements().clear();
            for (Measurement m : mockMeasurements) {
                if (r.nextDouble() < PROB_MEASUREMENTS) {
                    day.getMeasurements().add(m);
                }
            }

            if (r.nextDouble() < PROB_MOOD) {
                MoodRecording mr = getMockMoodRecording(day, mockMeasurements);
                day.setMoodRecording(mr);
            }

            days.add(day);
        }
        return days;
    }

    /**
     * Generate ArrayList of moodRecordings (old function)
     * @param day
     * @param mockMeasurements
     * @return
     */
    private ArrayList<MoodRecording> getMockMoodRecordings(Day day, ArrayList<Measurement> mockMeasurements) {
        ArrayList<MoodRecording> recordings = new ArrayList<>();

        for (int i = 0; i < NUM_MOODRECORDINGS; i++) {
            Timestamp timestamp = new Timestamp(day.getDate().getTime() + r.nextInt(MS_PER_DAY));

            try {
                Measurement m = Measurement.searchList(mockMeasurements, "Sleep");
                // Mood is calculated as a linear function of Sleep plus some random noise.
                float linear = m.getValue() - 1;
                float noise = r.nextFloat()*2 - 1;
                MoodRecording recording = new MoodRecording(timestamp, linear+noise);
                recordings.add(recording);
            } catch (NoSuchElementException e) {
                // do nothing
            }

        }
        return recordings;
    }

    /**
     * Generate one mood recording for the day.
     * @param day
     * @param mockMeasurements
     * @return
     */
    private MoodRecording getMockMoodRecording(Day day, ArrayList<Measurement> mockMeasurements) {
        Timestamp timestamp = new Timestamp(day.getDate().getTime() + r.nextInt(MS_PER_DAY));

        //MoodRecording recording = new MoodRecording;
        try {
            Measurement m = Measurement.searchList(mockMeasurements, "Sleep");
            // Mood is calculated as a linear function of Sleep plus some random noise.
            float linear = m.getValue() - 1;
            float noise = r.nextFloat()*2 - 1;
            MoodRecording recording = new MoodRecording(timestamp, linear+noise);
            return recording;
        } catch (NoSuchElementException e) {
            // do nothing
            throw e;
        }
    }

    private ArrayList<Measurement> getMockMeasurements(Day day) {
        ArrayList<Measurement> measurements = new ArrayList<>();

        Variable sleep = getVarData().get(0);
        float sleepVal = (float) (r.nextGaussian() + 8);
        Measurement sleepM = new Measurement(sleepVal, sleep);
        measurements.add(sleepM);

        Variable pizza = getVarData().get(1);
        float pizzaVal = (float) r.nextInt(5);
        Measurement pizzaM = new Measurement(pizzaVal, pizza);
        measurements.add(pizzaM);

        Variable exercised = getVarData().get(2);
        float exercisedVal = (float) r.nextInt(2);
        Measurement exercisedM = new Measurement(exercisedVal, exercised);
        measurements.add(exercisedM);

        return measurements;
    }


}
