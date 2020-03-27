package mud.arca.io.mud.Util;

import android.annotation.SuppressLint;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateUtil {

    @SuppressLint("NewApi")
    // TODO: Android Gradle Plugin 4.0 (and Android Studio 4.0) will desugar this for us
    public static Date dateFromLocalDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    @SuppressLint("NewApi")
    // TODO: Android Gradle Plugin 4.0 (and Android Studio 4.0) will desugar this for us
    public static LocalDate localDateFromDate(Date date) {
        return LocalDate.from(date.toInstant().atZone(ZoneId.systemDefault()));
    }
}
