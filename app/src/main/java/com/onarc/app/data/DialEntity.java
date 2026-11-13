package com.onarc.app.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.UUID;

@Entity(tableName = "dials")
public class DialEntity {

    @PrimaryKey
    @NonNull
    private String id = UUID.randomUUID().toString();

    private String name;
    private boolean isDefault;

    public enum Tags {
        WEEKDAY,
        WEEKEND,
        VACATION,
        HOLIDAY,
        BIRTHDAY,
        FAMILY,
        FRIENDS,
        PARTNER
    }

    // Empty constructor required by Room
    public DialEntity() {
    }

    // --- Getters & Setters ---

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
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
