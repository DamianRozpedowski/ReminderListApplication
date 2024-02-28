package edu.qc.seclass.rlm.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.qc.seclass.rlm.R;
import edu.qc.seclass.rlm.ReminderList;
import edu.qc.seclass.rlm.roomLocalDB.AppDatabase;
import edu.qc.seclass.rlm.roomLocalDB.ReminderListDao;

public class Home_ReminderLists extends AppCompatActivity {
    private Button createListButton;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> listNames;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_list_layout);

        listView = findViewById(R.id.remindersListView);
        listNames = new ArrayList<>();

        // Required to keep track of the UserID passed from MainActivity
        UUID userId = UUID.fromString(getIntent().getStringExtra("USERID"));
        AppDatabase userDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").build();
        ReminderListDao reminderListDao = userDatabase.reminderListDao();

        // Loads and displays all available reminder lists for the specific user
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ReminderList> reminderLists = reminderListDao.getReminderListsByUser(userId);
                for (ReminderList reminderList : reminderLists) {
                    listNames.add(reminderList.getListName());
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new ArrayAdapter<>(Home_ReminderLists.this, android.R.layout.simple_list_item_1, listNames);
                        listView.setAdapter(adapter);
                    }
                });
            }
        }).start();


        this.createListButton = findViewById(R.id.createListButton);

        // Moves to the  CreateReminderList Activity
        createListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_ReminderLists.this, CreateReminderList.class);
                String userIDString = getIntent().getStringExtra("USERID");
                intent.putExtra("USERID", userIDString); // Pass the String, not the UUID
                startActivity(intent);
            }
        });

        // Moves to the ReminderListView Activity
        // Allows users to see what reminders are within that specific reminderList
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedListName = listNames.get(position);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        UUID selectedListID = reminderListDao.getReminderListID(selectedListName);
                        Intent intent = new Intent(Home_ReminderLists.this, ReminderListView.class);
                        intent.putExtra("SELECTED_LIST_ID", selectedListID.toString());
                        intent.putExtra("LIST_NAME", selectedListName);
                        intent.putExtra("USERID", userId.toString());
                        startActivity(intent);
                    }
                }).start();
            }
        });
    }
}