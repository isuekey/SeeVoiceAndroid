package com.futhark.android.seevoice.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLite Database Open Helper
 * Created by liuhr on 12/04/2017.
 */

public class SeeVoiceSqliteDatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static  final String DATABASE_NAME = "SeeVoice.db";

    public SeeVoiceSqliteDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TableVoiceSpecification.createTableSql());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion){
            case 1:
                handleUpgradeFrom0001(db, newVersion);
                break;
        }
    }

    private void handleUpgradeFrom0001(SQLiteDatabase db, int newVersion) {
        switch (newVersion){
            case 2:
                break;
        }
    }

}
