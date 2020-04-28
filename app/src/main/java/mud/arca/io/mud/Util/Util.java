package mud.arca.io.mud.Util;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import androidx.core.content.FileProvider;
import mud.arca.io.mud.Analysis.charts.VariableVsTimeView;
import mud.arca.io.mud.DataStructures.Day;
import mud.arca.io.mud.DataStructures.Measurement;
import mud.arca.io.mud.DataStructures.User;
import mud.arca.io.mud.DataStructures.Variable;
import mud.arca.io.mud.R;

/**
 * A collection of utility functions
 */
public class Util {

    /**
     * String used to represent the "Variable name" of the Mood pseudo-variable.
     */
    public static final String MOOD_STRING = "_mood_var";

    /**
     * Given dayData and varName, return a list of variable values. Days with missing measurements
     * are ignored. Handles the special case for the Mood pseudo-variable.
     * @param dayData
     * @param varName
     * @return
     */
    public static ArrayList<Float> getVariableValues(Collection<Day> dayData, String varName) {
        ArrayList<Float> variableValues = new ArrayList<>();

        if (varName.equals(Util.MOOD_STRING)) {
            // Handle special case for Mood pseudo-variable
            for (Day day : dayData) {
                try {
                    variableValues.add(day.getAverageMood());
                } catch (NoSuchElementException e) {
                    // Do nothing
                }
            }
        } else {
            for (Day day : dayData) {
                Collection<Measurement> measurements = day.getMeasurements();
                try {
                    Measurement m = Measurement.searchList(measurements, varName);
                    variableValues.add(m.getValue());
                } catch (NoSuchElementException e) {
                    // Do nothing
                }
            }
        }

        return variableValues;
    }

    /**
     * @param s a String in the format "12-December-2012"
     * @return a Date from parsing the string. The Date has time 12:00 AM.
     */
    public static Date parseDate(String s) {
        Date d = new Date();
        try {
            SimpleDateFormat f = new SimpleDateFormat("dd-MMM-yyyy");
            d = f.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    /**
     * Returns the date that is i days after baseDate.
     * @param baseDate
     * @param i
     * @return
     */
    public static Date intToDate(Date baseDate, int i) {
        Calendar c = Calendar.getInstance();
        c.setTime(baseDate);
        c.add(Calendar.DAY_OF_MONTH, i);
        return c.getTime();
    }

    public static Date floatToDate(Date baseDate, float f) {
        return intToDate(baseDate, (int) f);
    }

    public static Date floatToDate(float f) {
        // For some reason you have to add 1 to value to get the correct axis label.
        // Maybe because of Daylight Saving Time?
        return Util.floatToDate(VariableVsTimeView.getBaseDate(), f + 1);
    }

    /**
     * Returns the number of days passed since base date.
     * @param d
     * @param baseDate
     * @return
     */
    public static float dateToFloat(Date d, Date baseDate) {
        long diff = d.getTime() - baseDate.getTime();
        return (float) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static float dateToFloat(Date d) {
        Date baseDate = VariableVsTimeView.getBaseDate();
        long diff = d.getTime() - baseDate.getTime();
        return (float) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    // Note: getColor() requires minSdkVersion 23 in app/gradle
    public static final int[] MUD_GRAPH_COLORS = {
            App.getContext().getColor(R.color.green),
            //App.getContext().getColor(R.color.green_dark),
    };

    public static final boolean DEBUG_ENABLED = true;

    public static void debug(String s) {
        if (DEBUG_ENABLED) {
            Log.d("QQ66", s);
        }
    }

    public static String formatDate(Date d) {
        if (d == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d");
        return sdf.format(d);
    }

    public static String formatDateWithYear(Date d) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
        return sdf.format(d);
    }

    public static Date parseDateWithYear(String s) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
        try {
            return sdf.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Util.parseDate("15-December-2019");
    }

    // Round a float to the nearest 0.5.
    public static float roundToHalf(float f) {
        double d = (double) f;
        return Math.round(d * 2) / 2f;
    }

    /**
     * Return true iff d1 is less than or equal to d2.
     * @param d1
     * @param d2
     * @return
     */
    public static boolean dateLTE(Date d1, Date d2) {
        return !d1.after(d2);
    }

    /**
     * Return a list of variable names for the current user.
     * @return
     */
    public static List<String> getVariableLabels() {
        List<String> ret = new ArrayList<>();
        for (Variable v : User.getCurrentUser().getVarData()) {
            ret.add(v.getName());
        }
        return ret;
    }

    /**
     * Set a CollectionReference in database to newVals.
     * @param cr
     * @param newVals
     */
    public static void setCollectionReference(CollectionReference cr, List<Object> newVals) {
        cr.get().addOnCompleteListener(task -> {
            // delete all
            for (DocumentSnapshot document : task.getResult().getDocuments()) {
                document.getReference().delete();
            }
            // add all
            for (Object o : newVals) {
                cr.add(o);
            }
        });
    }

    /**
     * Format a time.
     * formatTime(16, 7) => "4:07 PM"
     * @param hour
     * @param minute
     * @return
     */
    public static String formatTime(int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        Date date = cal.getTime();

        final SimpleDateFormat sdf = new SimpleDateFormat("h:mm aa");
        return sdf.format(date);
    }

    /**
     * Set any date's time to 12AM.
     */
    public static Date getDateNoTime(Date date) {
//        date.toInstant().truncatedTo(ChronoUnit.DAYS); // Only supported in Java8 (API26)
        Calendar d = new GregorianCalendar();
        d.setTime(date);
        d.set(Calendar.HOUR_OF_DAY,0);
        d.set(Calendar.MINUTE,0);
        d.set(Calendar.SECOND,0);
        d.set(Calendar.MILLISECOND,0);
        return d.getTime();
    }

    /**
     * Return the number of days between two dates.
     * This function is necessary because using (endDate.getTime() - startDate.getTime()) / MS_PER_DAY
     * does not return the correct value because of daylight saving time.
     * https://stackoverflow.com/questions/20165564/calculating-days-between-two-dates-with-java
     * @return
     */
    public static int daysBetween(Date date1, Date date2){
        Calendar dayOne = new GregorianCalendar();
        Calendar dayTwo = new GregorianCalendar();
        dayOne.setTime(date1);
        dayTwo.setTime(date2);

        if (dayOne.get(Calendar.YEAR) == dayTwo.get(Calendar.YEAR)) {
            return Math.abs(dayOne.get(Calendar.DAY_OF_YEAR) - dayTwo.get(Calendar.DAY_OF_YEAR));
        } else {
            if (dayTwo.get(Calendar.YEAR) > dayOne.get(Calendar.YEAR)) {
                //swap them
                Calendar temp = dayOne;
                dayOne = dayTwo;
                dayTwo = temp;
            }
            int extraDays = 0;

            int dayOneOriginalYearDays = dayOne.get(Calendar.DAY_OF_YEAR);

            while (dayOne.get(Calendar.YEAR) > dayTwo.get(Calendar.YEAR)) {
                dayOne.add(Calendar.YEAR, -1);
                // getActualMaximum() important for leap years
                extraDays += dayOne.getActualMaximum(Calendar.DAY_OF_YEAR);
            }

            return extraDays - dayTwo.get(Calendar.DAY_OF_YEAR) + dayOneOriginalYearDays ;
        }
    }


}
