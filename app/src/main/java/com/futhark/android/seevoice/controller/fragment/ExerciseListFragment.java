package com.futhark.android.seevoice.controller.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.futhark.android.seevoice.R;
import com.futhark.android.seevoice.base.BaseFragment;
import com.futhark.android.seevoice.controller.adapter.ExerciseListAdapter;
import com.futhark.android.seevoice.model.domain.ExerciseItemModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Exercise List Fragment
 * Created by liuhr on 06/04/2017.
 */

public class ExerciseListFragment extends BaseFragment {
    @BindView(R.id.fragment_empty_list_view)
    ListView exerciseListView;
    private ExerciseListAdapter<ExerciseItemModel> exerciseListAdapter;
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        prepareExerciseListView(getActivity());
        loadExerciseData();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    private void prepareExerciseListView(Context context){
        if(exerciseListAdapter != null){
            return;
        }
        exerciseListAdapter = new ExerciseListAdapter<>(context);
        exerciseListView.setAdapter(exerciseListAdapter);
    }

    private void loadExerciseData(){
        exerciseListAdapter.add(new ExerciseItemModel("测试"));
        exerciseListAdapter.notifyDataSetChanged();
    }
}
