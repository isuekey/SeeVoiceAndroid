package com.futhark.android.seevoice.base

import android.app.Fragment
import android.content.Intent
import android.os.Bundle

import com.futhark.android.seevoice.R

/**
 *
 * Created by liuhr on 06/04/2017.
 */

open class BaseFragmentActivity : BaseActivity() {
    private var contentFragment: Fragment? = null
    var isContentChangeSeal = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_base)
        handleActivityIntent(intent)
    }

    private fun handleActivityIntent(activityIntent: Intent) {
        val fragmentName = activityIntent.getStringExtra(DUTY_FATE_FRAGMENT_INTENT) ?: return
        if (isContentChangeSeal && contentFragment != null) {
            return
        }
        contentFragment = Fragment.instantiate(this, fragmentName)
        contentFragment!!.arguments = activityIntent.getBundleExtra(DUTY_FATE_FRAGMENT_DATA_INTENT)
        fragmentManager.beginTransaction().add(R.id.activity_fragment_base, contentFragment).commit()
    }

    companion object {
        val DUTY_FATE_FRAGMENT_INTENT = "duty_fate_fragment_intent"
        val DUTY_FATE_FRAGMENT_DATA_INTENT = "duty_fate_fragment_data_intent"
    }
}
