package com.futhark.android.seevoice

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button

import com.futhark.android.seevoice.base.BaseActivity
import com.futhark.android.seevoice.base.BaseFragmentActivity
import com.futhark.android.seevoice.feature.home.HomePageFragment
import com.futhark.android.seevoice.feature.home.HomeActivity

/**
 * 启动广告页
 * Created by liuhr on 06/04/2017.
 */

class AdvertisingActivity : BaseActivity() {
    internal var gotHomePageButton: Button? = null

    private val buttonClickListener = View.OnClickListener { v ->
        when (v.id) {
            R.id.button_go_to_home_page -> gotoMainActivity()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_advertising)
        initHandler()
        Log.d(BaseActivity.Companion.TAG, "AdvertisingActivity")
    }

    fun initHandler() {
        gotHomePageButton = this.findViewById(R.id.button_go_to_home_page)
        gotHomePageButton!!.setOnClickListener(this.buttonClickListener)
    }

    override fun onResume() {
        super.onResume()
        gotoMainActivity()
    }

    private fun gotoMainActivity() {
        val mainActivityIntent = Intent(this@AdvertisingActivity, HomeActivity::class.java)
        mainActivityIntent.putExtra(BaseFragmentActivity.DUTY_FATE_FRAGMENT_INTENT, HomePageFragment::class.java!!.getName())
        startActivity(mainActivityIntent)
    }

}
