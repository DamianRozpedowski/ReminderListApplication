package edu.qc.seclass.rlm;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

@Entity(tableName = "reminder",
        foreignKeys = @ForeignKey(entity = ReminderList.class,
                parentColumns = "reminderListID",
                childColumns = "reminderListForeignKey",
                onDelete = ForeignKey.CASCADE))
public class Reminder {
    @PrimaryKey
    @NonNull
    private UUID reminderID;

    @ColumnInfo(name = "reminderListForeignKey")
    private UUID reminderListForeignKey; // This column is the foreign key

    @ColumnInfo(name = "reminderName")
    private String reminderName;

    @ColumnInfo(name = "reminderType")
    private String reminderType;

    @ColumnInfo(name = "checkOff")
    private boolean checkOff;

    @ColumnInfo(name = "content")
    private String content;

    @ColumnInfo(name = "dayTimeAlert")
    private boolean dayTimeAlert;
    @ColumnInfo(name = "alertForDate")
    private LocalDate alertForDate;
    @ColumnInfo(name = "alertForTime")
    private LocalTime alertForTime;

    @ColumnInfo(name = "repeatReminder")
    private boolean repeatReminder;
    @ColumnInfo(name = "repetitionAmount")
    private int repetitionAmount;

    @ColumnInfo(name="setLocation")
    private boolean setLocation;

    @ColumnInfo(name="reminderLocation")
    private String location;

    public Reminder(UUID reminderId, String reminderNameString, String reminderTypeString, String reminderContentString,
                    boolean dateTimeAlertBool, String reminderDateString, String reminderTimeString,
                    boolean repeatBool, int repeatAmount, boolean locationAlertBool,
                    String reminderLocationString, boolean checkOffBool, UUID reminderListForeignKey) {
        this.reminderID = reminderId;
        this.reminderName = reminderNameString;
        this.reminderType = reminderTypeString;
        this.content = reminderContentString;
        this.checkOff = checkOffBool;
        this.dayTimeAlert = dateTimeAlertBool;
        if(reminderDateString.isEmpty()){
            this.alertForDate = LocalDate.now();
        }
        else{
            try{
                this.alertForDate = LocalDate.parse(reminderDateString, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            }
            catch (DateTimeParseException err){
                try{
                    this.alertForDate = LocalDate.parse(reminderDateString, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                }
                catch(DateTimeParseException err2){
                    throw new IllegalStateException("Invalid Date Format");
                }
            }

        }
        if(reminderTimeString.isEmpty()){
            this.alertForTime = LocalTime.now();
        }
        else{
            try{
                this.alertForTime = LocalTime.parse(reminderTimeString, DateTimeFormatter.ofPattern("HH:mm"));
            }
            catch (DateTimeParseException err){
                throw new IllegalStateException("Invalid Time Format");

            }
        }
        this.repeatReminder = repeatBool;
        this.repetitionAmount = repeatAmount;
        this.setLocation = locationAlertBool;
        this.location = reminderLocationString;
        this.reminderListForeignKey = reminderListForeignKey;

    }

    public boolean getCheckOff() {
        return checkOff;
    }

    public boolean getDayTimeAlert() {
        return dayTimeAlert;
    }

    public void setDayTimeAlert(boolean dayTimeAlert) {
        this.dayTimeAlert = dayTimeAlert;
    }

    public LocalDate getAlertForDate() {
        return alertForDate;
    }

    public void setAlertForDate(LocalDate alertForDate) {
        this.alertForDate = alertForDate;
    }

    public LocalTime getAlertForTime() {
        return alertForTime;
    }

    public void setAlertForTime(LocalTime alertForTime) {
        this.alertForTime = alertForTime;
    }

    public boolean getRepeatReminder() {
        return repeatReminder;
    }

    public void setRepeatReminder(boolean repeatReminder) {
        this.repeatReminder = repeatReminder;
    }

    public int getRepetitionAmount() {
        return repetitionAmount;
    }

    public void setRepetitionAmount(int repetitionAmount) {
        this.repetitionAmount = repetitionAmount;
    }

    public boolean getSetLocation() {
        return setLocation;
    }

    public void setSetLocation(boolean setLocation) {
        this.setLocation = setLocation;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Reminder() {
        this.reminderID = UUID.randomUUID();
        this.reminderListForeignKey = UUID.randomUUID();
        this.reminderName = "No Reminder Name";
        this.content = "No Content";
    }

    public Reminder(UUID reminderListID, String reminderName, String content, String reminderType) {
        this.reminderID = UUID.randomUUID();
        this.reminderListForeignKey = reminderListID;
        this.reminderName = reminderName;
        this.content = content;
        this.reminderType = reminderType;
        this.checkOff = false;

        this.dayTimeAlert = false;
        this.alertForDate = LocalDate.now();
        this.alertForTime = LocalTime.now();
        this.repeatReminder = false;
        this.repetitionAmount = 0;
        this.setLocation = false;
        this.location = "No Location";
    }
    public Reminder(UUID reminderListID, String reminderName, String reminderType, String content, boolean dayTimeAlertSwitch, String date, String time, boolean repeatSwitch, int repeatAmount, boolean locationAlertSwitch, String location) {

        this.reminderID = UUID.randomUUID();
        this.reminderListForeignKey = reminderListID;
        this.reminderName = reminderName;
        this.reminderType = reminderType;
        this.content = content;
        this.checkOff = false;
        this.dayTimeAlert = dayTimeAlertSwitch;
        if(date.isEmpty()){
            this.alertForDate = LocalDate.now();
        }
        else{

            try{
                this.alertForDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            }
            catch (DateTimeParseException err){
                try{
                    this.alertForDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                }
                catch(DateTimeParseException err2){
                    throw new IllegalStateException("Invalid Date Format");
                }
            }
        }
        if(time.isEmpty()){
            this.alertForTime = LocalTime.now();
        }
        else{
            try{
                this.alertForTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
            }
            catch(DateTimeParseException timeErr){
                throw new IllegalStateException("Invalid Time Format");
            }
        }
        this.repeatReminder = repeatSwitch;
        this.repetitionAmount = repeatAmount;
        this.setLocation = locationAlertSwitch;
        this.location = location;

    }
    public UUID getReminderID() {
        return reminderID;
    }
    public UUID getReminderListForeignKey() {
        return reminderListForeignKey;
    }
    public String getReminderName() {
        return reminderName;
    }
    public String getContent() {
        return content;
    }
    public String getReminderType() {return reminderType;}


    public void setReminderID(@NonNull UUID reminderID) {
        this.reminderID = reminderID;
    }
    public void setReminderListForeignKey(UUID reminderListForeignKey) {
        this.reminderListForeignKey = reminderListForeignKey;
    }
    public void setReminderName(String reminderName) {
        this.reminderName = reminderName;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setCheckOff(boolean checkOff){this.checkOff = checkOff;}
    public void setReminderType(String reminderType) {this.reminderType = reminderType;}

    // TODO DONE
    public void editContent(){}
    public void isCheckOff(){}
    public void clearCheckoff(){}
    public void setAlertDataTime(){}
    public void repeatReminder(){}
    public void setRepetitionAmount(){}
    public void getRepititionAmount(){}

    public void setLocationBool(){}
    public void setReminderLocation(){}
    public void getReminderLocation(){}

}
