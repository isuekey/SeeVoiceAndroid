package com.futhark.android.seevoice.feature.standard

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
import com.futhark.android.seevoice.model.database.TableVoiceSpecification

/**
 * display specification list
 * Created by liuhr on 30/05/2017.
 */

class SpecificationListAdapter : CursorAdapter {
  private var layoutInflater: LayoutInflater? = null
  private var context: Context? = null
  private val showRecord: Boolean

  constructor(context: Context, c: Cursor?, autoRequery: Boolean, showRecord: Boolean) : super(context, c, autoRequery) {
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
    private val titleView: TextView = itemView.findViewById(R.id.text_specification_title)
    private val phoneticView: TextView = itemView.findViewById(R.id.text_specification_phonetic)
    private val authorView: TextView = itemView.findViewById(R.id.text_specification_author)
    private val modifyButton: View = itemView.findViewById(R.id.button_to_modify)
    private val exerciseButton: View = itemView.findViewById(R.id.button_to_exercise)
    private var voiceSpecificationEntry: TableVoiceSpecification.VoiceSpecificationEntry? = null

    init {
      val onClickListener = View.OnClickListener { v ->
        when (v.id) {
          R.id.button_to_modify -> gotoModify(voiceSpecificationEntry)
          R.id.button_to_exercise -> gotoExercise(voiceSpecificationEntry)
        }
      }
      exerciseButton.setOnClickListener(onClickListener)
      modifyButton.setOnClickListener(onClickListener)
      modifyButton.visibility = if (!showRecord) View.INVISIBLE else View.VISIBLE
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
    bundle.putSerializable(SpecificationRecordingFragment.FRAGMENT_RECORDING_ARGUMENT_ITEM_MODEL, specificationEntry)
    exerciseIntent.putExtra(BaseFragmentActivity.DUTY_FATE_FRAGMENT_INTENT, SpecificationRecordingFragment::class.java!!.getName())
    exerciseIntent.putExtra(BaseFragmentActivity.DUTY_FATE_FRAGMENT_DATA_INTENT, bundle)
    context!!.startActivity(exerciseIntent)
  }
}
