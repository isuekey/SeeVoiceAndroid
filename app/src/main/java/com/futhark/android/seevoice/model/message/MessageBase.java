package com.futhark.android.seevoice.model.message;

/**
 * base message class
 * Created by liuhr on 06/04/2017.
 */

public class MessageBase {
    protected int messageId;
    protected String messageInfo;

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getMessageInfo() {
        return messageInfo;
    }

    public void setMessageInfo(String messageInfo) {
        this.messageInfo = messageInfo;
    }
}
