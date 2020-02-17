package mud.arca.io.mud.DataStructures;

import android.content.res.Resources;
import android.util.Log;

import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import mud.arca.io.mud.App;
import mud.arca.io.mud.R;

// Collection of utility functions
public class Util {
    // Create a Date object using a string in format "12-December-2012".
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

    // input: i = number of days since baseDate
    // output: date
    public static Date intToDate(Date baseDate, int i) {
        Calendar c = Calendar.getInstance();
        c.setTime(baseDate);
        c.add(Calendar.DAY_OF_MONTH, i);
        return c.getTime();
    }

    public static Date floatToDate(Date baseDate, float f) {
        return intToDate(baseDate, (int) f);
    }

    // Convert a date to float.
    // Returns the number of days passed since base date.
    public static float dateToFloat(Date d, Date baseDate) {
        long diff = d.getTime() - baseDate.getTime();
        return (float) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    // output an ArrayList to debug
    public static void printList(ArrayList<Long> al) {
        StringBuffer sb = new StringBuffer();

        sb.append("[");
        for (Long lo : al) {
            sb.append(Long.toString(lo));
            sb.append(" ");
        }
        sb.append("]");
        String str = sb.toString();
        Util.debug(str);
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
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd");
        return sdf.format(d);
    }

    public static String formatDateWithYear(Date d) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        return sdf.format(d);
    }

    // Round a float to the nearest 0.5.
    public static float roundToHalf(float f) {
        double d = (double) f;
        return Math.round(d * 2) / 2f;
    }
}
