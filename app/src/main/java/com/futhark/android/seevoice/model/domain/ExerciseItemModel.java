package com.futhark.android.seevoice.model.domain;

import com.futhark.android.seevoice.controller.adapter.ExerciseListAdapter;

/**
 * exercise item model
 * Created by liuhr on 06/04/2017.
 */

public class ExerciseItemModel implements ExerciseListAdapter.ExerciseItemInterface {
    private String title;

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
