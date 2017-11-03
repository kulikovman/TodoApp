package ru.kulikovman.todoapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ru.kulikovman.todoapp.database.DbHelper;
import ru.kulikovman.todoapp.database.TaskListCursorWrapper;
import ru.kulikovman.todoapp.database.DbSchema.TaskTable;
import ru.kulikovman.todoapp.models.Group;
import ru.kulikovman.todoapp.models.Task;

public class TaskLab {
    private static TaskLab sTaskLab;

    private Context mContext;
    private DbHelper mDbHelper;

    private List<Task> mTasks;
    private List<Group> mGroups;

    public static TaskLab get(Context context) {
        if (sTaskLab == null) {
            sTaskLab = new TaskLab(context);
        }
        return sTaskLab;
    }

    private TaskLab(Context context) {
        mContext = context.getApplicationContext();
        mDbHelper = new DbHelper(mContext);

        // Подгружаем из базы группы и задачи
        //mTasks = mDbHelper.getAllTasks();
        mGroups = mDbHelper.getAllGroups();
    }

    public List<Task> getTasks() {
        return mTasks;
    }

    public List<Group> getGroups() {
        return mGroups;
    }

















    /*public void addTask(Task task) {
        ContentValues values = getTaskContentValues(task);
        mDatabase.insert(TaskTable.NAME, null, values);
    }

    public List<Task> getTaskList() {
        List<Task> tasks = new ArrayList<>();

        TaskListCursorWrapper cursor = queryTasks(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                tasks.add(cursor.getTask());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return tasks;
    }

    public Task getTask(UUID id) {
        TaskListCursorWrapper cursor = queryTasks(TaskTable.Cols.UUID + " = ?", new String[]{id.toString()});

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getTask();
        } finally {
            cursor.close();
        }
    }

    public void updateTask(Task task) {
        String uuid = task.getId().toString();
        ContentValues values = getTaskContentValues(task);

        mDatabase.update(TaskTable.NAME, values, TaskTable.Cols.UUID + " = ?", new String[]{uuid});
    }

    private static ContentValues getTaskContentValues(Task task) {
        ContentValues values = new ContentValues();
        values.put(TaskTable.Cols.UUID, task.getId().toString());
        values.put(TaskTable.Cols.TITLE, task.getTitle());
        values.put(TaskTable.Cols.DATE, task.getDate());
        values.put(TaskTable.Cols.PRIORITY, task.getPriority());
        values.put(TaskTable.Cols.COLOR, task.getColor());
        values.put(TaskTable.Cols.REPEAT, task.getRepeat());
        values.put(TaskTable.Cols.DONE, task.isDone() ? 1 : 0);

        return values;
    }

    private TaskListCursorWrapper queryTasks(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(TaskTable.NAME,
                null, // null - выбирает все столбцы
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );

        return new TaskListCursorWrapper(cursor);
    }*/
}
