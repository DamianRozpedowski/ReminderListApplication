package edu.qc.seclass.rlm.Activities;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

import edu.qc.seclass.rlm.AlarmReceiver.ReminderAlarmReceiver;
import edu.qc.seclass.rlm.R;
import edu.qc.seclass.rlm.Reminder;
import edu.qc.seclass.rlm.roomLocalDB.AppDatabase;
import edu.qc.seclass.rlm.roomLocalDB.ReminderDao;
import edu.qc.seclass.rlm.roomLocalDB.ReminderListDao;

public class CreateReminder extends AppCompatActivity {
    private Button cancelButton;
    private Button addReminderButton;
    private AutoCompleteTextView reminderName;
    private AutoCompleteTextView reminderType;
    private EditText reminderContent;

    // Time and Date Alert
    private Switch dateTimeAlertSwitch;
    private EditText reminderDate;
    private EditText reminderTime;
    private Switch repeatSwitch;
    private EditText repeatAmount;

    // Location Alert
    private Switch locationAlertSwitch;
    private EditText reminderLocation;

    // For Alerts based on time
    private void scheduleReminderAlarm(LocalDateTime dateTime, String reminderText, Context context, String reminderListID, String reminderID, String listName, String userID) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderAlarmReceiver.class);
        // Must send these such that when the user clicks on the notification they will be sent to editReminder which requires these
        intent.putExtra(ReminderAlarmReceiver.REMINDER_TEXT_KEY, reminderText);
        intent.putExtra("SELECTED_LIST_ID", reminderListID);
        intent.putExtra("REMINDER_ID", reminderID);
        intent.putExtra("LIST_NAME", listName);
        intent.putExtra("USERID", userID);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                pendingIntent);
    }

    // Coming from ReminderList View
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_reminder);

        // Load up buttons and UI
        addReminderButton = findViewById(R.id.addNewButton);
        cancelButton = findViewById(R.id.cancelButton);
        reminderName = findViewById(R.id.rNameInput);
        reminderType = findViewById(R.id.rTypeInput);
        reminderContent = findViewById(R.id.reminderContent);
        dateTimeAlertSwitch = findViewById(R.id.dateTimeAlert);
        reminderDate = findViewById(R.id.reminderDate);
        reminderTime = findViewById(R.id.reminderTime);
        repeatSwitch = findViewById(R.id.repeatReminderSwitch);
        repeatAmount = findViewById(R.id.repAmount);
        locationAlertSwitch = findViewById(R.id.locationAlert);
        reminderLocation = findViewById(R.id.reminderLocation);

        // From ReminderListView, needs to be passed back to reminderListView if user cancels
        String listIDString = getIntent().getStringExtra("SELECTED_LIST_ID");
        UUID listID = UUID.fromString(listIDString);
        String listName = getIntent().getStringExtra("LIST_NAME");
        UUID userId = UUID.fromString(getIntent().getStringExtra("USERID"));

        AppDatabase userDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").build();
        ReminderListDao reminderListDao = userDatabase.reminderListDao();
        ReminderDao reminderDao = userDatabase.reminderDao();

        new Thread(new Runnable(){
           @Override
           public void run(){
               List<String> reminderNames = reminderDao.getAllReminderName(listID);
               List<String> reminderTypes = reminderDao.getAllReminderType(listID);
               runOnUiThread(new Runnable(){
                   @Override
                   public void run(){
                       ArrayAdapter<String> reminderNameAdapter = new ArrayAdapter<>(CreateReminder.this, android.R.layout.simple_list_item_1, reminderNames);
                       reminderName.setAdapter(reminderNameAdapter);
                       ArrayAdapter<String> reminderTypeAdapter = new ArrayAdapter<>(CreateReminder.this, android.R.layout.simple_list_item_1, reminderTypes);
                       reminderType.setAdapter(reminderTypeAdapter);
                   }
               });
           }
        }).start();

        // Button to add a new reminder to the reminderlist based on listID
        addReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reminderNameString = reminderName.getText().toString();
                String reminderTypeString = reminderType.getText().toString();
                String reminderContentString = reminderContent.getText().toString();
                boolean dateTimeAlertBool = dateTimeAlertSwitch.isChecked();
                String reminderDateString = reminderDate.getText().toString();
                String reminderTimeString = reminderTime.getText().toString();
                boolean repeatBool = repeatSwitch.isChecked();
                String repeatAmountString = repeatAmount.getText().toString();
                boolean locationAlertBool = locationAlertSwitch.isChecked();
                String reminderLocationString = reminderLocation.getText().toString();

                int repeatAmount = 0;
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                if(!repeatAmountString.isEmpty()) {
                    repeatAmount = Integer.valueOf(repeatAmountString);
                }
                Reminder reminder;
                try{
                    reminder = new Reminder(UUID.fromString(listIDString), reminderNameString, reminderTypeString,
                            reminderContentString, dateTimeAlertBool, reminderDateString, reminderTimeString,
                            repeatBool, repeatAmount, locationAlertBool, reminderLocationString);
                }
                catch(IllegalStateException err){
                    Toast.makeText(getApplicationContext(), "Invalid Date/Time Format, Current date and Time assigned (Use MM/dd/yyyy) and 24hr time", Toast.LENGTH_SHORT).show();
                    reminder = new Reminder(UUID.fromString(listIDString), reminderNameString, reminderTypeString,
                            reminderContentString, dateTimeAlertBool, LocalDate.now().format(dateFormatter), LocalTime.now().format(timeFormatter),
                            repeatBool, repeatAmount, locationAlertBool, reminderLocationString);
                }

                String reminderID = reminder.getReminderID().toString();
                // For Alerts
                if(dateTimeAlertBool){
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                    String formattedDate = LocalDate.now().format(formatter);
                    LocalDate date = LocalDate.parse(formattedDate, formatter);
                    LocalTime time = LocalTime.parse(LocalTime.now().format(timeFormatter));
                    try {
                        date = LocalDate.parse(reminderDateString, dateFormatter);
                        time = LocalTime.parse(reminderTimeString, timeFormatter);
                    }
                    catch(DateTimeParseException timeErr){
                        Toast.makeText(getApplicationContext(), "Invalid Date/Time Format", Toast.LENGTH_SHORT).show();
                    }
                    LocalDateTime dateTime = LocalDateTime.of(date, time);
                    String reminderText = reminderNameString + " - " + reminderContentString;
                    scheduleReminderAlarm(dateTime, reminderText, CreateReminder.this, listIDString, reminderID, listName, userId.toString() );
                }
                Reminder finalReminder = reminder;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        reminderDao.insert(finalReminder);
                    }
                }).start();
                Intent intent = new Intent(CreateReminder.this, ReminderListView.class);
                intent.putExtra("SELECTED_LIST_ID", listIDString);
                intent.putExtra("LIST_NAME", listName);
                intent.putExtra("USERID", userId.toString());
                startActivity(intent);
            }
        });
        // Cancel creation and go back to reminderListView
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateReminder.this, ReminderListView.class);
                intent.putExtra("SELECTED_LIST_ID", listIDString);
                intent.putExtra("LIST_NAME", listName);
                intent.putExtra("USERID", userId.toString());
                startActivity(intent);
            }
        });

    }
}
