package com.futhark.android.seevoice.feature.profile

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import com.futhark.android.seevoice.R
import com.futhark.android.seevoice.base.BaseFragment
import com.futhark.android.seevoice.model.database.SeeVoiceSqliteDatabaseHelper
import com.futhark.android.seevoice.model.database.TableVoiceAccount

/**
 * me fragment
 * Created by liuhr on 12/06/2017.
 */

class MeFragment : BaseFragment() {
  private var voiceAccountEntry: TableVoiceAccount.VoiceAccountEntry? = null
  private var needRefresh = true
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_me, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    voiceAccountEntry = TableVoiceAccount.getVoiceAccountEntry(SeeVoiceSqliteDatabaseHelper(activity).writableDatabase)
  }

  override fun onResume() {
    super.onResume()
    refreshView()
  }

  override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
    super.onCreateOptionsMenu(menu, inflater)
    inflater!!.inflate(R.menu.menu_me_account, menu)
    Log.i(TAG, "voiceAccountEntry on create menu" + (voiceAccountEntry!!.account))
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.menu_me_account_add -> {
        return true
      }
    }
    return super.onOptionsItemSelected(item)
  }

  private fun refreshView() {
    if (voiceAccountEntry == null || !needRefresh) return
    view.findViewById<TextView>(R.id.label_me_account).text = voiceAccountEntry!!.account
    view.findViewById<TextView>(R.id.label_me_title).text = voiceAccountEntry!!.accountTitle
    view.findViewById<TextView>(R.id.label_me_lang).text = voiceAccountEntry!!.lang
    view.findViewById<TextView>(R.id.label_me_full_name).text = voiceAccountEntry!!.fullName
    view.findViewById<TextView>(R.id.label_me_created_at).text = voiceAccountEntry!!.createdAt

    if (voiceAccountEntry!!.status != null && voiceAccountEntry!!.status!! > 0) {
      view.findViewById<Button>(R.id.button_me_edit_profile).visibility = View.GONE
      return
    }
    view.findViewById<Button>(R.id.button_me_edit_profile).setOnClickListener { v ->
      when (v.id) {
        R.id.button_me_edit_profile -> {

        }
      }
    }
  }
}
