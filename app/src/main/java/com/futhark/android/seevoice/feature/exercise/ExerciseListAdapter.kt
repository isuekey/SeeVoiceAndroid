package com.futhark.android.seevoice.feature.exercise

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

import com.futhark.android.seevoice.R

/**
 * exercise list adapter
 * Created by liuhr on 06/04/2017.
 */
@Suppress("unused")
class ExerciseListAdapter(context: Context) : ArrayAdapter<ExerciseListAdapter.ExerciseItemInterface>(context, 0) {
    private val inflater: LayoutInflater = LayoutInflater.from(context)


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var v = convertView
        val holder: ExerciseItemHolder
        if (v == null) {
            v = inflater.inflate(R.layout.item_of_exercise_list, parent, false)
            holder = ExerciseItemHolder(v)
            v.tag = holder
        } else {
            holder = v.tag as ExerciseItemHolder
        }
        holder.setDataToDisplay(getItem(position))
        return v!!
    }

    internal inner class ExerciseItemHolder(itemView: View) {
        private var titleText: TextView = itemView.findViewById(R.id.item_of_exercise_list_title)

        fun setDataToDisplay(data: ExerciseItemInterface) {
            titleText.text = data.title
        }
    }

    interface ExerciseItemInterface {
        var title: String?
    }
}
