package com.futhark.android.seevoice.base;

import android.app.Activity;
import android.os.Bundle;

import com.futhark.android.seevoice.model.message.BaseActivityMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * use eventbus
 * Created by liuhr on 2/7/17.
 */

public class BaseActivity extends Activity {
    public static final String TAG = "see_tag";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onMessageOfBaseActivity(BaseActivityMessage baseActivityMessage){
    }
}
