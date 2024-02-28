package edu.qc.seclass.rlm.AlarmReceiver;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import edu.qc.seclass.rlm.Activities.EditReminder;
import edu.qc.seclass.rlm.Activities.Home_ReminderLists;
import edu.qc.seclass.rlm.R;

public class ReminderAlarmReceiver extends BroadcastReceiver {

    public static final String REMINDER_TEXT_KEY = "REMINDER_TEXT";
    private static final String CHANNEL_ID = "REMINDER_CHANNEL";

    @Override
    public void onReceive(Context context, Intent intent) {
        String reminderText = intent.getStringExtra(REMINDER_TEXT_KEY);

        // Get the reminderListID, reminderID, listName, and userID from the intent
        String reminderListID = intent.getStringExtra("SELECTED_LIST_ID");
        String reminderID = intent.getStringExtra("REMINDER_ID");
        String listName = intent.getStringExtra("LIST_NAME");
        String userID = intent.getStringExtra("USERID");

        // Create an explicit intent
        // Pass the reminderID, reminderListID, listName, and userID back to the new intent
        Intent notificationIntent = new Intent(context, EditReminder.class);
        notificationIntent.putExtra("REMINDER_ID", reminderID);
        notificationIntent.putExtra("SELECTED_LIST_ID", reminderListID);
        notificationIntent.putExtra("LIST_NAME", listName);
        notificationIntent.putExtra("USERID", userID);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        // Build the notification, match channel with the created one
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "REMINDER_CHANNEL")
                .setSmallIcon(R.drawable.ic_launcher_foreground) // Use your app's icon
                .setContentTitle("Reminder")
                .setContentText(reminderText)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);


        // Show the notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }
}