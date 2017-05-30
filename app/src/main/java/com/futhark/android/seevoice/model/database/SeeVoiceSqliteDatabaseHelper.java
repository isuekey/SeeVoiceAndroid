package com.futhark.android.seevoice.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLite Database Open Helper
 * Created by liuhr on 12/04/2017.
 */

public class SeeVoiceSqliteDatabaseHelper extends SQLiteOpenHelper {
    public SeeVoiceSqliteDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
