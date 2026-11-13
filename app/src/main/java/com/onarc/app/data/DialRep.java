package com.onarc.app.data;

import android.content.Context;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// TODO (Claude wrote this whole file, explain it to Matthew later):
//  - same pattern as ArcRep, but for Dials
//  - walk through the new passthroughs: getDefaultDial() and getArcsForDial()
public class DialRep {

     private final ExecutorService executor =
               Executors.newSingleThreadExecutor();
     private final DialDao dialDao;

     public DialRep(Context context) {
          dialDao = OnArcDatabase.getInstance(context).dialDao();
     }

     public LiveData<List<DialEntity>> getAllDials() {
          return dialDao.getAllDials();
     }

     public LiveData<DialEntity> getDialById(String id) {
          return dialDao.getDialById(id);
     }

     public LiveData<DialEntity> getDefaultDial() {
          return dialDao.getDefaultDial();
     }

     public LiveData<List<ArcEntity>> getArcsForDial(String dialId) {
          return dialDao.getArcsForDial(dialId);
     }

     public void insert(DialEntity dial) {
          executor.execute(() -> dialDao.insert(dial));
     }

     public void update(DialEntity dial) {
          executor.execute(() -> dialDao.update(dial));
     }

     public void delete(DialEntity dial) {
          executor.execute(() -> dialDao.delete(dial));
     }
}
