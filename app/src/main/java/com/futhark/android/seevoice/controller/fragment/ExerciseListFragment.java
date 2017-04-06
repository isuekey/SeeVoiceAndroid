package com.futhark.android.seevoice.controller.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.futhark.android.seevoice.R;
import com.futhark.android.seevoice.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Exercise List Fragment
 * Created by liuhr on 06/04/2017.
 */

public class ExerciseListFragment extends BaseFragment {
    @BindView(R.id.fragment_empty_list_view)
    ListView exerciseListView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_empty_list, container, false);
        ButterKnife.bind(this, fragmentView);
        return fragmentView;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }
}
