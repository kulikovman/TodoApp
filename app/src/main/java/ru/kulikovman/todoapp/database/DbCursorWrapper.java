package ru.kulikovman.todoapp.database;


import android.database.Cursor;

import java.util.List;
import java.util.UUID;

import ru.kulikovman.todoapp.database.DbSchema.TaskTable;
import ru.kulikovman.todoapp.database.DbSchema.GroupTable;
import ru.kulikovman.todoapp.models.Group;
import ru.kulikovman.todoapp.models.Task;


public class DbCursorWrapper extends android.database.CursorWrapper {
    private List<Group> mGroups;

    public DbCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public DbCursorWrapper(Cursor cursor, List<Group> groups) {
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
        String repeatDate = getString(getColumnIndex(TaskTable.Cols.REPEAT_DATE));
        long reminderDate = getLong(getColumnIndex(TaskTable.Cols.REMINDER_DATE));
        String groupName = getString(getColumnIndex(TaskTable.Cols.GROUP));

        // Создаем задачу
        Task task = new Task(UUID.fromString(uuid), title, priority, done != 0,
                createDate, targetDate, completionDate, repeatDate, reminderDate);

        // Назначаем группу
        //if (mGroups != null) {
            for (Group group : mGroups) {
                if (group.getName().equals(groupName)) {
                    task.setGroup(group);
                    break;
                }
            }
        //}

        return task;
    }

    public Group getGroup() {
        String name = getString(getColumnIndex(GroupTable.Cols.NAME));
        String description = getString(getColumnIndex(GroupTable.Cols.DESCRIPTION));
        String color = getString(getColumnIndex(GroupTable.Cols.COLOR));

        return new Group(name, description, color);
    }
}
