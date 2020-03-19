package mud.arca.io.mud.Notifications;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import java.util.Calendar;

import androidx.core.app.NotificationCompat;

import androidx.core.app.NotificationManagerCompat;
import mud.arca.io.mud.DataStructures.Day;
import mud.arca.io.mud.DataStructures.User;
import mud.arca.io.mud.DayDetails.DayDetailsActivity;
import mud.arca.io.mud.Util.Util;
import mud.arca.io.mud.MainActivity;
import mud.arca.io.mud.R;

public class AlarmReceiver extends BroadcastReceiver {

    public static int id = 0;
    public static String MOOD_REMINDER_CHANNEL_ID = "mood_reminder";

    @Override
    public void onReceive(Context context, Intent intent) {
        Util.debug("onReceive() called ******************");

        createNotificationChannel(context);

        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = DayDetailsActivity.getLaunchIntentForDate(context, User.getCurrentUser().getLatestDate());
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                context, MOOD_REMINDER_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("How are you today?")
                .setContentText("Click this notification to record your mood")
                .setSound(alarmSound)
                .setAutoCancel(true)
                .setWhen(when)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        notificationManager.notify(id, mNotifyBuilder.build());
        id++;
    }

    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Daily Mood Reminders"; // TODO: Export to getResources
            String description ="Very long channel description that supports\nmultiple\nlines\netc..."; // TODO: Export to getResources
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(MOOD_REMINDER_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.createNotificationChannel(channel);
        }
    }



}