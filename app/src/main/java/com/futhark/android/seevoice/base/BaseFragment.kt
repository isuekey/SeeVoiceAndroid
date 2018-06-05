package com.futhark.android.seevoice.base

import android.app.Fragment
import android.os.Bundle

import com.futhark.android.seevoice.model.constant.AppConstant
import com.futhark.android.seevoice.model.message.BaseFragmentMessage

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 *
 * Created by liuhr on 06/04/2017.
 */

open class BaseFragment : Fragment() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)
    EventBus.getDefault().register(this)
  }

  override fun onDestroy() {
    super.onDestroy()
    EventBus.getDefault().unregister(this)
  }

  @Subscribe
  fun onMessageOfBaseFragment(baseFragmentMessage: BaseFragmentMessage) {
  }

  companion object {
    val TAG = AppConstant.TAG
  }
}
