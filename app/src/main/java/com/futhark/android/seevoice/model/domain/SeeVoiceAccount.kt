package com.futhark.android.seevoice.model.domain

import android.accounts.Account
import android.os.Parcel

import java.sql.Date

/**
 * Created by liuhr on 14/04/2017.
 */

class SeeVoiceAccount : Account {

    private val domain: String? = null
    private val type: String? = null
    private val createAt: Date? = null

    constructor(name: String, type: String) : super(name, type) {}

    constructor(`in`: Parcel) : super(`in`) {}
}
