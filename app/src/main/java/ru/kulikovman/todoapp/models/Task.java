package ru.kulikovman.todoapp.models;

import java.util.UUID;

public class Task {
    // TODO: 29.09.2017 Изменить типы полей
    // для даты, приоритета, цвета, повтора
    private UUID mId;
    private String mTitle;
    private String mDate;
    private String mPriority;
    private String mColor;
    private String mRepeat;
    private boolean mDone;

    // Новые поля, их пока нет в базе
    // также для них нет геттеров и сеттеров и их нет в конструкторе
    private long mDateOfCreation;
    private long mDateOfCompletion;
    private Group mGroup;

    public Task(UUID id, String title, String date, String priority, String color, String repeat, boolean done) {
        mId = id;
        mTitle = title;
        mDate = date;
        mPriority = priority;
        mColor = color;
        mRepeat = repeat;
        mDone = done;
    }

    public Task(String title, String date, String priority, String color, String repeat) {
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

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getPriority() {
        return mPriority;
    }

    public void setPriority(String priority) {
        mPriority = priority;
    }

    public String getColor() {
        return mColor;
    }

    public void setColor(String color) {
        mColor = color;
    }

    public String getRepeat() {
        return mRepeat;
    }

    public void setRepeat(String repeat) {
        mRepeat = repeat;
    }

    public boolean isDone() {
        return mDone;
    }

    public void setDone(boolean done) {
        mDone = done;
    }
}
