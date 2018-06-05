package com.futhark.android.seevoice.model.database

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns
import java.io.Serializable

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
  val COLUMNS = arrayOf(_ID, COLUMN_NAME_ACCOUNT, COLUMN_NAME_PASSWORD_MD5,
      COLUMN_NAME_TITLE, COLUMN_NAME_LANG, COLUMN_NAME_FULL_NAME, COLUMN_NAME_CREATED_AT,
      COLUMN_NAME_STATUS, COLUMN_NAME_ORDER)

  class VoiceAccountEntry(cursor: Cursor) : Serializable{
    val id: Long = cursor.getLong(cursor.getColumnIndex(_ID))
    val account = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ACCOUNT))
    val password = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PASSWORD_MD5))
    val acountTitle = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TITLE))
    val lang = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_LANG))
    val fullName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_FULL_NAME))
    var createdAt = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_CREATED_AT))
    var status = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_STATUS))
    var sort = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ORDER))
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
    builder.append(COLUMN_NAME_CREATED_AT).append(textType).append(commaSep)
    builder.append(COLUMN_NAME_STATUS).append(" integer").append(commaSep)
    builder.append(COLUMN_NAME_ORDER).append(" integer").append(commaSep)
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
}
