package com.futhark.android.seevoice.feature.profile

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.futhark.android.seevoice.R
import com.futhark.android.seevoice.base.BaseFragment
import com.futhark.android.seevoice.model.database.SeeVoiceSqliteDatabaseHelper
import com.futhark.android.seevoice.model.database.TableVoiceAccount
import com.futhark.android.seevoice.model.message.MessageUtil
import java.util.*

class EditAccountFragment : BaseFragment() {
  private var database: SQLiteDatabase? = null
  private val editResourceIdList = listOf(R.id.input_me_account, R.id.input_me_old_password,
      R.id.input_me_new_password, R.id.input_me_title, R.id.input_me_lang,
      R.id.input_me_full_name, R.id.input_me_confirm_password)
  private var oldVoiceAccountEntry: TableVoiceAccount.VoiceAccountEntry? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    oldVoiceAccountEntry = arguments?.getSerializable(DUTY_FATE_OLD_ACCOUNT) as? TableVoiceAccount.VoiceAccountEntry
  }
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    return inflater.inflate(R.layout.fragment_me_edit_account, container, false)
  }

  override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
    super.onCreateOptionsMenu(menu, inflater)
    menu?.findItem(R.id.menu_me_account_add)?.isVisible = false
    menu?.findItem(R.id.menu_me_account_edit)?.isVisible = false
    menu?.findItem(R.id.menu_me_account_switch)?.isVisible = false
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    database = SeeVoiceSqliteDatabaseHelper(activity).writableDatabase
    val buttonClickListener = View.OnClickListener { v ->
      when (v.id) {
        R.id.action_me_save -> {
          handleSaveButton()
        }
        R.id.action_me_reset -> {
          for (editResource in editResourceIdList) {
            view?.findViewById<EditText>(editResource)?.setText("")
          }
        }
      }
    }
    view.findViewById<Button>(R.id.action_me_save).setOnClickListener(buttonClickListener)
    view.findViewById<Button>(R.id.action_me_reset).setOnClickListener(buttonClickListener)
  }

  private fun handleSaveButton() {
    val inputString = editResourceIdList.map { resId ->
      view?.findViewById<TextView>(resId)?.text?.toString()?.trim()
    }
    val availableInput = inputString.filterNotNull().filterNot { s ->
      s == ""
    }
    if (availableInput.isEmpty()) {
      return
    }
    if (inputString[1] != inputString[5]) {
      return
    }
    val values = ContentValues()
    val oldMd5 = MessageUtil.md5(inputString[1])
    val newMd5 = MessageUtil.md5(inputString[2])
    values.put(TableVoiceAccount.COLUMN_NAME_ACCOUNT, inputString[0])
    values.put(TableVoiceAccount.COLUMN_NAME_PASSWORD_MD5, newMd5)
    values.put(TableVoiceAccount.COLUMN_NAME_TITLE, inputString[3])
    values.put(TableVoiceAccount.COLUMN_NAME_LANG, inputString[4])
    values.put(TableVoiceAccount.COLUMN_NAME_FULL_NAME, inputString[5])
    values.put(TableVoiceAccount.COLUMN_NAME_STATUS, TableVoiceAccount.STATUS_IS_ACTIVE)
    values.put(TableVoiceAccount.COLUMN_NAME_CREATED_AT, Date().time)
    val updatedRow = database?.update(TableVoiceAccount.TABLE_NAME, values, TableVoiceAccount.COLUMN_NAME_ACCOUNT + " = ? and " + TableVoiceAccount.COLUMN_NAME_PASSWORD_MD5 +" = ?",
        arrayOf(oldVoiceAccountEntry?.account ?: "", oldMd5))
  }

  companion object {
    const val DUTY_FATE_OLD_ACCOUNT: String = "see_voice_account_old_data"
  }
}