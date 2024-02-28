package edu.qc.seclass.rlm.roomLocalDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import edu.qc.seclass.rlm.User;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE userID IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);
    @Query("DELETE FROM user")
    void deleteAll();

    @Insert
    void insert(User user);

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);
    @Query("SELECT * from user where username=(:username) and password=(:password)")
    User login(String username,String password);

    @Query("DELETE FROM user")
    void deleteAllUsers();

    @Query("SELECT COUNT(*) FROM user WHERE username = :username")
    int checkUserName(String username);

    @Query("SELECT COUNT(*) FROM user WHERE username = :username AND password = :password")
    int checkUserPass(String username, String password);

    @Query("SELECT userID FROM user WHERE username = :username")
    String getUserID(String username);
}
