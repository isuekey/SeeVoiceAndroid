package com.futhark.android.seevoice.controller.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.futhark.android.seevoice.R;
import com.futhark.android.seevoice.base.BaseFragment;

/**
 * me fragment
 * Created by liuhr on 12/06/2017.
 */

public class MeFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_empty_list, container, false);
        return fragment;
    }
}
