package mud.arca.io.mud.Util;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import mud.arca.io.mud.DataStructures.User;
import mud.arca.io.mud.DataStructures.Variable;
import mud.arca.io.mud.R;

/**
 * A collection of utility functions
 */
public class Util {
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
}
