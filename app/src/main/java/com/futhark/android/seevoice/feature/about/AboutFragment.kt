package com.futhark.android.seevoice.feature.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.futhark.android.seevoice.R
import com.futhark.android.seevoice.base.BaseFragment

/**
 * about fragment
 * Created by liuhr on 12/06/2017.
 */

class AboutFragment : BaseFragment() {
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    return inflater.inflate(R.layout.fragment_about, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    val onClickListener = View.OnClickListener { v ->
      when(v.id) {
        R.id.trigger_display_eth_qr_code -> displayQRCodeView(R.mipmap.ic_etherum_address)
        R.id.trigger_display_btc_qr_code -> displayQRCodeView(R.mipmap.ic_bitcoin_address)
      }
    }
    view.findViewById<View>(R.id.trigger_display_eth_qr_code).setOnClickListener(onClickListener)
    view.findViewById<View>(R.id.trigger_display_btc_qr_code).setOnClickListener(onClickListener)
  }

  fun displayQRCodeView(imgRes:Int) {

  }
}
