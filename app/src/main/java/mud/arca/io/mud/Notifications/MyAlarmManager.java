package mud.arca.io.mud.Notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class MyAlarmManager {
    AlarmManager alarmMgr;
    PendingIntent alarmIntent;

    public MyAlarmManager(Context context) {
        Intent broadcastIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    /**
     * If a repeating notification already exists, it will update the time.
     * @param hour
     * @param minute
     */
    public void setRepeatingNotification(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
    }

    public void cancelRepeatingNotification() {
        alarmMgr.cancel(alarmIntent);
    }
}
