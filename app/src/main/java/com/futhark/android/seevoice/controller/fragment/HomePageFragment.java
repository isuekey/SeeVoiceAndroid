package com.futhark.android.seevoice.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.futhark.android.seevoice.R;
import com.futhark.android.seevoice.base.BaseFragment;
import com.futhark.android.seevoice.base.BaseFragmentActivity;
import com.futhark.android.seevoice.controller.adapter.SpecificationListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 首页的内容
 * Created by liuhr on 06/04/2017.
 */

public class HomePageFragment extends BaseFragment {
    @BindView(R.id.click_home_to_exercise) View homeToExerciseView;
    @BindView(R.id.click_home_to_record) View homeToRecordView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_home_page, container, false);
        ButterKnife.bind(this, fragmentView);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homeToExerciseView.setOnClickListener(onClickListener);
        homeToRecordView.setOnClickListener(onClickListener);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_home_account, menu);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.click_home_to_exercise:
                    Intent goToExerciseActivityIntent = new Intent(getActivity(), BaseFragmentActivity.class);
                    goToExerciseActivityIntent.putExtra(BaseFragmentActivity.DUTY_FATE_FRAGMENT_INTENT, ExerciseListFragment.class.getName());
                    getActivity().startActivity(goToExerciseActivityIntent);
                    break;
                case R.id.click_home_to_record:
                    Intent goToRecordActivityIntent = new Intent(getActivity(), BaseFragmentActivity.class);
                    goToRecordActivityIntent.putExtra(BaseFragmentActivity.DUTY_FATE_FRAGMENT_INTENT, SpecificationListFragment.class.getName());
                    getActivity().startActivity(goToRecordActivityIntent);
                    break;
            }
        }
    };
}
