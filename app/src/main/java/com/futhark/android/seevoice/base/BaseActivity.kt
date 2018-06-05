package com.futhark.android.seevoice.base

import android.app.Activity
import android.os.Bundle

import com.futhark.android.seevoice.model.constant.AppConstant
import com.futhark.android.seevoice.model.message.BaseActivityMessage

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * use eventbus
 * Created by liuhr on 2/7/17.
 */

open class BaseActivity : Activity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    EventBus.getDefault().register(this)
  }

  override fun onDestroy() {
    super.onDestroy()
    EventBus.getDefault().unregister(this)
  }

  @Subscribe
  fun onMessageOfBaseActivity(baseActivityMessage: BaseActivityMessage) {
  }

  companion object {
    val TAG = AppConstant.TAG
  }
}
