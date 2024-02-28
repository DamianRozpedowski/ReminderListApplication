package edu.qc.seclass.rlm.roomLocalDB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import edu.qc.seclass.rlm.Reminder;

@Dao
public interface ReminderDao {

    @Insert
    void insert(Reminder reminder);

    @Query("DELETE FROM reminder")
    void deleteAll();

    @Query("SELECT * FROM reminder")
    List<Reminder> getAll();

    @Query("SELECT * FROM reminder WHERE reminderListForeignKey = :reminderListID")
    List<Reminder> getAllReminders(UUID reminderListID);

    @Query("UPDATE reminder SET checkOff = FALSE WHERE reminderListForeignKey = :reminderListID")
    void clearAllCheckOff(UUID reminderListID);


    // Get all Appointment Types
    @Query("SELECT DISTINCT reminderType FROM reminder WHERE reminderListForeignKey = :reminderListID")
    List<String> getAllReminderType(UUID reminderListID);
    // Get all Appointment Names
    @Query("SELECT DISTINCT reminderName FROM reminder WHERE reminderListForeignKey = :reminderListID")
    List<String> getAllReminderName(UUID reminderListID);


    @Query("SELECT * FROM reminder WHERE reminderID = :reminderId")
    Reminder getReminderById(UUID reminderId);

    @Update
    void update(Reminder reminder);
    @Query("DELETE FROM reminder where reminderID = :reminderId")
    void deleteById(UUID reminderId);
    @Query("DELETE FROM reminder WHERE reminderListForeignKey = :reminderListID")
    void deleteAllRemindersOnListID(UUID reminderListID);

}
