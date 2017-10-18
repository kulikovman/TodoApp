package ru.kulikovman.todoapp.database;


import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.UUID;

import ru.kulikovman.todoapp.database.DbSchema.TaskTable;
import ru.kulikovman.todoapp.database.DbSchema.GroupTable;
import ru.kulikovman.todoapp.models.Group;
import ru.kulikovman.todoapp.models.Task;


public class TaskListCursorWrapper extends android.database.CursorWrapper {

    public TaskListCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Task getTask() {
        String uuid = getString(getColumnIndex(TaskTable.Cols.UUID));
        String title = getString(getColumnIndex(TaskTable.Cols.TITLE));
        String date = getString(getColumnIndex(TaskTable.Cols.DATE));
        String priority = getString(getColumnIndex(TaskTable.Cols.PRIORITY));
        String color = getString(getColumnIndex(TaskTable.Cols.COLOR));
        String repeat = getString(getColumnIndex(TaskTable.Cols.REPEAT));
        int done = getInt(getColumnIndex(TaskTable.Cols.DONE));

        return new Task(UUID.fromString(uuid), title, date, priority, color, repeat, done != 0);
    }

    public Group getGroup() {
        String name = getString(getColumnIndex(GroupTable.Cols.NAME));
        String description = getString(getColumnIndex(GroupTable.Cols.DESCRIPTION));
        int color = getInt(getColumnIndex(GroupTable.Cols.COLOR));

        return new Group(name, description, color);
    }
}
