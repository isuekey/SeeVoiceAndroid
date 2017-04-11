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

    @SuppressWarnings("unchecked")
    @Override
    @NonNull
    public View getView(int position, View convertView,@NonNull ViewGroup parent) {
        View v = convertView;
        ExerciseItemHolder holder;
        if(v == null){
            v = inflater.inflate(R.layout.item_of_exercise_list, parent, false);
            holder = new ExerciseItemHolder(v);
            v.setTag(holder);
        }else{
            holder = (ExerciseItemHolder)v.getTag();
        }
        holder.setDataToDisplay(getItem(position));
        return v;
    }

    class ExerciseItemHolder{
        @BindView(R.id.item_of_exercise_list_title)
        TextView titleText;
        ExerciseItemHolder(View itemView){
            ButterKnife.bind(this, itemView);
        }

        void setDataToDisplay(T data){
            titleText.setText(data.getTitle());
        }
    }

    public interface ExerciseItemInterface{
        String getTitle();
    }
}
