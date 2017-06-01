package com.futhark.android.seevoice.controller.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.futhark.android.seevoice.R;
import com.futhark.android.seevoice.base.BaseFragment;
import com.futhark.android.seevoice.base.BaseFragmentActivity;
import com.futhark.android.seevoice.controller.adapter.SpecificationListAdapter;
import com.futhark.android.seevoice.model.database.SeeVoiceSqliteDatabaseHelper;
import com.futhark.android.seevoice.model.database.TableVoiceSpecification;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * list specification
 * Created by liuhr on 30/05/2017.
 */

public class SpecificationListFragment extends BaseFragment {
    @BindView(R.id.fragment_empty_list_view)
    ListView specificationListView;

    private SpecificationListAdapter listAdapter;
    private Cursor cursor;
    private SQLiteDatabase database;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_empty_list, container, false);
        ButterKnife.bind(this, fragment);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listAdapter = new SpecificationListAdapter(getActivity(), null, true);
        specificationListView.setAdapter(listAdapter);
        database = new SeeVoiceSqliteDatabaseHelper(getActivity()).getReadableDatabase();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_exercise_record_standard, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_exercise_record_standard:
                gotoRecordSpecification();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        cursor = database.query(TableVoiceSpecification.VoiceSpecificationEntry.TABLE_NAME,
                TableVoiceSpecification.COLUMNS, null, null, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
            listAdapter.swapCursor(cursor);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(cursor != null){
            cursor.close();
            cursor = null;
        }
    }

    private void gotoRecordSpecification(){
        Intent goToRecordActivityIntent = new Intent(getActivity(), BaseFragmentActivity.class);
        goToRecordActivityIntent.putExtra(BaseFragmentActivity.DUTY_FATE_FRAGMENT_INTENT, RecordingFragment.class.getName());
        getActivity().startActivity(goToRecordActivityIntent);
    }


}
