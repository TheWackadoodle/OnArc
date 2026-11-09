package com.onarc.app.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.UUID;

@Entity(tableName = "arcs")
public class ArcEntity {

    @PrimaryKey
    @NonNull
    private String id = UUID.randomUUID().toString();

    // Core fields
    private String name;
    private String emoji;
    private String notes;
    private int startMinute;
    private int endMinute;
    private int color;

    // Behaviors
    private boolean focusEnabled;
    private boolean isDefault;

    // Nudges
    private int nudgeMinutesBefore;
    private boolean nudgeEnabled;
    private boolean nudgeVibrate;
    private boolean nudgeSound;

    // Default Constructor required by Room
    public ArcEntity() {
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

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isFocusEnabled() {
        return focusEnabled;
    }

    public void setFocusEnabled(boolean focusEnabled) {
        this.focusEnabled = focusEnabled;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public long getNudgeMinutesBefore() {
        return nudgeMinutesBefore;
    }

    public void setNudgeMinutesBefore(int nudgeMinutesBefore) {
        this.nudgeMinutesBefore = nudgeMinutesBefore;
    }

    public boolean isNudgeEnabled() {
        return nudgeEnabled;
    }

    public void setNudgeEnabled(boolean nudgeEnabled) {
        this.nudgeEnabled = nudgeEnabled;
    }

    public boolean isNudgeVibrate() {
        return nudgeVibrate;
    }

    public void setNudgeVibrate(boolean nudgeVibrate) {
        this.nudgeVibrate = nudgeVibrate;
    }

    public boolean isNudgeSound() {
        return nudgeSound;
    }

    public void setNudgeSound(boolean nudgeSound) {
        this.nudgeSound = nudgeSound;
    }
}