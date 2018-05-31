package com.futhark.android.seevoice.feature.record

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast

import com.futhark.android.seevoice.R
import com.futhark.android.seevoice.base.BaseFragment
import com.futhark.android.seevoice.model.database.SeeVoiceSqliteDatabaseHelper
import com.futhark.android.seevoice.model.database.TableVoiceSpecification.VoiceSpecification
import com.futhark.android.seevoice.model.database.TableVoiceSpecification.VoiceSpecificationEntry

import java.nio.ByteBuffer

/**
 * record fragment
 * Created by liuhr on 31/05/2017.
 */

class SpecificationRecordingFragment : BaseFragment() {
  private var buttonSave: View? = null
  private var titleEdit: EditText? = null
  private var phoneticEdit: EditText? = null
  private var authorEdit: EditText? = null

  private var database: SQLiteDatabase? = null
  private var seeVoiceFragment: SeeVoiceFragment? = null
  private var voiceSpecificationEntry: VoiceSpecificationEntry? = null

  private val onClickListener = View.OnClickListener { v ->
    when (v.id) {
      R.id.button_save_record -> {
        saveCurrentVoice()
        Toast.makeText(activity, getString(R.string.label_save_success), Toast.LENGTH_SHORT).show()
      }
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val arguments = arguments
    if (arguments != null && arguments.containsKey(FRAGMENT_RECORDING_ARGUMENT_ITEM_MODEL)) {
      voiceSpecificationEntry = arguments.get(FRAGMENT_RECORDING_ARGUMENT_ITEM_MODEL) as VoiceSpecificationEntry
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View? {
    val fragment = inflater.inflate(R.layout.fragment_recording, container, false)
    buttonSave = fragment.findViewById(R.id.button_save_record)
    titleEdit = fragment.findViewById(R.id.edit_voice_title)
    phoneticEdit = fragment.findViewById(R.id.edit_voice_phonetic)
    authorEdit = fragment.findViewById(R.id.edit_voice_author)
    return fragment
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    activity.setTitle(R.string.label_specification_record)
    seeVoiceFragment = SeeVoiceFragment.newInstance(if (voiceSpecificationEntry != null) voiceSpecificationEntry!!.data else null)
    childFragmentManager.beginTransaction().replace(R.id.fragment_display_voice, seeVoiceFragment).commit()
    val helper = SeeVoiceSqliteDatabaseHelper(activity)
    database = helper.writableDatabase
  }

  override fun onResume() {
    super.onResume()
    if (voiceSpecificationEntry != null) {
      titleEdit!!.setText(voiceSpecificationEntry!!.title)
      phoneticEdit!!.setText(voiceSpecificationEntry!!.phonetic)
      authorEdit!!.setText(voiceSpecificationEntry!!.author)
    }
  }

  private fun saveCurrentVoice(): Long {
    val title = titleEdit!!.text.toString()
    val phonetic = phoneticEdit!!.text.toString()
    val author = authorEdit!!.text.toString()
    val recordData = seeVoiceFragment!!.lastRecordData ?: return -1L
    val Size = recordData.size
    val byteBuffer = ByteBuffer.allocate(2 * Size)
    var index = 0
    while (Size > index) {
      byteBuffer.putShort(recordData[index])
      index++
    }
    val values = ContentValues()
    values.put(VoiceSpecification.COLUMN_NAME_TITLE, title)
    values.put(VoiceSpecification.COLUMN_NAME_PHONETIC, phonetic)
    values.put(VoiceSpecification.COLUMN_NAME_AUTHOR, author)
    values.put(VoiceSpecification.COLUMN_NAME_DATA, byteBuffer.array())
    if (voiceSpecificationEntry != null) {
      val rows = database!!.update(VoiceSpecification.TABLE_NAME, values, VoiceSpecification._ID + "= ?", arrayOf(voiceSpecificationEntry!!.id!!.toString()))
      return if (rows > 0) {
        voiceSpecificationEntry!!.id!!
      } else {
        -1L
      }
    } else {
      return database!!.insert(VoiceSpecification.TABLE_NAME, null, values)
    }
  }

  companion object {
    val FRAGMENT_RECORDING_ARGUMENT_ITEM_MODEL = "fragment_recording_arugment_item_model"
  }
}
