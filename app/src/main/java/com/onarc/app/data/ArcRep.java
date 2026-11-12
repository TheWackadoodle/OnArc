package com.onarc.app.data;

import android.content.Context;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ArcRep {

     private final ExecutorService executor =
               Executors.newSingleThreadExecutor();
     private final ArcDao arcDao;

     public ArcRep(Context context) {
          arcDao = OnArcDatabase.getInstance(context).arcDao();
     }

     public LiveData<List<ArcEntity>> getAllArcs() {
          return arcDao.getAllArcs();
     }

     public void insert(ArcEntity arc) {
          executor.execute(() -> arcDao.insert(arc));
     }

     public LiveData<ArcEntity> getArcById(String id) {
          return arcDao.getArcById(id);
     }

     public void update(ArcEntity arc) {
          executor.execute(() -> arcDao.update(arc));
     }

     public void delete(ArcEntity arc) {
          executor.execute(() -> arcDao.delete(arc));
     }
}