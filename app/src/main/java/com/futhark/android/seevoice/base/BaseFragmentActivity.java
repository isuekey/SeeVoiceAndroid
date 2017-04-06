package com.futhark.android.seevoice.base;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import com.futhark.android.seevoice.R;

/**
 *
 * Created by liuhr on 06/04/2017.
 */

public class BaseFragmentActivity extends BaseActivity {
    public static final String DUTY_FATE_FRAGMENT_INTENT = "duty_fate_fragment_intent";
    private Fragment contentFragmeent;
    private boolean contentChangeSeal = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_base);
        Intent activityIntent = getIntent();
        handleActivityIntent(activityIntent);
    }

    private void handleActivityIntent(Intent activityIntent){
        if(activityIntent == null) return;
        String fragmeentName = activityIntent.getStringExtra(DUTY_FATE_FRAGMENT_INTENT);
        if(fragmeentName == null) return;
        if(contentChangeSeal && contentFragmeent != null){
            return;
        }
        contentFragmeent = Fragment.instantiate(this, fragmeentName);
        getFragmentManager().beginTransaction().add(R.id.activity_fragment_base, contentFragmeent).commit();
    }
}
