package com.example.mutilnotes.dao;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.example.mutilnotes.models.NoteModel;

import java.util.ArrayList;
import java.util.List;

public class NoteRepo {
    private static volatile NoteRepo noteRepo;
    SQLiteHelper sqLiteHelper;

    private NoteRepo() {
    }

    public static NoteRepo getInstance() {
        if (noteRepo == null) {
            synchronized (NoteRepo.class) {
                if (noteRepo == null) {
                    noteRepo = new NoteRepo();
                }
            }
        }
        return noteRepo;
    }

    public long insert(Context context, NoteModel data) {
        sqLiteHelper = new SQLiteHelper(context);
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", data.getTitle());
        values.put("content", data.getContent());
        values.put("createDate", data.getLastModified());
        values.put("timer", data.getTimer());

        long id = db.insert("notes", null, values);
        return id;
    }

    public void update(Context context, NoteModel data) {
        sqLiteHelper = new SQLiteHelper(context);
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", data.getTitle());
        values.put("content", data.getContent());
        values.put("createDate", data.getLastModified());
        values.put("timer", data.getTimer());

        int rowsAffected = db.update("notes", values, "id=?", new String[]{String.valueOf(data.getId())});

        if (rowsAffected > 0) {
            Log.d("SQLite", "Update successful");
        } else {
            Log.d("SQLite", "Update failed");
        }
    }

    public List<NoteModel> selectAll(Context context) {
        sqLiteHelper = new SQLiteHelper(context);
        Cursor data = sqLiteHelper.readData("SELECT * FROM notes");
        List<NoteModel> result = new ArrayList<>();
        while (data.moveToNext()) {
            result.add(new NoteModel(
                    data.getInt(0),
                    data.getString(1),
                    data.getString(2),
                    data.getString(4),
                    data.getString(3)
            ));
        }
        return result;
    }

    public List<NoteModel> selectByTitle(Context context, String title) {
        sqLiteHelper = new SQLiteHelper(context);
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        List<NoteModel> result = new ArrayList<>();
        String[] selectionArgs = { "%" + title + "%" };
        Cursor data = db.rawQuery("SELECT * FROM notes WHERE title LIKE ?", selectionArgs);
        while (data.moveToNext()) {
            result.add(new NoteModel(
                    data.getInt(0),
                    data.getString(1),
                    data.getString(2),
                    data.getString(4),
                    data.getString(3)
            ));
        }
        data.close();
        return result;
    }

    public NoteModel selectById(Context context, int id) {
        sqLiteHelper = new SQLiteHelper(context);
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        NoteModel result = null;
        String[] selectionArgs = { String.valueOf(id) };
        Cursor data = db.rawQuery("SELECT * FROM notes WHERE id = ?", selectionArgs);
        if (data.moveToFirst()) {
            result = new NoteModel(
                    data.getInt(0),
                    data.getString(1),
                    data.getString(2),
                    data.getString(4),
                    data.getString(3)
            );
        }
        data.close();
        return result;
    }

    public void delete(Context context, int id) {
        sqLiteHelper = new SQLiteHelper(context);
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();

        String sql = "DELETE FROM notes WHERE id = ?";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.bindLong(1, id);

        statement.executeUpdateDelete();
        cancelSpecificNotification(context, id);
    }

    public void cancelSpecificNotification(Context context, int requestCode) {
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Intent notificationIntent = new Intent(context, NotificationActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
//
//        if (alarmManager != null) {
//            alarmManager.cancel(pendingIntent);
//        }
    }
}
