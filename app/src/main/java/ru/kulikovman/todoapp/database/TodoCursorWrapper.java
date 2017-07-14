package ru.kulikovman.todoapp.database;


import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import ru.kulikovman.todoapp.database.TodoDbSchema.TaskTable;
import ru.kulikovman.todoapp.models.Task;


public class TodoCursorWrapper extends CursorWrapper {

    public TodoCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Task getTask() {
        String uuidString = getString(getColumnIndex(TaskTable.Cols.UUID));
        String title = getString(getColumnIndex(TaskTable.Cols.TITLE));
        long date = getLong(getColumnIndex(TaskTable.Cols.DATE));
        int priority = getInt(getColumnIndex(TaskTable.Cols.PRIORITY));
        String color = getString(getColumnIndex(TaskTable.Cols.COLOR));
        long repeat = getLong(getColumnIndex(TaskTable.Cols.REPEAT));
        int done = getInt(getColumnIndex(TaskTable.Cols.DONE));

        Task task = new Task(UUID.fromString(uuidString));
        task.setTitle(title);
        task.setColor(color);
        task.setDate(new Date(date));
        task.setPriority(priority);
        task.setRepeat(new Date(repeat));
        task.setDone(done != 0);

        return task;
    }
}
