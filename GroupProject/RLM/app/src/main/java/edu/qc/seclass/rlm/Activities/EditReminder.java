package edu.qc.seclass.rlm.Activities;

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

public class EditReminder extends AppCompatActivity {
    private Switch checkOffSwitch;
    private Button cancelButton;
    private Button saveReminderButton;
    private Button deleteButton;
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
    private EditText locationInput;

    // Creation of Notification Pop up
    private void scheduleReminderAlarm(LocalDateTime dateTime, String reminderText, Context context, String reminderListID,String reminderID, String listName, String userID) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderAlarmReceiver.class);
        intent.putExtra(ReminderAlarmReceiver.REMINDER_TEXT_KEY, reminderText);

        // Needed to be passed over to notification so when user clicks on it they can still interact with the page
        intent.putExtra("REMINDER_ID", reminderID);
        intent.putExtra("SELECTED_LIST_ID", reminderListID);
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
        setContentView(R.layout.edit_reminder);

        String reminderIdString = getIntent().getStringExtra("REMINDER_ID");
        UUID reminderId = UUID.fromString(reminderIdString);
        String reminderListID = getIntent().getStringExtra("SELECTED_LIST_ID");
        UUID reminderListId = UUID.fromString(reminderListID);

        reminderName = findViewById(R.id.rNameInput);
        reminderType = findViewById(R.id.rTypeInput);
        reminderContent = findViewById(R.id.reminderContent2);
        reminderDate = findViewById(R.id.reminderDate);
        reminderTime = findViewById(R.id.reminderTime);
        dateTimeAlertSwitch = findViewById(R.id.dateTimeAlert);
        locationInput = findViewById(R.id.reminderLocation);
        locationAlertSwitch = findViewById(R.id.locationAlert);
        repeatSwitch = findViewById(R.id.repeatReminderSwitch2);
        repeatAmount = findViewById(R.id.repAmount);
        checkOffSwitch = findViewById(R.id.checkOffSwitch);

        saveReminderButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);
        cancelButton = findViewById(R.id.cancelButton);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").build();
        ReminderDao reminderDao = db.reminderDao();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Reminder reminder = reminderDao.getReminderById(reminderId);
                List<String> reminderNames = reminderDao.getAllReminderName(reminderListId);
                List<String> reminderTypes = reminderDao.getAllReminderType(reminderListId);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayAdapter<String> reminderNameAdapter = new ArrayAdapter<>(EditReminder.this, android.R.layout.simple_list_item_1, reminderNames);
                        reminderName.setAdapter(reminderNameAdapter);
                        ArrayAdapter<String> reminderTypeAdapter = new ArrayAdapter<>(EditReminder.this, android.R.layout.simple_list_item_1, reminderTypes);
                        reminderType.setAdapter(reminderTypeAdapter);

                        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                        reminderName.setText(reminder.getReminderName());
                        reminderType.setText(reminder.getReminderType());
                        reminderContent.setText(reminder.getContent());
                        reminderDate.setText(reminder.getAlertForDate().format(dateFormatter)); // Format date as needed
                        reminderTime.setText(reminder.getAlertForTime().format(timeFormatter)); // Format time as needed
                        dateTimeAlertSwitch.setChecked(reminder.getDayTimeAlert());
                        locationInput.setText(reminder.getLocation());
                        locationAlertSwitch.setChecked(reminder.getSetLocation());
                        repeatSwitch.setChecked(reminder.getRepeatReminder());
                        repeatAmount.setText(String.valueOf(reminder.getRepetitionAmount()));
                        checkOffSwitch.setChecked(reminder.getCheckOff());
                    }
                });
            }
        }).start();

        // Saves new reminder by updating the reminder in the database based on reminderlist ID
        saveReminderButton.setOnClickListener(new View.OnClickListener() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            @Override
            public void onClick(View v){
                String reminderNameString = reminderName.getText().toString();
                String reminderTypeString = reminderType.getText().toString();
                String reminderContentString = reminderContent.getText().toString();
                boolean dateTimeAlertBool = dateTimeAlertSwitch.isChecked();
                String reminderDateString = reminderDate.getText().toString();
                String reminderTimeString = reminderTime.getText().toString();
                boolean repeatBool = repeatSwitch.isChecked();
                String repeatAmountString = repeatAmount.getText().toString();
                boolean locationAlertBool = locationAlertSwitch.isChecked();
                String reminderLocationString = locationInput.getText().toString();
                boolean checkOffBool = checkOffSwitch.isChecked();
//                reminderDateString = reminderDateString.replace('-', '/');
                reminderNameString = reminderNameString != null ? reminderNameString : "";
                reminderTypeString = reminderTypeString != null ? reminderTypeString : "";
                reminderContentString = reminderContentString != null ? reminderContentString : "";
                reminderDateString = reminderDateString != null ? reminderDateString : "";
                reminderTimeString = reminderTimeString != null ? reminderTimeString : "";
                repeatAmountString = repeatAmountString != null ? repeatAmountString : "";
                reminderLocationString = reminderLocationString != null ? reminderLocationString : "";



                // Creation of Notification Pop up
                if(dateTimeAlertBool){
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                    String formattedDate = LocalDate.now().format(formatter);
                    LocalDate date = LocalDate.parse(formattedDate, formatter);
                    LocalTime time = LocalTime.parse(LocalTime.now().format(timeFormatter));
                    try{
                        date = LocalDate.parse(reminderDateString, dateFormatter);
                        time = LocalTime.parse(reminderTimeString, timeFormatter);
                    }
                    catch (DateTimeParseException timeErr){
                        Toast.makeText(getApplicationContext(), "Invalid Date/Time Format, Current date and Time assigned (Use MM/dd/yyyy and 24hr time)", Toast.LENGTH_SHORT).show();

                    }

                    LocalDateTime dateTime = LocalDateTime.of(date, time);
                    String reminderText = reminderNameString + " - " + reminderContentString;
                    String listIDString = getIntent().getStringExtra("SELECTED_LIST_ID");
                    String listName = getIntent().getStringExtra("LIST_NAME");
                    UUID userId = UUID.fromString(getIntent().getStringExtra("USERID"));
                    scheduleReminderAlarm(dateTime, reminderText, EditReminder.this, listIDString, reminderIdString, listName, userId.toString() );
                }

                int repeatAmount = 0;
                if(!repeatAmountString.isEmpty()) {
                    repeatAmount = Integer.valueOf(repeatAmountString);
                }
                Reminder reminder;
                try {
                    reminder = new Reminder(reminderId, reminderNameString, reminderTypeString,
                            reminderContentString, dateTimeAlertBool, reminderDateString, reminderTimeString,
                            repeatBool, repeatAmount, locationAlertBool, reminderLocationString, checkOffBool, reminderListId);
                }
                catch(IllegalStateException err){
                    Toast.makeText(getApplicationContext(), "Invalid Date/Time Format, Current date and Time assigned", Toast.LENGTH_SHORT);
                    reminder = new Reminder(reminderId, reminderNameString, reminderTypeString,
                            reminderContentString, dateTimeAlertBool, LocalDate.now().format(dateFormatter), LocalTime.now().format(timeFormatter),
                            repeatBool, repeatAmount, locationAlertBool, reminderLocationString, checkOffBool, reminderListId);
                }
                Reminder finalReminder = reminder;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        reminderDao.update(finalReminder);
                    }
                }).start();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(EditReminder.this, ReminderListView.class);
                        String listIDString = getIntent().getStringExtra("SELECTED_LIST_ID");
                        String listName = getIntent().getStringExtra("LIST_NAME");
                        UUID userId = UUID.fromString(getIntent().getStringExtra("USERID"));
                        intent.putExtra("SELECTED_LIST_ID", listIDString);
                        intent.putExtra("LIST_NAME", listName);
                        intent.putExtra("USERID", userId.toString());
                        startActivity(intent);
                    }
                });
            }
        });

        // Cancel edit reminder and go back to reminderListview
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditReminder.this, ReminderListView.class);
                String listIDString = getIntent().getStringExtra("SELECTED_LIST_ID");
                String listName = getIntent().getStringExtra("LIST_NAME");
                UUID userId = UUID.fromString(getIntent().getStringExtra("USERID"));
                intent.putExtra("SELECTED_LIST_ID", listIDString);
                intent.putExtra("LIST_NAME", listName);
                intent.putExtra("USERID", userId.toString());
                startActivity(intent);
            }
        });

        // Delete reminder from database based on reminder ID
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run(){
                        reminderDao.deleteById(reminderId);
                    }
                }).start();
                Intent intent = new Intent(EditReminder.this, ReminderListView.class);
                String listIDString = getIntent().getStringExtra("SELECTED_LIST_ID");
                String listName = getIntent().getStringExtra("LIST_NAME");
                UUID userId = UUID.fromString(getIntent().getStringExtra("USERID"));
                intent.putExtra("SELECTED_LIST_ID", listIDString);
                intent.putExtra("LIST_NAME", listName);
                intent.putExtra("USERID", userId.toString());
                startActivity(intent);
            }
        });

    }
}
