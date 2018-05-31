package com.futhark.android.seevoice.controller.adapter

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView

import com.futhark.android.seevoice.R
import com.futhark.android.seevoice.base.BaseFragmentActivity
import com.futhark.android.seevoice.controller.fragment.ExercisingFragment
import com.futhark.android.seevoice.controller.fragment.RecordingFragment
import com.futhark.android.seevoice.model.database.TableVoiceSpecification

/**
 * display specification list
 * Created by liuhr on 30/05/2017.
 */

class SpecificationListAdapter : CursorAdapter {
  private var layoutInflater: LayoutInflater? = null
  private var context: Context? = null
  private var showRecord = true

  constructor(context: Context, c: Cursor, autoRequery: Boolean) : super(context, c, autoRequery) {
    this.context = context
    layoutInflater = LayoutInflater.from(context)
  }

  constructor(context: Context, c: Cursor, autoRequery: Boolean, showRecord: Boolean) : super(context, c, autoRequery) {
    this.context = context
    layoutInflater = LayoutInflater.from(context)
    this.showRecord = showRecord
  }

  override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
    val itemView = layoutInflater!!.inflate(R.layout.item_of_specification_list, parent, false)
    itemView.tag = ItemHolder(itemView)
    return itemView
  }

  override fun bindView(view: View, context: Context, cursor: Cursor) {
    val itemHolder = view.tag as ItemHolder
    itemHolder?.display(cursor)
  }

  private inner class ItemHolder constructor(private val itemView: View) {
    private val titleView: TextView
    private val phoneticView: TextView
    private val authorView: TextView
    private val modifyBuffton: View
    private var voiceSpecificationEntry: TableVoiceSpecification.VoiceSpecificationEntry? = null

    private val onClickListener = View.OnClickListener { v ->
      when (v.id) {
        R.id.button_to_modify -> gotoModify(voiceSpecificationEntry)
        R.id.button_to_exercise -> gotoExercise(voiceSpecificationEntry)
      }
    }

    init {
      titleView = itemView.findViewById<View>(R.id.text_specification_title) as TextView
      phoneticView = itemView.findViewById<View>(R.id.text_specification_phonetic) as TextView
      authorView = itemView.findViewById<View>(R.id.text_specification_author) as TextView
      itemView.findViewById<View>(R.id.button_to_exercise).setOnClickListener(onClickListener)
      modifyBuffton = itemView.findViewById(R.id.button_to_modify)
      if (!showRecord) {
        modifyBuffton.visibility = View.INVISIBLE
      } else {
        modifyBuffton.setOnClickListener(onClickListener)
      }
    }

    fun display(cursor: Cursor) {
      titleView.text = cursor.getString(cursor.getColumnIndex(TableVoiceSpecification.VoiceSpecification.COLUMN_NAME_TITLE))
      phoneticView.text = cursor.getString(cursor.getColumnIndex(TableVoiceSpecification.VoiceSpecification.COLUMN_NAME_PHONETIC))
      authorView.text = cursor.getString(cursor.getColumnIndex(TableVoiceSpecification.VoiceSpecification.COLUMN_NAME_AUTHOR))
      voiceSpecificationEntry = TableVoiceSpecification.VoiceSpecificationEntry(cursor)
    }
  }

  private fun gotoExercise(specificationEntry: TableVoiceSpecification.VoiceSpecificationEntry?) {
    val exerciseIntent = Intent(context, BaseFragmentActivity::class.java)
    val bundle = Bundle()
    bundle.putSerializable(ExercisingFragment.FRAGMENT_EXERCISING_ARGUMENT_ITEM_MODEL, specificationEntry)
    exerciseIntent.putExtra(BaseFragmentActivity.DUTY_FATE_FRAGMENT_INTENT, ExercisingFragment::class.java!!.getName())
    exerciseIntent.putExtra(BaseFragmentActivity.DUTY_FATE_FRAGMENT_DATA_INTENT, bundle)
    context!!.startActivity(exerciseIntent)
  }

  private fun gotoModify(specificationEntry: TableVoiceSpecification.VoiceSpecificationEntry?) {
    val exerciseIntent = Intent(context, BaseFragmentActivity::class.java)
    val bundle = Bundle()
    bundle.putSerializable(RecordingFragment.FRAGMENT_RECORDING_ARGUMENT_ITEM_MODEL, specificationEntry)
    exerciseIntent.putExtra(BaseFragmentActivity.DUTY_FATE_FRAGMENT_INTENT, RecordingFragment::class.java!!.getName())
    exerciseIntent.putExtra(BaseFragmentActivity.DUTY_FATE_FRAGMENT_DATA_INTENT, bundle)
    context!!.startActivity(exerciseIntent)
  }
}
