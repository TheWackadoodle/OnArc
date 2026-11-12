package com.onarc.app.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface ArcDao {

     @Insert
     void insert(ArcEntity arc);

     @Update
     void update(ArcEntity arc);

     @Delete
     void delete(ArcEntity arc);

     @Query("SELECT * FROM arcs ORDER BY startMinute ASC")
     LiveData<List<ArcEntity>> getAllArcs();

     @Query("SELECT * FROM arcs WHERE id = :id LIMIT 1")
     ArcEntity getArcById(String id);
}