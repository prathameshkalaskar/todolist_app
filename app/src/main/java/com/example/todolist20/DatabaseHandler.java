package com.example.todolist20;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.todolist20.model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    public static final int VERSION = 1;
    public static final String NAME = "toDo ListDatabase";
    public static final String TODO_TABLE = "todo";
    public static final String ID = "id";
    public static final String STATUS = "status";
    public static final String TASK = "task";

    public static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, " + STATUS + " INTEGER)";

    public SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        onCreate(db);
    }

    public void OpenDatabase() {
        db = this.getWritableDatabase();
    }

    public void insertTask(ToDoModel task) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK, task.getTask());
        contentValues.put(STATUS, task.getStatus());
        db.insert(TODO_TABLE, null, contentValues);
    }

    @SuppressLint("Range")
    public List<ToDoModel> getAllTask() {
        List<ToDoModel> tasklist = new ArrayList<>();
        Cursor cursor = null;
        db.beginTransaction();
        try {
            cursor = db.query(TODO_TABLE, null, null, null, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        ToDoModel task = new ToDoModel();
                        task.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                        task.setTask(cursor.getString(cursor.getColumnIndex(TASK)));
                        task.setStatus(cursor.getInt(cursor.getColumnIndex(STATUS)));
                        tasklist.add(task);
                    }
                    while (cursor.moveToNext());
                }
            }
        } finally {
            db.endTransaction();
            cursor.close();
        }
        return tasklist;
    }


    public void updateStatus(int id, int status) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(STATUS, status);
        db.update(TODO_TABLE, contentValues, ID + "= ?", new String[]{String.valueOf(id)});
    }

    public void updateTask(int id, String task) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK, task);
        db.update(TODO_TABLE, contentValues, ID + "= ?", new String[]{String.valueOf(id)});
    }

    public void deleteTask(int id) {
        db.delete(TODO_TABLE, ID + "= ?", new String[]{String.valueOf(id)});
    }

}
