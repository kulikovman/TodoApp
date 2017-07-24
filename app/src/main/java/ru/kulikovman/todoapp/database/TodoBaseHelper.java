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
    private static final int VERSION = 2;
    private static final String DATABASE_NAME = "todoAppBase.db";

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
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = getContentValues(task);

        db.insertWithOnConflict(TaskTable.NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = getContentValues(task);
        String uuid = task.getId().toString();

        db.update(TaskTable.NAME, values, TaskTable.Cols.UUID + " = ?", new String[]{uuid});
        db.close();
    }

    public void deleteTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TaskTable.NAME, TaskTable.Cols.UUID + " = ?", new String[]{task.getId().toString()});

        db.close();
    }

    public List<Task> getTaskList() {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TaskTable.NAME,
                null,
                TaskTable.Cols.DONE + " = ?",
                new String[]{"0"},
                null,
                null,
                null
        );

        Log.d("myLog", "Записей в базе: " + String.valueOf(cursor.getCount()));

        while (cursor.moveToNext()) {
            String uuid = cursor.getString(cursor.getColumnIndex(TaskTable.Cols.UUID));
            String title = cursor.getString(cursor.getColumnIndex(TaskTable.Cols.TITLE));
            String date = cursor.getString(cursor.getColumnIndex(TaskTable.Cols.DATE));
            String priority = cursor.getString(cursor.getColumnIndex(TaskTable.Cols.PRIORITY));
            String color = cursor.getString(cursor.getColumnIndex(TaskTable.Cols.COLOR));
            String repeat = cursor.getString(cursor.getColumnIndex(TaskTable.Cols.REPEAT));
            int done = cursor.getInt(cursor.getColumnIndex(TaskTable.Cols.DONE));

            taskList.add(new Task(UUID.fromString(uuid),
                    title, date, priority, color, repeat, done != 0));
        }

        cursor.close();
        db.close();
        return taskList;
    }


    public Task getTask(UUID id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TaskTable.NAME,
                null, // columns - Список полей, которые мы хотим получить
                TaskTable.Cols.UUID + " = ?", // selection - Строка условия WHERE
                new String[]{id.toString()}, // selectionArgs - Массив аргументов для selection
                null, // groupBy - Группировка
                null, // having - Использование условий для агрегатных функций
                null // orderBy - Сортировка
        );

        Log.d("myLog", "Записей в базе: " + String.valueOf(cursor.getCount()));

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();

            String uuid = cursor.getString(cursor.getColumnIndex(TaskTable.Cols.UUID));
            String title = cursor.getString(cursor.getColumnIndex(TaskTable.Cols.TITLE));
            String date = cursor.getString(cursor.getColumnIndex(TaskTable.Cols.DATE));
            String priority = cursor.getString(cursor.getColumnIndex(TaskTable.Cols.PRIORITY));
            String color = cursor.getString(cursor.getColumnIndex(TaskTable.Cols.COLOR));
            String repeat = cursor.getString(cursor.getColumnIndex(TaskTable.Cols.REPEAT));
            int done = cursor.getInt(cursor.getColumnIndex(TaskTable.Cols.DONE));

            Task task = new Task(UUID.fromString(uuid),
                    title, date, priority, color, repeat, done != 0);

            return task;
        } finally {
            cursor.close();
            db.close();
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
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TaskTable.NAME,
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
