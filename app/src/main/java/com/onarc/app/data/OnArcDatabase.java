package com.onarc.app.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// TODO (Claude edit, explain to Matthew later):
//  ADDED:
//   - DialEntity.class to the entities list (the database now holds a dials table)
//   - dialDao() at the bottom (hands out the Dial DAO, same idea as arcDao())
//  CHANGED:
//   - version from 1 to 2 (the database's layout changed, so the number goes up)
//  REMOVED:
//   - nothing
@Database(entities = {ArcEntity.class, DialEntity.class}, version = 2, exportSchema = false)
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

     public abstract DialDao dialDao();
}
