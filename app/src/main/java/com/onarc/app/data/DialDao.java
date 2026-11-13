package com.onarc.app.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

// TODO (Claude wrote this whole file, explain it to Matthew later):
//  - mostly the same as ArcDao, but for Dials
//  - new: getDefaultDial() and getArcsForDial(), and how the WHERE queries work
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

     @Query("SELECT * FROM dials WHERE id = :id LIMIT 1")
     LiveData<DialEntity> getDialById(String id);

     @Query("SELECT * FROM dials WHERE isDefault = 1 LIMIT 1")
     LiveData<DialEntity> getDefaultDial();

     @Query("SELECT * FROM arcs WHERE dialId = :dialId ORDER BY startMinute ASC")
     LiveData<List<ArcEntity>> getArcsForDial(String dialId);
}
