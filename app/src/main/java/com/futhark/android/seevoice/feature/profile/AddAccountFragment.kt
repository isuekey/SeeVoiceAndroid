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

class AddAccountFragment : BaseFragment() {
  private val editResourceIdList = listOf(R.id.input_me_account, R.id.input_me_new_password,
      R.id.input_me_title, R.id.input_me_lang, R.id.input_me_full_name,
      R.id.input_me_confirm_password)
  private var database: SQLiteDatabase? = null

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    return inflater.inflate(R.layout.fragment_me_add_account, container, false)
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
          for (editResourceId in editResourceIdList) {
            view?.findViewById<EditText>(editResourceId)?.setText("")
          }
        }
      }
    }
    view.findViewById<Button>(R.id.action_me_save).setOnClickListener(buttonClickListener)
    view.findViewById<Button>(R.id.action_me_reset).setOnClickListener(buttonClickListener)
  }

  override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
    super.onCreateOptionsMenu(menu, inflater)
    menu?.findItem(R.id.menu_me_account_add)?.isVisible = false
    menu?.findItem(R.id.menu_me_account_edit)?.isVisible = false
    menu?.findItem(R.id.menu_me_account_switch)?.isVisible = false
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
    values.put(TableVoiceAccount.COLUMN_NAME_ACCOUNT, inputString[0])
    values.put(TableVoiceAccount.COLUMN_NAME_PASSWORD_MD5, MessageUtil.md5(inputString[1]))
    values.put(TableVoiceAccount.COLUMN_NAME_TITLE, inputString[2])
    values.put(TableVoiceAccount.COLUMN_NAME_LANG, inputString[3])
    values.put(TableVoiceAccount.COLUMN_NAME_FULL_NAME, inputString[4])
    values.put(TableVoiceAccount.COLUMN_NAME_STATUS, TableVoiceAccount.STATUS_IS_ACTIVE)
    values.put(TableVoiceAccount.COLUMN_NAME_CREATED_AT, Date().time)
    database?.insert(TableVoiceAccount.TABLE_NAME,null, values)
  }

  override fun onDestroy() {
    super.onDestroy()
    database?.close()
    database = null
  }
}