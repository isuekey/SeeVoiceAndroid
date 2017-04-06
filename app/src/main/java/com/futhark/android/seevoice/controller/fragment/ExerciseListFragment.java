package com.futhark.android.seevoice.controller.fragment;

import android.os.Bundle;
import android.view.Menu;

import com.futhark.android.seevoice.base.BaseFragment;

/**
 * Exercise List Fragment
 * Created by liuhr on 06/04/2017.
 */

public class ExerciseListFragment extends BaseFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }
}
