package com.futhark.android.seevoice.model.database

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns
import android.util.Log

import com.futhark.android.seevoice.model.constant.AppConstant

import java.io.Serializable
import java.nio.ByteBuffer

/**
 * voice exercise
 * Created by liuhr on 01/06/2017.
 */

object TableVoiceExercise {

  var COLUMNS = arrayOf<String>(VoiceExcercise._ID, VoiceExcercise.COLUMN_NAME_LOCALE,
      VoiceExcercise.COLUMN_NAME_TITLE, VoiceExcercise.COLUMN_NAME_PHONETIC,
      VoiceExcercise.COLUMN_NAME_DESCRIPTION, VoiceExcercise.COLUMN_NAME_AUTHOR,
      VoiceExcercise.COLUMN_NAME_ACCOUNT, VoiceExcercise.COLUMN_NAME_ORDER,
      VoiceExcercise.COLUMN_NAME_MAX_VOLUME, VoiceExcercise.COLUMN_NAME_DATA)

  private interface VoiceExcercise {
    companion object {
      const val TABLE_NAME = "voice_exercise"
      const val COLUMN_NAME_LOCALE = "locale_info"
      const val COLUMN_NAME_TITLE = "title"
      const val COLUMN_NAME_PHONETIC = "phonetic"
      const val COLUMN_NAME_DESCRIPTION = "description"
      const val COLUMN_NAME_DATA = "voice_data"
      const val COLUMN_NAME_AUTHOR = "author"
      const val COLUMN_NAME_ACCOUNT = "account"
      const val COLUMN_NAME_MAX_VOLUME = "max_volume"
      const val COLUMN_NAME_ORDER = "sort"
      const val COLUMN_NAME_SPECIFICATION_ID = "specification_id"
      const val COLUMN_NAME_SCORE = "exercise_score"
      const val _ID = BaseColumns._ID
      const val _COUNT = BaseColumns._COUNT
    }
  }

  class VoiceExcerciseEntry(cursor: Cursor?) : Serializable {
    var id: Long? = null
    var title: String? = null
    var phonetic: String? = null
    var author: String? = null
    var maxVolume: Int? = null
    val data: ShortArray?

    init {
      if (cursor == null || cursor.isClosed) {
        this.data = null
      } else {
        this.id = cursor!!.getLong(cursor.getColumnIndex(BaseColumns._ID))
        this.title = cursor.getString(cursor.getColumnIndex(TableVoiceExercise.VoiceExcercise.COLUMN_NAME_TITLE))
        this.phonetic = cursor.getString(cursor.getColumnIndex(TableVoiceExercise.VoiceExcercise.COLUMN_NAME_PHONETIC))
        this.author = cursor.getString(cursor.getColumnIndex(TableVoiceExercise.VoiceExcercise.COLUMN_NAME_AUTHOR))
        this.maxVolume = cursor.getInt(cursor.getColumnIndex(TableVoiceExercise.VoiceExcercise.COLUMN_NAME_MAX_VOLUME))
        val data = cursor.getBlob(cursor.getColumnIndex(TableVoiceExercise.VoiceExcercise.COLUMN_NAME_DATA))
        val buffer = ByteBuffer.wrap(data)
        val size = if (data == null) 0 else data.size / 2
        Log.d(AppConstant.TAG, "voice size:$size")
        this.data = ShortArray(size)
        var index = 0
        while (size > index) {
          this.data[index] = buffer.getShort(index * 2)
          index++
        }
      }
    }
  }

  fun createTableSql(): String {
    val textType = " text"
    val commaSep = ","
    val builder = StringBuilder()
    builder.append("create table ")
    builder.append(VoiceExcercise.TABLE_NAME).append(" ( ")
    builder.append(VoiceExcercise._ID).append(" integer primary key,")
    builder.append(VoiceExcercise.COLUMN_NAME_LOCALE).append(textType).append(commaSep)
    builder.append(VoiceExcercise.COLUMN_NAME_TITLE).append(textType).append(commaSep)
    builder.append(VoiceExcercise.COLUMN_NAME_PHONETIC).append(textType).append(commaSep)
    builder.append(VoiceExcercise.COLUMN_NAME_DESCRIPTION).append(textType).append(commaSep)
    builder.append(VoiceExcercise.COLUMN_NAME_AUTHOR).append(textType).append(commaSep)
    builder.append(VoiceExcercise.COLUMN_NAME_ACCOUNT).append(textType).append(commaSep)
    builder.append(VoiceExcercise.COLUMN_NAME_ORDER).append(" integer").append(commaSep)
    builder.append(VoiceExcercise.COLUMN_NAME_MAX_VOLUME).append(" integer").append(commaSep)
    builder.append(VoiceExcercise.COLUMN_NAME_SPECIFICATION_ID).append(" integer").append(commaSep)
    builder.append(VoiceExcercise.COLUMN_NAME_SCORE).append(" double").append(commaSep)
    builder.append(VoiceExcercise.COLUMN_NAME_DATA).append(" blob")
    builder.append(" ) ")
    return builder.toString()
  }

  fun dropTableSql(): String {
    val builder = StringBuilder()
    builder.append("drop table if exists ").append(VoiceExcercise.TABLE_NAME)
    return builder.toString()
  }

  fun tableUri(context: Context): Uri {
    return Uri.parse("content://" + context.packageName + "/" + VoiceExcercise.TABLE_NAME)
  }

}
