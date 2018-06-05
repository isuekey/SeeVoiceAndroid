package com.futhark.android.seevoice.feature.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.futhark.android.seevoice.R
import com.futhark.android.seevoice.base.BaseFragment

/**
 * me fragment
 * Created by liuhr on 12/06/2017.
 */

class MeFragment : BaseFragment() {
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_empty_list, container, false)
  }
}
