package com.futhark.android.seevoice.controller.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.futhark.android.seevoice.R;
import com.futhark.android.seevoice.base.BaseFragmentActivity;
import com.futhark.android.seevoice.controller.fragment.ExercisingFragment;
import com.futhark.android.seevoice.controller.fragment.RecordingFragment;
import com.futhark.android.seevoice.model.database.TableVoiceSpecification;

/**
 * display specification list
 * Created by liuhr on 30/05/2017.
 */

public class SpecificationListAdapter extends CursorAdapter {
    private LayoutInflater layoutInflater;
    private Context context;
    private boolean showRecord = true;
    public SpecificationListAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }
    public SpecificationListAdapter(Context context, Cursor c, boolean autoRequery, boolean showRecord) {
        super(context, c, autoRequery);
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.showRecord = showRecord;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View itemView = layoutInflater.inflate(R.layout.item_of_specification_list, parent, false);
        itemView.setTag(new ItemHolder(itemView));
        return itemView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ItemHolder itemHolder = (ItemHolder)view.getTag();
        if(itemHolder != null){
            itemHolder.display(cursor);
        }
    }

    private class ItemHolder{
        private View itemView;
        private TextView titleView;
        private TextView phoneticView;
        private TextView authorView;
        private View modifyBuffton;
        private TableVoiceSpecification.VoiceSpecificationEntry voiceSpecificationEntry;
        private ItemHolder(View itemView){
            this.itemView = itemView;
            titleView = (TextView)itemView.findViewById(R.id.text_specification_title);
            phoneticView = (TextView)itemView.findViewById(R.id.text_specification_phonetic);
            authorView = (TextView)itemView.findViewById(R.id.text_specification_author);
            itemView.findViewById(R.id.button_to_exercise).setOnClickListener(onClickListener);
            modifyBuffton = itemView.findViewById(R.id.button_to_modify);
            if(!showRecord){
                modifyBuffton.setVisibility(View.INVISIBLE);
            }else{
                modifyBuffton.setOnClickListener(onClickListener);
            }
        }

        public void display(Cursor cursor){
            titleView.setText(cursor.getString(cursor.getColumnIndex(TableVoiceSpecification.VoiceSpecificationEntry.COLUMN_NAME_TITLE)));
            phoneticView.setText(cursor.getString(cursor.getColumnIndex(TableVoiceSpecification.VoiceSpecificationEntry.COLUMN_NAME_PHONETIC)));
            authorView.setText(cursor.getString(cursor.getColumnIndex(TableVoiceSpecification.VoiceSpecificationEntry.COLUMN_NAME_AUTHOR)));
            voiceSpecificationEntry = new TableVoiceSpecification.VoiceSpecificationEntry(cursor);
        }

        private View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.button_to_modify:
                        gotoModify(voiceSpecificationEntry);
                        break;
                    case R.id.button_to_exercise:
                        gotoExercise(voiceSpecificationEntry);
                        break;
                }
            }
        };
    }

    private void gotoExercise(TableVoiceSpecification.VoiceSpecificationEntry specificationEntry){
        Intent exerciseIntent = new Intent(context, BaseFragmentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ExercisingFragment.FRAGMENT_EXERCISING_ARGUMENT_ITEM_MODEL, specificationEntry);
        exerciseIntent.putExtra(BaseFragmentActivity.Companion.getDUTY_FATE_FRAGMENT_INTENT(), ExercisingFragment.class.getName());
        exerciseIntent.putExtra(BaseFragmentActivity.Companion.getDUTY_FATE_FRAGMENT_DATA_INTENT(), bundle);
        context.startActivity(exerciseIntent);
    }
    private void gotoModify(TableVoiceSpecification.VoiceSpecificationEntry specificationEntry){
        Intent exerciseIntent = new Intent(context, BaseFragmentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(RecordingFragment.FRAGMENT_RECORDING_ARGUMENT_ITEM_MODEL, specificationEntry);
        exerciseIntent.putExtra(BaseFragmentActivity.Companion.getDUTY_FATE_FRAGMENT_INTENT(), RecordingFragment.class.getName());
        exerciseIntent.putExtra(BaseFragmentActivity.Companion.getDUTY_FATE_FRAGMENT_DATA_INTENT(), bundle);
        context.startActivity(exerciseIntent);
    }
}
