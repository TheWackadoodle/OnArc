package com.onarc.app.data;

import android.content.Context;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

     public LiveData<DialEntity> getDialById(Long dialId) {
          return dialDao.getDialById(dialId);
     }

     public LiveData<DialEntity> getDefaultDial() {
          return dialDao.getDefaultDial();
     }

     public LiveData<List<ArcEntity>> getArcsForDial(Long dialId) {
          return dialDao.getArcsForDial(dialId);
     }

     public interface InsertCallback {
          static void done(boolean saved) {

          }
     }

     public void insert(DialEntity dial) {
          executor.execute(() -> {
               int avalDials = dialDao.getDialCount();
               if (avalDials < 99) {
                    dialDao.insert(dial);
                    InsertCallback.done(true);
               } else {
                    InsertCallback.done(false);
               }
          });
     }

     public void update(DialEntity dial) {
          executor.execute(() -> {
               dialDao.update(dial);
          });
     }

     public void delete(DialEntity dial) {
          executor.execute(() -> dialDao.delete(dial));
     }

     public List<Long> dialIdsList() {
          return dialDao.getAllDialIds();
     }
}