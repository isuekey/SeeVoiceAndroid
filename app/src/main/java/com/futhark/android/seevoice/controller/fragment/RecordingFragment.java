package com.futhark.android.seevoice.controller.fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.futhark.android.seevoice.R;
import com.futhark.android.seevoice.base.BaseFragment;
import com.futhark.android.seevoice.model.database.SeeVoiceSqliteDatabaseHelper;
import com.futhark.android.seevoice.model.database.TableVoiceSpecification.VoiceSpecificationEntry;

import java.nio.ByteBuffer;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * record fragment
 * Created by liuhr on 31/05/2017.
 */

public class RecordingFragment extends BaseFragment {
    @BindView(R.id.button_save_record) View buttonSave;
    @BindView(R.id.edit_voice_title) EditText titleEdit;
    @BindView(R.id.edit_voice_phonetic) EditText phoneticEdit;
    @BindView(R.id.edit_voice_author) EditText authorEdit;

    private SQLiteDatabase database;
    private SeeVoiceFragment seeVoiceFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_recording, container, false);
        ButterKnife.bind(this, fragment);
        buttonSave.setOnClickListener(onClickListener);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        seeVoiceFragment = new SeeVoiceFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.fragment_display_voice, seeVoiceFragment).commit();
        SeeVoiceSqliteDatabaseHelper helper = new SeeVoiceSqliteDatabaseHelper(getActivity());
        database = helper.getWritableDatabase();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.button_save_record:
                    saveCurrentVoice();
                    break;
            }
        }
    };

    private long saveCurrentVoice(){
        String title = titleEdit.getText().toString();
        String phonetic = phoneticEdit.getText().toString();
        String author = authorEdit.getText().toString();
        short[] recordData = seeVoiceFragment.getLastRecordData();
        int Size = recordData.length;
        ByteBuffer byteBuffer = ByteBuffer.allocate(2 * Size);
        int index = 0;
        while(Size > index){
            byteBuffer.putShort(recordData[index]);
            index++;
        }
        ContentValues values = new ContentValues();
        values.put(VoiceSpecificationEntry.COLUMN_NAME_TITLE, title);
        values.put(VoiceSpecificationEntry.COLUMN_NAME_PHONETIC, phonetic);
        values.put(VoiceSpecificationEntry.COLUMN_NAME_AUTHOR, author);
        values.put(VoiceSpecificationEntry.COLUMN_NAME_DATA, byteBuffer.array());
        long newRowId = database.insert(VoiceSpecificationEntry.TABLE_NAME, null, values);
        return newRowId;
    }
}
