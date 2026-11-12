package com.onarc.app.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ArcEntity.class}, version = 1, exportSchema = false)
public abstract class OnArcDatabase extends RoomDatabase {
     private static OnArcDatabase instance;

     public static synchronized OnArcDatabase getInstance(Context context) {
          if (instance == null) {
               instance = Room.databaseBuilder(context.getApplicationContext(),
                         OnArcDatabase.class, "onarc_database").build();
          }
          return instance;
     }
     public abstract ArcDao arcDao();
}