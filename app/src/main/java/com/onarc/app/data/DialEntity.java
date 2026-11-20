package com.onarc.app.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "dials")
public class DialEntity {

    @PrimaryKey(autoGenerate = true)
    private long dialId;
    private String name;
    public boolean isDefault; // Bool for Creator Tag replacement

    // Empty constructor required by Room
    public DialEntity() {
    }

    // --- Getters & Setters ---

    public long getDialId() {
        return dialId;
    }

    public void setDialId(long dialId) {
        this.dialId = dialId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
}
