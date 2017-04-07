package com.futhark.android.seevoice.controller.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.futhark.android.seevoice.base.BaseFragment;
import com.futhark.android.seevoice.model.domain.ExerciseItemModel;

/**
 * exercising fragment
 * Created by liuhr on 07/04/2017.
 */

public class ExercisingFragment extends BaseFragment {
    public static final String FRAGMENT_EXERCISING_ARGUMENT_ITEM_MODEL = "fragment_exercising_argument_item_model";
    public static ExercisingFragment newInstance(@NonNull  ExerciseItemModel itemModel){
        ExercisingFragment fragment = new ExercisingFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(FRAGMENT_EXERCISING_ARGUMENT_ITEM_MODEL, itemModel);
        fragment.setArguments(arguments);
        return fragment;
    }

    private ExerciseItemModel itemModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if(arguments == null) return;
        if(arguments.containsKey(FRAGMENT_EXERCISING_ARGUMENT_ITEM_MODEL)){
            this.itemModel = (ExerciseItemModel) arguments.getSerializable(FRAGMENT_EXERCISING_ARGUMENT_ITEM_MODEL);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
