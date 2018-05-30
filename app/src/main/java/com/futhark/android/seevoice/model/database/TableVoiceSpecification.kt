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
 * voice specification
 * Created by liuhr on 30/05/2017.
 */

object TableVoiceSpecification {

    var COLUMNS = arrayOf<String>(VoiceSpecificationEntry._ID, VoiceSpecificationEntry.COLUMN_NAME_LOCALE, VoiceSpecificationEntry.COLUMN_NAME_TITLE, VoiceSpecificationEntry.COLUMN_NAME_PHONETIC, VoiceSpecificationEntry.COLUMN_NAME_DESCRIPTION, VoiceSpecificationEntry.COLUMN_NAME_AUTHOR, VoiceSpecificationEntry.COLUMN_NAME_ACCOUNT, VoiceSpecificationEntry.COLUMN_NAME_ORDER, VoiceSpecificationEntry.COLUMN_NAME_MAX_VOLUME, VoiceSpecificationEntry.COLUMN_NAME_DATA)

    private interface VoiceSpecification {
        companion object {
            val TABLE_NAME = "voice_specification"
            val COLUMN_NAME_LOCALE = "locale_info"
            val COLUMN_NAME_TITLE = "title"
            val COLUMN_NAME_PHONETIC = "phonetic"
            val COLUMN_NAME_DESCRIPTION = "description"
            val COLUMN_NAME_DATA = "voice_data"
            val COLUMN_NAME_AUTHOR = "author"
            val COLUMN_NAME_ACCOUNT = "account"
            val COLUMN_NAME_MAX_VOLUME = "max_volume"
            val COLUMN_NAME_ORDER = "sort"
            val _ID = BaseColumns._ID
            val _COUNT = BaseColumns._COUNT
        }
    }

    class VoiceSpecificationEntry(cursor: Cursor?) : VoiceSpecification, Serializable {
        var id: Long? = null
        var title: String? = null
        var phonetic: String? = null
        var author: String? = null
        var maxVolume: Int? = null
        var data: ShortArray?

        init {
            if (cursor == null || cursor.isClosed) {
                this.data = null
                return
            }
            this.id = cursor!!.getLong(cursor.getColumnIndex(VoiceSpecification._ID))
            this.title = cursor.getString(cursor.getColumnIndex(TableVoiceSpecification.VoiceSpecification.COLUMN_NAME_TITLE))
            this.phonetic = cursor.getString(cursor.getColumnIndex(TableVoiceSpecification.VoiceSpecification.COLUMN_NAME_PHONETIC))
            this.author = cursor.getString(cursor.getColumnIndex(TableVoiceSpecification.VoiceSpecification.COLUMN_NAME_AUTHOR))
            this.maxVolume = cursor.getInt(cursor.getColumnIndex(TableVoiceSpecification.VoiceSpecification.COLUMN_NAME_MAX_VOLUME))
            val data = cursor.getBlob(cursor.getColumnIndex(TableVoiceSpecification.VoiceSpecification.COLUMN_NAME_DATA))
            val buffer = ByteBuffer.wrap(data)
            val size = if (data == null) 0 else data.size / 2
            Log.d(AppConstant.TAG, "voice size:" + size)
            this.data = ShortArray(size)
            var index = 0
            while (size > index) {
                this.data[index] = buffer.getShort(index * 2)
                index++
            }
        }
    }

    fun createTableSql(): String {
        val textType = " text"
        val commaSep = ","
        val builder = StringBuilder()
        builder.append("create table ")
        builder.append(VoiceSpecificationEntry.TABLE_NAME).append(" ( ")
        builder.append(VoiceSpecificationEntry._ID).append(" integer primary key,")
        builder.append(VoiceSpecificationEntry.COLUMN_NAME_LOCALE).append(textType).append(commaSep)
        builder.append(VoiceSpecificationEntry.COLUMN_NAME_TITLE).append(textType).append(commaSep)
        builder.append(VoiceSpecificationEntry.COLUMN_NAME_PHONETIC).append(textType).append(commaSep)
        builder.append(VoiceSpecificationEntry.COLUMN_NAME_DESCRIPTION).append(textType).append(commaSep)
        builder.append(VoiceSpecificationEntry.COLUMN_NAME_AUTHOR).append(textType).append(commaSep)
        builder.append(VoiceSpecificationEntry.COLUMN_NAME_ACCOUNT).append(textType).append(commaSep)
        builder.append(VoiceSpecificationEntry.COLUMN_NAME_ORDER).append(" integer").append(commaSep)
        builder.append(VoiceSpecificationEntry.COLUMN_NAME_MAX_VOLUME).append(" integer").append(commaSep)
        builder.append(VoiceSpecificationEntry.COLUMN_NAME_DATA).append(" blob")
        builder.append(" ) ")
        return builder.toString()
    }

    fun dropTableSql(): String {
        val builder = StringBuilder()
        builder.append("drop table if exists ").append(VoiceSpecificationEntry.TABLE_NAME)
        return builder.toString()
    }

    fun tableUri(context: Context): Uri {
        return Uri.parse("content://" + context.packageName + "/" + VoiceSpecificationEntry.TABLE_NAME)
    }
}
