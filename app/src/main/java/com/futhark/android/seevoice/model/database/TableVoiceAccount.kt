package com.futhark.android.seevoice.model.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.provider.BaseColumns
import java.io.Serializable
import java.util.*

object TableVoiceAccount {
  const val TABLE_NAME = "voice_account"
  const val COLUMN_NAME_ACCOUNT = "account"
  const val COLUMN_NAME_PASSWORD_MD5 = "password_md5"
  const val COLUMN_NAME_TITLE = "title"
  const val COLUMN_NAME_LANG = "language"
  const val COLUMN_NAME_FULL_NAME = "full_name"
  const val COLUMN_NAME_CREATED_AT = "created_at"
  const val COLUMN_NAME_STATUS = "status"
  const val COLUMN_NAME_ORDER = "sort"
  const val _ID = BaseColumns._ID
  const val _COUNT = BaseColumns._COUNT
  private const val WHERE_STATUS_IS = " status=? or status='-1' "
  private const val STATUS_IS_ACTIVE = "1"
  val COLUMNS = arrayOf(_ID, COLUMN_NAME_ACCOUNT, COLUMN_NAME_PASSWORD_MD5,
      COLUMN_NAME_TITLE, COLUMN_NAME_LANG, COLUMN_NAME_FULL_NAME, COLUMN_NAME_CREATED_AT,
      COLUMN_NAME_STATUS, COLUMN_NAME_ORDER)

  class VoiceAccountEntry(cursor: Cursor) : Serializable{
    val id: Long = cursor.getLong(cursor.getColumnIndex(_ID))
    val account: String? = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ACCOUNT))
    val password: String? = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PASSWORD_MD5))
    val accountTitle: String? = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TITLE))
    val lang: String? = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_LANG))
    val fullName: String? = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_FULL_NAME))
    var createdAt: String? = Date(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_CREATED_AT))).toString()
    var status: Int? = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_STATUS))
    var sort: Int? = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ORDER))
  }

  fun createTableSql() : String {
    val textType = " text"
    val commaSep = ","
    val builder = StringBuilder()
    builder.append("create table ")
    builder.append(TABLE_NAME).append(" ( ")
    builder.append(_ID).append(" integer primary key,")
    builder.append(COLUMN_NAME_ACCOUNT).append(textType).append(commaSep)
    builder.append(COLUMN_NAME_PASSWORD_MD5).append(textType).append(commaSep)
    builder.append(COLUMN_NAME_TITLE).append(textType).append(commaSep)
    builder.append(COLUMN_NAME_LANG).append(textType).append(commaSep)
    builder.append(COLUMN_NAME_FULL_NAME).append(textType).append(commaSep)
    builder.append(COLUMN_NAME_CREATED_AT).append(" integer").append(commaSep)
    builder.append(COLUMN_NAME_STATUS).append(" integer").append(commaSep)
    builder.append(COLUMN_NAME_ORDER).append(" integer")
    builder.append(" ) ")
    return builder.toString()
  }

  fun dropTableSql(): String {
    val builder = StringBuilder()
    builder.append("drop table if exists ").append(TABLE_NAME)
    return builder.toString()
  }

  fun tableUri(context: Context): Uri {
    return Uri.parse("content://" + context.packageName + "/" + TABLE_NAME)
  }

  fun getVoiceAccountEntry(database: SQLiteDatabase) : VoiceAccountEntry {
    val cursor = database.query(TABLE_NAME,
        COLUMNS, WHERE_STATUS_IS, arrayOf(STATUS_IS_ACTIVE), null,
        null, null)
    val accountEntry: VoiceAccountEntry?
    if (cursor.moveToFirst()) {
      accountEntry = VoiceAccountEntry(cursor)
      cursor.close()
      return accountEntry
    }
    val values = ContentValues()
    values.put(COLUMN_NAME_ACCOUNT, "anonymous account")
    values.put(COLUMN_NAME_LANG, "zh-cn")
    values.put(COLUMN_NAME_TITLE, "anonymous")
    values.put(COLUMN_NAME_FULL_NAME, "mysterious name")
    values.put(COLUMN_NAME_STATUS, -1)
    values.put(COLUMN_NAME_CREATED_AT, Date().time)
    database.insert(TABLE_NAME, null, values)
    accountEntry = getVoiceAccountEntry(database)
    cursor.close()
    return accountEntry
  }
}
