package com.futhark.android.seevoice.feature.profile

import android.os.Bundle
import android.view.*
import android.widget.TextView
import com.futhark.android.seevoice.R
import com.futhark.android.seevoice.base.BaseFragment
import com.futhark.android.seevoice.base.BaseFragmentActivity
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
    val database = SeeVoiceSqliteDatabaseHelper(activity).writableDatabase
    voiceAccountEntry = TableVoiceAccount.getVoiceAccountEntry(database)
    database.close()
  }

  override fun onResume() {
    super.onResume()
    refreshView()
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater?) {
    super.onCreateOptionsMenu(menu, inflater)
    inflater!!.inflate(R.menu.menu_me_account, menu)
    val isAnonymous = isAnonymousAccount(voiceAccountEntry)
    menu.findItem(R.id.menu_me_account_edit).isVisible = !isAnonymous
    menu.findItem(R.id.menu_me_account_switch).isVisible = !isAnonymous
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.menu_me_account_add -> {
        activity.fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.activity_fragment_base, AddAccountFragment()).commit()
        return true
      }
      R.id.menu_me_account_edit -> {
        val fragment = EditAccountFragment()
        val bundle = Bundle()
        bundle.putSerializable(EditAccountFragment.DUTY_FATE_OLD_ACCOUNT, voiceAccountEntry)
        fragment.arguments = bundle
        activity.fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.activity_fragment_base, EditAccountFragment()).commit()
        return true
      }
      R.id.menu_me_account_switch -> {
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
  }

  private fun isAnonymousAccount(voiceAccountEntry: TableVoiceAccount.VoiceAccountEntry?): Boolean {
    return (voiceAccountEntry?.status ?: -1) < 0
  }
}
