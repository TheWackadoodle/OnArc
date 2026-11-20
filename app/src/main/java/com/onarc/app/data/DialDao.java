package com.onarc.app.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface DialDao {

     @Insert
     void insert(DialEntity dial);

     @Update
     void update(DialEntity dial);

     @Delete
     void delete(DialEntity dial);

     @Query("SELECT * FROM dials ORDER BY name ASC")
     LiveData<List<DialEntity>> getAllDials();

     @Query("SELECT * FROM dials WHERE dialId = :dialId LIMIT 1")
     LiveData<DialEntity> getDialById(Long dialId);

     @Query("SELECT * FROM dials WHERE isDefault = 1 LIMIT 1")
     LiveData<DialEntity> getDefaultDial();

     @Query("SELECT * FROM arcs WHERE dialId = :dialId ORDER BY startMinute ASC")
     LiveData<List<ArcEntity>> getArcsForDial(Long dialId);

     @Query("SELECT dialId FROM dials")
     List<Long> getAllDialIds();

     @Query("SELECT COUNT(dialId) FROM dials")
     int getDialCount();
}
