package com.futhark.android.seevoice.feature.standard

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ListView

import com.futhark.android.seevoice.R
import com.futhark.android.seevoice.base.BaseFragment
import com.futhark.android.seevoice.base.BaseFragmentActivity
import com.futhark.android.seevoice.model.database.SeeVoiceSqliteDatabaseHelper
import com.futhark.android.seevoice.model.database.TableVoiceSpecification


/**
 * list specification
 * Created by liuhr on 30/05/2017.
 */

class SpecificationListFragment : BaseFragment() {
  private var specificationListView: ListView? = null

  private var listAdapter: SpecificationListAdapter? = null
  private var cursor: Cursor? = null
  private var database: SQLiteDatabase? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)
  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Log.i(TAG, "will create specification list fragment")
    return inflater!!.inflate(R.layout.fragment_empty_list, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    specificationListView = view.findViewById(R.id.fragment_empty_list_view)
    listAdapter = SpecificationListAdapter(activity, null, true, true)
    specificationListView!!.adapter = listAdapter
    database = SeeVoiceSqliteDatabaseHelper(activity).readableDatabase
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    super.onCreateOptionsMenu(menu, inflater)
    inflater.inflate(R.menu.menu_exercise_record_standard, menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.menu_exercise_record_standard -> {
        gotoRecordSpecification()
        return true
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onResume() {
    super.onResume()
    cursor = database!!.query(TableVoiceSpecification.VoiceSpecification.TABLE_NAME,
        TableVoiceSpecification.COLUMNS, null, null, null, null, null)
    if (cursor != null) {
      cursor!!.moveToFirst()
      listAdapter!!.swapCursor(cursor)
    }
  }

  override fun onPause() {
    super.onPause()
    if (cursor != null) {
      cursor!!.close()
      cursor = null
    }
  }

  private fun gotoRecordSpecification() {
    val goToRecordActivityIntent = Intent(activity, BaseFragmentActivity::class.java)
    goToRecordActivityIntent.putExtra(BaseFragmentActivity.DUTY_FATE_FRAGMENT_INTENT, SpecificationRecordingFragment::class.java.name)
    activity.startActivity(goToRecordActivityIntent)
  }


}
