package ru.kulikovman.todoapp.models;

import java.util.Date;
import java.util.UUID;

public class Task {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private int mPriority;
    private String mColor;
    private Date mRepeat;
    private boolean mDone;

    public Task(UUID id, String title, Date date, int priority, String color, Date repeat, boolean done) {
        mId = id;
        mTitle = title;
        mDate = date;
        mPriority = priority;
        mColor = color;
        mRepeat = repeat;
        mDone = done;
    }

    public Task(String title, Date date, int priority, String color, Date repeat) {
        mId = UUID.randomUUID();
        mTitle = title;
        mDate = date;
        mPriority = priority;
        mColor = color;
        mRepeat = repeat;
        mDone = false;
    }

    public Task(String title) {
        mId = UUID.randomUUID();
        mTitle = title;
    }

    public Task(UUID id) {
        mId = id;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public int getPriority() {
        return mPriority;
    }

    public void setPriority(int priority) {
        mPriority = priority;
    }

    public String getColor() {
        return mColor;
    }

    public void setColor(String color) {
        mColor = color;
    }

    public Date getRepeat() {
        return mRepeat;
    }

    public void setRepeat(Date repeat) {
        mRepeat = repeat;
    }

    public boolean isDone() {
        return mDone;
    }

    public void setDone(boolean done) {
        mDone = done;
    }
}
