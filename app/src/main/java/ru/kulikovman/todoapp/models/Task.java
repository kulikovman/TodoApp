package ru.kulikovman.todoapp.models;

import java.util.Date;
import java.util.UUID;

public class Task {
    private UUID mId;
    private String mTitle;
    private int mPriority;
    private boolean mDone;

    private long mCreateDate;
    private long mTargetDate;
    private long mCompletionDate;
    private long mRepeatDate;
    private long mReminderDate;

    private Group mGroup;

    public Task(UUID id, String title, int priority, boolean done, long createDate, long targetDate, long completionDate, long repeatDate, long reminderDate, Group group) {
        mId = id;
        mTitle = title;
        mPriority = priority;
        mDone = done;
        mCreateDate = createDate;
        mTargetDate = targetDate;
        mCompletionDate = completionDate;
        mRepeatDate = repeatDate;
        mReminderDate = reminderDate;
        mGroup = group;
    }

    public Task(String title) {
        mId = UUID.randomUUID();
        mTitle = title;
        mCreateDate = new Date().getTime();
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

    public int getPriority() {
        return mPriority;
    }

    public void setPriority(int priority) {
        mPriority = priority;
    }

    public boolean isDone() {
        return mDone;
    }

    public void setDone(boolean done) {
        mDone = done;
    }

    public long getCreateDate() {
        return mCreateDate;
    }

    public void setCreateDate(long createDate) {
        mCreateDate = createDate;
    }

    public long getTargetDate() {
        return mTargetDate;
    }

    public void setTargetDate(long targetDate) {
        mTargetDate = targetDate;
    }

    public long getCompletionDate() {
        return mCompletionDate;
    }

    public void setCompletionDate(long completionDate) {
        mCompletionDate = completionDate;
    }

    public long getRepeatDate() {
        return mRepeatDate;
    }

    public void setRepeatDate(long repeatDate) {
        mRepeatDate = repeatDate;
    }

    public long getReminderDate() {
        return mReminderDate;
    }

    public void setReminderDate(long reminderDate) {
        mReminderDate = reminderDate;
    }

    public Group getGroup() {
        return mGroup;
    }

    public void setGroup(Group group) {
        mGroup = group;
    }
}
