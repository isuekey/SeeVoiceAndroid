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
    public static final String DUTY_FATE_FRAGMENT_DATA_INTENT = "duty_fate_fragment_data_intent";
    private Fragment contentFragment;
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
        String fragmentName = activityIntent.getStringExtra(DUTY_FATE_FRAGMENT_INTENT);
        if(fragmentName == null) return;
        if(contentChangeSeal && contentFragment != null){
            return;
        }
        contentFragment = Fragment.instantiate(this, fragmentName);
        contentFragment.setArguments(activityIntent.getBundleExtra(DUTY_FATE_FRAGMENT_DATA_INTENT));
        getFragmentManager().beginTransaction().add(R.id.activity_fragment_base, contentFragment).commit();
    }
    @SuppressWarnings("unused")
    public boolean isContentChangeSeal() {
        return contentChangeSeal;
    }

    @SuppressWarnings("unused")
    public void setContentChangeSeal(boolean contentChangeSeal) {
        this.contentChangeSeal = contentChangeSeal;
    }
}
