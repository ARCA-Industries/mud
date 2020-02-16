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

    // Probability that any given day exists
    private final static double PROB_DAY = .9;

    // Probability that there are mood recordings in a day.
    private final static double PROB_MOOD = 0.9;

    // Probability that there are measurement recordings in a day.
    private final static double PROB_MEASUREMENTS = 0.9;

    // number of milliseconds in 1 day
    private final static int MS_PER_DAY = 1000*60*60*24;

    private Random r;

    public MockUser() {
        this(0);
    }

    public MockUser(int seed) {
        super(Integer.toHexString(new Random(seed).nextInt()));

        r = new Random(seed);

        Variable sleep = new Variable("Sleep", VarType.FLOAT);
        Variable pizza = new Variable("Slices of pizza", VarType.INT);
        getVarData().add(sleep);
        getVarData().add(pizza);

        getDayData().clear();
        getDayData().addAll(getMockDays());
    }

    static Date getBaseDate() {
        return Util.parseDate("15-December-2019");
    }

    private ArrayList<Day> getMockDays() {
        ArrayList<Day> days = new ArrayList<>();
        for (int i = 0; i < NUM_DAYS; i++) {

//            if (r.nextDouble() > PROB_DAY) {
//                continue;
//            }

            Day day = new Day(Util.intToDate(getBaseDate(), i));
            ArrayList<Measurement> mockMeasurements = getMockMeasurements(day);

            if (r.nextDouble() < PROB_MEASUREMENTS) {
                day.getMeasurements().clear();
                day.getMeasurements().addAll(mockMeasurements);
            }

            if (r.nextDouble() < PROB_MOOD) {
                day.getMoodRecordings().clear();
                day.getMoodRecordings().addAll(getMockMoodRecordings(day, mockMeasurements));
            }

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

        Variable sleep = getVarData().get(0);
        Variable pizza = getVarData().get(1);

        float sleepVal = (float) (r.nextGaussian() + 8);
        Measurement sleepM = new Measurement(sleepVal, "hr", sleep);
        measurements.add(sleepM);

        float pizzaVal = (float) r.nextInt(4);
        Measurement pizzaM = new Measurement(pizzaVal, "slices", pizza);
        measurements.add(pizzaM);

        return measurements;
    }


}
