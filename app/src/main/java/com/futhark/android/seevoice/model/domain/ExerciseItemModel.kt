package com.futhark.android.seevoice.model.domain

import android.database.Cursor

import com.futhark.android.seevoice.controller.adapter.ExerciseListAdapter

import java.io.Serializable
import java.sql.Date

/**
 * exercise item model
 * Created by liuhr on 06/04/2017.
 */

class ExerciseItemModel : ExerciseListAdapter.ExerciseItemInterface, Serializable {
    private val id: Long? = null
    private val text: String? = null
    private val date: Date? = null
    override var title: String? = null

    constructor() {}

    constructor(title: String) {
        this.title = title
    }

    constructor(cursor: Cursor) {

    }
}
