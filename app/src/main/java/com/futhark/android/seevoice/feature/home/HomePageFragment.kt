package com.futhark.android.seevoice.feature.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView

import com.futhark.android.seevoice.R
import com.futhark.android.seevoice.base.BaseFragment
import com.futhark.android.seevoice.base.BaseFragmentActivity
import com.futhark.android.seevoice.controller.fragment.ExercisingFragment
import com.futhark.android.seevoice.controller.fragment.MeFragment
import com.futhark.android.seevoice.controller.fragment.SpecificationListFragment
import com.futhark.android.seevoice.controller.fragment.SupervisingListFragment
import com.futhark.android.seevoice.model.domain.ItemElement

import java.util.ArrayList


/**
 * 首页的内容
 * Created by liuhr on 06/04/2017.
 */

class HomePageFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater!!.inflate(R.layout.fragment_home_page, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var actionContainer: GridView = view.findViewById(R.id.home_action_grid_container)
        var gridAdapter = HomeActionGridAdapter(this.activity)
        actionContainer.adapter = gridAdapter
        actionContainer.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val action = gridAdapter.getItem(position)
            val intent = Intent(activity, BaseFragmentActivity::class.java)
            when (action.textRes) {
                R.string.label_record -> intent.putExtra(BaseFragmentActivity.DUTY_FATE_FRAGMENT_INTENT, SpecificationListFragment::class.java.name)
                R.string.label_exercise_supervise -> intent.putExtra(BaseFragmentActivity.DUTY_FATE_FRAGMENT_INTENT, SupervisingListFragment::class.java.name)
                R.string.label_exercise_examine -> intent.putExtra(BaseFragmentActivity.DUTY_FATE_FRAGMENT_INTENT, MeFragment::class.java.name)
                R.string.label_self -> intent.putExtra(BaseFragmentActivity.DUTY_FATE_FRAGMENT_INTENT, ExercisingFragment::class.java.name)
                R.string.label_about -> intent.putExtra(BaseFragmentActivity.DUTY_FATE_FRAGMENT_INTENT, ExercisingFragment::class.java.name)
                R.string.label_donate -> intent.putExtra(BaseFragmentActivity.DUTY_FATE_FRAGMENT_INTENT, ExercisingFragment::class.java.name)
                R.string.label_declaration -> intent.putExtra(BaseFragmentActivity.DUTY_FATE_FRAGMENT_INTENT, ExercisingFragment::class.java.name)
            }
            startActivity(intent)
        }
        gridAdapter.addAll(initItemElementArrayList())
        gridAdapter.notifyDataSetInvalidated()
    }

    private fun initItemElementArrayList(): ArrayList<ItemElement> {
        val itemElementArrayList = ArrayList<ItemElement>()
        itemElementArrayList.add(ItemElement(R.mipmap.ic_machine_bike, R.string.label_record))
        itemElementArrayList.add(ItemElement(R.mipmap.ic_try_voice, R.string.label_exercise_supervise))
        itemElementArrayList.add(ItemElement(R.mipmap.ic_eat_melon, R.string.label_exercise_examine))
        itemElementArrayList.add(ItemElement(R.mipmap.ic_any_try, R.string.label_self))
        itemElementArrayList.add(ItemElement(R.mipmap.ic_voice_mountain, R.string.label_about))
        itemElementArrayList.add(ItemElement(R.mipmap.ic_history_voice, R.string.label_declaration))
        return itemElementArrayList
    }

}
