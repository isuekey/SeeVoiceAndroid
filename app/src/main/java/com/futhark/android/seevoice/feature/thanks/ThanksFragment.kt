package com.futhark.android.seevoice.feature.thanks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.futhark.android.seevoice.R
import com.futhark.android.seevoice.base.BaseFragment


class ThanksFragment: BaseFragment(){
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    return inflater.inflate(R.layout.fragment_thanks, container, false)
  }
}