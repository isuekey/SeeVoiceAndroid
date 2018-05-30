package com.futhark.android.seevoice.controller.fragment

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
import com.futhark.android.seevoice.model.database.TableVoiceSpecification.VoiceSpecificationEntry

import java.nio.ByteBuffer

import butterknife.BindView
import butterknife.ButterKnife

/**
 * record fragment
 * Created by liuhr on 31/05/2017.
 */

class RecordingFragment : BaseFragment() {
    @BindView(R.id.button_save_record) internal var buttonSave: View? = null
    @BindView(R.id.edit_voice_title) internal var titleEdit: EditText? = null
    @BindView(R.id.edit_voice_phonetic) internal var phoneticEdit: EditText? = null
    @BindView(R.id.edit_voice_author) internal var authorEdit: EditText? = null

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle): View? {
        val fragment = inflater.inflate(R.layout.fragment_recording, container, false)
        ButterKnife.bind(this, fragment)
        buttonSave!!.setOnClickListener(onClickListener)
        return fragment
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
        values.put(VoiceSpecificationEntry.COLUMN_NAME_TITLE, title)
        values.put(VoiceSpecificationEntry.COLUMN_NAME_PHONETIC, phonetic)
        values.put(VoiceSpecificationEntry.COLUMN_NAME_AUTHOR, author)
        values.put(VoiceSpecificationEntry.COLUMN_NAME_DATA, byteBuffer.array())
        if (voiceSpecificationEntry != null) {
            val rows = database!!.update(VoiceSpecificationEntry.TABLE_NAME, values, VoiceSpecificationEntry._ID + "= ?", arrayOf(voiceSpecificationEntry!!.id!!.toString()))
            return if (rows > 0) {
                voiceSpecificationEntry!!.id!!
            } else {
                -1L
            }
        } else {
            return database!!.insert(VoiceSpecificationEntry.TABLE_NAME, null, values)
        }
    }

    companion object {

        val FRAGMENT_RECORDING_ARGUMENT_ITEM_MODEL = "fragment_recording_arugment_item_model"
    }
}
