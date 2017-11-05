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

import ru.kulikovman.todoapp.database.DbSchema.TaskTable;
import ru.kulikovman.todoapp.database.DbSchema.GroupTable;
import ru.kulikovman.todoapp.models.Group;
import ru.kulikovman.todoapp.models.Task;


public class DbHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "TaskListBase.db";

    private SQLiteDatabase mDb;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("myLog", "Запущен onCreate в DbHelper");

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
        Log.d("myLog", "Запущен onUpgrade в DbHelper");

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

        try (TaskListCursorWrapper cursor = queryTasks(null, null)) {
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
        try (TaskListCursorWrapper cursor = queryTasks(TaskTable.Cols.UUID + " = ?", new String[]{id.toString()})) {
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

    private TaskListCursorWrapper queryTasks(String where, String[] args) {
        mDb = this.getReadableDatabase();

        Cursor cursor = mDb.query(TaskTable.NAME,
                null, // columns - Список полей, которые мы хотим получить
                where,  // selection - Строка условия WHERE
                args, // selectionArgs - Массив аргументов для selection
                null, // groupBy - Группировка
                null, // having - Использование условий для агрегатных функций
                null // orderBy - Сортировка
        );

        return new TaskListCursorWrapper(cursor);
    }


    // Методы для работы с ГРУППАМИ
    public void addGroup(Group group) {
        Log.d("myLog", "Запущен addGroup в DbHelper");

        mDb = this.getWritableDatabase();
        ContentValues values = getGroupContentValues(group);
        mDb.insertWithOnConflict(GroupTable.NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        mDb.close();
    }

    public void updateGroup(Group group) {
        mDb = this.getWritableDatabase();
        ContentValues values = getGroupContentValues(group);
        mDb.update(GroupTable.NAME, values, GroupTable.Cols.UUID + " = ?", new String[]{group.getId().toString()});
        mDb.close();
    }

    public void deleteGroup(Group group) {
        mDb = this.getWritableDatabase();
        mDb.delete(GroupTable.NAME, GroupTable.Cols.UUID + " = ?", new String[]{group.getId().toString()});
        mDb.close();
    }

    public List<Group> getAllGroups() {
        List<Group> groups = new ArrayList<>();

        try (TaskListCursorWrapper cursor = queryGroups(null, null)) {
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

    public Group getGroupByUUID(UUID id) {
        try (TaskListCursorWrapper cursor = queryGroups(GroupTable.Cols.UUID + " = ?", new String[]{id.toString()})) {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getGroup();
        } finally {
            if (mDb != null) {
                mDb.close();
            }
        }
    }

    private static ContentValues getGroupContentValues(Group group) {
        ContentValues values = new ContentValues();
        values.put(GroupTable.Cols.NAME, group.getName());
        values.put(GroupTable.Cols.DESCRIPTION, group.getDescription());
        values.put(GroupTable.Cols.COLOR, group.getColor());

        return values;
    }

    private TaskListCursorWrapper queryGroups(String where, String[] args) {
        mDb = this.getReadableDatabase();

        Cursor cursor = mDb.query(GroupTable.NAME,
                null, // columns - Список полей, которые мы хотим получить
                where,  // selection - Строка условия WHERE
                args, // selectionArgs - Массив аргументов для selection
                null, // groupBy - Группировка
                null, // having - Использование условий для агрегатных функций
                null // orderBy - Сортировка
        );

        return new TaskListCursorWrapper(cursor);
    }
}
