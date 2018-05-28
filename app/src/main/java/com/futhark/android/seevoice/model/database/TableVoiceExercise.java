package com.futhark.android.seevoice.model.database;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.futhark.android.seevoice.model.constant.AppConstant;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * voice exercise
 * Created by liuhr on 01/06/2017.
 */

public class TableVoiceExercise {
    private TableVoiceExercise(){}

    private interface VoiceExcercise extends BaseColumns {
        String TABLE_NAME= "voice_exercise";
        String COLUMN_NAME_LOCALE = "locale_info";
        String COLUMN_NAME_TITLE = "title";
        String COLUMN_NAME_PHONETIC = "phonetic";
        String COLUMN_NAME_DESCRIPTION = "description";
        String COLUMN_NAME_DATA = "voice_data";
        String COLUMN_NAME_AUTHOR = "author";
        String COLUMN_NAME_ACCOUNT = "account";
        String COLUMN_NAME_MAX_VOLUME = "max_volume";
        String COLUMN_NAME_ORDER = "sort";
        String COLUMN_NAME_SPECIFICATION_ID = "specification_id";
        String COLUMN_NAME_SCORE = "exercise_score";
    }

    public static class VoiceExcerciseEntry implements TableVoiceExercise.VoiceExcercise, Serializable {
        private Long id;
        private String title;
        private String phonetic;
        private String author;
        private Integer maxVolume;
        private final short[] data;

        public VoiceExcerciseEntry(Cursor cursor){
            if(cursor == null || cursor.isClosed()){
                this.data = null;
                return;
            }
            this.id = cursor.getLong(cursor.getColumnIndex(_ID));
            this.title = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TITLE));
            this.phonetic = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PHONETIC));
            this.author = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_AUTHOR));
            this.maxVolume = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_MAX_VOLUME));
            byte[] data = cursor.getBlob(cursor.getColumnIndex(COLUMN_NAME_DATA));
            ByteBuffer buffer = ByteBuffer.wrap(data);
            int size = data == null ? 0 : data.length / 2;
            Log.d(AppConstant.TAG, "voice size:"+size);
            this.data = new short[size];
            int index = 0;
            while(size > index){
                this.data[index] = buffer.getShort(index *2);
                index++;
            }
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPhonetic() {
            return phonetic;
        }

        public void setPhonetic(String phonetic) {
            this.phonetic = phonetic;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public Integer getMaxVolume() {
            return maxVolume;
        }

        public void setMaxVolume(Integer maxVolume) {
            this.maxVolume = maxVolume;
        }

        public short[] getData() {
            return data;
        }
    }

    public static String createTableSql(){
        String textType = " text";
        String commaSep = ",";
        StringBuilder builder = new StringBuilder();
        builder.append("create table ");
        builder.append(VoiceExcerciseEntry.TABLE_NAME).append(" ( ");
        builder.append(VoiceExcerciseEntry._ID).append(" integer primary key,");
        builder.append(VoiceExcerciseEntry.COLUMN_NAME_LOCALE).append(textType).append(commaSep);
        builder.append(VoiceExcerciseEntry.COLUMN_NAME_TITLE).append(textType).append(commaSep);
        builder.append(VoiceExcerciseEntry.COLUMN_NAME_PHONETIC).append(textType).append(commaSep);
        builder.append(VoiceExcerciseEntry.COLUMN_NAME_DESCRIPTION).append(textType).append(commaSep);
        builder.append(VoiceExcerciseEntry.COLUMN_NAME_AUTHOR).append(textType).append(commaSep);
        builder.append(VoiceExcerciseEntry.COLUMN_NAME_ACCOUNT).append(textType).append(commaSep);
        builder.append(VoiceExcerciseEntry.COLUMN_NAME_ORDER).append(" integer").append(commaSep);
        builder.append(VoiceExcerciseEntry.COLUMN_NAME_MAX_VOLUME).append(" integer").append(commaSep);
        builder.append(VoiceExcerciseEntry.COLUMN_NAME_SPECIFICATION_ID).append(" integer").append(commaSep);
        builder.append(VoiceExcerciseEntry.COLUMN_NAME_SCORE).append(" double").append(commaSep);
        builder.append(VoiceExcerciseEntry.COLUMN_NAME_DATA).append(" blob");
        builder.append(" ) ");
        return builder.toString();
    }

    public static String dropTableSql(){
        StringBuilder builder = new StringBuilder();
        builder.append("drop table if exists ").append(VoiceExcerciseEntry.TABLE_NAME);
        return builder.toString();
    }

    public static Uri tableUri(Context context){
        return Uri.parse("content://"+context.getPackageName()+"/"+ VoiceExcerciseEntry.TABLE_NAME);
    }

    public static String[] COLUMNS = new String[]{
        VoiceExcerciseEntry._ID,
        VoiceExcerciseEntry.COLUMN_NAME_LOCALE,
        VoiceExcerciseEntry.COLUMN_NAME_TITLE,
        VoiceExcerciseEntry.COLUMN_NAME_PHONETIC,
        VoiceExcerciseEntry.COLUMN_NAME_DESCRIPTION,
        VoiceExcerciseEntry.COLUMN_NAME_AUTHOR,
        VoiceExcerciseEntry.COLUMN_NAME_ACCOUNT,
        VoiceExcerciseEntry.COLUMN_NAME_ORDER,
        VoiceExcerciseEntry.COLUMN_NAME_MAX_VOLUME,
        VoiceExcerciseEntry.COLUMN_NAME_DATA
    };

}
