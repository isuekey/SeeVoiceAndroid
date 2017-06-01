package com.futhark.android.seevoice.model.database;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.futhark.android.seevoice.model.constant.AppConstant;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.sql.Date;

/**
 * voice specification
 * Created by liuhr on 30/05/2017.
 */

public final class TableVoiceSpecification {
    private TableVoiceSpecification(){}

    private interface VoiceSpecification extends BaseColumns {
        String TABLE_NAME= "voice_specification";
        String COLUMN_NAME_LOCALE = "locale_info";
        String COLUMN_NAME_TITLE = "title";
        String COLUMN_NAME_PHONETIC = "phonetic";
        String COLUMN_NAME_DESCRIPTION = "description";
        String COLUMN_NAME_DATA = "voice_data";
        String COLUMN_NAME_AUTHOR = "author";
        String COLUMN_NAME_ACCOUNT = "account";
        String COLUMN_NAME_MAX_VOLUME = "max_volume";
        String COLUMN_NAME_ORDER = "sort";
    }

    public static class VoiceSpecificationEntry implements VoiceSpecification, Serializable{
        private Long id;
        private String title;
        private String phonetic;
        private String author;
        private Integer maxVolume;
        private final short[] data;

        public VoiceSpecificationEntry(Cursor cursor){
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
        builder.append(VoiceSpecificationEntry.TABLE_NAME).append(" ( ");
        builder.append(VoiceSpecificationEntry._ID).append(" integer primary key,");
        builder.append(VoiceSpecificationEntry.COLUMN_NAME_LOCALE).append(textType).append(commaSep);
        builder.append(VoiceSpecificationEntry.COLUMN_NAME_TITLE).append(textType).append(commaSep);
        builder.append(VoiceSpecificationEntry.COLUMN_NAME_PHONETIC).append(textType).append(commaSep);
        builder.append(VoiceSpecificationEntry.COLUMN_NAME_DESCRIPTION).append(textType).append(commaSep);
        builder.append(VoiceSpecificationEntry.COLUMN_NAME_AUTHOR).append(textType).append(commaSep);
        builder.append(VoiceSpecificationEntry.COLUMN_NAME_ACCOUNT).append(textType).append(commaSep);
        builder.append(VoiceSpecificationEntry.COLUMN_NAME_ORDER).append(" integer").append(commaSep);
        builder.append(VoiceSpecificationEntry.COLUMN_NAME_MAX_VOLUME).append(" integer").append(commaSep);
        builder.append(VoiceSpecificationEntry.COLUMN_NAME_DATA).append(" blob");
        builder.append(" ) ");
        return builder.toString();
    }

    public static String dropTableSql(){
        StringBuilder builder = new StringBuilder();
        builder.append("drop table if exists ").append(VoiceSpecificationEntry.TABLE_NAME);
        return builder.toString();
    }

    public static Uri tableUri(Context context){
        return Uri.parse("content://"+context.getPackageName()+"/"+VoiceSpecificationEntry.TABLE_NAME);
    }

    public static String[] COLUMNS = new String[]{
            VoiceSpecificationEntry._ID,
            VoiceSpecificationEntry.COLUMN_NAME_LOCALE,
            VoiceSpecificationEntry.COLUMN_NAME_TITLE,
            VoiceSpecificationEntry.COLUMN_NAME_PHONETIC,
            VoiceSpecificationEntry.COLUMN_NAME_DESCRIPTION,
            VoiceSpecificationEntry.COLUMN_NAME_AUTHOR,
            VoiceSpecificationEntry.COLUMN_NAME_ACCOUNT,
            VoiceSpecificationEntry.COLUMN_NAME_ORDER,
            VoiceSpecificationEntry.COLUMN_NAME_MAX_VOLUME,
            VoiceSpecificationEntry.COLUMN_NAME_DATA
    };
}
