package edu.qc.seclass.rlm;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.Room;

import java.util.List;
import java.util.UUID;

import edu.qc.seclass.rlm.roomLocalDB.AppDatabase;
import edu.qc.seclass.rlm.roomLocalDB.ReminderListDao;

@Entity(tableName = "reminderList",
        foreignKeys =@ForeignKey(entity = User.class,
                parentColumns = "userID",
                childColumns = "userIDForeignKey",
                onDelete = ForeignKey.CASCADE))
public class ReminderList {
    @PrimaryKey
    @NonNull
    public UUID reminderListID;

    @ColumnInfo(name = "listName")
    private String listName;


    @ColumnInfo(name = "userIDForeignKey")
    private UUID userIDForeignKey;

    public ReminderList(){
        this.reminderListID = UUID.randomUUID();
        this.listName = "No List Name";
    }
    public ReminderList(String reminderListName, UUID userIDForeignKey){
        this.reminderListID = UUID.randomUUID();
        this.listName = reminderListName;
        this.userIDForeignKey = userIDForeignKey;
    }
    public ReminderList(List<Reminder> reminderList, String listName){
        this.reminderListID = UUID.randomUUID();
        this.listName = listName;
    }
    @NonNull
    public UUID getReminderListID() {
        return reminderListID;
    }

    public String getListName() {
        return listName;
    }

    public void setReminderListID(@NonNull UUID reminderListID) {
        this.reminderListID = reminderListID;
    }
    public void setListName(String listName) {
        this.listName = listName;
    }
    public UUID getUserIDForeignKey() {
        return userIDForeignKey;
    }
    public void setUserIDForeignKey(UUID userIDForeignKey) {
        this.userIDForeignKey = userIDForeignKey;
    }

    private void saveToDB(){}
    public void createReminderList(){
    }
    public void addReminderByName(){}

    // TODO DONE
    public void searchReminderByName(){}

    // TODO DONE
    public void editReminder(){}

    // TODO DONE
    public void deleteReminder(){}
    // TODO DONE
    public void checkOffReminder(){}
    // TODO DONE
    public void clearAllCheckOff(){}
    // TODO DONE
    public void getReminderTypeGroup(){}




}
