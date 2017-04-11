package com.futhark.android.seevoice.model.domain;

import com.futhark.android.seevoice.controller.adapter.ExerciseListAdapter;

import java.io.Serializable;

/**
 * exercise item model
 * Created by liuhr on 06/04/2017.
 */

public class ExerciseItemModel implements ExerciseListAdapter.ExerciseItemInterface, Serializable {
    private String title;

    public ExerciseItemModel(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }
    @SuppressWarnings("unused")
    public void setTitle(String title) {
        this.title = title;
    }
}
