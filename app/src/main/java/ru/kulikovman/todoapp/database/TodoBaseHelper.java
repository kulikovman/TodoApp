package ru.kulikovman.todoapp.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ru.kulikovman.todoapp.database.TodoDbSchema.TaskTable;
import ru.kulikovman.todoapp.models.Task;


public class TodoBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "todoBase.db";

    private SQLiteDatabase mDb;

    public TodoBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TaskTable.NAME + "(_id integer primary key autoincrement, " +
                TaskTable.Cols.UUID + ", " +
                TaskTable.Cols.TITLE + ", " +
                TaskTable.Cols.DATE + ", " +
                TaskTable.Cols.PRIORITY + ", " +
                TaskTable.Cols.COLOR + ", " +
                TaskTable.Cols.REPEAT + ", " +
                TaskTable.Cols.DONE + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exist " + TaskTable.NAME);
        onCreate(db);
    }

    public void addTask(Task task) {
        mDb = this.getWritableDatabase();
        ContentValues values = getContentValues(task);
        mDb.insertWithOnConflict(TaskTable.NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        mDb.close();
    }

    public void updateTask(Task task) {
        mDb = this.getWritableDatabase();
        ContentValues values = getContentValues(task);
        mDb.update(TaskTable.NAME, values, TaskTable.Cols.UUID + " = ?", new String[]{task.getId().toString()});
        mDb.close();
    }

    public void deleteTask(Task task) {
        mDb = this.getWritableDatabase();
        mDb.delete(TaskTable.NAME, TaskTable.Cols.UUID + " = ?", new String[]{task.getId().toString()});
        mDb.close();
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();

        try (TodoCursorWrapper cursor = queryTasks(null, null)) {
            Log.d("myLog", "Всего задач в базе: " + String.valueOf(cursor.getCount()));
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                tasks.add(cursor.getTask());
                cursor.moveToNext();
            }
        } finally {
            if (mDb != null) {
                mDb.close();
            }
        }
        return tasks;
    }

    public Task getTaskByUUID(UUID id) {
        try (TodoCursorWrapper cursor = queryTasks(TaskTable.Cols.UUID + " = ?", new String[]{id.toString()})) {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getTask();
        } finally {
            if (mDb != null) {
                mDb.close();
            }
        }
    }

    private static ContentValues getContentValues(Task task) {
        ContentValues values = new ContentValues();
        values.put(TaskTable.Cols.UUID, task.getId().toString());
        values.put(TaskTable.Cols.TITLE, task.getTitle());
        values.put(TaskTable.Cols.DATE, task.getDate());
        values.put(TaskTable.Cols.PRIORITY, task.getPriority());
        values.put(TaskTable.Cols.COLOR, task.getColor());
        values.put(TaskTable.Cols.REPEAT, task.getRepeat());
        values.put(TaskTable.Cols.DONE, task.isDone() ? "1" : "0");

        return values;
    }

    private TodoCursorWrapper queryTasks(String where, String[] args) {
        mDb = this.getReadableDatabase();

        Cursor cursor = mDb.query(TaskTable.NAME,
                null, // columns - Список полей, которые мы хотим получить
                where,  // selection - Строка условия WHERE
                args, // selectionArgs - Массив аргументов для selection
                null, // groupBy - Группировка
                null, // having - Использование условий для агрегатных функций
                null // orderBy - Сортировка
        );

        return new TodoCursorWrapper(cursor);
    }
}
