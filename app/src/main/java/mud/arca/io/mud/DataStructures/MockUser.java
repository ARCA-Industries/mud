package mud.arca.io.mud.DataStructures;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import mud.arca.io.mud.Database.DatabaseHelper;
import mud.arca.io.mud.Util.Util;

/**
 * Use this as you'd use any User. For example,
 * User a = new MockUser()
 * will behave just as if it's a regular user, but it'll be filled with random data.
 * If you'd like, specify a seed with new MockUser(seed)
 */
public class MockUser extends User {
    private final static int NUM_DAYS = 100;
    private final static int NUM_MOODRECORDINGS = 1;

    // Probability that any given day exists
    private final static double PROB_DAY = .9;

    // Probability that there are mood recordings in a day.
    private final static double PROB_MOOD = 0.7;

    // Probability that there are measurement recordings in a day.
    private final static double PROB_MEASUREMENTS = 0.7;

    // number of milliseconds in 1 day
    private final static int MS_PER_DAY = 1000*60*60*24;

    public final static ArrayList<Variable> DEFAULT_VARIABLES = new ArrayList<>(Arrays.asList(
        new Variable("Sleep", "hr", Variable.VarType.FLOAT),
        new Variable("Exercised", "bool", Variable.VarType.BOOL),
        new Variable("Calories Eaten", "kcal", Variable.VarType.INT)
    ));

    private static Random r;

    public MockUser() {
        this(0);
    }

    public MockUser(int seed) {
        super();

        r = new Random(seed);

        setVarData(DEFAULT_VARIABLES);
        getDayData().clear();
        getDayData().addAll(getMockDays());
    }

    public static void addSampleData(User user) {
        r = new Random(0);

        List<Object> defaults = new ArrayList<>(DEFAULT_VARIABLES);
        List<Object> mockDays = new ArrayList<>(getMockDays());
        // Util.debug("!!!! " + defaults);
        Util.setCollectionReference(DatabaseHelper.getVariableCollection(), defaults);
        Util.setCollectionReference(DatabaseHelper.getDaysCollection(), mockDays);

    }

    public static Date getBaseDate() {
        return Util.parseDate("15-December-2019");
    }

    public static ArrayList<Day> getMockDays() {
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
     * Generate one mood recording for the day.
     * @param day
     * @param mockMeasurements
     * @return
     */
    private static MoodRecording getMockMoodRecording(Day day, ArrayList<Measurement> mockMeasurements) {
        Timestamp timestamp = new Timestamp(day.getDate().getTime() + r.nextInt(MS_PER_DAY));

        try {
            Measurement m = Measurement.searchList(mockMeasurements, "Sleep");
            // Mood is calculated as a linear function of Sleep plus some random noise.
            // Note that this occasionally results in sleep > 10.0, which is bad.
            // So clip to 10.0. This will technically result in skewed probability, but oh well.
            float linear = m.getValue() - 1;
            float noise = r.nextFloat()*2 - 1;
            MoodRecording recording = new MoodRecording(timestamp, Math.min(linear+noise, 10));
            return recording;
        } catch (NoSuchElementException e) {
            // do nothing
            throw e;
        }
    }

    private static ArrayList<Measurement> getMockMeasurements(Day day) {
        ArrayList<Measurement> measurements = new ArrayList<>();

        Variable sleep = DEFAULT_VARIABLES.get(0);
        float sleepVal = (float) (r.nextGaussian() + 8);
        Measurement sleepM = new Measurement(sleepVal, sleep);
        measurements.add(sleepM);

        Variable exercised = DEFAULT_VARIABLES.get(1);
        float exercisedVal = (float) r.nextInt(2);
        Measurement exercisedM = new Measurement(exercisedVal, exercised);
        measurements.add(exercisedM);

        Variable calories = DEFAULT_VARIABLES.get(2);
        float caloriesVal = (float) (2500 + r.nextGaussian()*200);
        Measurement caloriesM = new Measurement(caloriesVal, calories);
        measurements.add(caloriesM);

        return measurements;
    }
}
