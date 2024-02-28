package edu.qc.seclass.rlm;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "user")
public class User {
    @PrimaryKey
    @NonNull
    public UUID userID;
    @ColumnInfo (name = "username")
    public String username;
    @ColumnInfo (name = "password")
    public String password;

    public User(){
        this.userID = UUID.randomUUID();
        this.username = "No User Input";
    }
    public User(String uName, String password){
        this.userID = UUID.randomUUID();
        this.username = uName;
        this.password = password;
    }

    public UUID getUserID() {
        return userID;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public void setUserID(@NonNull UUID userID) {
        this.userID = userID;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) { this.password = password; }


}
