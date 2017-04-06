package com.futhark.android.seevoice.controller.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.futhark.android.seevoice.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * exercise list adapter
 * Created by liuhr on 06/04/2017.
 */

public class ExerciseListAdapter<T extends ExerciseListAdapter.ExerciseItemInterface> extends ArrayAdapter<T> {
    private LayoutInflater inflater;
    public ExerciseListAdapter(Context context) {
        super(context, 0);
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null){
            v = inflater.inflate(R.layout.item_of_exercise_list, parent, false);
        }
        return super.getView(position, convertView, parent);
    }

    private class ItemHolder{
        @BindView(R.id.item_of_exercise_list_title)
        TextView titleText;
        public ItemHolder(View itemView){
            ButterKnife.bind(this, itemView);
        }

        public void setDataToDisplay(T data){
            titleText.setText(data.getTitle());
        }
    }

    public interface ExerciseItemInterface{
        String getTitle();
    }
}
