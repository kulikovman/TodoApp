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
import ru.kulikovman.todoapp.database.TodoDbSchema.GroupTable;
import ru.kulikovman.todoapp.models.Group;
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

        db.execSQL("create table " + GroupTable.NAME + "(_id integer primary key autoincrement, " +
                GroupTable.Cols.NAME + ", " +
                GroupTable.Cols.DESCRIPTION + ", " +
                GroupTable.Cols.COLOR + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exist " + TaskTable.NAME);
        db.execSQL("drop table if exist " + GroupTable.NAME);
        onCreate(db);
    }


    // Методы для работы с ЗАДАЧАМИ
    public void addTask(Task task) {
        mDb = this.getWritableDatabase();
        ContentValues values = getTaskContentValues(task);
        mDb.insertWithOnConflict(TaskTable.NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        mDb.close();
    }

    public void updateTask(Task task) {
        mDb = this.getWritableDatabase();
        ContentValues values = getTaskContentValues(task);
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

    private static ContentValues getTaskContentValues(Task task) {
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


    // Методы для работы с ГРУППАМИ
    public void addGroup(Group group) {
        mDb = this.getWritableDatabase();
        ContentValues values = getGroupContentValues(group);
        mDb.insertWithOnConflict(GroupTable.NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        mDb.close();
    }

    public void updateGroup(Group group) {
        mDb = this.getWritableDatabase();
        ContentValues values = getGroupContentValues(group);
        mDb.update(GroupTable.NAME, values, GroupTable.Cols.NAME + " = ?", new String[]{group.getName()});
        mDb.close();
    }

    public void deleteGroup(Group group) {
        mDb = this.getWritableDatabase();
        mDb.delete(GroupTable.NAME, GroupTable.Cols.NAME + " = ?", new String[]{group.getName()});
        mDb.close();
    }

    public List<Group> getAllGroups() {
        List<Group> groups = new ArrayList<>();

        try (TodoCursorWrapper cursor = queryGroups(null, null)) {
            Log.d("myLog", "Всего групп в базе: " + String.valueOf(cursor.getCount()));
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                groups.add(cursor.getGroup());
                cursor.moveToNext();
            }
        } finally {
            if (mDb != null) {
                mDb.close();
            }
        }
        return groups;
    }

    private static ContentValues getGroupContentValues(Group group) {
        ContentValues values = new ContentValues();
        values.put(GroupTable.Cols.NAME, group.getName());
        values.put(GroupTable.Cols.DESCRIPTION, group.getDescription());
        values.put(GroupTable.Cols.COLOR, group.getColorId());

        return values;
    }

    private TodoCursorWrapper queryGroups(String where, String[] args) {
        mDb = this.getReadableDatabase();

        Cursor cursor = mDb.query(GroupTable.NAME,
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
