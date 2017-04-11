package com.futhark.android.seevoice.base;

import android.app.Fragment;
import android.os.Bundle;

import com.futhark.android.seevoice.model.constant.AppConstant;
import com.futhark.android.seevoice.model.message.BaseFragmentMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 *
 * Created by liuhr on 06/04/2017.
 */

public class BaseFragment extends Fragment {
    public static final String TAG = AppConstant.TAG;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @SuppressWarnings({"unused"})
    @Subscribe
    public void onMessageOfBaseFragment(BaseFragmentMessage baseFragmentMessage){
    }
}
