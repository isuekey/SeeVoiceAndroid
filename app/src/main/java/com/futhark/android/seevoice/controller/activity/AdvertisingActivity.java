package com.futhark.android.seevoice.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.futhark.android.seevoice.R;
import com.futhark.android.seevoice.base.BaseActivity;
import com.futhark.android.seevoice.base.BaseFragmentActivity;
import com.futhark.android.seevoice.controller.fragment.HomePageFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 启动广告页
 * Created by liuhr on 06/04/2017.
 */

public class AdvertisingActivity extends BaseActivity {
    @BindView(R.id.button_go_to_home_page)
    Button gotHomePageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_advertising);
        ButterKnife.bind(this);
        gotHomePageButton.setOnClickListener(this.buttonClickListener);
        Log.d(TAG, "AdvertisingActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        gotoMainActivity();
    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.button_go_to_home_page:
                    gotoMainActivity();
                    break;
            }
        }
    };

    private void gotoMainActivity(){
        Intent mainActivityIntent = new Intent(AdvertisingActivity.this, BaseFragmentActivity.class);
        mainActivityIntent.putExtra(BaseFragmentActivity.DUTY_FATE_FRAGMENT_INTENT, HomePageFragment.class.getName());
        startActivity(mainActivityIntent);
    }

}
