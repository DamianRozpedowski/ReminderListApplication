package edu.qc.seclass.rlm.roomLocalDB;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import edu.qc.seclass.rlm.Reminder;
import edu.qc.seclass.rlm.ReminderList;
import edu.qc.seclass.rlm.User;

@Database(entities = {User.class, ReminderList.class, Reminder.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract ReminderListDao reminderListDao();
    public abstract ReminderDao reminderDao();
}
