package edu.qc.seclass.rlm.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.UUID;

import edu.qc.seclass.rlm.R;
import edu.qc.seclass.rlm.ReminderList;
import edu.qc.seclass.rlm.roomLocalDB.AppDatabase;
import edu.qc.seclass.rlm.roomLocalDB.ReminderListDao;

public class CreateReminderList extends AppCompatActivity {
    private Button returnBackButton;
    private Button createReminderListButton;
    private EditText reminderListText;

    // Coming from the HomeReminderList activity
    // Allows user to create a reminderList and then moves back to the home_reminderList Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_reminder_list);

        returnBackButton = findViewById(R.id.returnToReminderListView);
        createReminderListButton = findViewById(R.id.createReminderListButton);
        reminderListText = findViewById(R.id.newReminderListName);

        AppDatabase userDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").build();
        ReminderListDao reminderListDao = userDatabase.reminderListDao();

        UUID username = UUID.fromString(getIntent().getStringExtra("USERID"));
        Log.d("CreateReminderList", "Username: " + username);

        returnBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateReminderList.this, Home_ReminderLists.class));
            }
        });
        createReminderListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reminderListName = reminderListText.getText().toString();

                if (reminderListName.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "No List Name Entered", Toast.LENGTH_SHORT).show();
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int count = reminderListDao.countListByName(reminderListName);

                        if (count > 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "List Name Already Exists", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            reminderListDao.insert(new ReminderList(reminderListName, username));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // These items must be passed back to Home_reminderList Activity
                                    Toast.makeText(getApplicationContext(), "List Created", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(CreateReminderList.this, Home_ReminderLists.class);
                                    String userIDString = getIntent().getStringExtra("USERID");
                                    intent.putExtra("USERID", userIDString);
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                }).start();
            }
        });




    }
}
