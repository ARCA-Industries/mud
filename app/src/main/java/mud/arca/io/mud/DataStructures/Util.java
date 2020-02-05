package mud.arca.io.mud.DataStructures;

import android.util.Log;

import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

    // input: days since base date (int)
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

    // output an ArrayList to debug
    public void printList(ArrayList<Long> al) {
        StringBuffer sb = new StringBuffer();

        sb.append("[");
        for (Long lo : al) {
            sb.append(Long.toString(lo));
            sb.append(" ");
        }
        sb.append("]");
        String str = sb.toString();
        Log.d("QQ55", str);
    }

    // normal aqua/green: #7ECFC0
    public static final int[] MUD_GRAPH_COLORS = {
            ColorTemplate.rgb("#7ECFC0")
    };

}
