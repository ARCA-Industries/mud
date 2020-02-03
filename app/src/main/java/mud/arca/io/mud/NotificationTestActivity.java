package mud.arca.io.mud;



import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;


public class NotificationTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_test);

        /**
         * I have no idea what is going on here
         * but I'm smart so eventually I'll figure this out
         *      - Robert
         **/

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "AndroidChannel")
                .setContentTitle("title")
                .setContentText("body")
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setAutoCancel(true);

    }

}


