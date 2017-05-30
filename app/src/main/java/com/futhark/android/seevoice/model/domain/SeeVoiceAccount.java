package com.futhark.android.seevoice.model.domain;

import android.accounts.Account;
import android.os.Parcel;

import java.sql.Date;

/**
 * Created by liuhr on 14/04/2017.
 */

public class SeeVoiceAccount extends Account {
    public SeeVoiceAccount(String name, String type) {
        super(name, type);
    }

    public SeeVoiceAccount(Parcel in) {
        super(in);
    }

    private String domain;
    private String type;
    private Date createAt;
}
