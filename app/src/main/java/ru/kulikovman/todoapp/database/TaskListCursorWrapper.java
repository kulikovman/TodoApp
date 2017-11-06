package ru.kulikovman.todoapp.database;


import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.List;
import java.util.UUID;

import ru.kulikovman.todoapp.database.DbSchema.TaskTable;
import ru.kulikovman.todoapp.database.DbSchema.GroupTable;
import ru.kulikovman.todoapp.models.Group;
import ru.kulikovman.todoapp.models.Task;


public class TaskListCursorWrapper extends android.database.CursorWrapper {
    private List<Group> mGroups;

    public TaskListCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public TaskListCursorWrapper(Cursor cursor, List<Group> groups) {
        super(cursor);
        mGroups = groups;
    }

    public Task getTask() {
        String uuid = getString(getColumnIndex(TaskTable.Cols.UUID));
        String title = getString(getColumnIndex(TaskTable.Cols.TITLE));
        int priority = getInt(getColumnIndex(TaskTable.Cols.PRIORITY));
        int done = getInt(getColumnIndex(TaskTable.Cols.DONE));
        long createDate = getLong(getColumnIndex(TaskTable.Cols.CREATE_DATE));
        long targetDate = getLong(getColumnIndex(TaskTable.Cols.TARGET_DATE));
        long completionDate = getLong(getColumnIndex(TaskTable.Cols.COMPLETION_DATE));
        long repeatDate = getLong(getColumnIndex(TaskTable.Cols.REPEAT_DATE));
        long reminderDate = getLong(getColumnIndex(TaskTable.Cols.REMINDER_DATE));
        String group = getString(getColumnIndex(TaskTable.Cols.GROUP));



        return new Task(UUID.fromString(uuid), title, date, priority, color, repeat, done != 0);



    }

    public Group getGroup() {
        String name = getString(getColumnIndex(GroupTable.Cols.NAME));
        String description = getString(getColumnIndex(GroupTable.Cols.DESCRIPTION));
        String color = getString(getColumnIndex(GroupTable.Cols.COLOR));

        return new Group(name, description, color);
    }
}
