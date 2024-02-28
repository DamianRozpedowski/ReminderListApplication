package edu.qc.seclass.rlm.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import edu.qc.seclass.rlm.R;
import edu.qc.seclass.rlm.Reminder;
import edu.qc.seclass.rlm.ReminderList;
import edu.qc.seclass.rlm.roomLocalDB.AppDatabase;
import edu.qc.seclass.rlm.roomLocalDB.ReminderDao;
import edu.qc.seclass.rlm.roomLocalDB.ReminderListDao;

public class ReminderListView extends AppCompatActivity {
    private ListView remindersListView;
    private Button backButton;
    private Button createReminderButton;
    private TextView reminderNameText;
    private List<Reminder> reminders;
    private Button clearButton;
    private Button deleteButton;
    private Button renameListButton;
    private AutoCompleteTextView renameListText;
    private Map<String, List<Reminder>> remindersByType = new HashMap<>();
    private List<Object> displayList = new ArrayList<>();


    // What each reminder item should look like
    private String reminderToString(Reminder reminder) {
        return "Name: " + reminder.getReminderName()  + "\nContent: " + reminder.getContent() + "\nCheck Off Status: " + reminder.getCheckOff();
    }

    // Coming from Home_ReminderLists
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_list_view_layout);
        remindersListView = findViewById(R.id.remindersListView);
        backButton = findViewById(R.id.backToReminderList);
        createReminderButton = findViewById(R.id.addReminderButton);
        reminderNameText = findViewById(R.id.withinReminderHeader);
        clearButton = findViewById(R.id.clearAllCheckOffs);
        deleteButton = findViewById(R.id.deleteListButton);
        renameListButton = findViewById(R.id.renameList);
        renameListText = findViewById(R.id.renameListTextBox);

        // Load reminders based on this listID
        String listIDString = getIntent().getStringExtra("SELECTED_LIST_ID");
        UUID listID = UUID.fromString(listIDString);
        String listName = getIntent().getStringExtra("LIST_NAME");
        reminderNameText.setText(listName);
        UUID userId = UUID.fromString(getIntent().getStringExtra("USERID"));

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").build();
        ReminderDao reminderDao = db.reminderDao();
        ReminderListDao reminderListDao = db.reminderListDao();

        // Load and display all reminders for this specific list
        new Thread(new Runnable() {
            @Override
            public void run() {
                reminders = reminderDao.getAllReminders(listID);
                Map<String, List<Reminder>> remindersByType = new HashMap<>();

                for (Reminder reminder : reminders) {
                    List<Reminder> typeList = remindersByType.getOrDefault(reminder.getReminderType(), new ArrayList<>());
                    typeList.add(reminder);
                    remindersByType.put(reminder.getReminderType(), typeList);
                }

                for (String type : remindersByType.keySet()) {
                    displayList.add("Type: " + type); // Add the type as a header
                    for (Reminder reminder : remindersByType.get(type)) {
                        displayList.add(reminder); // Add the reminder
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(ReminderListView.this, android.R.layout.simple_list_item_1, displayList) {
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                Object item = getItem(position);
                                TextView textView;

                                if (convertView == null || !(convertView instanceof TextView)) {
                                    textView = (TextView) LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                                } else {
                                    textView = (TextView) convertView;
                                }

                                if (item instanceof Reminder) {
                                    Reminder reminder = (Reminder) item;
                                    textView.setText(reminderToString(reminder));
                                } else {
                                    textView.setText((String) item);
                                }

                                return textView;
                            }
                        };
                        remindersListView.setAdapter(adapter);
                    }
                });
            }
        }).start();

        // Go back to Home_ReminderLists
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReminderListView.this, Home_ReminderLists.class);
                String userIDString = getIntent().getStringExtra("USERID");
                intent.putExtra("USERID", userIDString);
                startActivity(intent);
            }
        });

        // Go to CreateReminder
        createReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReminderListView.this, CreateReminder.class);
                intent.putExtra("SELECTED_LIST_ID", listIDString);
                intent.putExtra("LIST_NAME", listName);
                intent.putExtra("USERID", userId.toString());
                startActivity(intent);
            }
        });

        // If a specific reminder is pressed, go to EditReminder, where a user can check off or change the reminder
        remindersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = displayList.get(position);
                if (item instanceof Reminder) {
                    Reminder selectedReminder = (Reminder) item;

                    Intent intent = new Intent(ReminderListView.this, EditReminder.class);
                    intent.putExtra("REMINDER_ID", selectedReminder.getReminderID().toString());
                    intent.putExtra("SELECTED_LIST_ID", listIDString);
                    intent.putExtra("LIST_NAME", listName);
                    intent.putExtra("USERID", userId.toString());
                    startActivity(intent);
                }
            }
        });

        // Clears all checkoffs from all reminders within this list based on listID
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run(){
                        reminderDao.clearAllCheckOff(listID);
                    }
                }).start();
                Intent intent = new Intent(ReminderListView.this, ReminderListView.class);
                intent.putExtra("SELECTED_LIST_ID", listIDString);
                intent.putExtra("LIST_NAME", listName);
                intent.putExtra("USERID", userId.toString());
                startActivity(intent);
            }
        });

        // Deletes this list and all reminders within this list based on listID
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run(){
                        reminderDao.deleteAllRemindersOnListID(listID);
                        reminderListDao.deleteReminderList(listID);
                    }
                }).start();
                Intent intent = new Intent(ReminderListView.this, Home_ReminderLists.class);
                intent.putExtra("USERID", userId.toString());
                startActivity(intent);
            }
        });

        // Renames this list based on listID
        renameListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run(){
                        reminderListDao.renameReminderList( listID, renameListText.getText().toString());
                    }
                }).start();
                Intent intent = new Intent(ReminderListView.this, ReminderListView.class);
                intent.putExtra("SELECTED_LIST_ID", listIDString);
                intent.putExtra("LIST_NAME", renameListText.getText().toString());
                intent.putExtra("USERID", userId.toString());
                startActivity(intent);
            }
        });


    }
}
