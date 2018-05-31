package com.futhark.android.seevoice.model.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * SQLite Database Open Helper
 * Created by liuhr on 12/04/2017.
 */

class SeeVoiceSqliteDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

  override fun onCreate(db: SQLiteDatabase) {
    db.execSQL(TableVoiceSpecification.createTableSql())
  }

  override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    when (oldVersion) {
      1 -> handleUpgradeFrom0001(db, newVersion)
    }
  }

  private fun handleUpgradeFrom0001(db: SQLiteDatabase, newVersion: Int) {
    when (newVersion) {
      2 -> {
      }
    }
  }

  companion object {

    val DATABASE_VERSION = 1
    val DATABASE_NAME = "SeeVoice.db"
  }

}
