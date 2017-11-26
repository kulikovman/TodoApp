package ru.kulikovman.todoapp.models;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;

public class Task extends RealmObject {
    public static final String ID = "mId";
    public static final String TITLE = "mTitle";
    public static final String PRIORITY = "mPriority";
    public static final String DONE = "mDone";
    public static final String CREATE_DATE = "mCreateDate";
    public static final String TARGET_DATE = "mTargetDate";
    public static final String COMPLETION_DATE = "mCompletionDate";
    public static final String REPEAT = "mRepeat";
    public static final String REMINDER = "mReminder";
    public static final String GROUP = "mGroup";

    private String mId;
    private String mTitle;
    private int mPriority;
    private boolean mDone;
    private long mCreateDate;
    private long mTargetDate;
    private long mCompletionDate;
    private String mRepeat;
    private boolean mReminder;
    private Group mGroup;

    public Task(String id, String title, int priority, boolean done, long createDate, long targetDate, long completionDate, String repeat, boolean reminder) {
        mId = id;
        mTitle = title;
        mPriority = priority;
        mDone = done;
        mCreateDate = createDate;
        mTargetDate = targetDate;
        mCompletionDate = completionDate;
        mRepeat = repeat;
        mReminder = reminder;
    }

    public Task(String title) {
        mId = UUID.randomUUID().toString();
        mCreateDate = new Date().getTime();
        mTitle = title;
        mDone = false;
    }

    public Task() {
    }

    public String getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id.toString();
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

    public String getRepeat() {
        return mRepeat;
    }

    public void setRepeat(String repeat) {
        mRepeat = repeat;
    }

    public boolean getReminder() {
        return mReminder;
    }

    public void setReminder(boolean reminder) {
        mReminder = reminder;
    }

    public Group getGroup() {
        return mGroup;
    }

    public void setGroup(Group group) {
        mGroup = group;
    }
}
