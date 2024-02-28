package edu.qc.seclass.rlm.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.UUID;

import edu.qc.seclass.rlm.R;
import edu.qc.seclass.rlm.User;
import edu.qc.seclass.rlm.roomLocalDB.AppDatabase;
import edu.qc.seclass.rlm.roomLocalDB.ReminderDao;
import edu.qc.seclass.rlm.roomLocalDB.ReminderListDao;
import edu.qc.seclass.rlm.roomLocalDB.UserDao;

public class MainActivity extends AppCompatActivity {
    private EditText addReminderListBox;
    private Button submitButton;
    private Button displayButton;


    private EditText selectReminderList;
    private EditText addReminder;
    private Button addReminderButton;

    // Searching by reminderName
    private EditText searchReminderTextBox;
    private Button buttonSearch;

    // Edit Reminder
    private EditText editReminderTextBox;
    private Button buttonEdit;

    // Delete Reminder Button
    private Button deleteReminderButton;

    // Checkoff Reminder
    private Button reminderCheckOffButton;

    // CheckOff all reminders from list button
    private Button clearAllCheckOffButton;

    // get reminder type button
    private Button getReminderTypeButton;

    // Next Page Reminders only functions
    private Button nextPage;

    private EditText username,password;
    private Button login, register;
    public static final String CHANNEL_ID = "reminder_notifications";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);

        AppDatabase userDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").build();
        UserDao userDao = userDatabase.userDao();
        ReminderDao reminderDao = userDatabase.reminderDao();
        ReminderListDao reminderListDao = userDatabase.reminderListDao();



        // Request permission from user and also set up channel and description of the notification
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = "Reminder Notifications";
            String description = "Notifications for Reminders";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("REMINDER_CHANNEL", name, importance);
            channel.setDescription(description);
            // Register the channel with the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 0);
            }
        }


        // To wipe DB
        //        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                userDao.deleteAllUsers();
//                reminderDao.deleteAllReminders();
//                reminderListDao.deleteAllReminderLists();
//            }
//        }).start();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //Create Entity
                User user = new User();
                user.setUsername(username.getText().toString());
                user.setPassword(password.getText().toString());

                if (inputNotFilled(user)) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // Check if username already exists
                            int userCount = userDao.checkUserName(user.getUsername());
                            if (userCount > 0) {
                                // Username exists, show toast
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Username already exists", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                // Username does not exist, proceed with registration
                                userDao.insert(user);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Successfully Registered", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).start();
                } else {
                    Toast.makeText(getApplicationContext(), "Missing Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Login takes you to the Home_ReminderLists Activity
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputUsername = username.getText().toString();
                String inputPassword = password.getText().toString();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int userCount = userDao.checkUserPass(inputUsername, inputPassword);
                        if (userCount > 0) {
                            String userIDString = userDao.getUserID(inputUsername);
                            UUID userID = UUID.fromString(userIDString);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(MainActivity.this, Home_ReminderLists.class);
                                    intent.putExtra("USERID", userID.toString());
                                    startActivity(intent);
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }
    public Boolean inputNotFilled(User user){
        if(user.getUsername().isEmpty() ||
                user.getPassword().isEmpty()){
            return false;
        }
        return true;
    }
}
