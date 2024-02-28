package edu.qc.seclass.rlm.roomLocalDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
import java.util.UUID;

import edu.qc.seclass.rlm.Reminder;
import edu.qc.seclass.rlm.ReminderList;
import edu.qc.seclass.rlm.User;

@Dao
public interface ReminderListDao {
    @Insert
    void insert(ReminderList reminderList);
    @Query("DELETE FROM reminderList")
    void deleteAll();
    @Query("SELECT * FROM reminderList")
    List<ReminderList> getAll();

    @Query("SELECT listName FROM reminderList")
    List<String> getAllListNames();

    @Query("SELECT reminderListID FROM reminderList WHERE listName = :listName")
    UUID getReminderListID(String listName);
    @Query("SELECT * FROM reminderList WHERE userIDForeignKey = :userId")
    List<ReminderList> getReminderListsByUser(UUID userId);

    @Query("SELECT COUNT(listName) FROM reminderList WHERE listName = :listName")
    int countListByName(String listName);
    @Query("DELETE FROM reminderList WHERE reminderListID = :reminderListID")
    void deleteReminderList(UUID reminderListID);
    @Query("UPDATE reminderList SET listName = :newListName WHERE reminderListID = :reminderListID")
    void renameReminderList(UUID reminderListID, String newListName);
}
